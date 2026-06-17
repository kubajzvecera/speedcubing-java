package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolveRepository extends JpaRepository<Solve, Long> {

    @Query("SELECT s FROM Solve s WHERE s.round = :round")
    List<Solve> findByRound(@Param("round") Round round);

    @Query("SELECT s FROM Solve s WHERE s.round = :round AND s.competitor.id = :competitorId")
    List<Solve> findByRoundAndCompetitorId(@Param("round") Round round, @Param("competitorId") Long competitorId);

    @Query("SELECT s FROM Solve s WHERE s.competitor.id = :competitorId")
    List<Solve> findByCompetitorId(@Param("competitorId") Long competitorId);

    @Query("SELECT c.name, MIN(s.timeMs) FROM Solve s JOIN s.round r JOIN r.category c " +
           "WHERE s.competitor.id = :competitorId AND (s.penalty IS NULL OR s.penalty <> 'DNF') " +
           "GROUP BY c.name ORDER BY c.name")
    List<Object[]> findBestSingleByCategory(@Param("competitorId") Long competitorId);
}
