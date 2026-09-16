package ru.nsu.esapronov.task112;

import java.util.Scanner;

/// Класс отвечающий за проведение игры.
public class BlackjackGame {
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int round = 1;
    private final Scanner scanner;

    /// Инициализирует всё необходимое.
    /// @param scanner Откуда читается ввод
    public BlackjackGame(Scanner scanner) {
        this.deck = new Deck(1);
        this.player = new Player();
        this.dealer = new Dealer();
        this.scanner = scanner;
    }

    /// Запускает раунды.
    public void start() {
        System.out.println("Добро пожаловать в Блэкджек!");
        while (true) {
            playRound();

            System.out.println("\nНажмите Enter для начала следующего раунда, или введите 'q' для выхода.");

            String input = scanner.nextLine();
            if ("q".equalsIgnoreCase(input.trim())) {
                break;
            }

            round++;
        }
    }

    /// Проводит раунд.
    private void playRound() {
        System.out.println("\nРаунд " + round);
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.draw());
        dealer.addCard(deck.draw());
        player.addCard(deck.draw());
        dealer.addCard(deck.draw());

        System.out.println("Дилер раздал карты");
        printTable(true);

        if (player.hasBlackjack() || dealer.hasBlackjack()) {
            if (player.hasBlackjack() && dealer.hasBlackjack()) {
                System.out.println("У обоих блэкджек! Ничья.");
            } else if (player.hasBlackjack()) {
                System.out.println("Блэкджек! Вы выиграли раунд!");
                playerWins++;
            } else {
                System.out.println("Блэкджек у дилера!");
                printTable(false);
                System.out.println("Дилер выиграл раунд.");
                dealerWins++;
            }
            printScore();
            return;
        }

        System.out.println("\nВаш ход\n-------");

        boolean playerBusted = false, playerBlackjack = false;
        while (true) {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                Card drawn = deck.draw();
                player.addCard(drawn);

                System.out.printf("Вы открыли карту %s (%d)\n", drawn.getName(), drawn.getValue());
                printTable(true);

                if (player.isBusted()) {
                    System.out.println("Перебор! Вы проиграли раунд.");
                    dealerWins++;
                    playerBusted = true;
                    break;
                } else if (player.hasBlackjack()) {
                    System.out.println("Вы набрали 21!");
                    playerBlackjack = true;
                    break;
                }
            } else if ("0".equals(choice)) {
                break;
            }
        }

        if (playerBusted) {
            printScore();
            return;
        }

        if (!playerBlackjack) {
            System.out.println("\nХод дилера\n-------");

            Card hidden = dealer.getHand().getCard(1);
            System.out.printf("Дилер открывает закрытую карту %s (%d)\n",
                    hidden.getName(), hidden.getValue());
            printTable(false);

            while (dealer.getScore() < 17) {
                Card drawn = deck.draw();
                dealer.addCard(drawn);
                System.out.printf("Дилер открывает карту %s (%d)\n",
                        drawn.getName(), drawn.getValue());
                printTable(false);
            }
        }

        if (dealer.isBusted()) {
            System.out.println("У дилера перебор! Вы выиграли раунд!");
            playerWins++;
        } else {
            int pScore = player.getScore();
            int dScore = dealer.getScore();

            if (pScore > dScore) {
                System.out.println("Вы выиграли раунд!");
                playerWins++;
            } else if (dScore > pScore) {
                System.out.println("Дилер выиграл раунд.");
                dealerWins++;
            } else {
                System.out.println("Ничья!");
            }
        }

        printScore();
    }

    /// Выводит карты участнов.
    /// @param hideDealerCard Прятать вторую карту дилера.
    private void printTable(boolean hideDealerCard) {
        System.out.printf("Ваши карты: %s > %d\n",
                player.getHand().getCardsDisplay(false), player.getScore());
        if (hideDealerCard) {
            System.out.printf("Карты дилера: %s\n",
                    dealer.getHand().getCardsDisplay(true));
        } else {
            System.out.printf("Карты дилера: %s > %d\n",
                    dealer.getHand().getCardsDisplay(false), dealer.getScore());
        }
    }

    /// Выводит счёт участников.
    private void printScore() {
        if (playerWins > dealerWins) {
            System.out.printf("Счет %d:%d в вашу пользу.\n", playerWins, dealerWins);
        } else if (dealerWins > playerWins) {
            System.out.printf("Счет %d:%d в пользу дилера.\n", playerWins, dealerWins);
        } else {
            System.out.printf("Счет %d:%d, ничья.\n", playerWins, dealerWins);
        }
    }
}