package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.CompetitionService;
import com.speedcubers.speedcubing.service.ResultService;
import com.speedcubers.speedcubing.service.SolveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    private final CompetitionService competitionService;
    private final SolveService solveService;
    private final ResultService resultService;
    private final CompetitorRepository competitorRepository;
    private final RoundRepository roundRepository;

    public WebController(CompetitionService competitionService, SolveService solveService,
                         ResultService resultService, CompetitorRepository competitorRepository,
                         RoundRepository roundRepository) {
        this.competitionService = competitionService;
        this.solveService = solveService;
        this.resultService = resultService;
        this.competitorRepository = competitorRepository;
        this.roundRepository = roundRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/competitions")
    public String competitions(Model model) {
        model.addAttribute("competitions", competitionService.findAll());
        return "competitions";
    }

    @GetMapping("/competitions/{id}")
    public String competitionDetail(@PathVariable Long id, Model model) {
        Competition competition = competitionService.findById(id);
        if (competition == null) return "redirect:/competitions";
        model.addAttribute("competition", competition);
        return "competition-detail";
    }

    @GetMapping("/competitors")
    public String competitors(Model model) {
        model.addAttribute("competitors", competitorRepository.findAll());
        return "competitors";
    }

    @PostMapping("/competitors")
    public String addCompetitor(@RequestParam String name, @RequestParam String country) {
        Competitor competitor = new Competitor();
        competitor.setName(name);
        competitor.setCountry(country);
        competitorRepository.save(competitor);
        return "redirect:/competitors";
    }

    @GetMapping("/rounds/{id}")
    public String roundDetail(@PathVariable Long id, Model model) {
        Round round = roundRepository.findById(id).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitors", competitorRepository.findAll());
        model.addAttribute("solves", solveService.getSolvesByRound(id));
        return "round-detail";
    }

    @PostMapping("/rounds/{id}/solves")
    public String addSolve(@PathVariable Long id, @RequestParam Long competitorId,
                           @RequestParam int attemptNumber, @RequestParam int timeMs,
                           @RequestParam(required = false) String penalty) {
        solveService.addSolve(id, competitorId, attemptNumber, timeMs, penalty);
        return "redirect:/rounds/" + id;
    }

    @GetMapping("/results/{roundId}")
    public String results(@PathVariable Long roundId, Model model) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("results", resultService.getResultsByRound(roundId));
        return "results";
    }

    @PostMapping("/results/generate/{roundId}")
    public String generateResults(@PathVariable Long roundId) {
        resultService.generateResults(roundId);
        return "redirect:/results/" + roundId;
    }
}
