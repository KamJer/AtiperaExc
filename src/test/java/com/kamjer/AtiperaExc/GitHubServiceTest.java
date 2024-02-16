package com.kamjer.AtiperaExc;

import com.kamjer.AtiperaExc.app.client.GitHubClient;
import com.kamjer.AtiperaExc.app.dto.*;
import com.kamjer.AtiperaExc.app.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
class GitHubServiceTest {
	@MockBean
	private GitHubClient gitHubClient;

	@Autowired
	private GitHubService gitHubService;

	@Test
	void testGetGitHubRepository() {
		// Arrange
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";
		RepositoryDto repositoryDto = new RepositoryDto(repoName, new OwnerDto(owner), false);
		List<RepositoryDto> repositories = List.of(repositoryDto);
		ResponseEntity<List<RepositoryDto>> expectedRepositoryResponse = ResponseEntity.ok(repositories);
		ResponseEntity<List<BranchDto>> expectedBranchResponse = ResponseEntity.ok(List.of(new BranchDto("branch1", new CommitDto("123123123123"))));

		Mockito.when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositoryResponse);
		Mockito.when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName)))
				.thenReturn(expectedBranchResponse);

		// Act
		List<RepositoryResponse> result = gitHubService.getGitHubRepository(token, owner);

		// Assert
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getBranchDTOList().size());
		assertEquals("branch1", result.get(0).getBranchDTOList().get(0).getName());
	}

	@Test
	void testGetGitHubRepositoryWithFork() {
		// Arrange
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";
//		RepositoryDto repositoryDtoFork = new RepositoryDto(repoName, new OwnerDto(owner), true);
		RepositoryDto repositoryDto = new RepositoryDto(repoName, new OwnerDto(owner), false);
		List<RepositoryDto> repositories = List.of(repositoryDto);

		ResponseEntity<List<BranchDto>> expectedBranchResponse = ResponseEntity.ok(List.of(new BranchDto("branch1", new CommitDto("123123123123"))));
		ResponseEntity<List<RepositoryDto>> expectedRepositoryResponse = ResponseEntity.ok(repositories);
		expectedRepositoryResponse.getBody();

		Mockito.when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositoryResponse);
		Mockito.when((gitHubClient.getGitHubBranches(eq(owner), eq(repoName))))
				.thenReturn(expectedBranchResponse);

		// Act
		List<RepositoryResponse> result = gitHubService.getGitHubRepository(token, owner);

		// Assert
//		it should contain just 1 branch
		assertEquals(1, result.size());
	}

	@Test
	void testGetBranchesForRepoEmptyResponse() {
		String token = "yourToken";
		String owner = "owner";
		String repoName = "repo";
		RepositoryDto repositoryDtoFork = new RepositoryDto(repoName, new OwnerDto(owner), true);
		RepositoryDto repositoryDto = new RepositoryDto(repoName, new OwnerDto(owner), false);

		List<RepositoryDto> repositories = List.of(repositoryDtoFork, repositoryDto);
		ResponseEntity<List<RepositoryDto>> expectedRepositoryResponse = ResponseEntity.ok(repositories);

		ResponseEntity<List<BranchDto>> expectedBranchResponse = ResponseEntity.ok(new ArrayList<>());
		Mockito.when(gitHubClient.getGitHubRepositories(eq(Optional.of(token)), eq(owner)))
				.thenReturn(expectedRepositoryResponse);
		Mockito.when(gitHubClient.getGitHubBranches(eq(owner), eq(repoName)))
				.thenReturn(expectedBranchResponse);

		// Act
		List<BranchDto> result = gitHubService.getGitHubRepository(token, owner)
				.stream()
				.map(RepositoryResponse::getBranchDTOList)
				.flatMap(List::stream)
				.toList();

		assertEquals(0, result.size());
		Mockito.verify(gitHubClient).getGitHubBranches(owner, repoName);
	}
}
