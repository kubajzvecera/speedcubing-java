package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.dto.ResultDTO;
import com.speedcubers.speedcubing.dto.SolveDTO;
import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.CompetitionService;
import com.speedcubers.speedcubing.service.ResultService;
import com.speedcubers.speedcubing.service.SolveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final CompetitionService competitionService;
    private final SolveService solveService;
    private final ResultService resultService;
    private final CompetitorRepository competitorRepository;
    private final RoundRepository roundRepository;

    public RestApiController(CompetitionService competitionService, SolveService solveService,
                             ResultService resultService, CompetitorRepository competitorRepository,
                             RoundRepository roundRepository) {
        this.competitionService = competitionService;
        this.solveService = solveService;
        this.resultService = resultService;
        this.competitorRepository = competitorRepository;
        this.roundRepository = roundRepository;
    }

    @GetMapping("/competitions")
    public List<Competition> getAllCompetitions() {
        return competitionService.findAll();
    }

    @PostMapping("/competitions")
    public Competition createCompetition(@RequestBody Competition competition) {
        return competitionService.create(competition);
    }

    @DeleteMapping("/competitions/{id}")
    public void deleteCompetition(@PathVariable Long id) {
        competitionService.delete(id);
    }

    @GetMapping("/competitors")
    public List<Competitor> getAllCompetitors() {
        return competitorRepository.findAll();
    }

    @PostMapping("/competitors")
    public Competitor createCompetitor(@RequestBody Competitor competitor) {
        return competitorRepository.save(competitor);
    }

    @GetMapping("/rounds/{id}")
    public Round getRound(@PathVariable Long id) {
        return roundRepository.findById(id).orElse(null);
    }

    @GetMapping("/rounds/{id}/solves")
    public List<Solve> getSolvesByRound(@PathVariable Long id) {
        return solveService.getSolvesByRound(id);
    }

    @PostMapping("/solves")
    public ResponseEntity<Solve> addSolve(@RequestBody SolveDTO dto) {
        Solve solve = solveService.addSolve(dto.getRoundId(), dto.getCompetitorId(),
                dto.getAttemptNumber(), dto.getTimeMs(), dto.getPenalty());
        if (solve == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(solve);
    }

    @GetMapping("/rounds/{id}/results")
    public List<ResultDTO> getResultsByRound(@PathVariable Long id) {
        List<Result> results = resultService.getResultsByRound(id);
        return results.stream().map(r -> new ResultDTO(
                r.getCompetitor().getId(),
                r.getCompetitor().getName(),
                r.getBestTime(),
                r.getAverageTime(),
                r.getRank()
        )).collect(Collectors.toList());
    }

    @PostMapping("/results/generate/{roundId}")
    public List<ResultDTO> generateResults(@PathVariable Long roundId) {
        List<Result> results = resultService.generateResults(roundId);
        return results.stream().map(r -> new ResultDTO(
                r.getCompetitor().getId(),
                r.getCompetitor().getName(),
                r.getBestTime(),
                r.getAverageTime(),
                r.getRank()
        )).collect(Collectors.toList());
    }
}
