package ru.nsu.esapronov.task112;

import java.util.Scanner;

/**
 * Класс для работы с интерфейсом.
 */
public class GameUi {
    private final Scanner scanner;

    /**
     * Создаёт интерфейс с указанным источником ввода.
     *
     * @param scanner источник ввода
     */
    public GameUi(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printRoundStart(int round) {
        System.out.println("\nРаунд " + round);
    }

    /**
     * Выводит карты участников.
     *
     * @param player игрок
     * @param dealer дилер
     * @param hideDealerCard скрывать ли вторую карту дилера
     */
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

    /**
     * Выводит счёт игры.
     *
     * @param scoreBoard текущий счёт
     */
    public void printScore(ScoreBoard scoreBoard) {
        int playerWins = scoreBoard.getPlayerWins();
        int dealerWins = scoreBoard.getDealerWins();
        if (playerWins > dealerWins) {
            System.out.printf("Счет %d:%d в вашу пользу.\n", playerWins, dealerWins);
        } else if (dealerWins > playerWins) {
            System.out.printf("Счет %d:%d в пользу дилера.\n",
                    playerWins, dealerWins);
        } else {
            System.out.printf("Счет %d:%d, ничья.\n", playerWins, dealerWins);
        }
    }

    /**
     * Спрашивает выбор игрога про карту и возвращает.
     */
    public String askPlayerChoice() {
        System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться.");
        return scanner.nextLine().trim();
    }

    /**
     * Спрашивает выбор игрога про след раунд и возвращает.
     */
    public String askNextRound() {
        System.out.println("\nНажмите Enter для начала следующего раунда, "
                + "или введите 'q' для выхода.");
        return scanner.nextLine().trim();
    }

    /**
     * Пишет что игрок открыл карту.
     *
     * @param drawn карта
     */
    public void printPlayerDrewCard(Card drawn) {
        System.out.printf("Вы открыли карту %s (%d)\n", drawn.getName(), drawn.getValue());
    }

    /**
     * Пишет что дилер открыл закрытую карту.
     *
     * @param hidden карта
     */
    public void printDealerRevealsCard(Card hidden) {
        System.out.printf("Дилер открывает закрытую карту %s (%d)\n",
                hidden.getName(), hidden.getValue());
    }

    /**
     * Пишет что дилер открыт карту.
     *
     * @param drawn карта
     */
    public void printDealerDrewCard(Card drawn) {
        System.out.printf("Дилер открывает карту %s (%d)\n",
                drawn.getName(), drawn.getValue());
    }
}
