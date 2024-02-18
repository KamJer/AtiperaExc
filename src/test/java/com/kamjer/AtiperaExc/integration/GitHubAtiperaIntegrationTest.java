package com.kamjer.AtiperaExc.integration;

import com.kamjer.AtiperaExc.client.GitHubClient;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Log
public class GitHubAtiperaIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testGetRepositoryFromOwnerWithoutToken() throws Exception {
        // Arrange
        String headerValue = "application/json";
        String owner = "KamJer";

        when(gitHubClient.getGitHubRepositories(Optional.empty(), owner)).thenReturn(new ArrayList<>());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{owner}/repos", owner)
                        .header(HttpHeaders.ACCEPT, headerValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(gitHubClient).getGitHubRepositories(Optional.empty(), owner);
    }

    @Test
    public void testGetRepositoryFromOwnerWithToken() throws Exception {
        // Arrange
        String headerValue = "application/json";
        String owner = "KamJer";
        String token = "token";

        when(gitHubClient.getGitHubRepositories(Optional.of(token), owner)).thenReturn(new ArrayList<>());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/{owner}/repos", owner)
                        .header(HttpHeaders.ACCEPT, headerValue)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(gitHubClient).getGitHubRepositories(Optional.of(token), owner);
    }
}
