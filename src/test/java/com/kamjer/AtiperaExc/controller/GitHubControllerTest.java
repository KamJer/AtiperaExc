package com.kamjer.AtiperaExc.controller;

import com.kamjer.AtiperaExc.model.RepositoryDto;
import com.kamjer.AtiperaExc.model.RepositoryResponse;
import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GitHubControllerTest {

    @Mock
    private GitHubService gitHubService;

    private GitHubController gitHubController;

    @BeforeEach
    void setUp() {
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
        RepositoryResponse expectedResponse = new RepositoryResponse(repoDto, Flux.just());
        Flux<RepositoryResponse> repositoryResponses = Flux.just(expectedResponse);


        Mockito.when(gitHubService.getGitHubRepository(owner, token))
                .thenReturn(repositoryResponses);

        // Act
        Flux<RepositoryResponse> response = gitHubController.getRepositoryFromOwner(headerValue, token, owner);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(repositoryResponse -> {
                    return repositoryResponse.equals(expectedResponse);
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGetRepositoryFromOwnerWithoutToken() throws ErrorResponseException, RestClientException {
        // Arrange
        String headerValue = "application/json";
        String owner = "owner";
        RepositoryDto repoDto = Mockito.mock(RepositoryDto.class);
        RepositoryResponse expectedResponse = new RepositoryResponse(repoDto, Flux.just());
        Flux<RepositoryResponse> repositoryResponses = Flux.just(expectedResponse);

        Mockito.when(gitHubService.getGitHubRepository(owner))
                .thenReturn(repositoryResponses);

        // Act
        Flux<RepositoryResponse> response = gitHubController.getRepositoryFromOwner(headerValue, owner);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(repositoryResponse -> {
                    return repositoryResponse.equals(expectedResponse);
                })
                .expectComplete()
                .verify();
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
