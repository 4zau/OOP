package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.mockito.Mockito.*;

class BlackjackGameTest {

    private Card card(Rank rank) {
        return new Card(rank, Suit.SPADES);
    }

    @Test
    void testPlayerBlackjack() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(new Scanner("q\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.ACE),
                    card(Rank.NINE),
                    card(Rank.KING),
                    card(Rank.SEVEN)
            );

            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Блэкджек! Вы выиграли раунд!");
        }
    }

    @Test
    void testDealerBlackjack() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("q\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.ACE),
                    card(Rank.NINE),
                    card(Rank.KING)
            );

            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Блэкджек у дилера!");
            verify(ui).printMessage("Дилер выиграл раунд.");
        }
    }

    @Test
    void testBothHaveBlackjack() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("q\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.ACE),
                    card(Rank.ACE),
                    card(Rank.KING),
                    card(Rank.TEN)
            );

            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("У обоих блэкджек! Ничья.");
        }
    }

    @Test
    void testPlayerBusts() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("1\nq\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.SIX),
                    card(Rank.NINE),
                    card(Rank.FIVE),
                    card(Rank.FIVE)
            );

            when(ui.askPlayerChoice()).thenReturn("1");
            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Перебор! Вы проиграли раунд.");
        }
    }

    @Test
    void testDealerBusts() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("0\nq\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.EIGHT),
                    card(Rank.NINE),
                    card(Rank.SEVEN),
                    card(Rank.EIGHT)
            );

            when(ui.askPlayerChoice()).thenReturn("0");
            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("У дилера перебор! Вы выиграли раунд!");
        }
    }

    @Test
    void testPlayerChoosesStandAndDealerWins() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("0\nq\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.TEN),
                    card(Rank.EIGHT),
                    card(Rank.NINE)
            );

            when(ui.askPlayerChoice()).thenReturn("0");
            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Дилер выиграл раунд.");
        }
    }

    @Test
    void testPlayerWinsAfterDealerTurn() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("0\nq\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.EIGHT),
                    card(Rank.NINE),
                    card(Rank.NINE)
            );

            when(ui.askPlayerChoice()).thenReturn("0");
            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Вы выиграли раунд!");
        }
    }

    @Test
    void testDraw() {
        try (var deckMock = mockConstruction(Deck.class);
             var uiMock = mockConstruction(GameUI.class)) {

            BlackjackGame game = new BlackjackGame(
                    new Scanner("0\nq\n"));

            Deck deck = deckMock.constructed().get(0);
            GameUI ui = uiMock.constructed().get(0);

            when(deck.draw()).thenReturn(
                    card(Rank.TEN),
                    card(Rank.NINE),
                    card(Rank.EIGHT),
                    card(Rank.NINE)
            );

            when(ui.askPlayerChoice()).thenReturn("0");
            when(ui.askNextRound()).thenReturn("q");

            game.start();

            verify(ui).printMessage("Ничья!");
        }
    }
}