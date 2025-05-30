import java.io.DataInputStream;
import java.io.IOException;

/**
 * <a href="https://cses.fi/problemset/task/3220/"><h1>Sliding Window Sum</h1><br/></a>
 * Given an array of {@code n} integers. Task is to calculate the sum of each window of {@code k} elements, from left to right.<br/>
 * In this problem the input data is large and it is created using a generator.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of elements and the size of the window.<br/>
 * The next line contains four integers {@code x}, {@code a}, {@code b} and {@code c}: the input generator parameters.
 * The input is generated as follows:<br/>
 * <ul>
 *     <li>x1 = x</li>
 *     <li>x_i=(a * x_{i-1} + b) mod c for i=2,3,...,n</li>
 * </ul>
 * <i><b>Output</b></i>:<br/>
 * Print the xor of all window sums.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 10^7}<br/>
 * {@code 0 <= x, a, b <= 10^9}<br/>
 * {@code 1 <= c <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 5}<br/>
 * {@literal 3 7 1 11}<br/>
 * <i>Output</i>:<br/>
 * {@literal 12}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowSum algorithm = readInputData();
        final long result = algorithm.execute();

        System.out.println(result);
    }

    private static SlidingWindowSum readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final int x = reader.nextInt();
            final int a = reader.nextInt();
            final int b = reader.nextInt();
            final int c = reader.nextInt();

            return new SlidingWindowSum(n, k, x, a, b, c);
        }
    }

    static class SlidingWindowSum {

        private final int n;
        private final int k;
        private final int x;
        private final int a;
        private final int b;
        private final int c;

        SlidingWindowSum(int n, int k, int x, int a, int b, int c) {
            this.n = n;
            this.k = k;
            this.x = x;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        long execute() {

            long currentX = this.x;
            final long[] numbers = new long[this.n];

            long[] sums = new long[this.n - this.k + 1];
            for (int i = 0; i < this.k; i++) {
                sums[0] += currentX;
                numbers[i] = currentX;
                currentX = nextX(currentX);
            }

            for (int i = this.k; i < this.n; i++) {
                numbers[i] = currentX;
                sums[i - this.k + 1] = sums[i - this.k] + currentX - numbers[i - this.k];

                currentX = nextX(currentX);
            }

            long result = sums[0];
            for (int i = 1; i < sums.length; i++) {
                result ^= sums[i];
            }

            return result;
        }

        private long nextX(final long currentX) {
            return (this.a * currentX + this.b) % this.c;
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
