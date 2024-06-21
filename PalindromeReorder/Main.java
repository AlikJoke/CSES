import java.util.Scanner;

/**
 * <h1>Palindrome Reorder</h1><br/>
 * Given a string, task is to reorder its letters in such a way that it becomes a palindrome
 * (i.e., it reads the same forwards and backwards).<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has a string of length {@code n} consisting of characters {@literal A–Z}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print a palindrome consisting of the characters of the original string. Print any valid solution.
 * If there are no solutions, print {@literal NO SOLUTION}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal AAAACACBA}<br/>
 * <i>Output</i>:<br/>
 * {@literal AACABACAA}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String NO_SOLUTION = "NO SOLUTION";

    public static void main(String[] args) {
        final String inputSymbolsSequence = readInputSymbolsSequence();

        final PalindromeReorder algorithm = new PalindromeReorder(inputSymbolsSequence);
        final String palindrome = algorithm.tryComputePalindrome();

        System.out.print(palindrome == null ? NO_SOLUTION : palindrome);
    }

    private static String readInputSymbolsSequence() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    private static class PalindromeReorder {

        private static final char FIRST_ALPHABET_SYMBOL = 'A';
        private static final char LAST_ALPHABET_SYMBOL = 'Z';

        private final String sourceSymbolsSequence;

        PalindromeReorder(final String sourceSymbolsSequence) {
            this.sourceSymbolsSequence = sourceSymbolsSequence;
        }

        String tryComputePalindrome() {

            final char[] symbols = sourceSymbolsSequence.toCharArray();
            final int countOfPossibleUniqueSymbols = LAST_ALPHABET_SYMBOL - FIRST_ALPHABET_SYMBOL + 1;
            final int[] differentCharsCount = new int[countOfPossibleUniqueSymbols];

            for (char symbol : symbols) {
                final int charIndex = LAST_ALPHABET_SYMBOL - symbol;
                differentCharsCount[charIndex] = ++differentCharsCount[charIndex];
            }

            final boolean isSymbolsCountEven = symbols.length % 2 == 0;
            final char[] result = new char[symbols.length];
            int currentPalindromePosition = 0;

            char charWithOddCount = 0;
            for (int i = 0; i < differentCharsCount.length; i++) {
                final int oneCharCount = differentCharsCount[i];
                final char currentChar = (char) (LAST_ALPHABET_SYMBOL - i);
                if (oneCharCount % 2 == 1) {

                    if (isSymbolsCountEven || charWithOddCount != 0) {
                        return null;
                    } else {
                        charWithOddCount = currentChar;
                    }
                }

                for (int j = 0; j < oneCharCount / 2; j++) {
                    result[currentPalindromePosition] = currentChar;
                    result[result.length - ++currentPalindromePosition] = currentChar;
                }
            }

            if (charWithOddCount != 0) {
                result[currentPalindromePosition] = charWithOddCount;
            }

            return new String(result);
        }
    }
}
