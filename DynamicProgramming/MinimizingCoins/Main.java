import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Minimizing Coins</h1><br/>
 * Consider a money system consisting of {@code n} coins. Each coin has a positive integer value.
 * Task is to produce a sum of money {@code x} using the available coins in such a way that the number of coins is minimal.<br/>
 * For example, if the coins are {@code {1,5,7}} and the desired sum is 11, an optimal solution is {@code 5+5+1}
 * which requires 3 coins.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the number of coins and the desired sum of money.<br/>
 * The second line has {@code n} distinct integers {@code c_1},{@code c_2},...,{@code c_n}: the value of each coin.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum number of coins. If it is not possible to produce the desired sum, print {@code -1}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 100}<br/>
 * {@code 1 <= x <= 10^6}<br/>
 * {@code 1 <= c_i <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3 11}<br/>
 * {@literal 3 5 7}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws IOException {
        final MinimizingCoins algorithm = createAlgorithm();
        final int numberOfCoins = algorithm.computeNumberOfCoins();

        System.out.println(numberOfCoins);
    }

    private static MinimizingCoins createAlgorithm() throws IOException {
        try (Reader reader = new Reader()) {
            final int numberOfCoins = reader.nextInt();
            final int requiredSum = reader.nextInt();

            final int[] coins = new int[numberOfCoins];
            int minCoin = Integer.MAX_VALUE;
            for (int i = 0; i < numberOfCoins; i++) {
                coins[i] = reader.nextInt();
                minCoin = Math.min(minCoin, coins[i]);
            }

            return new MinimizingCoins(coins, minCoin, requiredSum);
        }
    }

    static class MinimizingCoins {

        private static final int NO_WAYS = -1;

        private final int[] coins;
        private final int minCoin;
        private final int requiredSum;

        MinimizingCoins(int[] coins, int minCoin, int requiredSum) {
            this.requiredSum = requiredSum;
            this.minCoin = minCoin;
            this.coins = coins;
        }

        int computeNumberOfCoins() {
            if (this.requiredSum < this.minCoin) {
                return NO_WAYS;
            } else if (this.requiredSum == this.minCoin) {
                return 1;
            }

            final int[] minCoinsCount = new int[this.requiredSum + 1];

            for (int i = this.minCoin; i <= this.requiredSum; i++) {
                int result = Integer.MAX_VALUE;

                for (int coin : this.coins) {
                    if (coin > i || i != coin && minCoinsCount[i - coin] == 0) {
                        continue;
                    }

                    result = Math.min(result, minCoinsCount[i - coin] + 1);
                }

                minCoinsCount[i] = result == Integer.MAX_VALUE ? 0 : result;
            }

            final int result = minCoinsCount[this.requiredSum];
            return result == 0 ? NO_WAYS : result;
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
