package com.kamjer.AtiperaExc.service;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
		when(repositoryDto.isFork()).thenReturn(false);
		when(repositoryDto.getName()).thenReturn(repoName);

		List<RepositoryDto> expectedRepositories = List.of(repositoryDto);
		List<BranchDto> expectedBranches = List.of(Mockito.mock(BranchDto.class));

		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositories);
		when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName)))
				.thenReturn(expectedBranches);

		// Act
		List<RepositoryResponse> result = gitHubService.getGitHubRepository(owner, token);

		// Assert
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getBranchDtoList().size());
	}

	@Test
	void testGetGitHubRepositoryWithFork() {
		// Arrange
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";
		RepositoryDto repositoryDto = Mockito.mock(RepositoryDto.class);
		RepositoryDto repositoryDtoFork = Mockito.mock(RepositoryDto.class);

		when(repositoryDto.getName()).thenReturn(repoName);
		when(repositoryDto.isFork()).thenReturn(false);

		when(repositoryDtoFork.isFork()).thenReturn(true);

		List<RepositoryDto> repositories = List.of(repositoryDto, repositoryDtoFork);
		List<BranchDto> expectedBranches = List.of(Mockito.mock(BranchDto.class));


		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(repositories);
		when((gitHubClient.getGitHubBranches(eq(owner), eq(repoName))))
				.thenReturn(expectedBranches);

		// Act
		List<RepositoryResponse> result = gitHubService.getGitHubRepository(owner, token);

		// Assert
//		it should contain just 1 branch
		assertEquals(1, result.size());
	}

	@Test
	void testGetBranchesForRepoEmptyResponse() {
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";

		RepositoryDto repositoryDto = Mockito.mock();

		when(repositoryDto.getName()).thenReturn(repoName);
		when(repositoryDto.isFork()).thenReturn(false);

		List<RepositoryDto> repositories = List.of(repositoryDto);

		List<BranchDto> expectedBranchResponse = new ArrayList<>();

		when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(repositories);
		when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName)))
				.thenReturn(expectedBranchResponse);

		// Act
		List<BranchDto> result = gitHubService.getGitHubRepository(owner , token)
				.stream()
				.map(RepositoryResponse::getBranchDtoList)
				.flatMap(List::stream)
				.toList();

		assertEquals(0, result.size());
		Mockito.verify(gitHubClient).getGitHubBranches(owner, repoName);
	}
}
