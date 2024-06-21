import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * <h1>Coin Piles</h1><br/>
 * You have two coin piles containing {@code a} and {@code b} coins. On each move, you can either remove
 * one coin from the left pile and two coins from the right pile, or two coins from the left pile and one coin
 * from the right pile.<br/>
 * Task is to efficiently find out if you can empty both the piles.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code t}: the number of tests.<br/>
 * After this, there are {@code t} lines, each of which has two integers {@code a} and {@code b}:
 * the numbers of coins in the piles.<br/>
 * <i><b>Output</b></i>:<br/>
 * For each test, print {@literal YES} if you can empty the piles and {@literal NO} otherwise.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= t <= 10^5}<br/>
 * {@code 0 <= a,b <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 2 1}<br/>
 * {@literal 2 2}<br/>
 * {@literal 3 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal YES}<br/>
 * {@literal NO}<br/>
 * {@literal YES}<br/>
 *
 * @author Alik
 *
 * @implNote If both piles contain {@literal 0} coins, then the problem is solved. Otherwise, it is possible
 * to bring both piles of coins to zero only when the sum of the coins in both piles is divisible by {@literal 3}
 * without a remainder, both piles contain at least one coin, and the number of coins in one of the piles does
 * not exceed the number of coins in the other pile by {@literal 2} times.
 */
public class Main {

    private static final String NO_ANSWER = "NO";
    private static final String YES_ANSWER = "YES";

    public static void main(String[] args) throws Exception {
        final int[][] allCoinPiles = readAllCoinPiles();
        final String result = computeResult(allCoinPiles);

        System.out.println(result);
    }

    private static String computeResult(final int[][] allCoinPiles) {
        final StringBuilder sb = new StringBuilder(allCoinPiles.length * 4);

        for (final int[] pairCoinPiles : allCoinPiles) {
            final boolean canMakeEmptyBothPilesBySteps = canMakeEmptyBothPilesBySteps(pairCoinPiles);

            sb.append(canMakeEmptyBothPilesBySteps ? YES_ANSWER : NO_ANSWER).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static boolean canMakeEmptyBothPilesBySteps(final int[] coinPiles) {
        final int firstPileSize = coinPiles[0];
        final int secondPileSize = coinPiles[1];

        final boolean isBothPilesEmpty = firstPileSize + secondPileSize == 0;
        if (isBothPilesEmpty) {
            return true;
        }

        final int maxCoinPile = Math.max(firstPileSize, secondPileSize);
        final int minCoinPile = Math.min(firstPileSize, secondPileSize);

        return firstPileSize > 0 && secondPileSize > 0
                && (firstPileSize + secondPileSize) % 3 == 0
                && (double) maxCoinPile / minCoinPile <= 2;
    }

    private static int[][] readAllCoinPiles() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int tests = reader.nextInt();

            final int[][] result = new int[tests][2];
            for (int i = 0; i < tests; i++) {
                result[i][0] = reader.nextInt();
                result[i][1] = reader.nextInt();
            }

            return result;
        }
    }

    private static class ConsoleReader implements AutoCloseable {
        private final BufferedReader reader;
        private final String delimiter;
        private StringTokenizer tokenizer;

        ConsoleReader(final String delimiter) {
            this.delimiter = delimiter;
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (this.tokenizer == null || !tokenizer.hasMoreElements()) {
                this.tokenizer = new StringTokenizer(this.reader.readLine(), this.delimiter);
            }
            return this.tokenizer.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        @Override
        public void close() throws Exception {
            this.reader.close();
        }
    }
}
