package com.speedcubers.speedcubing;

import com.speedcubers.speedcubing.repository.RoundRepository;
import com.speedcubers.speedcubing.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeedcubingApplication implements ApplicationRunner {

    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private ResultService resultService;

    public static void main(String[] args) {
        SpringApplication.run(SpeedcubingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        roundRepository.findAll().forEach(round -> resultService.generateResults(round));
    }
}
