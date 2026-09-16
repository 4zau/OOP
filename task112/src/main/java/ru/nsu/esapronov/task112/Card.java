package ru.nsu.esapronov.task112;

/// Класс карты содержащий ранг и масть.
public class Card {
    private final Rank rank;
    private final Suit suit;

    /// @param rank Ранг карты
    /// @param suit Масть карты
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /// Возвращает ранг карты.
    public Rank getRank() {
        return rank;
    }

    /// Возвращает значение карты.
    public int getValue() {
        return rank.getValue();
    }

    /// Возвращает строку ранг + масть карты.
    public String getName() {
        return rank.getName() + " " + suit.getName();
    }
}