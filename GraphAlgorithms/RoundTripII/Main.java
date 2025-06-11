import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1678/"><h1>Round Trip II</h1></a><br/>
 * Byteland has n cities and m flight connections. Task is to design a round trip that
 * begins in a city, goes through one or more other cities, and finally returns to the
 * starting city. Every intermediate city on the route has to be distinct.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of cities and flights.
 * The cities are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * Then, there are {@code m} lines describing the flights. Each line has two integers {@code a} and {@code b}:
 * there is a flight connection from city {@code a} to city {@code b}. All connections are one-way flights from a city to another city.<br/>
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@code k}: the number of cities on the route. Then print {@code k}
 * cities in the order they will be visited. You can print any valid solution.<br/>
 * If there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 5}<br/>
 * {@literal 1 3}<br/>
 * {@literal 2 1}<br/>
 * {@literal 2 4}<br/>
 * {@literal 3 2}<br/>
 * {@literal 3 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 4}<br/>
 * {@literal 2 1 3 2}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE_LABEL = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {
        final RoundTripII algorithm = createAlgorithm();
        final Integer[] path = algorithm.execute();

        final String outputResult = createOutputResult(path);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final Integer[] path) {
        if (path.length == 0) {
            return IMPOSSIBLE_LABEL;
        }

        final StringBuilder sb = new StringBuilder(path.length * 5);
        sb.append(path.length).append(System.lineSeparator());
        for (int i = path.length - 1; i >= 0; i--) {
            sb.append(path[i] + 1).append(' ');
        }

        return sb.toString();
    }

    private static RoundTripII createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int citiesCount = reader.nextInt();
            final int flightsCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[citiesCount];
            for (int i = 0; i < flightsCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> cityAdj = adjTable[from];
                if (cityAdj == null) {
                    adjTable[from] = cityAdj = new ArrayList<>(Math.min(citiesCount, 100));
                }

                cityAdj.add(to);
            }

            return new RoundTripII(adjTable);
        }
    }

    private static class RoundTripII {

        private final List<Integer>[] adjTable;

        RoundTripII(final List<Integer>[] adjTable) {
            this.adjTable = adjTable;
        }

        Integer[] execute() {

            final boolean[] visited = new boolean[this.adjTable.length];
            final int[] path = new int[this.adjTable.length];
            Arrays.fill(path, -1);

            final List<Integer> cycle = new LinkedList<>();
            final Set<Integer> onCyclePath = new HashSet<>();

            for (int i = 0; i < this.adjTable.length; i++) {
                if (!visited[i] && this.adjTable[i] != null) {

                    visitAdj(i, i, visited, path, cycle, onCyclePath);
                    if (!cycle.isEmpty()) {
                        if (path[i] != -1) {
                            cycle.add(i);
                        }

                        return cycle.toArray(new Integer[0]);
                    }
                }
            }

            return new Integer[0];
        }

        private boolean visitAdj(
                final int startNode,
                final int currentNode,
                final boolean[] visited,
                final int[] path,
                final List<Integer> cycle,
                final Set<Integer> onCyclePath
        ) {
            visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTable[currentNode];
            if (adjNodes == null) {
                return false;
            }

            for (final Integer adjNode : adjNodes) {
                if (!cycle.isEmpty()) {
                    return false;
                }

                if (visited[adjNode] && (path[adjNode] != -1 || adjNode == startNode)) {
                    path[adjNode] = currentNode;
                    cycle.add(adjNode);
                    onCyclePath.add(adjNode);

                    return true;
                }

                if (!visited[adjNode] && this.adjTable[adjNode] != null) {
                    path[adjNode] = currentNode;
                    if (visitAdj(startNode, adjNode, visited, path, cycle, onCyclePath)) {
                        if (onCyclePath.contains(adjNode)) {
                            cycle.add(adjNode);
                            return false;
                        } else {
                            cycle.add(adjNode);
                        }

                        return true;
                    }
                    path[adjNode] = -1;
                }
            }

            return false;
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