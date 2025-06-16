import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * <a href="https://cses.fi/problemset/task/1197/"><h1>Cycle Finding</h1></a><br/>
 * You are given a directed graph, and your task is to find out if it contains a negative cycle,
 * and also give an example of such a cycle.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of nodes and edges. The nodes
 * are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * After this, the input has {@code m} lines describing the edges. Each line has three integers {@code a}, {@code b} and {@code c}:
 * there is an edge from node {@code a} to node {@code b} whose length is {@code c}.<br/>
 * <i><b>Output</b></i>:<br/>
 * If the graph contains a negative cycle, print first {@literal YES}, and then the nodes in the cycle in their correct order.
 * If there are several negative cycles, you can print any of them. If there are no negative cycles, print {@literal NO}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2500}<br/>
 * {@code 1 <= m <= 5000}<br/>
 * {@code 1 <= a,b <= n}<br/>
 * {@code -10^9 <= c <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 5}<br/>
 * {@literal 1 2 1}<br/>
 * {@literal 2 4 1}<br/>
 * {@literal 3 1 1}<br/>
 * {@literal 4 1 -3}<br/>
 * {@literal 4 3 -2}<br/>
 * <i>Output</i>:<br/>
 * {@literal YES}<br/>
 * {@literal 1 2 4 1}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String NO_ANSWER = "NO";
    private static final String YES_ANSWER = "YES";

    public static void main(String[] args) throws Exception {
        final CycleFinding algorithm = createAlgorithm();
        final int[] path = algorithm.execute();

        final String outputResult = createOutputResult(path);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] path) {
        if (path.length == 0) {
            return NO_ANSWER;
        }

        final StringBuilder sb = new StringBuilder(path.length * 5);
        sb.append(YES_ANSWER).append(System.lineSeparator());
        for (int i = path.length - 1; i >= 0; i--) {
            if (path[i] == 0) {
                continue;
            }
            sb.append(path[i]).append(' ');
        }

        return sb.toString();
    }

    private static CycleFinding createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int nodesCount = reader.nextInt();
            final int edgesCount = reader.nextInt();

            final CycleFinding.Edge[] edges = new CycleFinding.Edge[edgesCount];
            for (int i = 0; i < edgesCount; i++) {
                edges[i] = new CycleFinding.Edge(reader.nextInt(), reader.nextInt(), reader.nextInt());
            }

            return new CycleFinding(nodesCount, edges);
        }
    }

    private static class CycleFinding {

        private final int nodesCount;
        private final Edge[] edges;

        CycleFinding(int nodesCount, Edge[] edges) {
            this.nodesCount = nodesCount;
            this.edges = edges;
        }

        int[] execute() {

            final long[] distances = new long[this.nodesCount + 1];

            final int[] paths = new int[this.nodesCount + 1];
            Arrays.fill(paths, -1);

            int lastRelaxedNode = -1;
            for (int i = 0; i < this.nodesCount; i++) {

                lastRelaxedNode = -1;
                for (final Edge edge : this.edges) {

                    final long currentWeight = distances[edge.from] + edge.weight;
                    if (distances[edge.to] > currentWeight) {
                        distances[edge.to] = currentWeight;
                        paths[edge.to] = edge.from;
                        lastRelaxedNode = edge.to;
                    }
                }
            }

            if (lastRelaxedNode == -1) {
                return new int[0];
            }

            int startCycleNode = lastRelaxedNode;
            for (int i = 1; i <= this.nodesCount; i++) {
                startCycleNode = paths[startCycleNode];
            }

            final int[] cycle = new int[this.nodesCount + 1];
            int i = 0;
            int cycleNode = startCycleNode;
            do {
                cycle[i] = cycleNode;
                cycleNode = paths[cycleNode];
            } while (++i < cycle.length && cycleNode != startCycleNode);
            cycle[i] = startCycleNode;

            return cycle;
        }

        private static class Edge {

            private final int from;
            private final int to;
            private final int weight;

            private Edge(int from, int to, int weight) {
                this.from = from;
                this.to = to;
                this.weight = weight;
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