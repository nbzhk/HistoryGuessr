package org.softuni.finalproject.web;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public String handle404(Model model) {

        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());

        return "error";
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String exception500(Error e, Model model) {


        model.addAttribute("errorMessage", "Something went wrong");
        model.addAttribute("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return "error";
    }

}
