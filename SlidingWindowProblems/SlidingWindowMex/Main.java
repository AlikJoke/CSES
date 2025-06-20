import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://cses.fi/problemset/task/3219/"><h1>Sliding Window Mex</h1><br/></a>
 * Given an array of {@code n} integers. Task is to calculate the mex of each window of {@code k} elements, from left to right.<br/>
 * The mex is the smallest nonnegative integer that does not appear in the array.
 * For example, the mex for {@code [3,1,4,3,0,5]} is {@code 2}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of elements and the size of the window.<br/>
 * Then there are {@code n} integers {@literal x_1,x_2,...,x_n}: the contents of the array.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@code n-k+1} values: the mex values.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 2*10^5}<br/>
 * {@code 0 <= x_i <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 1 2 1 0 5 1 1 0}<br/>
 * <i>Output</i>:<br/>
 * {@literal 0 3 2 2 0 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowMex algorithm = readInputData();
        final int[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] values) {
        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (int value : values) {
            sb.append(value).append(' ');
        }

        return sb.toString();
    }

    private static SlidingWindowMex readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final int[] sequence = new int[n];
            int maxNumber = 0;
            for (int i = 0; i < n; i++) {
                maxNumber = Math.max(maxNumber, sequence[i] = reader.nextInt());
            }

            return new SlidingWindowMex(k, sequence, maxNumber);
        }
    }

    static class SlidingWindowMex {

        private final int k;
        private final int[] sequence;
        private final int maxNumber;

        SlidingWindowMex(int k, int[] sequence, int maxNumber) {
            this.k = k;
            this.sequence = sequence;
            this.maxNumber = maxNumber;
        }

        int[] execute() {

            final int[] result = new int[this.sequence.length - this.k + 1];

            final BitSet bits = new BitSet(this.maxNumber);
            final Map<Integer, Integer> countOfNumbers = new HashMap<>(this.sequence.length);

            for (int i = 0; i < this.k; i++) {
                final int number = this.sequence[i];
                bits.set(number);
                countOfNumbers.merge(number, 1, Integer::sum);
            }

            result[0] = bits.nextClearBit(0);

            for (int i = this.k; i < this.sequence.length; i++) {
                final int numberToRemove = this.sequence[i - this.k];
                Integer countOfRemovedNumber = countOfNumbers.get(numberToRemove);
                if (countOfRemovedNumber - 1 == 0) {
                    bits.clear(numberToRemove);
                    countOfNumbers.remove(numberToRemove);
                }
                countOfNumbers.put(numberToRemove, countOfRemovedNumber - 1);

                final int numberToAdd = this.sequence[i];
                bits.set(numberToAdd);
                countOfNumbers.merge(numberToAdd, 1, Integer::sum);

                result[i - this.k + 1] = bits.nextClearBit(0);
            }

            return result;
        }
    }

    static class Reader implements AutoCloseable {

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
