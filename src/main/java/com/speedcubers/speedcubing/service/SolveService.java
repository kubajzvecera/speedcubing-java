package com.speedcubers.speedcubing.service;

import com.speedcubers.speedcubing.entity.Competitor;
import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import com.speedcubers.speedcubing.repository.CompetitorRepository;
import com.speedcubers.speedcubing.repository.RoundRepository;
import com.speedcubers.speedcubing.repository.SolveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolveService {

    @Autowired
    private SolveRepository solveRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private CompetitorRepository competitorRepository;

    public String addSolve(Long roundId, Long competitorId, int timeMs, String penalty) {
        if (timeMs <= 0) return "Time must be greater than 0.";
        Round round = roundRepository.findById(roundId).orElse(null);
        Competitor competitor = competitorRepository.findById(competitorId).orElse(null);
        if (round == null || competitor == null) return "Invalid round or competitor.";

        List<Solve> existing = solveRepository.findByRoundAndCompetitorId(round, competitorId);
        if (existing.size() >= 5) return "Maximum 5 solves per competitor per round reached.";

        int attemptNumber = existing.size() + 1;

        Solve solve = Solve.builder()
                .attemptNumber(attemptNumber)
                .timeMs(timeMs)
                .penalty(penalty)
                .round(round)
                .competitor(competitor)
                .build();
        solveRepository.save(solve);
        return null;
    }
}
