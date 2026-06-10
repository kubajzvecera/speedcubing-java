package com.speedcubers.speedcubing.repository;

import com.speedcubers.speedcubing.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long> {
}
