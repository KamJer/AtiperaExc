# GitHub Repository Service - Atipera Recruitment

## Overview

This repository contains a Spring Boot application for interacting with the GitHub API to retrieve and filter repositories and their branches. The project is part of the recruitment process for Atipera.

## Layers

- **GitHubController:** Exposes REST endpoints to retrieve GitHub repositories based on owner and authentication token. Handles Accept header validation and exception handling for not acceptable requests.
- **GitHubService:** Performs communication with the GitHub API using RestTemplate. Fetches repositories and their branches, filters out forked repositories, and returns the result.
- **GitHubControllerAdvice:** Global exception handler for specific exceptions, providing customized error responses.

## Prerequisites

- Java 8 or higher
- Maven
- Spring Boot
- GitHub API access token (optional but recommended for increased API rate limits)

## Configuration

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/AtipieraRecruitment.git
    ```

2. Open the project in your preferred IDE.

3. Update application.properties with your GitHub API token:

    ```properties
    github.api.base-url=https://api.github.com
    github.accept.value=application/vnd.github.v3+json
    ```

4. Build and run the application.

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

## Logging

- The application logs key information using Java's built-in logging.
