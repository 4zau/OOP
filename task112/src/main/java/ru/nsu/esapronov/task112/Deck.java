package ru.nsu.esapronov.task112;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final int numDecks;

    public Deck(int numDecks) {
        this.numDecks = numDecks;
        initialize();
    }

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

    public Card draw() {
        if (cards.isEmpty()) {
            initialize();
        }
        return cards.removeLast();
    }
}