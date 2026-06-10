package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolveRepository extends JpaRepository<Solve, Long> {
    List<Solve> findByRound(Round round);
    List<Solve> findByRoundAndCompetitorId(Round round, Long competitorId);
}
