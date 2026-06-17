package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Result;
import com.speedcubers.speedcubing.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT r FROM Result r WHERE r.round = :round")
    List<Result> findByRound(@Param("round") Round round);

    @Query("SELECT r FROM Result r WHERE r.round IN :rounds ORDER BY r.round.id, r.rank")
    List<Result> findByRoundIn(@Param("rounds") List<Round> rounds);

    @Modifying
    @Query("DELETE FROM Result r WHERE r.round = :round")
    void deleteByRound(@Param("round") Round round);

    @Query("SELECT r FROM Result r WHERE r.competitor.id = :competitorId")
    List<Result> findByCompetitorId(@Param("competitorId") Long competitorId);

    @Query("SELECT c.name, MIN(r.averageTime) FROM Result r JOIN r.round ro JOIN ro.category c " +
           "WHERE r.competitor.id = :competitorId " +
           "GROUP BY c.name ORDER BY c.name")
    List<Object[]> findBestAo5ByCategory(@Param("competitorId") Long competitorId);
}
