import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * <a href="https://cses.fi/problemset/task/3224/"><h1>Nearest Smaller Values</h1></a><br/>
 * Given an array of {@code n} integers, task is to find for each array position the nearest position
 * to its left having a smaller value.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the size of the array.<br/>
 * The second line has {@code n} integers {@literal x_1,x_2,...,x_n}: the array values.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print {@code n} integers: for each array position the nearest position with a smaller value. If there is no such position, print {@literal 0}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8}<br/>
 * {@literal 2 5 1 4 8 3 2 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 0 1 0 3 4 3 3 7}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final NearestSmallerValues algorithm = readInputData();
        final int[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] values) {
        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (long value : values) {
            sb.append(value).append(' ');
        }

        return sb.toString();
    }

    private static NearestSmallerValues readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();

            final OrderedInteger[] sequence = new OrderedInteger[n];
            for (int i = 0; i < n; i++) {
                sequence[i] = new OrderedInteger(reader.nextInt(), i);
            }

            return new NearestSmallerValues(sequence);
        }
    }

    static class NearestSmallerValues {

        private final OrderedInteger[] sequence;

        NearestSmallerValues(OrderedInteger[] sequence) {
            this.sequence = sequence;
        }

        int[] execute() {

            final int[] result = new int[this.sequence.length];

            final TreeSet<OrderedInteger> queue = new TreeSet<>();

            for (int i = this.sequence.length - 1; i >= 0; i--) {
                final OrderedInteger current = this.sequence[i];
                if (queue.isEmpty()) {
                    queue.add(current);
                    continue;
                }

                final Set<OrderedInteger> higherNumbers = queue.tailSet(current, false);
                for (OrderedInteger higherNumber : higherNumbers) {
                    result[higherNumber.order] = current.order + 1;
                }

                higherNumbers.clear();

                queue.add(current);
            }

            for (OrderedInteger leftNumbers : queue) {
                result[leftNumbers.order] = 0;
            }

            return result;
        }
    }

    static class OrderedInteger implements Comparable<OrderedInteger> {

        private final Integer value;
        private final int order;

        OrderedInteger(Integer value, int order) {
            this.value = value;
            this.order = order;
        }

        @Override
        public int compareTo(OrderedInteger o) {
            return Integer.compare(this.value, o.value);
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