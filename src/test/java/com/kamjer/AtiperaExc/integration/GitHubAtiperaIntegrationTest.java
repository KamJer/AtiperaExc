package com.kamjer.AtiperaExc.integration;

import com.kamjer.AtiperaExc.client.GitHubClient;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GitHubAtiperaIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetRepositoryFromOwnerWithoutToken() throws Exception {
        // Arrange
        String headerValue = "application/json";
        String owner = "KamJer";

        String firstResponse = Files.readString(Path.of("src","test", "resources", "firstResponse.json"));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://api.github.com/users/" + owner + "/repos")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(firstResponse)
                );

        String uriPrefix ="https://api.github.com/repos/" + owner;

            mockServer.expect(ExpectedCount.manyTimes(),
                    MockRestRequestMatchers.requestTo(Matchers.startsWith(uriPrefix)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(firstResponse)
                );

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/{owner}/repos", owner)
                        .header(HttpHeaders.ACCEPT, headerValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetRepositoryFromOwnerWithToken() throws Exception {
        // Arrange
        String headerValue = "application/json";
        String owner = "KamJer";
        String token = "token";

        String firstResponse = Files.readString(Path.of("src","test", "resources", "firstResponse.json"));

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://api.github.com/users/" + owner + "/repos")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(firstResponse)
                );

        String uriPrefix ="https://api.github.com/repos/" + owner;

        mockServer.expect(ExpectedCount.manyTimes(),
                        MockRestRequestMatchers.requestTo(Matchers.startsWith(uriPrefix)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(firstResponse)
                );

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/{owner}/repos", owner)
                        .header(HttpHeaders.ACCEPT, headerValue)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
