package ru.nsu.esapronov.task112;

/// Обозначает масть карты.
public enum Suit {
    SPADES("Пики"), HEARTS("Червы"), DIAMONDS("Бубны"), CLUBS("Трефы");

    private final String name;

    /// @param name Название масти.
    Suit(String name) {
        this.name = name;
    }

    /// Возвращает название масти.
    public String getName() {
        return name;
    }
}