import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * <a href="https://cses.fi/problemset/task/3221/"><h1>Sliding Window Minimum</h1><br/></a>
 * Given an array of {@code n} integers. Task is to calculate the minimum of each window of {@code k} elements, from left to right.<br/>
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
 * Print the xor of all window minimums.<br/>
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
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowMinimum algorithm = readInputData();
        final long result = algorithm.execute();

        System.out.println(result);
    }

    private static SlidingWindowMinimum readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final int x = reader.nextInt();
            final int a = reader.nextInt();
            final int b = reader.nextInt();
            final int c = reader.nextInt();

            return new SlidingWindowMinimum(n, k, x, a, b, c);
        }
    }

    static class SlidingWindowMinimum {

        private final int n;
        private final int k;
        private final int x;
        private final int a;
        private final int b;
        private final int c;

        SlidingWindowMinimum(int n, int k, int x, int a, int b, int c) {
            this.n = n;
            this.k = k;
            this.x = x;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        long execute() {

            long currentX = this.x;

            final int[] windowMinimumPositions = new int[this.n];
            Arrays.fill(windowMinimumPositions, -1);

            final long[] numbers = new long[this.n];

            final long[] minimums = new long[this.n - this.k + 1];
            minimums[0] = currentX;

            for (int i = 0; i < this.k; i++) {
                if (minimums[0] >= currentX) {
                    minimums[0] = currentX;
                    windowMinimumPositions[0] = i;
                }

                numbers[i] = currentX;
                currentX = nextX(currentX);
            }

            int currentPosition = 1;
            int j = windowMinimumPositions[0] + 1;
            while (j < this.k) {
                for (int i = j; i < this.k; i++) {
                    if (windowMinimumPositions[currentPosition] == -1 || numbers[windowMinimumPositions[currentPosition]] >= numbers[i]) {
                        windowMinimumPositions[currentPosition] = i;
                    }
                }

                j = windowMinimumPositions[currentPosition++] + 1;
            }

            int posOffset = 0;

            for (int i = this.k; i < this.n; i++) {
                numbers[i] = currentX;

                if (windowMinimumPositions[posOffset] <= i - this.k) {
                    posOffset++;
                }

                if (windowMinimumPositions[posOffset] == -1 || numbers[windowMinimumPositions[posOffset]] >= currentX) {
                    Arrays.fill(windowMinimumPositions, 0, currentPosition, -1);
                    posOffset = 0;
                    windowMinimumPositions[posOffset] = i;
                    currentPosition = 1;
                    minimums[i - this.k + 1] = currentX;
                } else {
                    minimums[i - this.k + 1] = numbers[windowMinimumPositions[posOffset]];

                    if (numbers[windowMinimumPositions[currentPosition - 1]] >= currentX) {
                        for (int k = posOffset + 1; k < currentPosition; k++) {
                            if (numbers[windowMinimumPositions[k]] >= currentX) {
                                Arrays.fill(windowMinimumPositions, k, currentPosition, -1);
                                currentPosition = k + 1;
                                windowMinimumPositions[k] = i;
                                break;
                            }
                        }
                    } else {
                        windowMinimumPositions[currentPosition++] = i;
                    }
                }

                currentX = nextX(currentX);
            }

            long result = minimums[0];
            for (int i = 1; i < minimums.length; i++) {
                result ^= minimums[i];
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
