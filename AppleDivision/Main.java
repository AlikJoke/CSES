import java.util.Scanner;

/**
 * <h1>Apple Division</h1><br/>
 * There are {@code n} apples with known weights. Task is to divide the apples into two
 * groups so that the difference between the weights of the groups is minimal.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of apples.<br/>
 * The next line has {@code n} integers {@code p_1},{@code p_2},...,{@code p_n}:
 * the weight of each apple.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum difference between the weights of the groups.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 20}<br/>
 * {@code 1 <= p_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 3 2 7 4 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 1}<br/>
 *
 * @author Alik
 *
 * @implNote The implementation is based on searching through all possible combinations of
 * numbers in the sequence and calculating the differences between these combinations and
 * the remaining numbers in the sequence.
 */
public class Main {

    public static void main(String[] args) {
        final int[] weights = readWeights();
        final long minDifference = computeMinDifference(weights);
        System.out.println(minDifference);
    }

    private static long sumOfWeights(final int[] weights, final int fromIndex) {
        return fromIndex == weights.length ? 0 : weights[fromIndex] + sumOfWeights(weights, fromIndex + 1);
    }

    private static long computeMinDifference(final int[] weights) {

        final long sumOfWeights = sumOfWeights(weights, 0);
        long minDifference = sumOfWeights;
        for (int i = 0; i < weights.length; i++) {
            minDifference = computeMinDifferenceInBranch(weights, i, sumOfWeights, sumOfWeights, minDifference);
        }

        return minDifference;
    }

    private static long computeMinDifferenceInBranch(
            final int[] weights,
            final int index,
            final long sumOfAllWeights,
            final long currentSumOfWeightsInBranch,
            final long currentMinDifference) {

        if (index == weights.length - 1) {
            return currentMinDifference;
        }

        final long remainingSumOfWeightsInBranch = currentSumOfWeightsInBranch - weights[index];
        final long currentDifference = Math.abs(sumOfAllWeights - 2 * remainingSumOfWeightsInBranch);
        long minDifferenceInBranch = Math.min(currentMinDifference, currentDifference);

        for (int i = 1; i < weights.length - index; i++) {
            minDifferenceInBranch = computeMinDifferenceInBranch(
                    weights,
                    index + i,
                    sumOfAllWeights,
                    remainingSumOfWeightsInBranch,
                    minDifferenceInBranch
            );
        }

        return minDifferenceInBranch;
    }

    private static int[] readWeights() {
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
