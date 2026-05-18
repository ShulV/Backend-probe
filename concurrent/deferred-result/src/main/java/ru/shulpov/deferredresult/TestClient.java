package ru.shulpov.deferredresult;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 29.12.2025 16:07
 */
@Component
public class TestClient {

    private static final Logger log = LoggerFactory.getLogger(TestClient.class);
    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public TestClient(WebClient webClient, RestTemplate restTemplate) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void test() {
//        sendSyncRequests();
//        sendAsyncRequests();
    }


    public void sendSyncRequests() {
        sendSyncRequest(6000L);
        sendSyncRequest(5000L);
        sendSyncRequest(7000L);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendAsyncRequests() {
        Mono<String> m1 = sendAsyncRequest(6000);
        Mono<String> m2 = sendAsyncRequest(5000);
        Mono<String> m3 = sendAsyncRequest(7000);

        Mono.zip(m1, m2, m3)
                .doOnNext(tuple3 -> {
                    System.out.println("resp1 = " + tuple3.getT1());
                    System.out.println("resp2 = " + tuple3.getT2());
                    System.out.println("resp3 = " + tuple3.getT3());
                })
                .block();
        log.info("final");
    }

    public Mono<String> sendAsyncRequest(long timeMs) {
        return webClient.get()
                .uri("/long-running?time={time}", timeMs)
                .retrieve()
                .bodyToMono(String.class);
    }

    public ResponseEntity<String> sendSyncRequest(long timeMs) {
        return restTemplate.exchange("http://127.0.0.1:8080/long-running?time=" + timeMs, HttpMethod.GET, null, String.class);
    }
}
