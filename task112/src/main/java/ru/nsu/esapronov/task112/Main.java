package ru.nsu.esapronov.task112;

import java.util.Scanner;


/// Запускает игру.
public class Main {
    static void main(String[] args) {
        BlackjackGame game = new BlackjackGame(new Scanner(System.in));
        game.start();
    }
}