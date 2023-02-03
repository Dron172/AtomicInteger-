import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final AtomicInteger THREE_LETTER_COUNTER = new AtomicInteger();
    public static final AtomicInteger FOUR_LETTER_COUNTER = new AtomicInteger();
    public static final AtomicInteger FIVE_LETTER_COUNTER = new AtomicInteger();

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        System.out.println(Arrays.toString(texts));

        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (text.equals(
                        new StringBuffer()
                        .append(text)
                        .reverse()
                        .toString())) {
                    wordCounter(text);
                }
            }
        });

        Thread sameLettersThread = new Thread(() -> {
            for (String text : texts) {
                char letter = text.charAt(0);
                if (text.chars()
                        .allMatch((c) -> c == letter)) {
                    wordCounter(text);
                }
            }
        });

        Thread sortedLettersThread = new Thread(() -> {
            for (String text : texts) {
                if (checkSortedLetters(text)) {
                    wordCounter(text);
                }
            }
        });

        palindromeThread.start();
        sameLettersThread.start();
        sortedLettersThread.start();


        try {
            palindromeThread.join();
            sameLettersThread.join();
            sortedLettersThread.join();
        } catch ( Exception e) {
            System.out.println("Прерван");
        }


        System.out.printf("Красивых слов с длиной 3: %d шт\n", THREE_LETTER_COUNTER.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", FOUR_LETTER_COUNTER.get());
        System.out.printf("Красивых слов с длиной 5: %d шт", FIVE_LETTER_COUNTER.get());
    }

    //Генерация текста заданной длины
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //Сортировка слов по возрастанию порядка в таблице uniCode
    public static boolean checkSortedLetters(String text) {
        char firstChar = text.charAt(0);
        char[] array = text.toCharArray();
        boolean isLettersSorted = false;
        for (char c : array) {
            if (c > firstChar) {
                firstChar = c;
                isLettersSorted = true;
            } else if (c == firstChar) {
                isLettersSorted = true;
            } else {
                isLettersSorted = false;
                break;
            }
        }
        return isLettersSorted;
    }

    //Cчетчик букв в никнеймах
       public static void wordCounter(String text) {
        switch (text.length()) {
            case 3:
                THREE_LETTER_COUNTER.getAndIncrement();
                break;
            case 4:
                FOUR_LETTER_COUNTER.getAndIncrement();
                break;
            case 5:
                FIVE_LETTER_COUNTER.getAndIncrement();
                break;
        }
    }
}