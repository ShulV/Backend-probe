package ru.shulpov.deferredresult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 29.12.2025 16:02
 */
@RestController
public class TestController {

    private final long TIMEOUT_MS = 10_000L;

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final TestClient testClient;

    public TestController(TestClient testClient) {
        this.testClient = testClient;
    }

    @GetMapping("/long-running")
    public ResponseEntity<String> longRunningMethod(@RequestParam long time) {
        log.info("Long-running start: [time = '{}']", time);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Long-running interrupted", e);
            throw new RuntimeException(e);
        }
        log.info("Long-running end: [time = '{}']", time);
        return ResponseEntity.ok("ok body: time=" + time);
    }

    @GetMapping("/deferred-result")
    public DeferredResult<ResponseEntity<String>> deferredResultMethod(@RequestParam long time) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(TIMEOUT_MS);
//        DeferredResult.DeferredResultHandler deferredResultHandler = new De
        deferredResult.setResultHandler((e) -> {

        });
        deferredResult.onTimeout(() -> {
            log.error("Timeout happened: [timeout = '{}', time = '{}']", TIMEOUT_MS, time);
            throw new RuntimeException("Timeout happened");
        });
        CompletableFuture.supplyAsync(() -> {
            log.info("deferred result start: [timeout = '{}', time = '{}']", TIMEOUT_MS, time);
            ResponseEntity<String> responseEntity = testClient.sendSyncRequest(time);
            log.info("deferred result end: [timeout = '{}', time = '{}']", TIMEOUT_MS, time);
            return responseEntity;
        }).whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("deferred result exception: [throwable_message = '{}']", throwable.getMessage());
                if (throwable.getCause() != null) {
                    log.error("deferred result with inner exception: [cause_message = '{}']", throwable.getCause().getMessage());
                    deferredResult.setErrorResult("throwable.getCause()");
                }
            } else {
                deferredResult.setResult(result);
            }
        });
        log.info("It's ok! Before return.");
        return deferredResult;

    }
}
