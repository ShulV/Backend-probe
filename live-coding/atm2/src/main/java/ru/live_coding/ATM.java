package ru.live_coding;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ATM implements Withdrawable {
    private static final Logger log = Logger.getLogger(ATM.class.getName());

    private final Map<Currency, NavigableMap<Nominal, Integer>> currencyNominalMap;

    public ATM(Map<Currency, NavigableMap<Nominal, Integer>> currencyNominalMap) {
        if (currencyNominalMap == null) {
            throw new IllegalArgumentException("currencyNominalMap must not be null");
        }
        log.info("Initialized map: " + currencyNominalMap);
        this.currencyNominalMap = currencyNominalMap;
    }
    
    @Override
    public void withdraw(long sumInSubUnits, Currency currency) {
        validateInput(sumInSubUnits, currency);

        synchronized (this) {
            validateAtmState(sumInSubUnits, currency);
            Map<Nominal, Integer> nominalsToTake = matchNominals(sumInSubUnits, currency);
            takeNominals(currency, nominalsToTake);
        }
    }

    private Map<Nominal, Integer> matchNominals(long sumInSubUnits, Currency currency) {
        NavigableMap<Nominal, Integer> nominalMap = currencyNominalMap.get(currency);
        Map<Nominal, Integer> result = new TreeMap<>();
        long rest = sumInSubUnits;

        for (Map.Entry<Nominal, Integer> entry : nominalMap.descendingMap().entrySet()) {
            Nominal nominal = entry.getKey();
            long nominalInSubUnits = nominal.units() * currency.subUnitsInUnit();
            int availableCount = entry.getValue();
            int countToTake = (int) Math.min(rest / nominalInSubUnits, availableCount);

            if (countToTake > 0) {
                result.put(nominal, countToTake);
                rest -= nominalInSubUnits * countToTake;
            }
        }

        if (rest != 0) {
            log.info("Cannot match nominals for sum=" + sumInSubUnits + " ;rest=" + rest);
            throw new RuntimeException();
        }

        return result;
    }

    private void takeNominals(Currency currency, Map<Nominal, Integer> nominalsToTake) {
        NavigableMap<Nominal, Integer> nominalMap = currencyNominalMap.get(currency);

        for (Map.Entry<Nominal, Integer> entry : nominalsToTake.entrySet()) {
            Nominal nominal = entry.getKey();
            int currentCount = nominalMap.get(nominal);
            int newCount = currentCount - entry.getValue();

            if (newCount == 0) {
                nominalMap.remove(nominal);
            } else {
                nominalMap.put(nominal, newCount);
            }
        }
    }

    private void validateInput(long sumInSubUnits, Currency currency) {
        if (sumInSubUnits < 0) {
            log.info("Negative sum");
            throw new IllegalArgumentException();
        }

        if (currency == null) {
            log.info("No currency");
            throw new RuntimeException();
        }
    }

    private void validateAtmState(long sumInSubUnits, Currency currency) {
        if (currencyNominalMap.get(currency) == null) {
            log.info("Unknown currency");
            throw new RuntimeException();
        }

        if (sumInSubUnits % currency.subUnitsInUnit() != 0) {
            log.info("Sum cannot be represented by whole nominals: " + sumInSubUnits);
            throw new RuntimeException();
        }

        long allSumInSubUnits = getAllSumInSubUnits(currency);
        if (sumInSubUnits > allSumInSubUnits) {
            log.info("Too little money in ATM: has=" + allSumInSubUnits + " ;your=" + sumInSubUnits);
            throw new RuntimeException();
        } else {
            log.info("Enough money in ATM: has=" + allSumInSubUnits + " ;your=" + sumInSubUnits);
        }
    }

    private long getAllSumInSubUnits(Currency currency) {
        NavigableMap<Nominal, Integer> nominalMap = currencyNominalMap.get(currency);
        return nominalMap.entrySet().stream()
                .map(n -> (long) n.getKey().units() * currency.subUnitsInUnit() * n.getValue())
                .reduce(0L, Long::sum);
    }


}
