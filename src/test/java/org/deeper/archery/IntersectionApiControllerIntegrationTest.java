package org.deeper.archery;

import org.deeper.archery.api.model.Point;
import org.deeper.archery.api.model.request.ArrowRequest;
import org.deeper.archery.api.model.request.SquareTargetRequest;
import org.deeper.archery.api.model.request.IntersectionRequest;
import org.deeper.archery.api.model.response.RequestCountResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.security.user.name=${java.io.tmpdir}/test-user.txt",
        "spring.security.user.password=${java.io.tmpdir}/test-password.txt"
})
@AutoConfigureTestRestTemplate
public class IntersectionApiControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private TestRestTemplate authenticatedRestTemplate;

    private static final String GET_ACTIVE_REQUESTS_ENDPOINT = "archery/stats/requests";
    private static final String POST_INTERSECTION_CHECK_ENDPOINT = "archery/intersection/check";
    private static final int CONCURRENT_REQUEST_COUNT = 5;

    @BeforeAll
    static void createCredentialFiles() throws IOException {
        Path tempDir = Path.of(System.getProperty("java.io.tmpdir"));
        Files.writeString(tempDir.resolve("test-user.txt"), "testuser");
        Files.writeString(tempDir.resolve("test-password.txt"), "testpassword");
    }

    @BeforeEach
    void setUp() {
        authenticatedRestTemplate = restTemplate.withBasicAuth("testuser", "testpassword");
    }

    @Test
    void getRequestCount_shouldReturn401_whenNotAuthenticated() {
        ResponseEntity<String> response = restTemplate
                .getForEntity(GET_ACTIVE_REQUESTS_ENDPOINT, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void getRequestCount_shouldReturn200_whenAuthenticated() {
        ResponseEntity<RequestCountResponse> response = authenticatedRestTemplate
                .getForEntity(GET_ACTIVE_REQUESTS_ENDPOINT, RequestCountResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void checkIntersection_shouldReturn200_whenNotAuthenticated() {
        IntersectionRequest request = buildIntersectionRequest();
        ResponseEntity<String> response = sendCheckIntersectionRequest(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getRequestCount_shouldReturnNumberOfActiveRequests() throws InterruptedException {
        CountDownLatch requestsStarted = new CountDownLatch(CONCURRENT_REQUEST_COUNT);
        CountDownLatch releaseRequests = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_REQUEST_COUNT);

        try {
            for (int i = 0; i < CONCURRENT_REQUEST_COUNT; i++) {
                executor.submit(() -> {
                    requestsStarted.countDown();
                    try {
                        releaseRequests.await();
                        sendCheckIntersectionRequest(buildIntersectionRequest());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            requestsStarted.await();
            releaseRequests.countDown();

            await().atMost(2, SECONDS)
                    .untilAsserted(() -> {
                        ResponseEntity<RequestCountResponse> response = authenticatedRestTemplate
                                .getForEntity(GET_ACTIVE_REQUESTS_ENDPOINT, RequestCountResponse.class);

                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                        assertThat(response.getBody()).isNotNull();
                        assertThat(response.getBody().activeRequestsCount()).isGreaterThan(0);
                    });
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void getRequestCount_shouldReturnOne_whenNoOtherActiveRequests() {
        ResponseEntity<RequestCountResponse> response = authenticatedRestTemplate
                .getForEntity(GET_ACTIVE_REQUESTS_ENDPOINT, RequestCountResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().activeRequestsCount()).isEqualTo(1);
    }

    private ResponseEntity<String> sendCheckIntersectionRequest(IntersectionRequest request) {
        return restTemplate.exchange(
                POST_INTERSECTION_CHECK_ENDPOINT,
                HttpMethod.POST,
                new HttpEntity<>(request),
                String.class
        );
    }

    private IntersectionRequest buildIntersectionRequest() {
        return new IntersectionRequest(
                new SquareTargetRequest(
                        List.of(new Point(0.0, 1.0),
                                new Point(1.0, 1.0),
                                new Point(1.0, 0.0),
                                new Point(0.0, 0.0))

                ),
                new ArrowRequest(
                        new Point(-1.0, 2.0),
                        new Point(5.0, 2.0)
                )
        );
    }

}