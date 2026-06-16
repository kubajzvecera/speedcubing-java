package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.CompetitionService;
import com.speedcubers.speedcubing.service.ResultService;
import com.speedcubers.speedcubing.service.SolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

import com.speedcubers.speedcubing.dto.*; // CompetitionFormDTO, CategoryFormDTO, CompetitorFormDTO, RoundFormDTO, RegistrationFormDTO, SolveDTO
import com.speedcubers.speedcubing.entity.Result;

@Controller
public class WebController {

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
    private CategoryRepository categoryRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SolveRepository solveRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ---- Competitions ----

    @GetMapping("/competitions")
    public String competitions(Model model) {
        model.addAttribute("competitions", competitionService.findAll());
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("form", new CompetitionFormDTO());
        return "competitions";
    }

    @PostMapping("/competitions")
    public String createCompetition(@ModelAttribute CompetitionFormDTO form) {
        Competition competition = new Competition();
        competition.setName(form.getName());
        competition.setDate(form.getDate());
        competition.setLocation(form.getLocation());
        competition.setEndDate(form.getEndDate());
        competitionService.create(competition);
        return "redirect:/competitions";
    }

    @GetMapping("/competitions/{id}")
    public String competitionDetail(@PathVariable Long id, Model model) {
        Competition competition = competitionService.findById(id);
        if (competition == null) return "redirect:/competitions";
        model.addAttribute("competition", competition);
        model.addAttribute("competitors", competitorRepository.findAll());
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("roundForm", new RoundFormDTO());

        // Build per-category rounds for this competition
        Map<Long, List<Round>> roundsByCategory = new HashMap<>();
        Map<Long, List<Result>> resultsByRound = new HashMap<>();
        if (competition.getCategories() != null) {
            for (Category cat : competition.getCategories()) {
                List<Round> catRounds = new ArrayList<>();
                for (Round r : competition.getRounds()) {
                    if (r.getCategory().getId().equals(cat.getId())) {
                        catRounds.add(r);
                        List<Result> results = resultRepository.findByRound(r);
                        if (results != null && !results.isEmpty()) {
                            results.sort(Comparator.comparingInt(Result::getRank));
                            resultsByRound.put(r.getId(), results);
                        }
                    }
                }
                roundsByCategory.put(cat.getId(), catRounds);
            }
        }
        model.addAttribute("roundsByCategory", roundsByCategory);
        model.addAttribute("resultsByRound", resultsByRound);

        return "competition-detail";
    }

    @PostMapping("/competitions/{id}/delete")
    public String deleteCompetition(@PathVariable Long id) {
        competitionService.delete(id);
        return "redirect:/competitions";
    }

    // ---- Categories (global) ----

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("form", new CategoryFormDTO());
        return "categories";
    }

    @PostMapping("/categories")
    public String addCategory(@ModelAttribute CategoryFormDTO form) {
        Category category = new Category();
        category.setName(form.getName());
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    // ---- Rounds ----

    @PostMapping("/competitions/{id}/add-round")
    public String addRoundToCompetition(@PathVariable Long id,
                                        @RequestParam Long categoryId,
                                        @RequestParam String name,
                                        @RequestParam int roundNumber) {
        Round round = new Round();
        round.setName(name);
        round.setRoundNumber(roundNumber);
        round.setCategory(categoryRepository.findById(categoryId).orElse(null));
        round.setCompetition(competitionService.findById(id));
        roundRepository.save(round);
        return "redirect:/competitions/" + id;
    }

    @PostMapping("/categories/{id}/rounds")
    public String addRound(@PathVariable Long id, @ModelAttribute RoundFormDTO form) {
        Round round = new Round();
        round.setName(form.getName());
        round.setRoundNumber(form.getRoundNumber());
        Category category = categoryRepository.findById(id).orElse(null);
        Competition competition = competitionService.findById(form.getCompetitionId());
        if (category == null || competition == null) return "redirect:/competitions";
        round.setCategory(category);
        round.setCompetition(competition);
        roundRepository.save(round);
        return "redirect:/competitions/" + form.getCompetitionId();
    }

    @GetMapping("/rounds/{id}")
    public String roundDetail(@PathVariable Long id,
                              @RequestParam(required = false) Long competitionId,
                              Model model) {
        Round round = roundRepository.findById(id).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitionId", competitionId);
        if (competitionId != null && round.getCategory() != null) {
            model.addAttribute("competitors",
                registrationRepository.findCompetitorsByCompetitionAndCategory(
                    competitionId, round.getCategory().getId()));
        } else {
            model.addAttribute("competitors", competitorRepository.findAll());
        }
        model.addAttribute("solves", solveService.getSolvesByRound(id));
        model.addAttribute("hasResults", !resultRepository.findByRound(round).isEmpty());
        model.addAttribute("solveForm", new SolveDTO());
        return "round-detail";
    }

    @PostMapping("/rounds/{id}/solves")
    public String addSolve(@PathVariable Long id, @ModelAttribute SolveDTO form,
                           RedirectAttributes redirectAttributes) {
        String error = solveService.addSolve(id, form.getCompetitorId(), form.getTimeMs(), form.getPenalty());
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
        }
        if (form.getCompetitionId() != null) {
            return "redirect:/rounds/" + id + "?competitionId=" + form.getCompetitionId();
        }
        return "redirect:/rounds/" + id;
    }

    @PostMapping("/solves/{id}/delete")
    public String deleteSolve(@PathVariable Long id,
                              @RequestParam(required = false) Long competitionId) {
        Solve solve = solveRepository.findById(id).orElse(null);
        if (solve != null) {
            Long roundId = solve.getRound().getId();
            solveRepository.deleteById(id);
            if (competitionId != null) {
                return "redirect:/rounds/" + roundId + "?competitionId=" + competitionId;
            }
            return "redirect:/rounds/" + roundId;
        }
        return "redirect:/competitions";
    }

    @PostMapping("/rounds/{id}/delete")
    public String deleteRound(@PathVariable Long id,
                              @RequestParam(required = false) Long competitionId) {
        Round round = roundRepository.findById(id).orElse(null);
        if (round != null) {
            Long compId = competitionId != null ? competitionId : round.getCompetition().getId();
            roundRepository.deleteById(id);
            return "redirect:/competitions/" + compId;
        }
        return "redirect:/competitions";
    }

    // ---- Results ----

    @GetMapping("/results/{roundId}")
    public String results(@PathVariable Long roundId,
                          @RequestParam(required = false) Long competitionId,
                          Model model) {
        Round round = roundRepository.findById(roundId).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitionId", competitionId);
        model.addAttribute("results", resultService.getResultsByRound(roundId));
        return "results";
    }

    @PostMapping("/results/generate/{roundId}")
    public String generateResults(@PathVariable Long roundId,
                                  @RequestParam(required = false) Long competitionId,
                                  RedirectAttributes redirectAttributes) {
        List<Result> generated = resultService.generateResults(roundId);
        if (generated.isEmpty()) {
            String roundName = roundRepository.findById(roundId).map(Round::getName).orElse("Unknown");
            redirectAttributes.addFlashAttribute("error",
                "Cannot generate results for \"" + roundName + "\": need at least 2 competitors with 5 solves each.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Results generated successfully.");
        }
        if (competitionId != null) {
            return "redirect:/competitions/" + competitionId;
        }
        return "redirect:/competitions";
    }

    // ---- Competitors ----

    @GetMapping("/competitors")
    public String competitors(Model model) {
        model.addAttribute("competitors", competitorRepository.findAll());
        model.addAttribute("form", new CompetitorFormDTO());
        return "competitors";
    }

    @PostMapping("/competitors")
    public String addCompetitor(@ModelAttribute CompetitorFormDTO form) {
        Competitor competitor = new Competitor();
        competitor.setFirstName(form.getFirstName());
        competitor.setLastName(form.getLastName());
        competitor.setEmail(form.getEmail());
        competitor.setBirthDate(form.getBirthDate());
        competitor.setCountry(form.getCountry());
        competitorRepository.save(competitor);
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
        model.addAttribute("registrationForm", new RegistrationFormDTO());
        List<Registration> registrations = registrationRepository.findByCompetitorId(id);
        Map<Competition, List<Registration>> regsByComp = new LinkedHashMap<>();
        for (Registration reg : registrations) {
            regsByComp.computeIfAbsent(reg.getCompetition(), k -> new ArrayList<>()).add(reg);
        }
        model.addAttribute("regsByComp", regsByComp);
        List<Solve> allSolves = solveRepository.findByCompetitorId(id);
        List<Result> allResults = resultRepository.findByCompetitorId(id);
        model.addAttribute("solves", allSolves);
        model.addAttribute("results", allResults);

        // competitions not yet registered
        List<Long> registeredIds = registrations.stream()
                .map(r -> r.getCompetition().getId()).toList();
        List<Competition> allCompetitions = competitionService.findAll();
        List<Competition> available = allCompetitions.stream()
                .filter(c -> !registeredIds.contains(c.getId()))
                .filter(c -> !c.getCategories().isEmpty())
                .toList();
        model.addAttribute("availableCompetitions", available);
        model.addAttribute("allCategories", categoryRepository.findAll());

        // competition -> category IDs map for JS filtering
        Map<Long, List<Long>> competitionCategories = new HashMap<>();
        for (Competition c : allCompetitions) {
            competitionCategories.put(c.getId(),
                c.getCategories().stream().map(Category::getId).toList());
        }
        model.addAttribute("competitionCategories", competitionCategories);

        // per-category best single + best Ao5
        List<String> categories = allSolves.stream()
                .map(s -> s.getRound().getCategory().getName())
                .distinct()
                .sorted()
                .toList();
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (String cat : categories) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("categoryName", cat);

            OptionalInt bestSingleOpt = allSolves.stream()
                    .filter(s -> s.getRound().getCategory().getName().equals(cat) && !"DNF".equals(s.getPenalty()))
                    .mapToInt(Solve::getTimeMs)
                    .min();
            stat.put("bestSingle", bestSingleOpt.isPresent() ? bestSingleOpt.getAsInt() : null);

            OptionalDouble bestAo5Opt = allResults.stream()
                    .filter(r -> r.getRound().getCategory().getName().equals(cat))
                    .mapToDouble(Result::getAverageTime)
                    .min();
            stat.put("bestAo5", bestAo5Opt.isPresent() ? bestAo5Opt.getAsDouble() : null);

            categoryStats.add(stat);
        }
        model.addAttribute("categoryStats", categoryStats);

        return "competitor-detail";
    }

    // ---- Registration ----

    @PostMapping("/competitors/{id}/register")
    public String registerCompetitor(@PathVariable Long id, @ModelAttribute RegistrationFormDTO form) {
        Competition competition = competitionService.findById(form.getCompetitionId());
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competition == null || competitor == null) return "redirect:/competitors";
        if (!form.getCategoryIds().isEmpty()) {
            var now = LocalDateTime.now();
            for (Long catId : form.getCategoryIds()) {
                Category category = categoryRepository.findById(catId).orElse(null);
                if (category == null) continue;
                Registration registration = new Registration();
                registration.setCompetition(competition);
                registration.setCompetitor(competitor);
                registration.setCategory(category);
                registration.setRegistrationDatetime(now);
                registrationRepository.save(registration);
            }
        }
        return "redirect:/competitors/" + id;
    }
}
