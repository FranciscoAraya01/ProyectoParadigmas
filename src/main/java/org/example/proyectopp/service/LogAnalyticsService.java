package org.example.proyectopp.service;

import lombok.RequiredArgsConstructor;
import org.example.proyectopp.dto.*;
import org.example.proyectopp.entity.LogEntry;
import org.example.proyectopp.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogAnalyticsService {
    private final LogEntryRepository repo;

    // errores http por código (400, 500, etc)
    public List<ErrorCount> errorCountByCode() {
        return repo.findAll().stream()
                .filter(le -> le.getStatus() >= 400) // para solo errores
                .collect(Collectors.groupingBy(
                        LogEntry::getStatus,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new ErrorCount(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(ErrorCount::getCount).reversed()
                        .thenComparing(ErrorCount::getCode))
                .toList();
    }

    // los más frecuentes
    public List<TopError> top3Errors() {
        return repo.findAll().stream()
                .filter(le -> le.getStatus() >= 400 || le.getExceptionName() != null)
                .map(le -> {
                    String label = (le.getExceptionName() != null && !le.getExceptionName().isBlank())
                            ? le.getExceptionName()
                            : "HTTP_" + le.getStatus();
                    return label;
                })
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(3)
                .map(e -> new TopError(e.getKey(), e.getValue()))
                .toList();
    }

    // horas pico
    public List<HourlyErrors> peakErrorHours() {
        ZoneId zone = ZoneId.systemDefault();
        return repo.findAll().stream()
                .filter(le -> le.getStatus() >= 400 || le.getExceptionName() != null)
                .collect(Collectors.groupingBy(
                        le -> ZonedDateTime.ofInstant(le.getTimestamp(), zone).getHour(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new HourlyErrors(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(HourlyErrors::getCount).reversed()
                        .thenComparingInt(HourlyErrors::getHour))
                .toList();
    }

    // tiempos de respuesta
    public ResponseStats responseStats() {
        var durations = repo.findAll().stream()
                .mapToLong(LogEntry::getDurationMs)
                .sorted()
                .toArray();

        if (durations.length == 0) {
            return new ResponseStats(0.0, 0L, 0L, 0.0);
        }

        double avg = Arrays.stream(durations).average().orElse(0.0);
        long min = durations[0];
        long max = durations[durations.length - 1];

        double median;
        int n = durations.length;
        if (n % 2 == 1) {
            median = durations[n / 2];
        } else {
            median = (durations[n / 2 - 1] + durations[n / 2]) / 2.0;
        }

        return new ResponseStats(avg, min, max, median);
    }

    // suma total por endpoint
    public List<EndpointSum> responseTimeSumByEndpoint() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getEndpoint,
                        Collectors.summingLong(LogEntry::getDurationMs)
                ))
                .entrySet().stream()
                .map(e -> new EndpointSum(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(EndpointSum::getTotalMs).reversed()
                        .thenComparing(EndpointSum::getEndpoint))
                .toList();
    }

    // reportes, top 3 primeros y últimos por cantidad de solicitudes
    public Map<String,Object> endpointUsageTop3AndBottom3() {
        var usage = repo.findAll().stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getEndpoint,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new EndpointUsage(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(EndpointUsage::getTotal).reversed()
                        .thenComparing(EndpointUsage::getEndpoint))
                .toList();

        var top3 = usage.stream().limit(3).toList();
        var bottom3 = usage.size() <= 3 ? usage : usage.subList(Math.max(usage.size() - 3, 0), usage.size());

        return Map.of(
                "top3", top3,
                "bottom3", bottom3
        );
    }

    // conteo x metodo http
    public List<HttpMethodCount> countByHttpMethod() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(
                        LogEntry::getHttpMethod,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(e -> new HttpMethodCount(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(HttpMethodCount::getTotal).reversed()
                        .thenComparing(HttpMethodCount::getMethod))
                .toList();
    }

    // alertas y eventos criticos
    public List<CriticalEvent> criticalEvents() {
        return repo.findAll().stream()
                .filter(le -> le.getStatus() >= 500 || (le.getExceptionName() != null && !le.getExceptionName().isBlank()))
                .map(le -> {
                    String type = (le.getExceptionName() != null && !le.getExceptionName().isBlank())
                            ? "EXCEPTION"
                            : "HTTP_5XX";
                    String detail = (le.getExceptionName() != null && !le.getExceptionName().isBlank())
                            ? le.getExceptionName() + " @ " + le.getEndpoint()
                            : "HTTP " + le.getStatus() + " @ " + le.getEndpoint();
                    return new CriticalEvent(type, detail, le.getTimestamp());
                })
                .sorted(Comparator.comparing(CriticalEvent::getAt)) // cronológico (asc)
                .toList();
    }

    // total eventos criticos
    public long criticalEventsCount() {
        return repo.findAll().stream()
                .filter(le -> le.getStatus() >= 500 || (le.getExceptionName() != null && !le.getExceptionName().isBlank()))
                .count();
    }

    // estado de la aplicacion
    public AppStatus appStatus() {
        var all = repo.findAll();
        long totalReq = all.size();
        long totalErr = all.stream().filter(le -> le.getStatus() >= 400).count();
        double avgMs = all.stream().mapToLong(LogEntry::getDurationMs).average().orElse(0.0);
        return new AppStatus(totalReq, totalErr, avgMs);
    }
}