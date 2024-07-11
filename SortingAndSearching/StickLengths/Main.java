import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * <h1>Stick Lengths</h1><br/>
 * There are {@code n} sticks with some lengths. Task is to modify the sticks so that each stick has the same length.<br/>
 * You can either lengthen and shorten each stick. Both operations cost {@code x} where {@code x} is the difference between the new and original length.<br/>
 * What is the minimum total cost?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of sticks.<br/>
 * Then there are {@code n} integers: {@code p_1},{@code p_2},...,{@code p_n}: the lengths of the sticks.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum total cost.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= p_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2 3 1 5 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final StickLengths algorithm = readInputData();
        final long minTotalCost = algorithm.computeMinTotalCost();

        System.out.println(minTotalCost);
    }

    private static StickLengths readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int sticksCount = reader.nextInt();

            final int[] sticksLengths = new int[sticksCount];
            for (int i = 0; i < sticksCount; i++) {
                sticksLengths[i] = reader.nextInt();
            }

            return new StickLengths(sticksLengths);
        }
    }

    static class StickLengths {

        private final SplittableRandom random;
        private final int[] sticksLengths;

        StickLengths(int[] sticksLengths) {
            this.sticksLengths = sticksLengths;
            this.random = new SplittableRandom();
        }

        long computeMinTotalCost() {
            quickSort(this.sticksLengths, 0, this.sticksLengths.length - 1);
            final int median = this.sticksLengths[this.sticksLengths.length >> 1];

            long totalCost = 0;
            for (int stickLength : this.sticksLengths) {
                totalCost += Math.abs(median - stickLength);
            }

            return totalCost;
        }

        private void quickSort(final int[] a, final int leftBound, final int rightBound) {
            final int median  = a[leftBound == rightBound ? leftBound : this.random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i] < median) {
                    i++;
                }

                while (a[j] > median) {
                    j--;
                }

                if (i <= j) {
                    final int t = a[i];
                    a[i] = a[j];
                    a[j] = t;

                    i++;
                    j--;
                }
            }

            if (j > leftBound) {
                quickSort(a, leftBound, j);
            }

            if (i < rightBound) {
                quickSort(a, i, rightBound);
            }
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
