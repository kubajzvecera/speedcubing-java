package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Result;
import com.speedcubers.speedcubing.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByRound(Round round);
    void deleteByRound(Round round);
}
