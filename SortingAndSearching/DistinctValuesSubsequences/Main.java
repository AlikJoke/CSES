import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/3421/"><h1>Distinct Values Subsequences</h1></a><br/>
 * Given an array of {@code n} integers, count the number of subsequences where each element is dictinct.<br/>
 * A subsequence is a sequence of array elements from left to right that may have gaps.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line has an integer {@code n}: the array size.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of subsequences with distinct elements. The answer can be large, so print it modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4}<br/>
 * {@literal 1 2 1 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 11}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final DistinctValuesSubsequences algorithm = createAlgorithm();
        final long count = algorithm.execute();

        System.out.println(count);
    }

    private static DistinctValuesSubsequences createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();

            final Map<Integer, Integer> number2count = new HashMap<>(Math.min(arraySize, 100_000), 1);

            for (int i = 0; i < arraySize; i++) {
                final Integer number = reader.nextInt();
                final int count = number2count.getOrDefault(number, 0);
                number2count.put(number, count + 1);
            }

            return new DistinctValuesSubsequences(number2count);
        }
    }

    private static class DistinctValuesSubsequences {

        private static final int MODULO = 1_000_000_000 + 7;

        private final Map<Integer, Integer> number2count;

        DistinctValuesSubsequences(Map<Integer, Integer> number2count) {
            this.number2count = number2count;
        }

        long execute() {
            long result = 1;

            for (final Map.Entry<Integer, Integer> numberCount : this.number2count.entrySet()) {
                final int count = numberCount.getValue();
                result = (result * (count + 1)) % MODULO;
            }

            return result - 1;
        }
    }

    private static class Reader implements AutoCloseable {

        private static final int BUFFER_SIZE = 1 << 16;

        private final DataInputStream din;
        private final byte[] buffer;

        private int bufferPointer;
        private int bytesRead;

        Reader() {
            this.din = new DataInputStream(System.in);
            this.buffer = new byte[BUFFER_SIZE];
        }

        int nextInt() throws IOException {

            byte c = read();
            while (c <= ' ') {
                c = read();
            }

            final boolean negate = (c == '-');
            if (negate) {
                c = read();
            }

            int result = 0;
            do {
                result = result * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            return negate ? -result : result;
        }

        private void fillBuffer() throws IOException {
            this.bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (this.bytesRead == -1) {
                this.buffer[0] = -1;
            }
        }

        private byte read() throws IOException {
            if (this.bufferPointer == this.bytesRead) {
                fillBuffer();
            }

            return this.buffer[this.bufferPointer++];
        }

        @Override
        public void close() throws IOException {
            this.din.close();
        }
    }
}