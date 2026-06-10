package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
