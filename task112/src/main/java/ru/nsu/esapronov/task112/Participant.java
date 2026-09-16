package ru.nsu.esapronov.task112;

public abstract class Participant {
    protected final Hand hand = new Hand();
    protected final String name;

    public Participant(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }
    public Hand getHand() {
        return hand;
    }
    public int getScore() {
        return hand.getScore();
    }
    public void clearHand() {
        hand.clear();
    }
    public boolean isBusted() {
        return hand.isBust();
    }
    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }
    public String getName() {
        return name;
    }
}

class Player extends Participant {
    public Player() {
        super("Игрок");
    }
}

class Dealer extends Participant {
    public Dealer() {
        super("Дилер");
    }
}