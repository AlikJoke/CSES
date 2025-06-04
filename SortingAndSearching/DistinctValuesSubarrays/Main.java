import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/3420/"><h1>Distinct Values Subarrays</h1></a><br/>
 * Given an array of {@code n} integers, count the number of subarrays where each element is dictinct.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line has an integer {@code n}: the array size.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of subarrays with distinct elements.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4}<br/>
 * {@literal 1 2 1 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 8}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final DistinctValuesSubarrays algorithm = createAlgorithm();
        final long count = algorithm.execute();

        System.out.println(count);
    }

    private static DistinctValuesSubarrays createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();

            final Integer[] sequence = new Integer[arraySize];
            for (int i = 0; i < arraySize; i++) {
                sequence[i] = reader.nextInt();
            }

            return new DistinctValuesSubarrays(sequence);
        }
    }

    private static class DistinctValuesSubarrays {

        private final Integer[] sequence;

        DistinctValuesSubarrays(Integer[] sequence) {
            this.sequence = sequence;
        }

        long execute() {
            final Queue<Integer> queue = new LinkedList<>();
            final Set<Integer> currentNumbers = new HashSet<>();

            long result = 0;
            for (final Integer number : this.sequence) {

                if (currentNumbers.add(number)) {
                    queue.offer(number);

                    result += 1 + (currentNumbers.size() - 1);
                } else {
                    while (true) {
                        final Integer numberToRemove = queue.poll();
                        if (!number.equals(numberToRemove)) {
                            currentNumbers.remove(numberToRemove);
                        } else {
                            queue.offer(number);
                            result += 1 + (currentNumbers.size() - 1);
                            break;
                        }
                    }
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