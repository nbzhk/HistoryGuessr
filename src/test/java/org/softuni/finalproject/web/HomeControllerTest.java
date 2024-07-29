package org.softuni.finalproject.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHomeController_ShouldReturnIndex_WhenUserIsAnonymous() {
        Authentication authentication = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        String viewName = homeController.homeNotLoggedIn(securityContext);

        assertEquals("index", viewName);
    }

    @Test
    void testHomeController_ShouldReturnHome_WhenUserIsAuthenticated() {
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        String viewName = homeController.homeNotLoggedIn(securityContext);

        assertEquals("home", viewName);
    }


}
