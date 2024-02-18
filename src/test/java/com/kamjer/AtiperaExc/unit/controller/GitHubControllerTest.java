package com.kamjer.AtiperaExc.unit.controller;

import com.kamjer.AtiperaExc.controller.GitHubController;
import com.kamjer.AtiperaExc.model.OwnerDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import com.kamjer.AtiperaExc.model.RepositoryResponse;
import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GitHubControllerTest {

    @MockBean
    private GitHubService gitHubService;

    private GitHubController gitHubController;

    @BeforeEach
    void setUp() {
        gitHubService = Mockito.mock(GitHubService.class);
        String acceptedHeaderValue = "application/json";
        String notAcceptableMessage = "Header value not-acceptable";
        gitHubController = new GitHubController(gitHubService, acceptedHeaderValue, notAcceptableMessage);
    }

    @Test
    void testGetRepositoryFromOwnerWithToken() throws ErrorResponseException, RestClientException {
        // Arrange
        String headerValue = "application/json";
        String token = "yourToken";
        String owner = "owner";
        RepositoryDto repoDto = Mockito.mock(RepositoryDto.class);
        List<RepositoryResponse> repositoryResponses = Collections.singletonList(new RepositoryResponse(repoDto, new ArrayList<>()));

        Mockito.when(gitHubService.getGitHubRepository(owner, token))
                .thenReturn(repositoryResponses);

        // Act
        ResponseEntity<List<RepositoryResponse>> response = gitHubController.getRepositoryFromOwner(headerValue, token, owner);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(repositoryResponses, response.getBody());
    }

    @Test
    void testGetRepositoryFromOwnerWithoutToken() throws ErrorResponseException, RestClientException {
        // Arrange
        String headerValue = "application/json";
        String owner = "owner";
        RepositoryDto repoDto = Mockito.mock(RepositoryDto.class);
        List<RepositoryResponse> repositoryResponses = Collections.singletonList(new RepositoryResponse(repoDto, new ArrayList<>()));

        Mockito.when(gitHubService.getGitHubRepository(owner))
                .thenReturn(repositoryResponses);

        // Act
        ResponseEntity<List<RepositoryResponse>> response = gitHubController.getRepositoryFromOwner(headerValue, owner);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(repositoryResponses, response.getBody());
    }

    @Test
    void testGetRepositoryFromOwnerNotAcceptable() {
        // Arrange
        String headerValue = "text/html";
        String owner = "owner";

        // Act and Assert
        assertThrows(ErrorResponseException.class, () -> gitHubController.getRepositoryFromOwner(headerValue, owner));
    }
}
