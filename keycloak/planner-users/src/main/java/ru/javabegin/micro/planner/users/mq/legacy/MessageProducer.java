//package ru.javabegin.micro.planner.users.mq.legacy;
//
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableBinding(TodoBinding.class)
//public class MessageProducer {
//// Связываем класс, чтобы он имел возможность исп каналы, которые описаны в интерфейсе
//    private final TodoBinding todoBinding;
//
//
//    public MessageProducer(TodoBinding todoBinding) {
//        this.todoBinding = todoBinding;
//    }
//
//    // Отправка сообщения при создании нового юзера
//    public void newUserAction(Long userId) {
//        // Контейнер для добавления данных и headers
//        Message message = MessageBuilder.withPayload(userId).build();
//
//        todoBinding.todoOutputChannel().send(message);
//    }
//}
