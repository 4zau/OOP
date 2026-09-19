package ru.nsu.esapronov.task112;

import java.util.Scanner;

/// Класс отвечающий за проведение игры.
public class BlackjackGame {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private final ScoreBoard scoreBoard;
    private final GameUI ui;
    private int round = 1;

    /// Инициализирует всё необходимое.
    /// @param scanner Откуда читается ввод
    public BlackjackGame(Scanner scanner) {
        this.deck = new Deck(1);
        this.player = new Player();
        this.dealer = new Dealer();
        this.scoreBoard = new ScoreBoard();
        this.ui = new GameUI(scanner);
    }

    /// Запускает раунды.
    public void start() {
        ui.printMessage("Добро пожаловать в Блэкджек!");
        while (true) {
            playRound();

            String input = ui.askNextRound();
            if ("q".equalsIgnoreCase(input)) {
                break;
            }

            round++;
        }
    }

    /// Проводит раунд.
    private void playRound() {
        ui.printRoundStart(round);
        dealInitialCards();

        ui.printMessage("Дилер раздал карты");
        ui.printTable(player, dealer, true);

        if (checkInitialBlackjack()) {
            return;
        }

        boolean playerBusted = playerTurn();
        if (playerBusted) {
            return;
        }

        dealerTurn();
        determineWinner();
    }

    /// Раздает начальные карты.
    private void dealInitialCards() {
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.draw());
        dealer.addCard(deck.draw());
        player.addCard(deck.draw());
        dealer.addCard(deck.draw());
    }

    /// Проверяет наличие блэкджека сразу после раздачи.
    private boolean checkInitialBlackjack() {
        boolean pBj = player.hasBlackjack();
        boolean dBj = dealer.hasBlackjack();

        if (pBj || dBj) {
            if (pBj && dBj) {
                ui.printMessage("У обоих блэкджек! Ничья.");
            } else if (pBj) {
                ui.printMessage("Блэкджек! Вы выиграли раунд!");
                scoreBoard.playerWon();
            } else {
                ui.printMessage("Блэкджек у дилера!");
                ui.printTable(player, dealer, false);
                ui.printMessage("Дилер выиграл раунд.");
                scoreBoard.dealerWon();
            }
            ui.printScore(scoreBoard);
            return true;
        }
        return false;
    }

    /// Ход игрока.
    private boolean playerTurn() {
        ui.printMessage("\nВаш ход\n-------");
        while (true) {
            String choice = ui.askPlayerChoice();

            if ("1".equals(choice)) {
                Card drawn = deck.draw();
                player.addCard(drawn);

                ui.printPlayerDrewCard(drawn);
                ui.printTable(player, dealer, true);

                if (player.isBusted()) {
                    ui.printMessage("Перебор! Вы проиграли раунд.");
                    scoreBoard.dealerWon();
                    ui.printScore(scoreBoard);
                    return true;
                } else if (player.hasBlackjack()) {
                    ui.printMessage("Вы набрали 21!");
                    return false;
                }
            } else if ("0".equals(choice)) {
                return false;
            }
        }
    }

    /// Ход дилера.
    private void dealerTurn() {
        ui.printMessage("\nХод дилера\n-------");

        Card hidden = dealer.getHand().getCard(1);
        ui.printDealerRevealsCard(hidden);
        ui.printTable(player, dealer, false);

        while (dealer.shouldHit()) {
            Card drawn = deck.draw();
            dealer.addCard(drawn);
            ui.printDealerDrewCard(drawn);
            ui.printTable(player, dealer, false);
        }
    }

    /// Определение победителя в конце раунда.
    private void determineWinner() {
        if (dealer.isBusted()) {
            ui.printMessage("У дилера перебор! Вы выиграли раунд!");
            scoreBoard.playerWon();
        } else {
            ScoreBoard.Winner winner = scoreBoard.compareScores(player.getScore(), dealer.getScore());

            if (winner == ScoreBoard.Winner.PLAYER) {
                ui.printMessage("Вы выиграли раунд!");
                scoreBoard.playerWon();
            } else if (winner == ScoreBoard.Winner.DEALER) {
                ui.printMessage("Дилер выиграл раунд.");
                scoreBoard.dealerWon();
            } else {
                ui.printMessage("Ничья!");
            }
        }
        ui.printScore(scoreBoard);
    }
}