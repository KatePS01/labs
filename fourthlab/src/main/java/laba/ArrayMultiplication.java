package laba;

import java.util.Arrays;
import java.util.Random;

public class ArrayMultiplication {
    public static void main(String[] args) {
        int n = 10000;

        int[] input1 = generateRandomArray(n);
        int[] input2 = generateRandomArray(n);

        // Традиційний синхронний варіант
        long time1 = System.currentTimeMillis();
        int[] result1 = multiplyArraysSync(input1, input2);
        System.out.printf("sync : %s\n", System.currentTimeMillis() - time1);

        // Варіант з parallel stream
        long time2 = System.currentTimeMillis();
        int[] result2 = multiplyArraysParallel(input1, input2);
        System.out.printf("parallel : %s\n", System.currentTimeMillis() - time2);

        System.out.println(Arrays.equals(result1, result2));
    }

    private static int[] generateRandomArray(int n) {
        Random random = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(101);
        }
        return array;
    }

    private static int[] multiplyArraysSync(int[] input1, int[] input2) {
        int n = Math.min(input1.length, input2.length);
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            result[i] = input1[i] * input2[i];
        }
        return result;
    }

    private static int[] multiplyArraysParallel(int[] input1, int[] input2) {
        int n = Math.min(input1.length, input2.length);
        int[] result = new int[n];
        Arrays.parallelSetAll(result, i -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return input1[i] * input2[i];
        });
        return result;
    }
}
