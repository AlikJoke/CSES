import java.util.Scanner;

/**
 * <h1>Dice Combinations</h1><br/>
 * Task is to count the number of ways to construct sum {@code n} by throwing a dice one or more times.
 * Each throw produces an outcome between {@literal 1} and {@literal 6}.<br/>
 * For example, if {@code n=3}, there are 4 ways:<br/>
 * <ul>
 *     <li>{@code 1 + 1 + 1}</li>
 *     <li>{@code 1 + 2}</li>
 *     <li>{@code 2 + 1}</li>
 *     <li>{@code 3}</li>
 * </ul><br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of ways modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 4}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final DiceCombinations algorithm = createAlgorithm();
        final int waysCount = algorithm.computeWaysCount();

        System.out.println(waysCount);
    }

    private static DiceCombinations createAlgorithm() {
        try (Scanner scanner = new Scanner(System.in)) {
            final int sum = scanner.nextInt();
            return new DiceCombinations(sum);
        }
    }

    static class DiceCombinations {

        private static final int MODULO = 1_000_000_000 + 7;

        private final int sum;

        DiceCombinations(int sum) {
            this.sum = sum;
        }

        int computeWaysCount() {
            final int[] combinations = new int[this.sum + 1];

            combinations[0] = 1;

            for (int i = 1; i <= this.sum; i++) {
                int result = 0;

                for (int j = 1; j <= 6 && j <= i; j++) {
                    result = (result + combinations[i - j]) % MODULO;
                }

                combinations[i] = result;
            }

            return combinations[this.sum];
        }
    }
}
