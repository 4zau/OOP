package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {
    @Test
    public void testPlayerWinsIncrement() {
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.playerWon();
        assertEquals(1, scoreBoard.getPlayerWins());
        assertEquals(0, scoreBoard.getDealerWins());
    }

    @Test
    public void testDealerWinsIncrement() {
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.dealerWon();
        scoreBoard.dealerWon();
        assertEquals(0, scoreBoard.getPlayerWins());
        assertEquals(2, scoreBoard.getDealerWins());
    }

    @Test
    public void testPlayerWins() {
        ScoreBoard scoreBoard = new ScoreBoard();
        assertEquals(ScoreBoard.Winner.PLAYER, scoreBoard.compareScores(20, 18));
    }

    @Test
    public void testDealerWins() {
        ScoreBoard scoreBoard = new ScoreBoard();
        assertEquals(ScoreBoard.Winner.DEALER, scoreBoard.compareScores(18, 20));
    }

    @Test
    public void testDraw() {
        ScoreBoard scoreBoard = new ScoreBoard();
        assertEquals(ScoreBoard.Winner.DRAW, scoreBoard.compareScores(19, 19));
    }
}