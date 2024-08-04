package com.kamjer.AtiperaExc.service;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {
	@Mock
	private GitHubClient gitHubClient;

	private GitHubService gitHubService;

	@BeforeEach
	void setUp() {
		gitHubService = new GitHubService(gitHubClient);
	}

	@Test
	void testGetGitHubRepository() {
		// Arrange
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";

		RepositoryDto repositoryDto = Mockito.mock(RepositoryDto.class);
		BranchDto branchDto = Mockito.mock(BranchDto.class);
		when(repositoryDto.isFork()).thenReturn(false);
		when(repositoryDto.getName()).thenReturn(repoName);

		Flux<RepositoryDto> expectedRepositories = Flux.just(repositoryDto);
		Flux<BranchDto> expectedBranches = Flux.just(branchDto);
		RepositoryResponse repositoryResponseExpected = new RepositoryResponse(repositoryDto, expectedBranches);

		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositories);
		when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName), eq(Optional.of(token))))
				.thenReturn(expectedBranches);

		// Act
		Flux<RepositoryResponse> result = gitHubService.getGitHubRepository(owner, token);

		// Assert
		StepVerifier.create(result)
				.expectNextMatches(repositoryResponse -> {
					return repositoryResponse.equals(repositoryResponseExpected);
				})
				.expectComplete()
				.verify();
	}

	@Test
	void testGetGitHubRepositoryWithFork() {
		// Arrange
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";
		RepositoryDto repositoryDto = Mockito.mock(RepositoryDto.class);
		RepositoryDto repositoryDtoFork = Mockito.mock(RepositoryDto.class);
		BranchDto branchDto = Mockito.mock(BranchDto.class);

		when(repositoryDto.getName()).thenReturn(repoName);
		when(repositoryDto.isFork()).thenReturn(false);

		Flux<RepositoryDto> expectedRepositories = Flux.just(repositoryDto);
		Flux<BranchDto> expectedBranches = Flux.just(branchDto);
		RepositoryResponse repositoryResponseExpected = new RepositoryResponse(repositoryDto, expectedBranches);


		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositories);
		when((gitHubClient.getGitHubBranches(eq(owner), eq(repoName), eq(Optional.of(token)))))
				.thenReturn(expectedBranches);

		// Act
		Flux<RepositoryResponse> result = gitHubService.getGitHubRepository(owner, token);

		// Assert
		StepVerifier.create(result)
				.expectNextMatches(repositoryResponse -> {
					return repositoryResponse.equals(repositoryResponseExpected);
				})
				.expectComplete()
				.verify();
	}

	@Test
	void testGetBranchesForRepoEmptyResponse() {
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";

		RepositoryDto repositoryDto = Mockito.mock();

		when(repositoryDto.getName()).thenReturn(repoName);
		when(repositoryDto.isFork()).thenReturn(false);

		Flux<RepositoryDto> repositories = Flux.just(repositoryDto);
		Flux<BranchDto> expectedBranchResponse = Flux.just();

		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(repositories);
		when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName), eq(Optional.of(token))))
				.thenReturn(expectedBranchResponse);

		// Act
		Flux<RepositoryResponse> result = gitHubService.getGitHubRepository(owner , token);

		StepVerifier.create(result)
				.expectNextMatches(repoResponse -> {
					List<BranchDto> branchList = repoResponse.getBranchDtoList().collectList().block();
					return branchList != null && branchList.isEmpty();
				})
				.expectComplete()
				.verify();

		Mockito.verify(gitHubClient).getGitHubBranches(owner, repoName, Optional.of(token));
	}
}
