import java.util.Scanner;

/**
 * <h1>Two Sets</h1><br/>
 * Task is to divide the numbers {@code 1,2,...,n} into two sets of equal sum.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line contains an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print {@literal YES}, if the division is possible, and {@literal NO} otherwise.<br/>
 * After this, if the division is possible, print an example of how to create the sets. First,
 * print the number of elements in the first set followed by the elements themselves in a separate line,
 * and then, print the second set in a similar way.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 7}<br/>
 * <i>Output</i>:<br/>
 * {@literal YES}<br/>
 * {@literal 4}<br/>
 * {@literal 1 2 4 7}<br/>
 * {@literal 3}<br/>
 * {@literal 3 5 6}<br/>
 *
 * @author Alik
 *
 * @implNote A series can be divided into two sets of the same sum only if the sum of the series is an even
 * number (odd will not be divisible by {@code 2}). Accordingly, first need to find the sum of a series of numbers
 * from {@code 1} to {@code n}. To do this, we use the arithmetic progression formula with step {@code 1}.
 * Next, we divide the sum by {@code 2} and find the sum that each of the two sets should make up.
 * From this sum we sequentially subtract the numbers of the series, starting with the largest, until the resulting
 * sum becomes less than the largest remaining number in the series. The numbers that were subtracted from the
 * sum and the resulting number, if it is not equal to {@code 0}, are the numbers that make up the first set.
 * The second set is made up of the entire set of numbers minus the numbers from the first set.
 */
public class Main {

    private static final String NO_ANSWER = "NO";
    private static final String YES_ANSWER = "YES";

    public static void main(String[] args) {
        final int n = readInputNumber();
        final String result = computeSetsInStringRepresentation(n);
        System.out.println(result == null ? NO_ANSWER : result);
    }

    private static int readInputNumber() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static String computeSetsInStringRepresentation(final int n) {

        final long seriesSum = (long) n * (n + 1) / 2;
        if (seriesSum % 2 == 1) {
            return null;
        }

        final int[] firstSet = new int[n];
        final int[] secondSet = new int[n];

        for (int i = 0; i < n; i++) {
            secondSet[i] = i + 1;
        }

        long requiredSumOfNumbersInOneSet = seriesSum / 2;
        int firstSetSize = 0;
        for (int i = 0; i < n && requiredSumOfNumbersInOneSet > 0; i++, firstSetSize++) {
            final int currentNumber = (int) Math.min(requiredSumOfNumbersInOneSet, n - i);

            firstSet[i] = currentNumber;
            secondSet[currentNumber - 1] = 0;

            requiredSumOfNumbersInOneSet = requiredSumOfNumbersInOneSet - currentNumber;
        }

        return createOutputString(firstSet, secondSet, firstSetSize);
    }

    private static String createOutputString(final int[] firstSet, final int[] secondSet, final int firstSetSize) {

        final int n = firstSet.length;
        final StringBuilder sb = new StringBuilder(n * 100);

        sb.append(YES_ANSWER)
                .append(System.lineSeparator())
                .append(firstSetSize)
                .append(System.lineSeparator());

        for (int i = 0; i < firstSetSize; i++) {
            sb.append(firstSet[i]).append(' ');
        }

        sb.append(System.lineSeparator())
                .append(n - firstSetSize)
                .append(System.lineSeparator());
        for (int i = 0; i < n; i++) {
            if (secondSet[i] == 0) {
                continue;
            }

            sb.append(secondSet[i]).append(' ');
        }

        return sb.toString();
    }
}
