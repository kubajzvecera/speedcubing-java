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

        List<Solve> allSolves = solveRepository.findByCompetitorId(id);
        List<Result> allResults = resultRepository.findByCompetitorId(id);

        //collects all registrations for competitions
        List<Registration> registrations = registrationRepository.findByCompetitorId(id);
        Map<Competition, List<Registration>> regsByComp = new LinkedHashMap<>();
        for (Registration reg : registrations) {
            if (!regsByComp.containsKey(reg.getCompetition())) {
                regsByComp.put(reg.getCompetition(), new ArrayList<>());
            }
            regsByComp.get(reg.getCompetition()).add(reg);
        }
        model.addAttribute("regsByComp", regsByComp);
        model.addAttribute("results", allResults);

        //competitions where he hasn't registered
        List<Competition> available = competitionRepository.findAvailableForCompetitor(id);
        model.addAttribute("availableCompetitions", available);
        model.addAttribute("allCategories", categoryRepository.findAll());


        Set<Long> availableIds = new HashSet<>();
        for (Competition c : available) {
            availableIds.add(c.getId());
        }
        //all competition categories [competition_id : [category_id]]
        Map<Long, List<Long>> competitionCategories = new LinkedHashMap<>();
        Map<Long, Set<Long>> seenCategoryIds = new HashMap<>();
        for (Round r : roundRepository.findAllRoundsWithCategory()) {
            Long compId = r.getCompetition().getId();
            if (!availableIds.contains(compId)) {
                continue;
            }
            if (!seenCategoryIds.containsKey(compId)) {
                seenCategoryIds.put(compId, new HashSet<>());
            }
            if (seenCategoryIds.get(compId).add(r.getCategory().getId())) {
                if (!competitionCategories.containsKey(compId)) {
                    competitionCategories.put(compId, new ArrayList<>());
                }
                competitionCategories.get(compId).add(r.getCategory().getId());
            }
        }
        model.addAttribute("competitionCategories", competitionCategories);

        //best times at each category solver attempted

        Map<String, Integer> bestSingleMap = new LinkedHashMap<>();
        for (Solve solve : allSolves) {
            //skip worst solves
            if ("DNF".equals(solve.getPenalty())) continue;
            String cat = solve.getRound().getCategory().getName();
            Integer current = bestSingleMap.get(cat);
            if (current == null || solve.getTimeMs() < current) {
                bestSingleMap.put(cat, solve.getTimeMs());
            }
        }
        //best times in ao5 in results
        Map<String, Double> bestAo5Map = new LinkedHashMap<>();
        for (Result result : allResults) {
            String cat = result.getRound().getCategory().getName();
            Double current = bestAo5Map.get(cat);
            if (current == null || result.getAverageTime() < current) {
                bestAo5Map.put(cat, result.getAverageTime());
            }
        }
        //set of categories solver attempted
        Set<String> catNames = new HashSet<>();

        catNames.addAll(bestSingleMap.keySet()); // {category_id : time_ms} takes category_id
        catNames.addAll(bestAo5Map.keySet()); // same

        List<String> sortedCats = new ArrayList<>(catNames);
        Collections.sort(sortedCats);

        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (String cat : sortedCats) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("categoryName", cat);
            stat.put("bestSingle", bestSingleMap.get(cat));
            stat.put("bestAo5", bestAo5Map.get(cat));
            categoryStats.add(stat);
        }
        model.addAttribute("categoryStats", categoryStats);
        return "competitor-detail";
    }

    // ---- Registration ----

    @PostMapping("/competitors/{id}/register")
    public String registerCompetitor(@PathVariable Long id,@RequestParam Long competitionId,@RequestParam List<Long> categoryIds) {
        Competition competition = competitionRepository.findById(competitionId).orElse(null);
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competition == null || competitor == null) return "redirect:/competitors";
        List<Registration> registrations = categoryRepository.findAllById(categoryIds).stream()
                .map(cat -> Registration.builder()
                    .competition(competition).competitor(competitor)
                    .category(cat).build())
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
