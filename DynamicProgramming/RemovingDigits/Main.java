import java.util.Scanner;

/**
 * <h1>Removing Digits</h1><br/>
 * Given an integer {@code n}. On each step, you may subtract one of the digits from the number.<br/>
 * How many steps are required to make the number equal to {@code 0}?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum number of steps.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 27}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final RemovingDigits algorithm = createAlgorithm();
        final int steps = algorithm.computeSteps();

        System.out.println(steps);
    }

    private static RemovingDigits createAlgorithm() {
        try (Scanner scanner = new Scanner(System.in)) {
            final int number = scanner.nextInt();
            return new RemovingDigits(number);
        }
    }

    static class RemovingDigits {

        private final int number;

        RemovingDigits(int number) {
            this.number = number;
        }

        int computeSteps() {
            final int[] steps = new int[this.number + 1];

            for (int i = 1; i <= this.number; i++) {

                int maxNumber = 0;
                int temp = i;
                while (temp > 0) {
                    maxNumber = Math.max(temp % 10, maxNumber);
                    temp /= 10;
                }

                steps[i] = steps[i - maxNumber] + 1;
            }

            return steps[this.number];
        }
    }
}
