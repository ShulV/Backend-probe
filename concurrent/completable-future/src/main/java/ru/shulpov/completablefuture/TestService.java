package ru.shulpov.completablefuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Shulpov Victor, E-Mail: shulpov.v@soft-logic.team
 * Created on 20.01.2026 14:54
 */
@Service
public class TestService {

    Logger log = LoggerFactory.getLogger(TestService.class);

    public void test1() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            Future<String> future1 = executorService.submit(new TestCall("call 1"));
            Future<String> future2 = executorService.submit(new TestCall("call 2"));
            Future<String> future3 = executorService.submit(new TestCall("call 3"));

            Thread.sleep(6000);
            log.info("future1: is_done = {}", future1.isDone());
            Thread.sleep(6000);
            log.info("future1: is_done = {}", future1.isDone());

            log.info("method finished");
            log.info("future1: is_done = {}", future1.isDone());
            log.info("future2: is_done = {}", future2.isDone());
            log.info("future3: is_done = {}", future3.isDone());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
