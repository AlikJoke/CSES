import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * <a href="https://cses.fi/problemset/task/1745/"><h1>Money Sums</h1></a><br/>
 * You have {@code n} coins with certain values. Task is to find all money sums you can create using these coins.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of coins.<br/>
 * The next line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the values of the coins.<br/>
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@code k}: the number of distinct money sums. After this, print all possible sums in increasing order.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 100}<br/>
 * {@code 1 <= x_i <= 1000}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4}<br/>
 * {@literal 4 2 5 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 9}<br/>
 * {@literal 2 4 5 6 7 8 9 11 13}</br>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final MoneySums algorithm = createAlgorithm();
        final Set<Integer> sums = algorithm.execute();

        final String outputResult = createOutputResult(sums);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final Set<Integer> result) {
        final StringBuilder sb = new StringBuilder(result.size() * 5);
        sb.append(result.size()).append(System.lineSeparator());

        for (Integer element : result) {
            sb.append(element).append(' ');
        }

        return sb.toString();
    }

    private static MoneySums createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int coinsCount = reader.nextInt();

            final int[] coins = new int[coinsCount];

            int maxSum = 0;
            for (int i = 0; i < coinsCount; i++) {
                final int coin = reader.nextInt();
                coins[i] = coin;
                maxSum += coin;
            }

            return new MoneySums(coins, maxSum);
        }
    }

    private static class MoneySums {

        private final int[] coins;
        private final int maxSum;

        MoneySums(int[] coins, int maxSum) {
            this.coins = coins;
            this.maxSum = maxSum;
        }

        Set<Integer> execute() {

            final boolean[][] combinations = new boolean[this.coins.length + 1][this.maxSum + 1];
            combinations[0][0] = true;

            for (int i = 1; i <= this.coins.length; i++) {
                final int coin = this.coins[i - 1];
                for (int j = 0; j <= this.maxSum; j++) {
                    if (combinations[i - 1][j] || j - coin >= 0 && combinations[i - 1][j - coin]) {
                        combinations[i][j] = true;
                    }
                }
            }

            final Set<Integer> result = new TreeSet<>();
            for (int j = 1; j <= this.maxSum; j++) {
                if (combinations[this.coins.length][j]) {
                    result.add(j);
                }
            }

            return result;
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