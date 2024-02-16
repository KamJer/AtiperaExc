package com.kamjer.AtiperaExc;

import com.kamjer.AtiperaExc.app.controller.GitHubController;
import com.kamjer.AtiperaExc.app.dto.RepositoryResponse;
import com.kamjer.AtiperaExc.app.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.app.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GitHubControllerTest {

    @MockBean
    private GitHubService gitHubService;

    @Autowired
    private GitHubController gitHubController;

    @Test
    void testGetRepositoryFromOwnerWithToken() throws ErrorResponseException, RestClientException {
        // Arrange
        String headerValue = "application/json";
        String token = "yourToken";
        String owner = "owner";
        List<RepositoryResponse> repositoryResponses = Collections.singletonList(new RepositoryResponse());

        Mockito.when(gitHubService.getGitHubRepository(token, owner))
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
        List<RepositoryResponse> repositoryResponses = Collections.singletonList(new RepositoryResponse());

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
