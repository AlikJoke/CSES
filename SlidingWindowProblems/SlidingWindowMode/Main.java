import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/3224/"><h1>Sliding Window Mode</h1><br/></a>
 * Given an array of {@code n} integers. Task is to calculate the mode each window of {@code k} elements, from left to right.<br/>
 * The mode is the most frequent element in an array. If there are several possible modes, choose the smallest of them.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of elements and the size of the window.<br/>
 * Then there are {@code n} integers {@literal x_1,x_2,...,x_n}: the contents of the array.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@code n-k+1} values: the modes.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 1 2 3 2 5 2 4 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 1 2 2 2 2 4}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowMode algorithm = readInputData();
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

    private static SlidingWindowMode readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final Integer[] sequence = new Integer[n];
            for (int i = 0; i < n; i++) {
                sequence[i] = reader.nextInt();
            }

            return new SlidingWindowMode(n, k, sequence);
        }
    }

    static class SlidingWindowMode {

        private final int n;
        private final int k;
        private final Integer[] sequence;

        SlidingWindowMode(int n, int k, Integer[] sequence) {
            this.n = n;
            this.k = k;
            this.sequence = sequence;
        }

        int[] execute() {

            final int[] result = new int[this.n - this.k + 1];

            final TreeMap<Integer, Integer> freq2number = new TreeMap<>();
            final TreeMap<Integer, TreeSet<Integer>> numbers2freq = new TreeMap<>();

            for (int i = 0; i < this.k; i++) {

                final Integer number = this.sequence[i];
                final Integer oldFreq = freq2number.get(number);
                final Integer newFreq = oldFreq == null ? 1 : (oldFreq + 1);
                freq2number.put(number, newFreq);

                numbers2freq.computeIfAbsent(newFreq, k -> new TreeSet<>()).add(number);
            }

            result[0] = numbers2freq.lastEntry().getValue().iterator().next();

            for (int i = this.k; i < this.n; i++) {

                final Integer numberToRemove = this.sequence[i - this.k];
                final Integer oldFreqOfNumberToRemove = freq2number.get(numberToRemove);

                final TreeSet<Integer> numbersByOldFreq = numbers2freq.get(oldFreqOfNumberToRemove);
                if (numbersByOldFreq.size() == 1) {
                    numbers2freq.remove(oldFreqOfNumberToRemove);
                } else {
                    numbersByOldFreq.remove(numberToRemove);
                }

                if (oldFreqOfNumberToRemove.equals(1)) {
                    freq2number.remove(numberToRemove);
                } else {
                    final Integer newFreqOfNumberToRemove = oldFreqOfNumberToRemove - 1;
                    freq2number.put(numberToRemove, newFreqOfNumberToRemove);
                    numbers2freq.computeIfAbsent(newFreqOfNumberToRemove, k -> new TreeSet<>()).add(numberToRemove);
                }

                final Integer number = this.sequence[i];
                final Integer oldFreq = freq2number.get(number);
                final Integer newFreq = oldFreq == null ? 1 : (oldFreq + 1);

                freq2number.put(number, newFreq);

                numbers2freq.computeIfAbsent(newFreq, k -> new TreeSet<>()).add(number);

                result[i - this.k + 1] = numbers2freq.lastEntry().getValue().iterator().next();
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
