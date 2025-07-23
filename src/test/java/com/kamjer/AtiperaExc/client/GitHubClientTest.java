package com.kamjer.AtiperaExc.client;

import com.kamjer.AtiperaExc.model.RepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GitHubClientTest {

    @Mock
    private WebClient webClient;

    private GitHubClient gitHubClient;


    @BeforeEach
    void setUp() {
        String githubAcceptValue = "application/vnd.github+json";
        gitHubClient = new GitHubClient(webClient, githubAcceptValue);
    }

    @Test
    void testGetGitHubRepositories() {
        // Arrange
        String token = "yourToken";
        String owner = "owner";
        String apiUrl = "/users/owner/repos";
        Flux<RepositoryDto> repositories = Flux.just(
                        Mockito.mock(RepositoryDto.class),
                        Mockito.mock(RepositoryDto.class));

        WebClient.RequestHeadersUriSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);


        Mockito.when(webClient.get()).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.uri(apiUrl)).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.headers(Mockito.any())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.bodyToFlux(RepositoryDto.class)).thenReturn(repositories);

        // Act
        Flux<RepositoryDto> actualResponse = gitHubClient.getGitHubRepositories(Optional.of(token), owner);

        // Assert
        assertEquals(repositories, actualResponse);
    }
}
