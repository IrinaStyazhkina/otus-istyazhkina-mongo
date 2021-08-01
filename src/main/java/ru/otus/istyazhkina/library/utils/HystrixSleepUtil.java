package ru.otus.istyazhkina.library.utils;

import java.util.Random;

public class HystrixSleepUtil {

    public static void sleepRandomly(Integer seconds) {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
