package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.softuni.finalproject.service.exception.DailyChallengeNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/challenge")
public class DailyChallengeAPIController {

    private final DailyChallengeAPIService dailyChallengeAPIService;

    public DailyChallengeAPIController(DailyChallengeAPIService dailyChallengeAPIService) {
        this.dailyChallengeAPIService = dailyChallengeAPIService;
    }

    @GetMapping("/create")
    public void createChallenge() {
        this.dailyChallengeAPIService.create();
    }

    @GetMapping("/current")
    public ResponseEntity<DailyChallengeDTO> getDailyChallenge() {
        DailyChallengeDTO todayChallenge = this.dailyChallengeAPIService.getCurrentChallenge();

        if (todayChallenge == null) {
            throw new DailyChallengeNotFound(LocalDate.now());
        }

        return ResponseEntity.ok().body(todayChallenge);
    }

    @GetMapping("/for-user/{username}")
    public ResponseEntity<CurrentParticipantDataDTO> getForUser(@PathVariable String username) {

        CurrentParticipantDataDTO forCurrentUser = this.dailyChallengeAPIService.getForCurrentUser(username);

        return ResponseEntity.ok().body(forCurrentUser);
    }

    @GetMapping("/challenge/user-already-participated")
    public ResponseEntity<ChallengeParticipantDTO> getUserParticipated() {

        ChallengeParticipantDTO challengeParticipantDTO = this.dailyChallengeAPIService.userAlreadyParticipated();

        return ResponseEntity.ok().body(challengeParticipantDTO);
    }

    @PostMapping("/add-participant")
    public ResponseEntity<ChallengeParticipantDTO> addParticipant(
            @RequestBody DailyChallengeDTO dailyChallengeDTO) {

        this.dailyChallengeAPIService.addParticipant(dailyChallengeDTO);

        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(DailyChallengeNotFound.class)
    public ResponseEntity<String> handleDailyChallengeNotFound(DailyChallengeNotFound exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
