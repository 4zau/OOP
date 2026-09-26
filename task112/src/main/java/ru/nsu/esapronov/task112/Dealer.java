package ru.nsu.esapronov.task112;

/**
 * Класс дилера.
 */
public class Dealer extends Participant {
    /**
     * Создает участника с именем Дилер.
     */
    public Dealer() {
        super("Дилер");
    }

    /**
     * Определяет, нужно ли дилеру брать еще карту (останавливается на 17).
     */
    public boolean shouldHit() {
        return getScore() < 17;
    }
}