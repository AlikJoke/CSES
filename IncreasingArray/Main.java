import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * <h1>Increasing Array</h1><br/>
 * Given an array of {@literal n} integers. Modify the array so that it is increasing, i.e., every element is
 * at least as large as the previous element.<br/>
 * On each move, you may increase the value of any element by one. What is the minimum number of moves required?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@literal n}: the size of the array.<br/>
 * The second line contains {@literal n} integers {@literal x_1},{@literal x_2},...,{@literal x_n}: the contents of the array.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the minimum number of moves.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2 * 10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 3 2 5 1 7}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final int[] sourceArray = readArray();

        long summaryIncreasingCount = 0;
        int prevNumber = sourceArray[0];
        for (int i = 1; i < sourceArray.length; i++) {
            int currentNumber = sourceArray[i];

            if (prevNumber > currentNumber) {
                summaryIncreasingCount += prevNumber - currentNumber;
            } else {
                prevNumber = currentNumber;
            }
        }

        System.out.print(summaryIncreasingCount);
    }

    private static int[] readArray() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int length = reader.nextInt();

            final int[] result = new int[length];
            for (int i = 0; i < length; i++) {
                result[i] = reader.nextInt();
            }

            return result;
        }
    }

    private static class ConsoleReader implements AutoCloseable {
        private final BufferedReader reader;
        private final String delimiter;
        private StringTokenizer tokenizer;

        ConsoleReader(final String delimiter) {
            this.delimiter = delimiter;
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (this.tokenizer == null || !tokenizer.hasMoreElements()) {
                this.tokenizer = new StringTokenizer(this.reader.readLine(), this.delimiter);
            }
            return this.tokenizer.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        @Override
        public void close() throws Exception {
            this.reader.close();
        }
    }
}