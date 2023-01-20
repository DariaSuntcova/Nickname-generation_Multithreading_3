package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static domain.NicknameGeneration.generateText;

public class Main {
    public static AtomicInteger threeLetterWords = new AtomicInteger(0);
    public static AtomicInteger fourLetterWords = new AtomicInteger(0);
    public static AtomicInteger fiveLetterWords = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threads = new ArrayList<>();

        Thread palindromeSearch = new Thread(() -> {
            for (String word : texts) {
                if (word.equals(new StringBuilder(word).reverse().toString())) {
                    addBeautifulWord(word);
                }
            }
        });
        palindromeSearch.start();
        threads.add(palindromeSearch);

        Thread sameLettersSearch = new Thread(() -> {
            for (String word : texts) {
                char[] chars = word.toCharArray();
                boolean sameLetters = true;

                for (int i = 1; i < chars.length; i++) {
                    if (chars[i] != chars[0]) {
                        sameLetters = false;
                        break;
                    }
                }
                if (sameLetters) {
                    addBeautifulWord(word);
               }
            }
        });
        sameLettersSearch.start();
        threads.add(sameLettersSearch);

        Thread ascendingLettersSearch = new Thread(() -> {
            for (String word : texts) {
                char[] chars = word.toCharArray();
                boolean sameLetters = true;

                for (int i = 0; i < chars.length-1; i++) {
                    if (chars[i] > chars[i+1]) {
                        sameLetters = false;
                        break;
                    }
                }
                if (sameLetters) {
                    addBeautifulWord(word);
                }
            }
        });
        ascendingLettersSearch.start();
        threads.add(ascendingLettersSearch);

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Красивых слов с длиной 3: " + threeLetterWords);
        System.out.println("Красивых слов с длиной 4: " + fourLetterWords);
        System.out.println("Красивых слов с длиной 5: " + fiveLetterWords);
    }

    public static void addBeautifulWord(String word) {
        switch (word.length()) {
            case 3:
                threeLetterWords.getAndIncrement();
                break;
            case 4:
                fourLetterWords.getAndIncrement();
                break;
            case 5:
                fiveLetterWords.getAndIncrement();
                break;
        }
    }

}
