package main;

import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Fibonacci {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Введіть число: ");
        int f = in.nextInt();

        System.out.println("Початок обчислення асинхронного потоку.");

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> fibonacci(f));

        System.out.println("Очікування завершення асинхронного потоку.");

        try {
            int res = future.get();
            System.out.println("Отримане значення: " + res);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static int fibonacci(int f) {
        if (f <= 1)
            return f;
        else
            return fibonacci(f - 1) + fibonacci(f - 2);
    }
}