package org.softuni.finalproject.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String homeNotLoggedIn(@CurrentSecurityContext SecurityContext context) {

        if (context.getAuthentication() instanceof AnonymousAuthenticationToken) {
            return "index";
        }

        return "home";
    }

}
