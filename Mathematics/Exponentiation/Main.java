import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Exponentation</h1><br/>
 * Task is to efficiently calculate values {@code a^b} modulo {@code 10^9+7}.<br/>
 * Note that in this task {@code 0^0=1}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of calculations.<br/>
 * After this, there are {@code n} lines, each containing two integers {@code a} and {@code b}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print each value {@code a^b} modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 0 <= a,b <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 3 4}<br/>
 * {@literal 2 8}<br/>
 * {@literal 123 123}<br/>
 * <i>Output</i>:<br/>
 * {@literal 81}<br/>
 * {@literal 256}<br/>
 * {@literal 921450052}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Exponentiation algorithm = createAlgorithm();
        final long[] results = algorithm.compute();

        final String outputResult = createOutputResult(results);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final long[] results) {
        final StringBuilder sb = new StringBuilder(results.length * 5);
        for (long result : results) {
            sb.append(result).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static Exponentiation createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int computationsCount = reader.nextInt();

            final int[][] array = new int[computationsCount][2];
            for (int i = 0; i < computationsCount; i++) {
                array[i][0] = reader.nextInt();
                array[i][1] = reader.nextInt();
            }

            return new Exponentiation(array);
        }
    }

    static class Exponentiation {

        private static final int MODULO = 1_000_000_000 + 7;

        private final int[][] numbers;

        Exponentiation(int[][] numbers) {
            this.numbers = numbers;
        }

        long[] compute() {

            final long[] results = new long[this.numbers.length];
            for (int i = 0; i < results.length; i++) {
                final int[] singleComputationNumbers = this.numbers[i];
                results[i] = compute(singleComputationNumbers[0], singleComputationNumbers[1]);
            }

            return results;
        }

        private long compute(int base, int degree) {

            if (degree == 0) {
                return 1;
            } else if (degree == 1) {
                return base % MODULO;
            }

            final long t = compute(base, degree / 2);
            if (degree % 2 == 0) {
                return (t * t) % MODULO;
            } else {
                return (((t * t) % MODULO) * base) % MODULO;
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
