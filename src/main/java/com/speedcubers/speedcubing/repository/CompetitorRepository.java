package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
}
