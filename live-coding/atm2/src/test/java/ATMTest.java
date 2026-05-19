import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.live_coding.ATM;
import ru.live_coding.Currency;
import ru.live_coding.Nominal;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class ATMTest {

    @Test
    public void initATM() {
        //подготовка
        Map<Currency, NavigableMap<Nominal, Integer>> nominalMap = new HashMap<>();
        nominalMap.put(
                new Currency("Р", 100L),
                new TreeMap<>(Map.of(
                        new Nominal(50), 33,
                        new Nominal(1000), 3,
                        new Nominal(100), 22,
                        new Nominal(5000), 2))
        );
        //действие
        ATM atm = new ATM(nominalMap);
        //проверка
        Assertions.assertNotNull(atm);
    }

    @Test
    public void tooLittleMoneyAtmForFirstTry() {
        //подготовка
        Currency rubCurrency = new Currency("Р", 100L);
        Map<Currency, NavigableMap<Nominal, Integer>> nominalMap = new HashMap<>();
        nominalMap.put(rubCurrency,
                new TreeMap<>(Map.of(
                        new Nominal(50), 33,
                        new Nominal(1000), 3,
                        new Nominal(100), 22,
                        new Nominal(5000), 2))
        );
        ATM atm = new ATM(nominalMap);

        //действие
        Executable task = () -> atm.withdraw(10000000, rubCurrency);

        //проверка
        Assertions.assertThrows(RuntimeException.class, task);
    }

    @Test
    public void enoughMoneyAtmForFirstTry() {
        //подготовка
        Currency rubCurrency = new Currency("Р", 100L);
        Map<Currency, NavigableMap<Nominal, Integer>> nominalMap = new HashMap<>();
        nominalMap.put(rubCurrency,
                new TreeMap<>(Map.of(
                        new Nominal(50), 33,
                        new Nominal(1000), 3,
                        new Nominal(100), 22,
                        new Nominal(5000), 2))
        );
        ATM atm = new ATM(nominalMap);

        //действие
        Executable task = () -> atm.withdraw(100000, rubCurrency);

        //проверка
        Assertions.assertDoesNotThrow(task);
        Assertions.assertEquals(2, nominalMap.get(rubCurrency).get(new Nominal(5000)));
        Assertions.assertEquals(2, nominalMap.get(rubCurrency).get(new Nominal(1000)));
    }

    @Test
    public void withdrawChangesNominalCounts() {
        //подготовка
        Currency rubCurrency = new Currency("Р", 100L);
        Map<Currency, NavigableMap<Nominal, Integer>> nominalMap = new HashMap<>();
        nominalMap.put(rubCurrency,
                new TreeMap<>(Map.of(
                        new Nominal(50), 3,
                        new Nominal(100), 2,
                        new Nominal(1000), 1))
        );
        ATM atm = new ATM(nominalMap);

        //действие
        atm.withdraw(115000, rubCurrency);

        //проверка
        Assertions.assertFalse(nominalMap.get(rubCurrency).containsKey(new Nominal(1000)));
        Assertions.assertEquals(1, nominalMap.get(rubCurrency).get(new Nominal(100)));
        Assertions.assertEquals(2, nominalMap.get(rubCurrency).get(new Nominal(50)));
    }

    @Test
    public void impossibleNominalCombinationDoesNotChangeAtm() {
        //подготовка
        Currency rubCurrency = new Currency("Р", 100L);
        Map<Currency, NavigableMap<Nominal, Integer>> nominalMap = new HashMap<>();
        nominalMap.put(rubCurrency,
                new TreeMap<>(Map.of(
                        new Nominal(50), 1,
                        new Nominal(100), 1))
        );
        ATM atm = new ATM(nominalMap);

        //действие
        Executable task = () -> atm.withdraw(3000, rubCurrency);

        //проверка
        Assertions.assertThrows(RuntimeException.class, task);
        Assertions.assertEquals(1, nominalMap.get(rubCurrency).get(new Nominal(50)));
        Assertions.assertEquals(1, nominalMap.get(rubCurrency).get(new Nominal(100)));
    }
}
