package com.speedcubers.speedcubing.service;

import com.speedcubers.speedcubing.entity.Competitor;
import com.speedcubers.speedcubing.entity.Result;
import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import com.speedcubers.speedcubing.repository.CompetitorRepository;
import com.speedcubers.speedcubing.repository.ResultRepository;
import com.speedcubers.speedcubing.repository.RoundRepository;
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
    private RoundRepository roundRepository;
    @Autowired
    private SolveRepository solveRepository;
    @Autowired
    private CompetitorRepository competitorRepository;

    public List<Result> getResultsByRound(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return List.of();
        return resultRepository.findByRound(round);
    }

    @Transactional
    public List<Result> generateResults(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return List.of();

        List<Solve> allSolves = solveRepository.findByRound(round);
        Map<Long, List<Solve>> byCompetitor = allSolves.stream()
                .collect(Collectors.groupingBy(s -> s.getCompetitor().getId()));

        // každý účastněný musí mít aspoň 5 pokusů
        for (Map.Entry<Long, List<Solve>> entry : byCompetitor.entrySet()) {
            if (entry.getValue().size() < 5) return List.of();
        }

        // aspoň 2 účastníci
        if (byCompetitor.size() < 2) return List.of();

        resultRepository.deleteByRound(round);

        List<Result> results = new ArrayList<>();

        for (Map.Entry<Long, List<Solve>> entry : byCompetitor.entrySet()) {
            Competitor competitor = competitorRepository.findById(entry.getKey()).orElse(null);
            if (competitor == null) continue;

            List<Solve> solves = entry.getValue();
            List<Integer> times = new ArrayList<>();
            for (Solve solve : solves) {
                if ("DNF".equals(solve.getPenalty())) continue;
                int time = solve.getTimeMs();
                if ("+2".equals(solve.getPenalty())) time += 2000;
                times.add(time);
            }

            if (times.size() < 3) continue;

            times.sort(Comparator.naturalOrder());
            int bestTime = times.getFirst();
            List<Integer> middle = times.subList(1, times.size() - 1);
            double avg = middle.stream().mapToInt(Integer::intValue).average().orElse(0);

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
