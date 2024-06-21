import java.util.Scanner;

/**
 * <h1>Weird algorithm</h1><br/>
 * An algorithm that takes as input a positive integer {@code n}. If {@code n} is even, the algorithm divides it by two,
 * and if {@code n} is odd, the algorithm multiplies it by three and adds one. The algorithm repeats this, until {@code n} is one.
 * For example, the sequence for {@code n=3} is as follows:<br/>
 * {@literal 3 -> 10 -> 5 -> 16 -> 8 -> 4 -> 2 -> 1}<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line contains an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print a line that contains all values of {@code n} during the algorithm.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3 10 5 16 8 4 2 1}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final long n = readInputNumber();
        final String sequenceString = computeSequenceNumberString(n);

        System.out.println(sequenceString);
    }

    private static int readInputNumber() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static String computeSequenceNumberString(final long n) {
        final StringBuilder sb = new StringBuilder(1024);
        sb.append(n).append(' ');

        long currentNumber = n;
        while (currentNumber != 1) {
            currentNumber = computeNext(currentNumber);

            sb.append(currentNumber).append(' ');
        }

        return sb.toString();
    }

    private static long computeNext(final long n) {
        return n % 2 == 0 ? n >> 1 : n * 3 + 1;
    }
}
