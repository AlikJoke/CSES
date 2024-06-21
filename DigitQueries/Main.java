import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <h1>Digit Queries</h1><br/>
 * Consider an infinite string that consists of all positive integers in increasing order:<br/>
 * {@literal 12345678910111213141516171819202122232425...}.</br>
 * Task is to process {@code q} queries of the form: what is the digit at position {@code k} in the string?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code q}: the number of queries.<br/>
 * After this, there are {@code q} lines that describe the queries. Each line has an integer {@code k}:
 * a {@literal 1}-indexed position in the string.<br/>
 * <i><b>Output</b></i>:<br/>
 * For each query, print the corresponding digit.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= q <= 1000}<br/>
 * {@code 1 <= k <= 10^18}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 7}<br/>
 * {@literal 19}<br/>
 * {@literal 12}<br/>
 * <i>Output</i>:<br/>
 * {@literal 7}<br/>
 * {@literal 4}<br/>
 * {@literal 1}<br/>
 *
 * @author Alik
 *
 * @implNote First, we determine the digit to which the number whose position is transmitted belongs. This is done as follows:
 * from the specified position, the number of characters from all low-order digits (starting from {@literal 1}) is subtracted
 * until the difference becomes negative. The digit at which the difference became negative is the target digit of the desired
 * number. Together with the digit, by subtracting symbols from all low-order digits, the number of symbols in the target digit
 * of the desired number is determined, up to the desired number from the first number in this digit. This number of characters
 * is divided by the digit of the number, thus obtaining the number of numbers in this digit up to the desired number.
 * To the number of numbers in a given digit, the maximum number of numbers in the previous digit is added -
 * this is the number {@literal k}. If the division of the number of characters in the target digit by the digit of the number
 * occurs without a remainder, then the desired number is {@literal k}. Otherwise, the next number after {@literal k} is taken.
 * In a given number we take the last position if the division was without remainder, otherwise a position equal to the remainder of the division.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final long[] queries = readQueries();

        final char[] results = executeQueries(queries);

        final String formattedOutputResults = composeOutputResults(results);
        System.out.println(formattedOutputResults);
    }

    private static String composeOutputResults(final char[] results) {
        final StringBuilder outputAccumulator = new StringBuilder(results.length * 2);
        for (char result : results) {
            outputAccumulator.append(result).append(System.lineSeparator());
        }

        return outputAccumulator.toString();
    }

    private static char[] executeQueries(final long[] queries) {
        final char[] results = new char[queries.length];
        for (int i = 0; i < queries.length; i++) {
            results[i] = executeQuery(queries[i]);
        }

        return results;
    }

    private static char executeQuery(final long query) {
        long countOfSymbolsInBiggestDigitUntilQueryNumber = query;
        int digit = 0;

        long countOfSymbolsInDigitNumbers = 0;
        long countOfNumbersInPrevDigits = 0;
        do {
            countOfSymbolsInBiggestDigitUntilQueryNumber -= countOfSymbolsInDigitNumbers;
            final long countOfNumbersInDigit = power(10, ++digit) - 1 - countOfNumbersInPrevDigits;
            countOfSymbolsInDigitNumbers = countOfNumbersInDigit * digit;
            countOfNumbersInPrevDigits += countOfNumbersInDigit;
        } while (countOfSymbolsInBiggestDigitUntilQueryNumber > countOfSymbolsInDigitNumbers);

        final long nearestNumberToTarget = (countOfSymbolsInBiggestDigitUntilQueryNumber / digit) + power(10, digit - 1) - 1;
        final int countOfPositionShiftsFromNearestNumber = (int) (countOfSymbolsInBiggestDigitUntilQueryNumber % digit);

        final long targetNumber = countOfPositionShiftsFromNearestNumber > 0 ? nearestNumberToTarget + 1 : nearestNumberToTarget;
        final String targetNumberAsString = Long.toString(targetNumber);

        final int targetPositionInNumber = countOfPositionShiftsFromNearestNumber > 0 ? countOfPositionShiftsFromNearestNumber : targetNumberAsString.length();
        return targetNumberAsString.charAt(targetPositionInNumber - 1);
    }

    static long power(int a, int b) {
        return b == 0 ? 1 : a * power(a, b - 1);
    }

    private static long[] readQueries() throws Exception {
        try (ConsoleReader reader = new ConsoleReader()) {
            final int queriesCount = reader.nextInt();

            final long[] queries = new long[queriesCount];
            for (int i = 0; i < queriesCount; i++) {
                queries[i] = reader.nextLong();
            }

            return queries;
        }
    }

    private static class ConsoleReader implements AutoCloseable {
        private final BufferedReader reader;

        ConsoleReader() {
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            return this.reader.readLine();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        long nextLong() throws IOException {
            return Long.parseLong(next());
        }

        @Override
        public void close() throws Exception {
            this.reader.close();
        }
    }
}
