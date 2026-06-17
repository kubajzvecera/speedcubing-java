package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoundRepository extends JpaRepository<Round, Long> {

    @Query("SELECT r FROM Round r JOIN FETCH r.category")
    List<Round> findAllRoundsWithCategory();
}
