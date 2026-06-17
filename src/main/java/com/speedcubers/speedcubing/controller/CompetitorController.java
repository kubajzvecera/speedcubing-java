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
    public String addCompetitor(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) String email,
                                @RequestParam LocalDate birthDate,
                                @RequestParam(required = false) String country) {
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
        List<Registration> registrations = registrationRepository.findByCompetitorId(id);
        Map<Competition, List<Registration>> regsByComp = new LinkedHashMap<>();
        for (Registration reg : registrations) {
            regsByComp.computeIfAbsent(reg.getCompetition(), k -> new ArrayList<>()).add(reg);
        }
        model.addAttribute("regsByComp", regsByComp);
        List<Solve> allSolves = solveRepository.findByCompetitorId(id);
        List<Result> allResults = resultRepository.findByCompetitorId(id);
        model.addAttribute("results", allResults);

        List<Long> registeredIds = registrations.stream()
                .map(r -> r.getCompetition().getId()).toList();
        List<Competition> allCompetitions = competitionRepository.findAll();
        List<Competition> available = allCompetitions.stream()
                .filter(c -> !registeredIds.contains(c.getId()))
                .filter(c -> !c.getCategories().isEmpty())
                .toList();
        model.addAttribute("availableCompetitions", available);
        model.addAttribute("allCategories", categoryRepository.findAll());

        Map<Long, List<Long>> competitionCategories = new HashMap<>();
        for (Competition c : allCompetitions) {
            competitionCategories.put(c.getId(),
                c.getCategories().stream().map(Category::getId).toList());
        }
        model.addAttribute("competitionCategories", competitionCategories);

        List<String> categories = allSolves.stream()
                .map(s -> s.getRound().getCategory().getName())
                .distinct().sorted().toList();
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (String cat : categories) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("categoryName", cat);
            OptionalInt bestSingleOpt = allSolves.stream()
                    .filter(s -> s.getRound().getCategory().getName().equals(cat) && !"DNF".equals(s.getPenalty()))
                    .mapToInt(Solve::getTimeMs).min();
            stat.put("bestSingle", bestSingleOpt.isPresent() ? bestSingleOpt.getAsInt() : null);
            OptionalDouble bestAo5Opt = allResults.stream()
                    .filter(r -> r.getRound().getCategory().getName().equals(cat))
                    .mapToDouble(Result::getAverageTime).min();
            stat.put("bestAo5", bestAo5Opt.isPresent() ? bestAo5Opt.getAsDouble() : null);
            categoryStats.add(stat);
        }
        model.addAttribute("categoryStats", categoryStats);
        return "competitor-detail";
    }

    // ---- Registration ----

    @PostMapping("/competitors/{id}/register")
    public String registerCompetitor(@PathVariable Long id,
                                     @RequestParam Long competitionId,
                                     @RequestParam List<Long> categoryIds) {
        Competition competition = competitionRepository.findById(competitionId).orElse(null);
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competition == null || competitor == null) return "redirect:/competitors";
        var now = LocalDateTime.now();
        for (Long catId : categoryIds) {
            Category category = categoryRepository.findById(catId).orElse(null);
            if (category == null) continue;
            registrationRepository.save(Registration.builder()
                    .competition(competition).competitor(competitor)
                    .category(category).registrationDatetime(now).build());
        }
        return "redirect:/competitors/" + id;
    }

    // ---- Results ----

    @GetMapping("/results/{roundId}")
    public String results(@PathVariable Long roundId,
                          @RequestParam Long competitionId, Model model) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitionId", competitionId);
        model.addAttribute("results", resultService.getResultsByRound(round));
        return "results";
    }

    @PostMapping("/results/generate/{roundId}")
    public String generateResults(@PathVariable Long roundId,
                                  @RequestParam Long competitionId,
                                  RedirectAttributes redirectAttributes) {
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
