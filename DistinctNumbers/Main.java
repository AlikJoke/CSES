import java.util.Scanner;

/**
 * <h1>Distinct Numbers</h1><br/>
 * Given a list of {@literal n} integers, and your task is to calculate the number of distinct values in the list.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@literal n}: the number of values.<br/>
 * The second line has {@literal n} integers {@literal x_1},{@literal x_2},...,{@literal x_n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integers: the number of distinct values.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2 3 2 2 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 *
 * @implNote The implementation works by sorting an array of numbers using Quick Sort, after which go through
 * the sorted array again and increase the counter of distinct numbers each time the previous number
 * is not equal to the current one.
 */
public class Main {

    public static void main(String[] args) {
        final int[] numbers = readNumbers();
        final int distinctNumbersCount = computeDistinctNumbersCount(numbers);
        System.out.println(distinctNumbersCount);
    }

    private static int computeDistinctNumbersCount(final int[] numbers) {
        int distinctNumbersCount = 0;

        quickSort(numbers, 0, numbers.length - 1);

        int prevNumber = 0;
        for (int number : numbers) {
            if (number != prevNumber) {
                distinctNumbersCount++;
                prevNumber = number;
            }
        }

        return distinctNumbersCount;
    }

    private static void quickSort(final int[] a, final int leftBound, final int rightBound) {
        final int median = a[leftBound + (rightBound - leftBound) / 2];

        int i = leftBound;
        int j = rightBound;

        while (i <= j) {

            while (a[i] < median) {
                i++;
            }

            while (a[j] > median) {
                j--;
            }

            if (i <= j) {
                final int t = a[i];
                a[i] = a[j];
                a[j] = t;

                i++;
                j--;
            }
        }

        if (j > leftBound) {
            quickSort(a, leftBound, j);
        }

        if (i < rightBound) {
            quickSort(a, i, rightBound);
        }
    }

    private static int[] readNumbers() {
        try (Scanner scanner = new Scanner(System.in)) {
            final int numbersCount = scanner.nextInt();

            final int[] numbers = new int[numbersCount];
            for (int i = 0; i < numbersCount; i++) {
                numbers[i] = scanner.nextInt();
            }

            return numbers;
        }
    }
}
