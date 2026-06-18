package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;

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

        Map<Long, List<Category>> competitionCategories = new LinkedHashMap<>();
        Map<Long, Set<Long>> seenCategoryIds = new HashMap<>();
        for (Round r : roundRepository.findAllRoundsWithCategory()) {
            Long compId = r.getCompetition().getId();
            Category cat = r.getCategory();
            if (!seenCategoryIds.containsKey(compId)) {
                seenCategoryIds.put(compId, new HashSet<>());
            }
            if (seenCategoryIds.get(compId).add(cat.getId())) {
                if (!competitionCategories.containsKey(compId)) {
                    competitionCategories.put(compId, new ArrayList<>());
                }
                competitionCategories.get(compId).add(cat);
            }
        }
        model.addAttribute("competitionCategories", competitionCategories);
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

        List<Category> categories = allRounds.stream()
                .map(Round::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        model.addAttribute("categories", categories);
        model.addAttribute("roundsByCategory", roundsByCategory);
        model.addAttribute("resultsByRound", resultsByRound);
        return "competition-detail";
    }

    @PostMapping("/competitions/{id}/delete")
    public String deleteCompetition(@PathVariable Long id) {
        competitionRepository.deleteById(id);
        return "redirect:/competitions";
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
