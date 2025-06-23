import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1682/"><h1>Flight Routes Check</h1><br/></a>
 * There are {@code n} cities and {@code }m flight connections. Task is to check if you can travel from
 * any city to any other city using the available flights.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code m}: the number of cities and flights.<br/>
 * After this, there are {@code m} lines describing the flights. Each line has two integers {@code a} and {@code b}:
 * there is a flight from city {@code a} to city {@code b}. All flights are one-way flights.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@literal YES} if all routes are possible, and {@code NO} otherwise. In the latter case also print
 * two cities {@code a} and {@code b} such that you cannot travel from city {@code a} to city {@code b}.
 * If there are several possible solutions, you can print any of them.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 5}<br/>
 * {@literal 1 2}<br/>
 * {@literal 2 3}<br/>
 * {@literal 3 1}<br/>
 * {@literal 1 4}<br/>
 * {@literal 3 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal NO}<br/>
 * {@literal 4 2}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String YES_ANSWER = "YES";
    private static final String NO_ANSWER = "NO";

    public static void main(String[] args) throws Exception {
        final FlightRoutesCheck algorithm = createAlgorithm();
        final int[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] result) {
        if (result.length == 0) {
            return YES_ANSWER;
        }

        return NO_ANSWER + System.lineSeparator() + (result[0] + 1) + ' ' + (result[1] + 1);
    }

    private static FlightRoutesCheck createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int citiesCount = reader.nextInt();
            final int roadsCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[citiesCount];
            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTableReversed = new ArrayList[citiesCount];
            for (int i = 0; i < roadsCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> fromCityAdj = adjTable[from];
                if (fromCityAdj == null) {
                    adjTable[from] = fromCityAdj = new ArrayList<>();
                }

                fromCityAdj.add(to);

                List<Integer> toCityAdj = adjTableReversed[to];
                if (toCityAdj == null) {
                    adjTableReversed[to] = toCityAdj = new ArrayList<>();
                }

                toCityAdj.add(from);
            }

            return new FlightRoutesCheck(adjTable, adjTableReversed);
        }
    }

    private static class FlightRoutesCheck {

        private final List<Integer>[] adjTable;
        private final List<Integer>[] adjTableReversed;
        private Deque<Integer> orderedNodes;
        private final boolean[] visited;

        FlightRoutesCheck(final List<Integer>[] adjTable, final List<Integer>[] adjTableReversed) {
            this.adjTable = adjTable;
            this.adjTableReversed = adjTableReversed;
            this.orderedNodes = new LinkedList<>();
            this.visited = new boolean[adjTable.length];
        }

        int[] execute() {

            for (int i = 0; i < this.adjTable.length; i++) {
                if (!this.visited[i]) {
                    visitAdj(i);
                }
            }

            for (int i = 0; i < this.visited.length; i++) {
                if (!this.visited[i]) {
                    return new int[] { 0, i };
                }
            }

            final Deque<Integer> orderedNodes = this.orderedNodes;
            this.orderedNodes = new LinkedList<>();

            Arrays.fill(this.visited, false);

            while (!orderedNodes.isEmpty()) {
                final int node = orderedNodes.pollLast();
                if (!this.visited[node]) {
                    visitAdjReversed(node);
                    if (this.orderedNodes.size() == this.adjTable.length) {
                        break;
                    }

                    for (int i = 0; i < this.adjTable.length; i++) {
                        if (!this.visited[i]) {
                            return new int[] { i, this.orderedNodes.pop() };
                        }
                    }
                }
            }

            return new int[0];
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

        private void visitAdjReversed(final int currentNode) {
            this.visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTableReversed[currentNode];
            this.orderedNodes.add(currentNode);

            if (adjNodes == null) {
                return;
            }

            for (final Integer adjNode : adjNodes) {

                if (!this.visited[adjNode]) {
                    visitAdjReversed(adjNode);
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