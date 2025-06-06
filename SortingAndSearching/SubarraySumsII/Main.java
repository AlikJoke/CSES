import java.io.DataInputStream;
import java.io.IOException;

/**
 * <a href="https://cses.fi/problemset/task/1661/"><h1>Subarray Sums II</h1><br/></a><br/>
 * Given an array of {@code n} positive integers, task is to count the number of subarrays having sum {@code x}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the size of the array and the target sum {@code x}.<br/>
 * The next line has {@code n} integers {@literal a_1},{@literal a_2},...,{@literal a_n}: the contents of the array.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the required number of subarrays.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code -10^9 <= x,a_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 7}<br/>
 * {@literal 2 -1 3 5 -2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SubarraySumsII algorithm = createAlgorithm();
        final long countOfSubarrays = algorithm.computeCountOfSubarrays();

        System.out.println(countOfSubarrays);
    }

    private static SubarraySumsII createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int targetSum = reader.nextInt();

            final Long[] prefixSums = new Long[arraySize + 1];
            prefixSums[0] = 0L;

            for (int i = 1; i <= arraySize; i++) {
                prefixSums[i] = prefixSums[i - 1] + reader.nextInt();
            }

            return new SubarraySumsII(prefixSums, targetSum);
        }
    }

    private static class SubarraySumsII {

        private final Long[] prefixSums;
        private final int targetSum;

        SubarraySumsII(Long[] prefixSums, int targetSum) {
            this.prefixSums = prefixSums;
            this.targetSum = targetSum;
        }

        long computeCountOfSubarrays() {

            long countOfSubarrays = 0;
            final Map<Long, Long> countOfPrefixSums = new HashMap<>(this.prefixSums.length + 1, 1);
            for (Long prefixSum : this.prefixSums) {
                final long diff = prefixSum - this.targetSum;
                countOfSubarrays += countOfPrefixSums.getOrDefault(diff, 0L);

                countOfPrefixSums.put(prefixSum, countOfPrefixSums.getOrDefault(prefixSum, 0L) + 1);
            }

            return countOfSubarrays;
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