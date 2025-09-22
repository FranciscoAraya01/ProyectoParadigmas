package org.example.proyectopp.repository;

import org.example.proyectopp.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {}
