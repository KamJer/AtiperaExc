package com.kamjer.AtiperaExc.client;

import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GitHubClientTest {

    @Mock
    private RestTemplate restTemplate;

    private GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        String githubApiBaseUrl = "https://api.github.com";
        gitHubClient = new GitHubClient(restTemplate, githubApiBaseUrl);
    }

    @Test
    void testGetGitHubRepositories() {
        // Arrange
        String token = "yourToken";
        String owner = "owner";
        String apiUrl = "https://api.github.com/users/owner/repos";
        List<RepositoryDto> repositories = Arrays.asList(
                        Mockito.mock(RepositoryDto.class),
                        Mockito.mock(RepositoryDto.class));
        ResponseEntity<List<RepositoryDto>> expectedResponse = ResponseEntity.ok(repositories);

        Mockito.when(restTemplate.exchange(Mockito.eq(apiUrl), Mockito.eq(HttpMethod.GET), Mockito.any(RequestEntity.class), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResponse);

        // Act
        List<RepositoryDto> actualResponse = gitHubClient.getGitHubRepositories(Optional.of(token), owner);

        // Assert
        assertEquals(expectedResponse.getBody(), actualResponse);
    }

    @Test
    void testGetGitHubBranches() {
        // Arrange
        String owner = "owner";
        String repoName = "repo";
        String apiUrl = "https://api.github.com/repos/owner/repo/branches";
        List<BranchDto> branches = Arrays.asList(Mockito.mock(BranchDto.class),
                Mockito.mock(BranchDto.class));
        ResponseEntity<List<BranchDto>> expectedResponse = ResponseEntity.ok(branches);

        Mockito.when(restTemplate.exchange(Mockito.eq(apiUrl), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResponse);

        // Act
        List<BranchDto> actualResponse = gitHubClient.getGitHubBranches(owner, repoName);

        // Assert
        assertEquals(expectedResponse.getBody(), actualResponse);
    }
}
