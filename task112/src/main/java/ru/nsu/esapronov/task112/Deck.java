package ru.nsu.esapronov.task112;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/// Колода из которой тянутся карты.
public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final int numDecks;

    /// Создаёт колоду.
    /// @param numDecks Сколько колод использовать
    public Deck(int numDecks) {
        this.numDecks = numDecks;
        initialize();
    }

    /// Тусуем карты.
    private void initialize() {
        cards.clear();
        for (int i = 0; i < numDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        Collections.shuffle(cards);
    }

    /// Тянем карту.
    public Card draw() {
        if (cards.isEmpty()) {
            initialize();
        }
        return cards.removeLast();
    }
}