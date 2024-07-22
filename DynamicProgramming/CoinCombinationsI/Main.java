import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Coin Combinations I</h1><br/>
 * Consider a money system consisting of {@code n} coins. Each coin has a positive integer value.
 * Task is to calculate the number of distinct ways you can produce a money sum {@code x} using the available coins.<br/>
 * For example, if the coins are {@code {2,3,5}} and the desired sum is 9, there are 8 ways:
 * <ul>
 *     <li>{@code 2 + 2 + 5}</li>
 *     <li>{@code 2 + 5 + 2}</li>
 *     <li>{@code 5 + 2 + 2}</li>
 *     <li>{@code 3 + 3 + 3}</li>
 *     <li>{@code 3 + 2 + 2 + 2}</li>
 *     <li>{@code 2 + 3 + 2 + 2}</li>
 *     <li>{@code 2 + 2 + 3 + 2}</li>
 *     <li>{@code 2 + 2 + 2 + 3}</li>
 * </ul><br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the number of coins and the desired sum of money.<br/>
 * The second line has {@code n} distinct integers {@code c_1},{@code c_2},...,{@code c_n}: the value of each coin.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of ways modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 100}<br/>
 * {@code 1 <= x <= 10^6}<br/>
 * {@code 1 <= c_i <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3 9}<br/>
 * {@literal 2 3 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 8}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws IOException {
        final CoinCombinationsI algorithm = createAlgorithm();
        final int waysCount = algorithm.computeNumberOfWays();

        System.out.println(waysCount);
    }

    private static CoinCombinationsI createAlgorithm() throws IOException {
        try (Reader reader = new Reader()) {
            final int numberOfCoins = reader.nextInt();
            final int requiredSum = reader.nextInt();

            final int[] coins = new int[numberOfCoins];
            int minCoin = Integer.MAX_VALUE;
            for (int i = 0; i < numberOfCoins; i++) {
                coins[i] = reader.nextInt();
                minCoin = Math.min(minCoin, coins[i]);
            }

            return new CoinCombinationsI(coins, minCoin, requiredSum);
        }
    }

    static class CoinCombinationsI {

        private static final int MODULO = 1_000_000_000 + 7;

        private final int[] coins;
        private final int minCoin;
        private final int requiredSum;

        CoinCombinationsI(int[] coins, int minCoin, int requiredSum) {
            this.requiredSum = requiredSum;
            this.minCoin = minCoin;
            this.coins = coins;
        }

        int computeNumberOfWays() {
            if (this.requiredSum < this.minCoin) {
                return 0;
            } else if (this.requiredSum == this.minCoin) {
                return 1;
            }

            final int[] waysCount = new int[this.requiredSum + 1];

            for (int i = this.minCoin; i <= this.requiredSum; i++) {

                for (int coin : this.coins) {
                    if (coin > i || i != coin && waysCount[i - coin] == 0) {
                        continue;
                    }

                    waysCount[i] = (waysCount[i] + waysCount[i - coin] + (i == coin ? 1 : 0)) % MODULO;
                }
            }

            return waysCount[this.requiredSum];
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
