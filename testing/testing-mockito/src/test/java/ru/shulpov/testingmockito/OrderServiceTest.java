package ru.shulpov.testingmockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.shulpov.testingmockito.model.Order;
import ru.shulpov.testingmockito.repo.OrderRepo;
import ru.shulpov.testingmockito.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//Без этой аннотации:
//@Mock и @InjectMocks не будут автоматически инициализированы.
//Тебе пришлось бы явно вызывать MockitoAnnotations.openMocks(this) в @BeforeEach, как это делали до появления расширения.
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

//    Создаёт объект — полностью фейковый OrderRepository, который ничего не делает по умолчанию
//    Записывает все вызовы — любой метод на нём Mockito запомнит (что вызвали, с какими аргументами, сколько раз)
//    Возвращает null/пустые значения по умолчанию для геттеров (или 0/false для примитивов)
    @Mock
    private OrderRepo orderRepo;

//new OrderService(orderRepository) — вызывает реальный конструктор
//Внедряет mock вместо настоящего репозитория
//orderService — живой объект с фейковыми зависимостями
    @InjectMocks
    private OrderService orderService;

    @Test
    void create_shouldSaveValidOrder() {
        // given
        Order order = new Order(
                1L,
                List.of("item1", "item2"),
                new BigDecimal("100.00")
        );

        Mockito.when(orderRepo.save(order))
                .thenReturn(order);

        // when
        Order result = orderService.create(order);

        // then
        Assertions.assertEquals(order, result);
        Mockito.verify(orderRepo, Mockito.times(1)).save(order);
    }

    @Test
    void create_shouldThrowWhenTotalLessOrEqualZero() {
        // given
        Order order = new Order(
                1L,
                List.of("item1"),
                BigDecimal.ZERO
        );

        // when + then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> orderService.create(order)
        );

        Mockito.verify(orderRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getById_shouldReturnOrder() {
        // given
        Long id = 1L;
        Order order = new Order(id, List.of("item1"), new BigDecimal("10.00"));

        Mockito.when(orderRepo.findById(id))
                .thenReturn(Optional.of(order));

        // when
        Order result = orderService.getById(id);

        // then
        Assertions.assertEquals(order, result);
        Mockito.verify(orderRepo).findById(id);
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        // given
        Long id = 42L;
        Mockito.when(orderRepo.findById(id))
                .thenReturn(Optional.empty());

        // when + then
        Assertions.assertThrows(
                NoSuchElementException.class,
                () -> orderService.getById(id)
        );
    }
}
