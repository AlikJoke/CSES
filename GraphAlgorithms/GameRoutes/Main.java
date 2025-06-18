import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://cses.fi/problemset/task/1681/"><h1>Game Routes</h1></a><br/>
 * A game has {@code n} levels, connected by {@code m} teleporters, and task is to get from level {@code 1} to level {@code n}.
 * The game has been designed so that there are no directed cycles in the underlying graph. In how many ways can
 * you complete the game?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of levels and teleporters. The levels
 * are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * After this, there are {@code m} lines describing the teleporters. Each line has two integers {@code a} and {@code b}:
 * there is a teleporter from level {@code a} to level {@code b}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of ways you can complete the game. Since the result may be large, print it modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 5}<br/>
 * {@literal 1 2}<br/>
 * {@literal 2 4}<br/>
 * {@literal 1 3}<br/>
 * {@literal 3 4}<br/>
 * {@literal 1 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final GameRoutes algorithm = createAlgorithm();
        final long result = algorithm.execute();

        System.out.println(result);
    }

    private static GameRoutes createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int levelsCount = reader.nextInt();
            final int teleportersCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[levelsCount];
            for (int i = 0; i < teleportersCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> levelAdj = adjTable[from];
                if (levelAdj == null) {
                    adjTable[from] = levelAdj = new ArrayList<>();
                }

                levelAdj.add(to);
            }

            return new GameRoutes(adjTable);
        }
    }

    private static class GameRoutes {

        private static final int MODULO = 1_000_000_000 + 7;

        private final List<Integer>[] adjTable;
        private final boolean[] visited;
        private final long[] ways;

        GameRoutes(final List<Integer>[] adjTable) {
            this.adjTable = adjTable;
            this.ways = new long[adjTable.length];
            this.visited = new boolean[adjTable.length];
        }

        long execute() {
            return visitAdj(0);
        }

        private long visitAdj(final int currentNode) {

            this.visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTable[currentNode];
            if (adjNodes == null) {
                return 0;
            }

            long result = 0;
            for (final Integer adjNode : adjNodes) {

                if (adjNode == this.adjTable.length - 1) {
                    result = (result + 1) % MODULO;
                    continue;
                }

                if (!this.visited[adjNode]) {
                    final long ways = visitAdj(adjNode);
                    if (ways > 0) {
                        this.ways[adjNode] += ways;
                        result = (result + ways) % MODULO;
                    }
                } else if (this.ways[adjNode] > 0) {
                    result = (result + this.ways[adjNode]) % MODULO;
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