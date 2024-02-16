package com.kamjer.AtiperaExc;

import com.kamjer.AtiperaExc.app.client.GitHubClient;
import com.kamjer.AtiperaExc.app.dto.BranchDto;
import com.kamjer.AtiperaExc.app.dto.CommitDto;
import com.kamjer.AtiperaExc.app.dto.OwnerDto;
import com.kamjer.AtiperaExc.app.dto.RepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GitHubClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private GitHubClient gitHubClient;

    @Test
    void testGetGitHubRepositories() {
        // Arrange
        String token = "yourToken";
        String owner = "owner";
        String apiUrl = "https://api.github.com/users/owner/repos";
        List<RepositoryDto> repositories = Arrays.asList(
                new RepositoryDto("repo1", new OwnerDto(owner), false),
                new RepositoryDto("repo2", new OwnerDto(owner), false));
        ResponseEntity<List<RepositoryDto>> expectedResponse = ResponseEntity.ok(repositories);

        Mockito.when(restTemplate.exchange(Mockito.eq(apiUrl), Mockito.eq(HttpMethod.GET), Mockito.any(RequestEntity.class), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<RepositoryDto>> actualResponse = gitHubClient.getGitHubRepositories(Optional.of(token), owner);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetGitHubBranches() {
        // Arrange
        String owner = "owner";
        String repoName = "repo";
        String apiUrl = "https://api.github.com/repos/owner/repo/branches";
        List<BranchDto> branches = Arrays.asList(new BranchDto(
                "branch1", new CommitDto("123123123123")),
                new BranchDto("branch2", new CommitDto("123123123123")));
        ResponseEntity<List<BranchDto>> expectedResponse = ResponseEntity.ok(branches);

        Mockito.when(restTemplate.exchange(Mockito.eq(apiUrl), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<List<BranchDto>> actualResponse = gitHubClient.getGitHubBranches(owner, repoName);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }
}
