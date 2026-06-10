package com.speedcubers.speedcubing.service;

import com.speedcubers.speedcubing.entity.Competitor;
import com.speedcubers.speedcubing.entity.Result;
import com.speedcubers.speedcubing.entity.Round;
import com.speedcubers.speedcubing.entity.Solve;
import com.speedcubers.speedcubing.repository.CompetitorRepository;
import com.speedcubers.speedcubing.repository.ResultRepository;
import com.speedcubers.speedcubing.repository.RoundRepository;
import com.speedcubers.speedcubing.repository.SolveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final RoundRepository roundRepository;
    private final SolveRepository solveRepository;
    private final CompetitorRepository competitorRepository;

    public ResultService(ResultRepository resultRepository, RoundRepository roundRepository,
                         SolveRepository solveRepository, CompetitorRepository competitorRepository) {
        this.resultRepository = resultRepository;
        this.roundRepository = roundRepository;
        this.solveRepository = solveRepository;
        this.competitorRepository = competitorRepository;
    }

    public List<Result> getResultsByRound(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return List.of();
        return resultRepository.findByRound(round);
    }

    @Transactional
    public List<Result> generateResults(Long roundId) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return List.of();

        resultRepository.deleteByRound(round);

        List<Competitor> competitors = competitorRepository.findAll();
        List<Result> results = new ArrayList<>();

        for (Competitor competitor : competitors) {
            List<Solve> solves = solveRepository.findByRoundAndCompetitorId(round, competitor.getId());
            if (solves.size() < 5) continue;

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
