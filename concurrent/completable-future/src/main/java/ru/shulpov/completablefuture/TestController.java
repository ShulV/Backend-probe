package ru.shulpov.completablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 20.01.2026 14:51
 */
@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

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
}