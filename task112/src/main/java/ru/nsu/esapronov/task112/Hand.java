package ru.nsu.esapronov.task112;

import java.util.ArrayList;
import java.util.List;

/// Класс отвечающий за руку играющего.
public class Hand {
    private final List<Card> cards = new ArrayList<>();
    private int score;
    private int acesAs11;

    /// Добавляет карту в колоду.
    /// @param card Карта которая будет добавлена
    public void addCard(Card card) {
        cards.add(card);

        int total = 0, aces = 0;

        for (Card c : cards) {
            if (c.getRank() == Rank.ACE) {
                aces++;
            } else {
                total += c.getValue();
            }
        }

        total += aces * 11;

        acesAs11 = aces;

        while (total > 21 && acesAs11 > 0) {
            total -= 10;
            acesAs11--;
        }

        score = total;
    }

    /// Возвращает текущее значение руки.
    public int getScore() {
        return score;
    }

    /// Выводит строку содержащую карты в колоде.
    /// @param hideSecond Если да, то прячем вторую карту
    public String getCardsDisplay(boolean hideSecond) {
        StringBuilder sb = new StringBuilder("[");
        int countedAces11 = 0;

        for (int i = 0; i < cards.size(); i++) {
            if (hideSecond && i == 1) {
                sb.append("<закрытая карта> ]");
                return sb.toString();
            }

            Card c = getCard(i);
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

    /// Возвращает карту по индексу.
    /// @param index Собственно сам индекс.
    public Card getCard(int index) {
        return cards.get(index);
    }

    /// Очищает руку.
    public void clear() {
        cards.clear();
        score = 0;
        acesAs11 = 0;
    }

    /// Блэкджек!.
    public boolean isBlackjack() {
        return getScore() == 21;
    }

    /// Не блекджек...
    public boolean isBust() {
        return getScore() > 21;
    }
}