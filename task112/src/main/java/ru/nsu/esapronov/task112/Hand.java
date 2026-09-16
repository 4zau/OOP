package ru.nsu.esapronov.task112;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getScore() {
        int score = 0;
        int aces = 0;

        for (Card c : cards) {
            if (c.getRank() == Rank.ACE) {
                aces++;
            } else {
                score += c.getValue();
            }
        }

        int total = score + aces * 11;

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    public String getCardsDisplay(boolean hideSecond) {
        int score = 0, aces = 0;
        for (Card c : cards) {
            if (c.getRank() == Rank.ACE) {
                aces++;
            } else {
                score += c.getValue();
            }
        }

        int total = score + aces * 11;

        int acesAs11 = aces;

        while (total > 21 && acesAs11 > 0) {
            total -= 10;
            acesAs11--;
        }

        StringBuilder sb = new StringBuilder("[");
        int countedAces11 = 0;

        for (int i = 0; i < cards.size(); i++) {
            if (hideSecond && i == 1) {
                sb.append("<закрытая карта> ]");
                return sb.toString();
            }

            Card c = cards.get(i);
            if (c.getRank() == Rank.ACE) {
                if (countedAces11 < acesAs11) {
                    sb.append(c.getName()).append(" (11)");
                    countedAces11++;
                } else {
                    sb.append(c.getName()).append(" (1)");
                }
            } else {
                sb.append(c.getName()).append(" (").append(c.getValue()).append(")");
            }

            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public Card getCard(int index) {
        return cards.get(index);
    }
    public void clear() {
        cards.clear();
    }
    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }
    public boolean isBust() {
        return getScore() > 21;
    }
}