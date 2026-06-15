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

    public List<Solve> getSolvesByRound(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return List.of();
        return solveRepository.findByRound(round);
    }

    public Solve addSolve(Long roundId, Long competitorId, int timeMs, String penalty) {
        Round round = roundRepository.findById(roundId).orElse(null);
        Competitor competitor = competitorRepository.findById(competitorId).orElse(null);
        if (round == null || competitor == null) return null;

        List<Solve> existing = solveRepository.findByRoundAndCompetitorId(round, competitorId);
        int attemptNumber = existing.size() + 1;

        Solve solve = new Solve();
        solve.setAttemptNumber(attemptNumber);
        solve.setTimeMs(timeMs);
        solve.setPenalty(penalty);
        solve.setRound(round);
        solve.setCompetitor(competitor);
        return solveRepository.save(solve);
    }
}
