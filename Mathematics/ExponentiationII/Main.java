import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Exponentation II</h1><br/>
 * Task is to efficiently calculate values {@code a^(b^c)} modulo {@code 10^9+7}.<br/>
 * Note that in this task {@code 0^0=1}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of calculations.<br/>
 * After this, there are {@code n} lines, each containing three integers {@code a}, {@code b} and {@code c}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print each value {@code a^(b^c)} modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 0 <= a,b,c <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 3 7 1}<br/>
 * {@literal 15 2 2}<br/>
 * {@literal 3 4 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2187}<br/>
 * {@literal 50625}<br/>
 * {@literal 763327764}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ExponentiationII algorithm = createAlgorithm();
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

    private static ExponentiationII createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int computationsCount = reader.nextInt();

            final int[][] array = new int[computationsCount][3];
            for (int i = 0; i < computationsCount; i++) {
                array[i][0] = reader.nextInt();
                array[i][1] = reader.nextInt();
                array[i][2] = reader.nextInt();
            }

            return new ExponentiationII(array);
        }
    }

    static class ExponentiationII {

        private static final int MODULO = 1_000_000_000 + 7;

        private final int[][] numbers;

        ExponentiationII(int[][] numbers) {
            this.numbers = numbers;
        }

        long[] compute() {

            final long[] results = new long[this.numbers.length];
            for (int i = 0; i < results.length; i++) {
                final int[] singleComputationNumbers = this.numbers[i];
                results[i] = compute(singleComputationNumbers[0], singleComputationNumbers[1], singleComputationNumbers[2]);
            }

            return results;
        }

        private long compute(int a, int b, int c) {
            final long degree = exponentiate(b, c, MODULO - 1);
            return exponentiate(a, (int) degree, MODULO);
        }

        private long exponentiate(int base, int degree, int modulo) {

            if (degree == 0) {
                return 1;
            } else if (degree == 1) {
                return base % modulo;
            }

            final long t = exponentiate(base, degree / 2, modulo);
            if (degree % 2 == 0) {
                return (t * t) % modulo;
            } else {
                return (((t * t) % modulo) * base) % modulo;
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
