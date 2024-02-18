# GitHub Repository Service - Atipera Recruitment

## Overview

This repository contains a Spring Boot application for interacting with the GitHub API to retrieve and filter repositories and their branches. The project is part of the recruitment process for Atipera.

## Layers

- **GitHubController:** REST endpoints for retrieving GitHub repositories based on owner and optional authentication token. Handles Accept header validation and custom exception handling.
- **GitHubService:** Manages communication with the GitHub API using a GitHubClient. Fetches repositories, filters out forks, and retrieves branches for each repository.
- **GitHubClient:** Utilizes RestTemplate to make requests to the GitHub API for repositories and branches.
- **GitHubControllerAdvice:** Global exception handler for specific exceptions, providing customized error responses.

## Prerequisites

- Java 8 or higher
- Maven
- Spring Boot
- GitHub API access token (optional but recommended for increased API rate limits)

## Usage

### Get Repositories by Owner

- Endpoint: `/auth/{owner}/repos`
- Method: GET
- Headers:
    - `Accept`: application/vnd.github.v3+json (configurable)
    - `Authorization`: Bearer {GitHub API token}

Example:

```bash
curl -X GET -H "Accept: application/vnd.github.v3+json" -H "Authorization: Bearer YOUR_GITHUB_TOKEN" http://localhost:8080/auth/owner/repos
```

### Get Repositories by Owner (without authentication)

- Endpoint: `/{owner}/repos`
- Method: GET
- Headers:
    - `Accept`: application/vnd.github.v3+json (configurable)

Example:

```bash
curl -X GET -H "Accept: application/vnd.github.v3+json" http://localhost:8080/owner/repos
```

## Exception Handling

- `ErrorResponseException` is thrown for specific error scenarios, providing a custom error response with HTTP status and message.
