import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Counting Divisors</h1><br/>
 * Given {@code n} integers, task is to report for each integer the number of its divisors.<br/>
 * For example, if {@code x=18}, the correct answer is {@literal 6} because its divisors are {@literal 1},
 * {@literal 2},{@literal 3},{@literal 6},{@literal 9},{@literal 18}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of integers.<br/>
 * After this, there are {@code n} lines, each containing an integer {@code x}.<br/>
 * <i><b>Output</b></i>:<br/>
 * For each integer, print the number of its divisors.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= x <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 16}<br/>
 * {@literal 17}<br/>
 * {@literal 18}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2}<br/>
 * {@literal 6}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final CountingDivisors algorithm = createAlgorithm();
        final int[] divisorsCount = algorithm.computeDivisorsCount();

        final String outputResult = createOutputResult(divisorsCount);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] divisorsCount) {
        final StringBuilder sb = new StringBuilder(divisorsCount.length * 5);
        for (int oneNumberDivisorsCount : divisorsCount) {
            sb.append(oneNumberDivisorsCount).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static CountingDivisors createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int computationsCount = reader.nextInt();

            final int[] array = new int[computationsCount];
            for (int i = 0; i < computationsCount; i++) {
                array[i] = reader.nextInt();
            }

            return new CountingDivisors(array);
        }
    }

    static class CountingDivisors {

        private static final int MAX_NUMBER = 1_000_000;

        private final int[] numbers;

        CountingDivisors(int[] numbers) {
            this.numbers = numbers;
        }

        int[] computeDivisorsCount() {

            final int[] divisorsCountByNumber = new int[MAX_NUMBER + 1];
            for (int i = 1; i <= MAX_NUMBER; i++) {
                for (int j = i; j <= MAX_NUMBER; j += i) {
                    divisorsCountByNumber[j]++;
                }
            }

            final int[] result = new int[this.numbers.length];
            for (int i = 0; i < this.numbers.length; i++) {
                final int number = this.numbers[i];
                result[i] = divisorsCountByNumber[number];
            }

            return result;
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
