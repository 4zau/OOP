package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    @Test
    public void testHandScoreWithoutAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN, Suit.SPADES));
        hand.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        assertEquals(17, hand.getScore());
    }

    @Test
    public void testHandScoreWithAceAsEleven() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        assertEquals(18, hand.getScore());
    }

    @Test
    public void testHandScoreWithAceAsOne() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.TEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.EIGHT, Suit.DIAMONDS));
        assertEquals(19, hand.getScore());
    }

    @Test
    public void testMultipleAces() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.ACE, Suit.DIAMONDS));
        assertEquals(13, hand.getScore());
    }

    @Test
    public void testBlackjack() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand.addCard(new Card(Rank.KING, Suit.HEARTS));
        assertTrue(hand.isBlackjack());
    }

    @Test
    public void testGetCardsDisplayOpen() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.QUEEN, Suit.SPADES));
        hand.addCard(new Card(Rank.THREE, Suit.HEARTS));

        String display = hand.getCardsDisplay(false);
        assertEquals("[Дама Пики (10), Тройка Червы (3)]", display);
    }

    @Test
    public void testGetCardsDisplayHidden() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.CLUBS));
        hand.addCard(new Card(Rank.TEN, Suit.DIAMONDS));

        String display = hand.getCardsDisplay(true);
        assertEquals("[Туз Трефы (11), <закрытая карта> ]", display);
    }
}