//package ru.javabegin.micro.planner.todo.mq.legacy;
//
//import org.springframework.cloud.stream.annotation.Input;
//import org.springframework.messaging.MessageChannel;
//
//// Название может быть любым
//// писывает каналы для работы с mq
//public interface TodoBinding {
//    String INPUT_CHANNEL = "todoInputChannel";// Нужен, чтобы ссылаться на него, а не везде
//
//    // Создаем канал для приема сообщений
//    @Input(INPUT_CHANNEL)
//    MessageChannel todoInputChannel();// Название канала может браться из названия метода
//}
