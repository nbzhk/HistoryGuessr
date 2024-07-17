package org.softuni.finalproject.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class DailyChallengeNotFound extends RuntimeException {
    private final LocalDate date;

    public DailyChallengeNotFound(LocalDate date) {
        super("Daily challenge not found");
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
