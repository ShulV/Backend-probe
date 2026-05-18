package ru.shulpov.completablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 20.01.2026 15:04
 */
public class TestCall implements Callable<String> {
    Logger log = LoggerFactory.getLogger(TestCall.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final String name;

    public TestCall(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        log.info("Calling start {}", name);
        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("http://localhost:8080/long-running?time=10000", String.class);
        log.info("Calling end {}", name);
        return responseEntity.getBody();
    }
}
