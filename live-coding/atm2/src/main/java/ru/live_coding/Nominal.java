package ru.live_coding;

public record Nominal(int units) implements Comparable<Nominal> {

    @Override
    public int compareTo(Nominal nominal) {
        return Integer.compare(this.units, nominal.units);
    }
}
