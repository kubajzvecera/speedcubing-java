package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import org.apache.el.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CompetitionController {

    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ---- Competitions ----

    @GetMapping("/competitions")
    public String competitions(Model model) {
        model.addAttribute("competitions", competitionRepository.findAll());
        model.addAttribute("allCategories", categoryRepository.findAll());
        return "competitions";
    }

    @PostMapping("/competitions")
    public String createCompetition(@RequestParam String name,@RequestParam LocalDate date,@RequestParam String location,@RequestParam LocalDate endDate) {
        competitionRepository.save(Competition.builder()
                .name(name).date(date).location(location).endDate(endDate).build());
        return "redirect:/competitions";
    }

    @GetMapping("/competitions/{id}")
    public String competitionDetail(@PathVariable Long id, Model model) {
        Competition competition = competitionRepository.findById(id).orElse(null);
        if (competition == null) return "redirect:/competitions";
        model.addAttribute("competition", competition);
        model.addAttribute("allCategories", categoryRepository.findAll());

        List<Round> allRounds = competition.getRounds();
        Map<Long, List<Round>> roundsByCategory = allRounds.stream()
                .collect(Collectors.groupingBy(r -> r.getCategory().getId()));

        List<Result> allResults = resultRepository.findByRoundIn(allRounds);
        Map<Long, List<Result>> resultsByRound = allResults.stream()
                .collect(Collectors.groupingBy(r -> r.getRound().getId()));

        model.addAttribute("roundsByCategory", roundsByCategory);
        model.addAttribute("resultsByRound", resultsByRound);
        return "competition-detail";
    }

    @PostMapping("/competitions/{id}/delete")
    public String deleteCompetition(@PathVariable Long id) {
        competitionRepository.deleteById(id);
        return "redirect:/competitions";
    }

    // ---- Categories ----

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @PostMapping("/categories")
    public String addCategory(@RequestParam String name) {
        categoryRepository.save(Category.builder().name(name).build());
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    // ---- Rounds within competition ----

    @PostMapping("/competitions/{id}/add-round")
    public String addRoundToCompetition(@PathVariable Long id,@RequestParam Long categoryId,@RequestParam String name,@RequestParam int roundNumber) {
        roundRepository.save(Round.builder()
                .name(name)
                .roundNumber(roundNumber)
                .category(categoryRepository.findById(categoryId).orElse(null))
                .competition(competitionRepository.findById(id).orElse(null))
                .build());
        return "redirect:/competitions/" + id;
    }
}
