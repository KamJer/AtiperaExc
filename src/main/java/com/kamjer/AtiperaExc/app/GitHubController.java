package com.kamjer.AtiperaExc.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.logging.Logger;

@RestController
public class GitHubController {

    private GitHubService service;

    @Value("${acceptedHeaderValue}")
    private String acceptedHeaderValue;
    @Value("${exception.message.not-acceptable}")
    private String otAcceptableMessage;


    private Logger log = Logger.getLogger(GitHubController.class.getName());

    @Autowired
    public GitHubController(GitHubService service) {
        this.service = service;
    }

    @GetMapping(path = "/auth/{owner}/repos")
        public ResponseEntity<?> getRepositoryFromOwner(@RequestHeader(value = HttpHeaders.ACCEPT) String headerValue, @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token, @PathVariable String owner) throws ErrorResponseException, RestClientException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, otAcceptableMessage);
        }
        return service.getGitHubRepository(token, owner);
    }

    @GetMapping(path = "/{owner}/repos")
    public ResponseEntity<?> getRepositoryFromOwner(@RequestHeader(value = HttpHeaders.ACCEPT) String headerValue, @PathVariable String owner) throws ErrorResponseException, RestClientException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, otAcceptableMessage);
        }
        return service.getGitHubRepository("", owner);
    }
}
