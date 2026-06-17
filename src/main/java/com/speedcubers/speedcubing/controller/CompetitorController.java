package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CompetitorController {

    @Autowired
    private CompetitorRepository competitorRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private SolveRepository solveRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CompetitionRepository competitionRepository;
    @Autowired
    private ResultService resultService;

    // ---- Competitors ----

    @GetMapping("/competitors")
    public String competitors(Model model) {
        model.addAttribute("competitors", competitorRepository.findAll());
        return "competitors";
    }

    @PostMapping("/competitors")
    public String addCompetitor(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,@RequestParam LocalDate birthDate,@RequestParam String country) {
        competitorRepository.save(Competitor.builder()
                .firstName(firstName).lastName(lastName)
                .email(email).birthDate(birthDate).country(country)
                .build());
        return "redirect:/competitors";
    }

    @PostMapping("/competitors/{id}/delete")
    public String deleteCompetitor(@PathVariable Long id) {
        competitorRepository.deleteById(id);
        return "redirect:/competitors";
    }
    @GetMapping("/competitors/{id}")
    public String competitorDetail(@PathVariable Long id, Model model) {
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competitor == null) return "redirect:/competitors";
        model.addAttribute("competitor", competitor);

        model.addAttribute("regsByComp", registrationRepository.findByCompetitorId(id).stream()
                .collect(Collectors.groupingBy(Registration::getCompetition, LinkedHashMap::new, Collectors.toList())));
        model.addAttribute("results", resultRepository.findByCompetitorId(id));

        List<Competition> available = competitionRepository.findAvailableForCompetitor(id);
        model.addAttribute("availableCompetitions", available);
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("competitionCategories", available.stream()
                .collect(Collectors.toMap(Competition::getId,
                    c -> c.getCategories().stream().map(Category::getId).toList())));

        Map<String, Integer> bestSingleMap = solveRepository.findBestSingleByCategory(id).stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> ((Number) r[1]).intValue()));
        Map<String, Double> bestAo5Map = resultRepository.findBestAo5ByCategory(id).stream()
                .collect(Collectors.toMap(r -> (String) r[0], r -> ((Number) r[1]).doubleValue()));

        Set<String> catNames = new TreeSet<>(bestSingleMap.keySet());
        catNames.addAll(bestAo5Map.keySet());
        model.addAttribute("categoryStats", catNames.stream()
                .map(c -> Map.of("categoryName", c, "bestSingle", bestSingleMap.get(c), "bestAo5", bestAo5Map.get(c)))
                .toList());
        return "competitor-detail";
    }

    // ---- Registration ----

    @PostMapping("/competitors/{id}/register")
    public String registerCompetitor(@PathVariable Long id,@RequestParam Long competitionId,@RequestParam List<Long> categoryIds) {
        Competition competition = competitionRepository.findById(competitionId).orElse(null);
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competition == null || competitor == null) return "redirect:/competitors";
        var now = LocalDateTime.now();
        List<Registration> registrations = categoryRepository.findAllById(categoryIds).stream()
                .map(cat -> Registration.builder()
                    .competition(competition).competitor(competitor)
                    .category(cat).registrationDatetime(now).build())
                .toList();
        registrationRepository.saveAll(registrations);
        return "redirect:/competitors/" + id;
    }

    // ---- Results ----

    @GetMapping("/results/{roundId}")
    public String results(@PathVariable Long roundId,@RequestParam Long competitionId, Model model) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitionId", competitionId);
        model.addAttribute("results", resultService.getResultsByRound(round));
        return "results";
    }

    @PostMapping("/results/generate/{roundId}")
    public String generateResults(@PathVariable Long roundId,@RequestParam Long competitionId,RedirectAttributes redirectAttributes) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return "redirect:/competitions";
        List<Result> generated = resultService.generateResults(round);
        if (generated.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                "Cannot generate results for \"" + round.getName() + "\": need at least 2 competitors with 5 solves each.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Results generated successfully.");
        }
        return "redirect:/competitions/" + competitionId;
    }
}
