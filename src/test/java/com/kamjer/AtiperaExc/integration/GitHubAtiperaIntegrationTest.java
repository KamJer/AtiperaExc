package com.kamjer.AtiperaExc.integration;

import com.kamjer.AtiperaExc.config.TestConfig;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations="classpath:test.properties")
@Import(TestConfig.class)
@ContextConfiguration(classes = {TestConfig.class})
public class GitHubAtiperaIntegrationTest {

    public static MockWebServer mockBackEnd;

    @Autowired
    private MockMvc mockMvc;

    private final String headerValue = "application/json";
    private final String owner = "KamJer";


    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void beforeAll() throws IOException {
        String firstResponse = Files.readString(Path.of("src", "test", "resources", "firstResponse.json"));
        String branchResponse = Files.readString(Path.of("src", "test", "resources", "branchResponse.json"));
        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().contains("/" + owner + "/repos") ) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .setBody(firstResponse);
                } else if (request.getPath().contains("repos/" + owner + "/")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .setBody(branchResponse);
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockBackEnd.setDispatcher(dispatcher);
    }

    @Test
    public void testGetRepositoryFromOwnerWithoutToken() throws Exception {
        // Assert
        mockMvc.perform(get("/{owner}/repos", owner)
                        .header("Accept", headerValue))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRepositoryFromOwnerWithToken() throws Exception {
        // Assert
        String token = "testToken";
        mockMvc.perform(get("/auth/{owner}/repos", owner)
                        .header("Accept", headerValue)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
    }
}
