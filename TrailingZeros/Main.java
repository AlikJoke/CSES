import java.util.Scanner;

/**
 * <h1>Trailing Zeros</h1><br/>
 * Task is to calculate the number of trailing zeros in the factorial {@code n!}.<br/>
 * For example, {@code 20!=2432902008176640000} and it has {@literal 4} trailing zeros.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of trailing zeros in {@code n!}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 20}<br/>
 * <i>Output</i>:<br/>
 * {@literal 4}<br/>
 *
 * @author Alik
 *
 * @implNote The number of zeros in the factorial increases with the number of numbers divisible
 * by powers of {@literal 5} without a remainder. The total number of zeros is the sum of numbers that
 * are divisible by all powers of {@literal 5} without a remainder.
 */
public class Main {

    public static void main(String[] args) {
        final int n = readInputNumber();

        final TrailingZeros algorithm = new TrailingZeros(n);
        final int result = algorithm.computeCountOfTrailingZeros();

        System.out.print(result);
    }

    private static int readInputNumber() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static class TrailingZeros {

        private final int n;

        TrailingZeros(final int n) {
            this.n = n;
        }

        int computeCountOfTrailingZeros() {

            int countOfTrailingZeros = 0;
            for (int i = 1;;i++) {
                final int divisionByPowerOfFive = (int) (this.n / (Math.pow(5, i)));
                if (divisionByPowerOfFive == 0) {
                    break;
                }

                countOfTrailingZeros += divisionByPowerOfFive;
            }

            return countOfTrailingZeros;
        }
    }
}
