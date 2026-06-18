package com.speedcubers.speedcubing.controller;

import com.speedcubers.speedcubing.entity.*;
import com.speedcubers.speedcubing.repository.*;
import com.speedcubers.speedcubing.service.SolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoundController {

    @Autowired
    private SolveService solveService;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private SolveRepository solveRepository;
    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private ResultRepository resultRepository;

    @GetMapping("/rounds/{id}")
    public String roundDetail(@PathVariable Long id,
                              @RequestParam Long competitionId,
                              Model model) {
        Round round = roundRepository.findById(id).orElse(null);
        if (round == null) return "redirect:/competitions";
        model.addAttribute("round", round);
        model.addAttribute("competitionId", competitionId);
        model.addAttribute("competitors",
            registrationRepository.findCompetitorsByCompetitionAndCategory(
                competitionId, round.getCategory().getId()));
        model.addAttribute("solves", solveRepository.findByRound(round));
        model.addAttribute("hasResults", !resultRepository.findByRound(round).isEmpty());
        return "round-detail";
    }

    @PostMapping("/rounds/{id}/solves")
    public String addSolve(@PathVariable Long id,
                           @RequestParam Long competitorId,
                           @RequestParam int timeMs,
                           @RequestParam(required = false) String penalty,
                           @RequestParam Long competitionId,
                           RedirectAttributes redirectAttributes) {
        String error = solveService.addSolve(id, competitorId, timeMs, penalty);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
        }
        return "redirect:/rounds/" + id + "?competitionId=" + competitionId;
    }

    @PostMapping("/solves/{id}/delete")
    @Transactional
    public String deleteSolve(@PathVariable Long id,
                              @RequestParam Long competitionId) {
        Solve solve = solveRepository.findById(id).orElse(null);
        if (solve != null) {
            Round round = solve.getRound();
            Long roundId = round.getId();
            resultRepository.deleteByRound(round);
            solveRepository.deleteById(id);
            return "redirect:/rounds/" + roundId + "?competitionId=" + competitionId;
        }
        return "redirect:/competitions";
    }

    @PostMapping("/rounds/{id}/delete")
    public String deleteRound(@PathVariable Long id,
                              @RequestParam Long competitionId) {
        Round round = roundRepository.findById(id).orElse(null);
        if (round != null) {
            roundRepository.deleteById(id);
            return "redirect:/competitions/" + competitionId;
        }
        return "redirect:/competitions";
    }
}
