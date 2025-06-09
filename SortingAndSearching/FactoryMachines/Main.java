import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://cses.fi/problemset/task/1620/"><h1>Factory Machines</h1></a><br/>
 * A factory has {@code n} machines which can be used to make products. Goal is to make a total of {@code t} products.<br/>
 * For each machine, you know the number of seconds it needs to make a single product. The machines can work simultaneously, and you can freely decide their schedule.<br/>
 * What is the shortest time needed to make {@code t} products?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code t}: the number of machines and products.<br/>
 * The second line has {@code n} integers {@code k_1},{@code k_2},...,{@code k_n}: the time needed to make a product using each machine..<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum time needed to make {@code t} products.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= t <= 10^9}<br/>
 * {@code 1 <= k_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3 7}<br/>
 * {@literal 3 2 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 8}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final FactoryMachines algorithm = createAlgorithm();
        final long count = algorithm.execute();

        System.out.println(count);
    }

    private static FactoryMachines createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int productsCount = reader.nextInt();

            final long[] times = new long[arraySize];

            for (int i = 0; i < arraySize; i++) {
                times[i] = reader.nextInt();
            }

            return new FactoryMachines(productsCount, times);
        }
    }

    private static class FactoryMachines {

        private final Map<Long, Integer> number2lastPosition;
        private final long[] times;
        private final int productsCount;
        private long lastMinTime;
        private final Map<Long, Long> products2time;

        FactoryMachines(int productsCount, long[] times) {
            this.times = times;
            this.productsCount = productsCount;
            this.products2time = new HashMap<>();
            this.number2lastPosition = new HashMap<>(this.times.length);
        }

        long execute() {

            Arrays.sort(this.times);

            for (int i = 0; i < this.times.length; i++) {
                this.number2lastPosition.put(this.times[i], i);
            }

            long low = 1;
            long high = this.times[0] * this.productsCount;

            final int result = binarySearch(low, high);
            return result == -1 ? this.lastMinTime : result;
        }

        private int binarySearch(long leftBound, long rightBound) {

            if (leftBound > rightBound) {
                return -1;
            }

            final long medianTime = (rightBound + leftBound) >> 1;
            long maxProducts;
            final int medianPosition;
            if (medianTime >= this.times[this.times.length - 1]) {
                medianPosition = this.times.length;
            } else {
                final Integer positionOfNumber = this.number2lastPosition.get(medianTime);
                medianPosition = positionOfNumber == null ? Math.abs(Arrays.binarySearch(this.times, medianTime)) : positionOfNumber + 1;
            }

            if ((maxProducts = this.products2time.getOrDefault((long) medianPosition, 0L)) == 0) {
                for (int i = 0; i < medianPosition; i++) {
                    maxProducts += medianTime / this.times[i];
                }
            }

            if (maxProducts < this.productsCount) {
                return binarySearch(medianTime + 1, rightBound);
            } else {
                this.lastMinTime = medianTime;
                return binarySearch(leftBound, medianTime - 1);
            }
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