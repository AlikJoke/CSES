import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * <h1>Missing Coin Sum</h1><br/>
 * There are {@code n} coins with positive integer values. What is the smallest sum you cannot create
 * using a subset of the coins?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of coins.<br/>
 * Then there are {@code n} integers: {@code x_1},{@code x_2},...,{@code x_n}: the value of each coin.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the smallest coin sum.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2 9 1 2 7}<br/>
 * <i>Output</i>:<br/>
 * {@literal 6}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final MissingCoinSum algorithm = readInputData();
        final long missingSum = algorithm.computeMinMissingCoinSum();

        System.out.println(missingSum);
    }

    private static MissingCoinSum readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int coinsCount = reader.nextInt();

            final int[] coinWeights = new int[coinsCount];
            for (int i = 0; i < coinsCount; i++) {
                coinWeights[i] = reader.nextInt();
            }

            return new MissingCoinSum(coinWeights);
        }
    }

    static class MissingCoinSum {

        private final SplittableRandom random;
        private final int[] coinWeights;

        MissingCoinSum(int[] coinWeights) {
            this.coinWeights = coinWeights;
            this.random = new SplittableRandom();
        }

        long computeMinMissingCoinSum() {
            quickSort(this.coinWeights, 0, this.coinWeights.length - 1);

            long missingSum = 1;
            for (int coinWeight : this.coinWeights) {
                if (coinWeight > missingSum) {
                    return missingSum;
                }

                missingSum += coinWeight;
            }

            return missingSum;
        }

        private void quickSort(final int[] a, final int leftBound, final int rightBound) {
            final int auxiliaryItem = a[leftBound == rightBound ? leftBound : this.random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i] < auxiliaryItem) {
                    i++;
                }

                while (a[j] > auxiliaryItem) {
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
