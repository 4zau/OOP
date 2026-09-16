package ru.nsu.esapronov.task112;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }
    public int getValue() {
        return rank.getValue();
    }
    public String getName() {
        return rank.getName() + " " + suit.getName();
    }
}