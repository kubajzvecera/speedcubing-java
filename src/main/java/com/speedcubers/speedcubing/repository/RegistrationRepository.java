package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Competitor;
import com.speedcubers.speedcubing.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("SELECT r FROM Registration r WHERE r.competitor.id = :competitorId")
    List<Registration> findByCompetitorId(@Param("competitorId") Long competitorId);

    @Query("SELECT r.competitor FROM Registration r WHERE r.competition.id = :competitionId AND r.category.id = :categoryId")
    List<Competitor> findCompetitorsByCompetitionAndCategory(@Param("competitionId") Long competitionId, @Param("categoryId") Long categoryId);

    @Modifying
    @Query("DELETE FROM Registration r WHERE r.category.id = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);
}
