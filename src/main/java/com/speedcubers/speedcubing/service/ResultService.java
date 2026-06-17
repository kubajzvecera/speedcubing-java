package com.speedcubers.speedcubing.service;

import com.speedcubers.speedcubing.entity.Competitor;
import com.speedcubers.speedcubing.entity.Result;
import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import com.speedcubers.speedcubing.repository.CompetitorRepository;
import com.speedcubers.speedcubing.repository.ResultRepository;
import com.speedcubers.speedcubing.repository.SolveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SolveRepository solveRepository;
    @Autowired
    private CompetitorRepository competitorRepository;

    public List<Result> getResultsByRound(Round round) {
        return resultRepository.findByRound(round);
    }

    @Transactional
    public List<Result> generateResults(Round round) {
        List<Solve> allSolves = solveRepository.findByRound(round);
        //group solves by competitor
        Map<Long, List<Solve>> byCompetitor = allSolves.stream()
                .collect(Collectors.groupingBy(s -> s.getCompetitor().getId()));

        // every competitor must have at least 5 solves
        for (List<Solve> solves : byCompetitor.values()) {
            if (solves.size() < 5) return List.of();
        }
        // in round there must be at least 2 competitors
        if (byCompetitor.size() < 2) return List.of();
        // delete old results (might have changed by deleting solves etc.)
        resultRepository.deleteByRound(round);

        List<Result> results = new ArrayList<>();
        //calc results for all competitors that attempted in this round
        for (Long competitorId : byCompetitor.keySet()) {
            Competitor competitor = competitorRepository.findById(competitorId).orElse(null);
            if (competitor == null) continue;

            List<Solve> solves = byCompetitor.get(competitorId);
            List<Integer> times = new ArrayList<>();
            //penalty and times preparation
            for (Solve solve : solves) {
                if ("DNF".equals(solve.getPenalty())) continue;
                int time = solve.getTimeMs();
                if ("+2".equals(solve.getPenalty())) time += 2000;
                times.add(time);
            }

            if (times.size() < 3) continue;

            Collections.sort(times);
            int bestTime = times.getFirst();
            int sum = times.get(1) + times.get(2) + times.get(3);
            double avg = Math.round(sum / 3.0 * 100.0) / 100.0;

            Result result = new Result();
            result.setBestTime(bestTime);
            result.setAverageTime(avg);
            result.setCompetitor(competitor);
            result.setRound(round);
            results.add(result);
        }

        results.sort(Comparator.comparingDouble(Result::getAverageTime));
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }

        return resultRepository.saveAll(results);
    }
}
