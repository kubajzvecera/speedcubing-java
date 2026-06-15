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

    @Modifying
    @Query("DELETE FROM Result r WHERE r.round = :round")
    void deleteByRound(@Param("round") Round round);

    @Query("SELECT r FROM Result r WHERE r.competitor.id = :competitorId")
    List<Result> findByCompetitorId(@Param("competitorId") Long competitorId);
}
