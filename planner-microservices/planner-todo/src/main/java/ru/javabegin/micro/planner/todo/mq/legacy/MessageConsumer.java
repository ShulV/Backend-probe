package ru.javabegin.micro.planner.todo.mq.legacy;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.javabegin.micro.planner.todo.service.TestDataService;

@Component
@EnableBinding(TodoBinding.class)
public class MessageConsumer {
    private final TestDataService testDataService;

    public MessageConsumer(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
    public void initTestData(Long userId) throws Exception {
        if (userId % 2 == 0) {
            throw new Exception("dead letter queue test exception...");
        }
        testDataService.initTestData(userId);
    }
}
