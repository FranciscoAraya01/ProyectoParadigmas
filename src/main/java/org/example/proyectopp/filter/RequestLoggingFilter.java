package org.example.proyectopp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.proyectopp.entity.LogEntry;
import org.example.proyectopp.repository.LogEntryRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final LogEntryRepository repo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long start = System.nanoTime();
        String exceptionName = null;

        try {
            filterChain.doFilter(request, response);
        } catch (Throwable t) {
            exceptionName = t.getClass().getSimpleName();
            throw t;
        } finally {
            long end = System.nanoTime();
            long ms = Math.max(0, (end - start) / 1_000_000);

            int status = response.getStatus();
            String level = status >= 500 ? "ERROR" : (status >= 400 ? "WARN" : "INFO");

            LogEntry entry = LogEntry.builder()
                    .timestamp(Instant.now())
                    .level(level)
                    .httpMethod(request.getMethod())
                    .endpoint(request.getRequestURI())
                    .status(status)
                    .durationMs(ms)
                    .exceptionName(exceptionName)
                    .message(null)
                    .build();

            repo.save(entry);
        }
    }
}