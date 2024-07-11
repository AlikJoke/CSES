import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Maximum Subarray Sum</h1><br/>
 * Given an array of {@code n} integers, task is to find the maximum sum of values in a contiguous, nonempty subarray.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the size of the array.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the array values.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the maximum subarray sum.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code -10^9 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8}<br/>
 * {@literal -1 3 -2 5 3 -5 2 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 9}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final MaximumSubarraySum algorithm = readInputData();
        final long maxSum = algorithm.computeMaxSubarraySum();

        System.out.println(maxSum);
    }

    private static MaximumSubarraySum readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();

            final int[] array = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = reader.nextInt();
            }

            return new MaximumSubarraySum(array);
        }
    }

    static class MaximumSubarraySum {

        private final int[] array;

        MaximumSubarraySum(int[] array) {
            this.array = array;
        }

        long computeMaxSubarraySum() {

            long currentSum = Integer.MIN_VALUE;
            long maxSum = Integer.MIN_VALUE;
            for (int j : this.array) {
                if (currentSum <= 0) {
                    currentSum = Math.max(currentSum, j);
                    continue;
                }

                maxSum = Math.max(maxSum, currentSum);
                currentSum += j;
            }

            return Math.max(maxSum, currentSum);
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
