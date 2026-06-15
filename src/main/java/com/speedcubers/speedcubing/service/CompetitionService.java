package com.speedcubers.speedcubing.service;

import com.speedcubers.speedcubing.entity.Competition;
import com.speedcubers.speedcubing.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    public List<Competition> findAll() {
        return competitionRepository.findAll();
    }

    public Competition findById(Long id) {
        return competitionRepository.findById(id).orElse(null);
    }

    public Competition create(Competition competition) {
        return competitionRepository.save(competition);
    }

    public void delete(Long id) {
        competitionRepository.deleteById(id);
    }
}
