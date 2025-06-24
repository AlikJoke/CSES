import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1683/"><h1>Planets and Kingdoms</h1><br/></a>
 * A game has {@code n} planets, connected by {@code m} teleporters. Two planets {@code a} and {@code b} belong
 * to the same kingdom exactly when there is a route both from {@code a} to {@code b} and from {@code b} to {@code a}.
 * Task is to determine for each planet its kingdom.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of planets and teleporters. The planets
 * are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * After this, there are {@code m} lines describing the teleporters. Each line has two integers {@code a} and {@code b}:
 * you can travel from planet {@code a} to planet {@code b} through a teleporter.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@code k}: the number of kingdoms. After this, print for each planet a kingdom
 * label between {@code 1} and {@code k}. You can print any valid solution.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 6}<br/>
 * {@literal 1 2}<br/>
 * {@literal 2 3}<br/>
 * {@literal 3 1}<br/>
 * {@literal 3 4}<br/>
 * {@literal 4 5}<br/>
 * {@literal 5 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 * {@literal 1 1 1 2 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final PlanetsAndKingdoms algorithm = createAlgorithm();
        final int[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] result) {
        final StringBuilder sb = new StringBuilder(result.length * 5);
        sb.append(Arrays.stream(result).distinct().count()).append(System.lineSeparator());
        for (int k : result) {
            sb.append(k + 1).append(' ');
        }

        return sb.toString();
    }

    private static PlanetsAndKingdoms createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int planetsCount = reader.nextInt();
            final int teleportersCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[planetsCount];
            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTableReversed = new ArrayList[planetsCount];
            for (int i = 0; i < teleportersCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> fromAdj = adjTable[from];
                if (fromAdj == null) {
                    adjTable[from] = fromAdj = new ArrayList<>();
                }

                fromAdj.add(to);

                List<Integer> toAdj = adjTableReversed[to];
                if (toAdj == null) {
                    adjTableReversed[to] = toAdj = new ArrayList<>();
                }

                toAdj.add(from);
            }

            return new PlanetsAndKingdoms(adjTable, adjTableReversed);
        }
    }

    private static class PlanetsAndKingdoms {

        private final List<Integer>[] adjTable;
        private final List<Integer>[] adjTableReversed;
        private Deque<Integer> orderedNodes;
        private final boolean[] visited;
        private final int[] group;

        PlanetsAndKingdoms(final List<Integer>[] adjTable, final List<Integer>[] adjTableReversed) {
            this.adjTable = adjTable;
            this.adjTableReversed = adjTableReversed;
            this.orderedNodes = new LinkedList<>();
            this.visited = new boolean[adjTable.length];
            this.group = new int[adjTable.length];
        }

        int[] execute() {

            for (int i = 0; i < this.adjTable.length; i++) {
                if (!this.visited[i]) {
                    visitAdj(i);
                }
            }

            final Deque<Integer> orderedNodes = this.orderedNodes;
            this.orderedNodes = new LinkedList<>();

            Arrays.fill(this.visited, false);

            int group = 0;
            while (!orderedNodes.isEmpty()) {
                final int node = orderedNodes.pollLast();
                if (!this.visited[node]) {
                    visitAdjReversed(node, group++);
                }
            }

            return this.group;
        }

        private void visitAdj(final int currentNode) {

            this.visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTable[currentNode];
            if (adjNodes == null) {
                this.orderedNodes.add(currentNode);
                return;
            }

            for (final Integer adjNode : adjNodes) {

                if (!this.visited[adjNode]) {
                    visitAdj(adjNode);
                }
            }

            this.orderedNodes.add(currentNode);
        }

        private void visitAdjReversed(final int currentNode, int group) {
            this.visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTableReversed[currentNode];
            this.orderedNodes.add(currentNode);
            this.group[currentNode] = group;

            if (adjNodes == null) {
                return;
            }

            for (final Integer adjNode : adjNodes) {

                if (!this.visited[adjNode]) {
                    visitAdjReversed(adjNode, group);
                }
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