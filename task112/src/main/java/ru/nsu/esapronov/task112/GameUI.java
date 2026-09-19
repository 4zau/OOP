package ru.nsu.esapronov.task112;

import java.util.Scanner;

/// Класс для работы с интерфейсом.
public class GameUI {
    private final Scanner scanner;

    public GameUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printRoundStart(int round) {
        System.out.println("\nРаунд " + round);
    }

    public void printTable(Player player, Dealer dealer, boolean hideDealerCard) {
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

    public void printScore(ScoreBoard scoreBoard) {
        int playerWins = scoreBoard.getPlayerWins();
        int dealerWins = scoreBoard.getDealerWins();
        if (playerWins > dealerWins) {
            System.out.printf("Счет %d:%d в вашу пользу.\n", playerWins, dealerWins);
        } else if (dealerWins > playerWins) {
            System.out.printf("Счет %d:%d в пользу дилера.\n", playerWins, dealerWins);
        } else {
            System.out.printf("Счет %d:%d, ничья.\n", playerWins, dealerWins);
        }
    }

    public String askPlayerChoice() {
        System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
        return scanner.nextLine().trim();
    }

    public String askNextRound() {
        System.out.println("\nНажмите Enter для начала следующего раунда, или введите 'q' для выхода.");
        return scanner.nextLine().trim();
    }

    public void printPlayerDrewCard(Card drawn) {
        System.out.printf("Вы открыли карту %s (%d)\n", drawn.getName(), drawn.getValue());
    }

    public void printDealerRevealsCard(Card hidden) {
        System.out.printf("Дилер открывает закрытую карту %s (%d)\n",
                hidden.getName(), hidden.getValue());
    }

    public void printDealerDrewCard(Card drawn) {
        System.out.printf("Дилер открывает карту %s (%d)\n",
                drawn.getName(), drawn.getValue());
    }
}