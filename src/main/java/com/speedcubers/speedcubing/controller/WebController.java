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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private OrganizerRepository organizerRepository;
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
        model.addAttribute("organizers", organizerRepository.findAll());
        model.addAttribute("allCategories", categoryRepository.findAll());
        return "competitions";
    }

    @PostMapping("/competitions")
    public String createCompetition(@RequestParam String name, @RequestParam LocalDate date,
                                    @RequestParam String location, @RequestParam(required = false) LocalDate endDate,
                                    @RequestParam(required = false) Long organizerId,
                                    @RequestParam(required = false) List<Long> categoryIds) {
        Competition competition = new Competition();
        competition.setName(name);
        competition.setDate(date);
        competition.setLocation(location);
        competition.setEndDate(endDate);
        if (organizerId != null) {
            competition.setOrganizer(organizerRepository.findById(organizerId).orElse(null));
        }
        if (categoryIds != null) {
            competition.setCategories(categoryRepository.findAllById(categoryIds));
        }
        competitionService.create(competition);
        return "redirect:/competitions";
    }

    @GetMapping("/competitions/{id}")
    public String competitionDetail(@PathVariable Long id, Model model) {
        Competition competition = competitionService.findById(id);
        if (competition == null) return "redirect:/competitions";
        model.addAttribute("competition", competition);
        model.addAttribute("organizers", organizerRepository.findAll());
        model.addAttribute("competitors", competitorRepository.findAll());

        Map<Long, List<Result>> resultsByRound = new HashMap<>();
        if (competition.getCategories() != null) {
            for (Category cat : competition.getCategories()) {
                if (cat.getRounds() != null) {
                    for (Round round : cat.getRounds()) {
                        List<Result> results = resultRepository.findByRound(round);
                        if (results != null && !results.isEmpty()) {
                            results.sort(Comparator.comparingInt(Result::getRank));
                            resultsByRound.put(round.getId(), results);
                        }
                    }
                }
            }
        }
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
        return "categories";
    }

    @PostMapping("/categories")
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    // ---- Rounds ----

    @PostMapping("/categories/{id}/rounds")
    public String addRound(@PathVariable Long id, @RequestParam String name,
                           @RequestParam int roundNumber,
                           @RequestParam Long competitionId) {
        Round round = new Round();
        round.setName(name);
        round.setRoundNumber(roundNumber);
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return "redirect:/competitions";
        round.setCategory(category);
        roundRepository.save(round);
        return "redirect:/competitions/" + competitionId;
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
        return "round-detail";
    }

    @PostMapping("/rounds/{id}/solves")
    public String addSolve(@PathVariable Long id, @RequestParam Long competitorId,
                           @RequestParam int timeMs,
                           @RequestParam(required = false) String penalty,
                           @RequestParam(required = false) Long competitionId) {
        solveService.addSolve(id, competitorId, timeMs, penalty);
        if (competitionId != null) {
            return "redirect:/rounds/" + id + "?competitionId=" + competitionId;
        }
        return "redirect:/rounds/" + id;
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

    // ---- Organizers ----

    @GetMapping("/organizers")
    public String organizers(Model model) {
        model.addAttribute("organizers", organizerRepository.findAll());
        return "organizers";
    }

    @PostMapping("/organizers")
    public String addOrganizer(@RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam String email) {
        Organizer organizer = new Organizer();
        organizer.setFirstName(firstName);
        organizer.setLastName(lastName);
        organizer.setEmail(email);
        organizerRepository.save(organizer);
        return "redirect:/organizers";
    }

    @PostMapping("/organizers/{id}/delete")
    public String deleteOrganizer(@PathVariable Long id) {
        organizerRepository.deleteById(id);
        return "redirect:/organizers";
    }

    // ---- Competitors ----

    @GetMapping("/competitors")
    public String competitors(Model model) {
        model.addAttribute("competitors", competitorRepository.findAll());
        return "competitors";
    }

    @PostMapping("/competitors")
    public String addCompetitor(@RequestParam String firstName, @RequestParam String lastName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String birthDate,
                                @RequestParam String country) {
        Competitor competitor = new Competitor();
        competitor.setFirstName(firstName);
        competitor.setLastName(lastName);
        competitor.setEmail(email);
        if (birthDate != null && !birthDate.isEmpty())
            competitor.setBirthDate(LocalDate.parse(birthDate));
        competitor.setCountry(country);
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
        List<Registration> registrations = registrationRepository.findByCompetitorId(id);
        model.addAttribute("registrations", registrations);
        model.addAttribute("results", resultRepository.findByCompetitorId(id));
        model.addAttribute("solves", solveRepository.findByCompetitorId(id));

        // competitions not yet registered
        List<Long> registeredIds = registrations.stream()
                .map(r -> r.getCompetition().getId()).toList();
        List<Competition> allCompetitions = competitionService.findAll();
        List<Competition> available = allCompetitions.stream()
                .filter(c -> !registeredIds.contains(c.getId()))
                .toList();
        model.addAttribute("availableCompetitions", available);
        model.addAttribute("allCategories", categoryRepository.findAll());

        // best single
        Integer bestSingle = solveRepository.findByCompetitorId(id).stream()
                .filter(s -> !"DNF".equals(s.getPenalty()))
                .map(Solve::getTimeMs)
                .min(Integer::compareTo).orElse(null);
        model.addAttribute("bestSingle", bestSingle);

        // best Ao5
        Double bestAo5 = resultRepository.findByCompetitorId(id).stream()
                .mapToDouble(Result::getAverageTime)
                .min().orElse(Double.NaN);
        model.addAttribute("bestAo5", Double.isNaN(bestAo5) ? null : bestAo5);

        return "competitor-detail";
    }

    // ---- Registration ----

    @PostMapping("/competitors/{id}/register")
    public String registerCompetitor(@PathVariable Long id, @RequestParam Long competitionId,
                                     @RequestParam(required = false) List<Long> categoryIds) {
        Competition competition = competitionService.findById(competitionId);
        Competitor competitor = competitorRepository.findById(id).orElse(null);
        if (competition == null || competitor == null) return "redirect:/competitors";
        Registration registration = new Registration();
        registration.setCompetition(competition);
        registration.setCompetitor(competitor);
        registration.setRegistrationDatetime(LocalDateTime.now());
        if (categoryIds != null) {
            registration.setCategories(categoryRepository.findAllById(categoryIds));
        }
        registrationRepository.save(registration);
        return "redirect:/competitors/" + id;
    }
}
