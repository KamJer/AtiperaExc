package com.kamjer.AtiperaExc.app;

import com.kamjer.AtiperaExc.app.DTO.RepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class GitHubController {

    private GitHubService service;

    private Logger log = Logger.getLogger(GitHubController.class.getName());

    @Autowired
    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping("/{owner}/repos")
    public ResponseEntity<List<RepositoryDTO>> getRepositoryFromOwner(@PathVariable String owner) {
        return service.getGitHubRepository(owner);
    }
}
