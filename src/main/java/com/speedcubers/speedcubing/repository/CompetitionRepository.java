package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query("SELECT DISTINCT c FROM Competition c WHERE c.id NOT IN " +
           "(SELECT r.competition.id FROM Registration r WHERE r.competitor.id = :competitorId) " +
           "AND c.id IN (SELECT DISTINCT r2.competition.id FROM Round r2)")
    List<Competition> findAvailableForCompetitor(@Param("competitorId") Long competitorId);

}
