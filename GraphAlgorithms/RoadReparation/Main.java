import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1675/"><h1>Road Reparation</h1><br/></a>
 * There are {@code n} cities and {@code m} roads between them. Unfortunately, the condition of the roads is so
 * poor that they cannot be used. Task is to repair some of the roads so that there will be a decent route
 * between any two cities.<br/>
 * For each road, you know its reparation cost, and you should find a solution where the total cost
 * is as small as possible.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of cities and roads. The cities
 * are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * Then, there are {@code m} lines describing the roads. Each line has three integers {@code a}, {@code b} and {@code c}:
 * there is a road between cities {@code a} and {@code b}, and its reparation cost is {@code c}. All roads are two-way roads.<br/>
 * Every road is between two different cities, and there is at most one road between two cities.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum total reparation cost. However, if there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/>
 * {@code 1 <= c <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 6}<br/>
 * {@literal 1 2 3}<br/>
 * {@literal 2 3 5}<br/>
 * {@literal 2 4 2}<br/>
 * {@literal 3 4 8}<br/>
 * {@literal 5 1 7}<br/>
 * {@literal 5 4 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 14}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE_LABEL = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {
        final RoadReparation algorithm = createAlgorithm();
        final long result = algorithm.execute();

        System.out.println(result == -1 ? IMPOSSIBLE_LABEL : result);
    }

    private static RoadReparation createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int citiesCount = reader.nextInt();
            final int roadsCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<int[]>[] adjTable = new ArrayList[citiesCount];
            for (int i = 0; i < roadsCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;
                final int cost = reader.nextInt();

                List<int[]> fromCitiesAdj = adjTable[from];
                if (fromCitiesAdj == null) {
                    adjTable[from] = fromCitiesAdj = new ArrayList<>();
                }

                fromCitiesAdj.add(new int[] { to, cost });

                List<int[]> toCitiesAdj = adjTable[to];
                if (toCitiesAdj == null) {
                    adjTable[to] = toCitiesAdj = new ArrayList<>();
                }

                toCitiesAdj.add(new int[] { from, cost });
            }

            return new RoadReparation(adjTable);
        }
    }

    private static class RoadReparation {

        private static final long IMPOSSIBLE = -1;

        private final List<int[]>[] adjTable;
        private final boolean[] included;

        RoadReparation(final List<int[]>[] adjTable) {
            this.adjTable = adjTable;
            this.included = new boolean[adjTable.length];
        }

        long execute() {
            long totalCost = 0;

            final Queue<int[]> edges = new PriorityQueue<>(Comparator.comparingInt(e -> e[1]));
            edges.addAll(this.adjTable[0]);

            int leftNodes = this.adjTable.length - 1;
            this.included[0] = true;

            while (leftNodes > 0) {

                while (true) {
                    final int[] edge = edges.poll();
                    if (edge == null) {
                        return IMPOSSIBLE;
                    }

                    final int linkedCity = edge[0];
                    if (this.included[linkedCity]) {
                        continue;
                    }

                    this.included[linkedCity] = true;
                    totalCost += edge[1];

                    for (int[] adjCity : this.adjTable[linkedCity]) {
                        if (!this.included[adjCity[0]]) {
                            edges.add(adjCity);
                        }
                    }

                    break;
                }

                leftNodes--;
            }

            return totalCost;
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