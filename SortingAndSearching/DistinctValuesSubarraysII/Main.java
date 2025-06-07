import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/2428/"><h1>Distinct Values Subarrays II</h1></a><br/>
 * Given an array of {@code n} integers, task is to calculate the number of subarrays that have at most {@code k} distinct values.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code k}.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of subarrays.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 2}<br/>
 * {@literal 1 2 3 1 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 10}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final DistinctValuesSubarraysII algorithm = createAlgorithm();
        final long count = algorithm.execute();

        System.out.println(count);
    }

    private static DistinctValuesSubarraysII createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int maxDistinctCount = reader.nextInt();

            final Integer[] sequence = new Integer[arraySize];
            for (int i = 0; i < arraySize; i++) {
                sequence[i] = reader.nextInt();
            }

            return new DistinctValuesSubarraysII(sequence, maxDistinctCount);
        }
    }

    private static class DistinctValuesSubarraysII {

        private final Integer[] sequence;
        private final int maxDistinctCount;

        DistinctValuesSubarraysII(Integer[] sequence, int maxDistinctCount) {
            this.sequence = sequence;
            this.maxDistinctCount = maxDistinctCount;
        }

        long execute() {
            final Queue<Integer> queue = new LinkedList<>();
            final Map<Integer, Integer> currentNumbers = new HashMap<>(1000);

            long result = 0;
            for (final Integer number : this.sequence) {

                currentNumbers.compute(number, (k, v) -> v == null ? 1 : v + 1);
                if (currentNumbers.size() <= this.maxDistinctCount) {
                    queue.offer(number);
                    result += 1 + (queue.size() - 1);
                } else {
                    while (currentNumbers.size() > this.maxDistinctCount) {
                        final Integer numberToRemove = queue.poll();

                        final int countOfOccurrences = currentNumbers.get(numberToRemove);
                        if (countOfOccurrences == 1) {
                            currentNumbers.remove(numberToRemove);
                        } else {
                            currentNumbers.put(numberToRemove, countOfOccurrences - 1);
                        }
                    }

                    queue.offer(number);
                    result += 1 + (queue.size() - 1);
                }
            }

            return result;
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