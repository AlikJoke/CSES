import java.util.Scanner;

/**
 * <h1>Distinct Numbers</h1><br/>
 * Given a list of {@code n} integers, and your task is to calculate the number of distinct values in the list.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of values.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}.<br/>
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

        final DistinctNumbers algorithm = new DistinctNumbers(numbers);
        final int distinctNumbersCount = algorithm.computeDistinctNumbersCount();

        System.out.println(distinctNumbersCount);
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

    private static class DistinctNumbers {

        private final int[] numbersSequence;

        DistinctNumbers(final int[] numbersSequence) {
            this.numbersSequence = numbersSequence;
        }

        int computeDistinctNumbersCount() {
            int distinctNumbersCount = 0;

            quickSort(this.numbersSequence, 0, this.numbersSequence.length - 1);

            int prevNumber = 0;
            for (int number : this.numbersSequence) {
                if (number != prevNumber) {
                    distinctNumbersCount++;
                    prevNumber = number;
                }
            }

            return distinctNumbersCount;
        }

        private void quickSort(final int[] a, final int leftBound, final int rightBound) {
            final int median = a[leftBound + ((rightBound - leftBound) >> 1)];

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
    }
}
