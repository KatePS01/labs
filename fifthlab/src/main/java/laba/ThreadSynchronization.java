package laba;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSynchronization {
    private static volatile int counter = 0;

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("result.txt"))) {
            Thread thread1 = createThread("Thread 1", 250, writer);
            Thread thread2 = createThread("Thread 2", 500, writer);
            Thread thread3 = createThread("Thread 3", 1000, writer);

            thread1.start();
            thread2.start();
            thread3.start();

            while (counter < 240) {
                Thread.sleep(100);
            }

            thread1.interrupt();
            thread2.interrupt();
            thread3.interrupt();

            thread1.join();
            thread2.join();
            thread3.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Thread createThread(String threadName, int sleepTime, PrintWriter writer) {
        return new Thread(() -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
            while (!Thread.currentThread().isInterrupted()) {
                counter++;
                String currentTime = dateFormat.format(new Date());
                String message = threadName + ": " + currentTime + ", Counter: " + counter;
                writer.println(message);
                System.out.println(message);

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}

