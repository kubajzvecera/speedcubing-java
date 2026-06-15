package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.dto.ResultDTO;
import com.speedcubers.speedcubing.dto.SolveDTO;
import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.CompetitionService;
import com.speedcubers.speedcubing.service.ResultService;
import com.speedcubers.speedcubing.service.SolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private CompetitionService competitionService;
    @Autowired
    private SolveService solveService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private CompetitorRepository competitorRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegistrationRepository registrationRepository;

    // ---- Competitions ----

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

    // ---- Organizers ----

    @GetMapping("/organizers")
    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    @PostMapping("/organizers")
    public Organizer createOrganizer(@RequestBody Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    // ---- Categories ----

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/competitions/{id}/categories")
    public List<Category> getCategoriesByCompetition(@PathVariable Long id) {
        Competition competition = competitionService.findById(id);
        if (competition == null) return List.of();
        return competition.getCategories();
    }

    // ---- Rounds ----

    @GetMapping("/categories/{id}/rounds")
    public List<Round> getRoundsByCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return List.of();
        return category.getRounds();
    }

    // ---- Competitors ----

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
                dto.getTimeMs(), dto.getPenalty());
        if (solve == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(solve);
    }

    @GetMapping("/rounds/{id}/results")
    public List<ResultDTO> getResultsByRound(@PathVariable Long id) {
        List<Result> results = resultService.getResultsByRound(id);
        return results.stream().map(r -> new ResultDTO(
                r.getCompetitor().getId(),
                r.getCompetitor().getFirstName() + " " + r.getCompetitor().getLastName(),
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
                r.getCompetitor().getFirstName() + " " + r.getCompetitor().getLastName(),
                r.getBestTime(),
                r.getAverageTime(),
                r.getRank()
        )).collect(Collectors.toList());
    }

    // ---- Registration ----

    @PostMapping("/registrations")
    public Registration registerCompetitor(@RequestBody Registration registration) {
        return registrationRepository.save(registration);
    }
}
