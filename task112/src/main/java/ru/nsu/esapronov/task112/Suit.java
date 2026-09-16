package ru.nsu.esapronov.task112;

public enum Suit {
    SPADES("Пики"), HEARTS("Червы"), DIAMONDS("Бубны"), CLUBS("Трефы");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}