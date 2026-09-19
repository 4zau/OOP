package ru.nsu.esapronov.task112;

import java.util.Scanner;


/// Основной класс.
public class Main {
    /// Запускает игру
    static void main(String[] args) {
        BlackjackGame game = new BlackjackGame(new Scanner(System.in));
        game.start();
    }
}