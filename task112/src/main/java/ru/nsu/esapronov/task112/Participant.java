package ru.nsu.esapronov.task112;

/// Участник игры.
public abstract class Participant {
    protected final Hand hand = new Hand();
    protected final String name;

    /// @param name Как звать.
    public Participant(String name) {
        this.name = name;
    }

    /// Добавляем карту к игроку.
    /// @param card Карта которая будет добавлена.
    public void addCard(Card card) {
        hand.addCard(card);
    }

    /// Возвращает руку.
    public Hand getHand() {
        return hand;
    }

    /// Возвращает текущие очки.
    public int getScore() {
        return hand.getScore();
    }

    /// Очищает руку.
    public void clearHand() {
        hand.clear();
    }

    /// Проверяет проиграл ли.
    public boolean isBusted() {
        return hand.isBust();
    }

    /// Проверяет на блэкджэк.
    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    /// Возвращает имя.
    public String getName() {
        return name;
    }
}

/// Класс игрока
class Player extends Participant {
    /// Создает участника с именем Игрок.
    public Player() {
        super("Игрок");
    }
}

/// Класс дилера.
class Dealer extends Participant {
    /// Создает участника с именем Дилер.
    public Dealer() {
        super("Дилер");
    }
}