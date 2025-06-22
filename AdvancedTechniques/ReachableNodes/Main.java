import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * <a href="https://cses.fi/problemset/task/2138/"><h1>Reachable Nodes</h1><br/></a>
 * A directed acyclic graph consists of {@code n} nodes and {@code }m edges. The nodes are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * Calculate for each node the number of nodes you can reach from that node (including the node itself).<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of nodes and edges.<br/>
 * Then there are {@code m} lines describing the edges. Each line has two distinct integers {@code a} and {@code b}: there is an edge from node {@code a} to node {@code b}.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@code n} integers: for each node the number of reachable nodes.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 5*10^4}<br/>
 * {@code 1 <= m <= 10^5}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 6}<br/>
 * {@literal 1 2}<br/>
 * {@literal 1 3}<br/>
 * {@literal 1 4}<br/>
 * {@literal 2 3}<br/>
 * {@literal 3 5}<br/>
 * {@literal 4 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5 3 2 2 1}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ReachableNodes algorithm = createAlgorithm();
        final BitSet[] reachableNodesCount = algorithm.execute();

        final String outputResult = createOutputResult(reachableNodesCount);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final BitSet[] reachableNodesCount) {
        final StringBuilder sb = new StringBuilder(reachableNodesCount.length * 5);
        for (BitSet countSet : reachableNodesCount) {
            sb.append(countSet.cardinality()).append(' ');
        }

        return sb.toString();
    }

    private static ReachableNodes createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int nodesCount = reader.nextInt();
            final int edgesCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[nodesCount];
            for (int i = 0; i < edgesCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> nodeAdj = adjTable[from];
                if (nodeAdj == null) {
                    adjTable[from] = nodeAdj = new ArrayList<>();
                }

                nodeAdj.add(to);
            }

            return new ReachableNodes(adjTable);
        }
    }

    private static class ReachableNodes {

        private final List<Integer>[] adjTable;
        private final boolean[] visited;
        private final BitSet[] visitedFromNodes;

        ReachableNodes(final List<Integer>[] adjTable) {
            this.adjTable = adjTable;
            this.visited = new boolean[this.adjTable.length];
            this.visitedFromNodes = new BitSet[this.adjTable.length];
            for (int i = 0; i < this.adjTable.length; i++) {
                this.visitedFromNodes[i] = new BitSet(this.adjTable.length);
            }
        }

        BitSet[] execute() {
            for (int i = 0; i < this.adjTable.length; i++) {
                if (!this.visited[i]) {
                    traverse(i);
                }
            }

            return visitedFromNodes;
        }

        private void traverse(int node) {

            final List<Integer> child = this.adjTable[node];
            this.visited[node] = true;
            this.visitedFromNodes[node].set(node);
            if (child == null) {
                return;
            }

            for (Integer c : child) {
                if (this.visited[c]) {
                    this.visitedFromNodes[node].or(visitedFromNodes[c]);
                    continue;
                }

                traverse(c);
                visitedFromNodes[node].or(visitedFromNodes[c]);
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