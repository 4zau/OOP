package ru.nsu.esapronov.task112;

/**
 * Класс для ведения счета игры.
 */
public class ScoreBoard {
    private int playerWins = 0;
    private int dealerWins = 0;

    /** Возможный результат сравнения очков. */
    public enum Winner {
        PLAYER,
        DEALER,
        DRAW
    }

    public void playerWon() {
        playerWins++;
    }

    public void dealerWon() {
        dealerWins++;
    }

    public int getPlayerWins() {
        return playerWins;
    }

    public int getDealerWins() {
        return dealerWins;
    }

    /**
     * Сравнивает очки участников и обновляет счёт победителя.
     *
     * @param playerScore очки игрока
     * @param dealerScore очки дилера
     * @return победитель или ничья
     */
    public Winner compareScores(int playerScore, int dealerScore) {
        if (playerScore > dealerScore) {
            this.playerWon();
            return Winner.PLAYER;
        } else if (dealerScore > playerScore) {
            this.dealerWon();
            return Winner.DEALER;
        } else {
            return Winner.DRAW;
        }
    }

}
