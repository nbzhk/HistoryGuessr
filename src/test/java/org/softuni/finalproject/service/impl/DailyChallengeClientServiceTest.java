//package org.softuni.finalproject.service.impl;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.softuni.finalproject.model.dto.DailyChallengeDTO;
//import org.softuni.finalproject.repository.DailyChallengeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.client.RestClient;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@AutoConfigureMockRestServiceServer
//@ExtendWith(MockitoExtension.class)
//public class DailyChallengeClientServiceTest {
//    @Autowired
//    private RestClient restClient;
//
//    @Autowired
//    private MockRestServiceServer mockServer;
//
//    @Autowired
//    private DailyChallengeRepository dailyChallengeRepository;
//
//    @InjectMocks
//    private DailyChallengeClientService dailyChallengeClientService;
//
//    @BeforeEach
//    public void setUp() {
//
//
//    }
//
//    @Test
//    public void getDailyChallengeTest() {
//        DailyChallengeDTO expected = new DailyChallengeDTO();
//
//        when(this.restClient
//                .get()
//                .uri(anyString())
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .body(DailyChallengeDTO.class))
//                .thenReturn(expected);
//
//        DailyChallengeDTO actual = dailyChallengeClientService.getDailyChallenge();
//        assertEquals(expected, actual);
//    }
//}
