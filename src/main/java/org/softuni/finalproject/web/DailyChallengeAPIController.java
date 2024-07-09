package org.softuni.finalproject.web;

import org.softuni.finalproject.model.dto.ChallengeParticipantDTO;
import org.softuni.finalproject.model.dto.CurrentParticipantDataDTO;
import org.softuni.finalproject.model.dto.DailyChallengeDTO;
import org.softuni.finalproject.service.DailyChallengeAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/challenge")
public class DailyChallengeAPIController {

    private final DailyChallengeAPIService dailyChallengeAPIService;

    public DailyChallengeAPIController(DailyChallengeAPIService dailyChallengeAPIService) {
        this.dailyChallengeAPIService = dailyChallengeAPIService;
    }

    @GetMapping("/current")
    public ResponseEntity<DailyChallengeDTO> getDailyChallenge() {
        DailyChallengeDTO todayChallenge = this.dailyChallengeAPIService.getCurrentChallenge();

        return ResponseEntity.ok().body(todayChallenge);
    }

    @GetMapping("/for-user/{username}")
    public ResponseEntity<CurrentParticipantDataDTO> getForUser(@PathVariable String username) {

        CurrentParticipantDataDTO forCurrentUser = this.dailyChallengeAPIService.getForCurrentUser(username);

        return ResponseEntity.ok().body(forCurrentUser);
    }

    @PostMapping("/create")
    public ResponseEntity<DailyChallengeDTO> create() {
        this.dailyChallengeAPIService.create();
        return ResponseEntity.ok().body(this.dailyChallengeAPIService.getCurrentChallenge());
    }

    @PostMapping("/add-participant")
    public ResponseEntity<ChallengeParticipantDTO> addParticipant(
            @RequestBody DailyChallengeDTO dailyChallengeDTO
    ){
        ChallengeParticipantDTO challengeParticipantDTO = this.dailyChallengeAPIService.addParticipant(dailyChallengeDTO);

        return ResponseEntity.ok().build();
    }
}
