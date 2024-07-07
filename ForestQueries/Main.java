import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Forest Queries</h1><br/>
 * Given an {@code n x n} grid representing the map of a forest. Each square is either empty or contains a tree.
 * The upper-left square has coordinates {@code (1,1)}, and the lower-right square has coordinates {@code (n,n)}.<br/>
 * Task is to process q queries of the form: how many trees are inside a given rectangle in the forest?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code q}: the size of the forest and the number of queries.<br/>
 * Then, there are {@code n} lines describing the forest. Each line has {@code n} characters: {@literal .} is an empty
 * square and {@literal *} is a tree.<br/>
 * Finally, there are {@code q} lines describing the queries. Each line has four integers {@code y_1}, {@code x_1},
 * {@code y_2}, {@code x_2} corresponding to the corners of a rectangle.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of trees inside each rectangle.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 1000}<br/>
 * {@code 1 <= q <= 2*10^5}<br/>
 * {@code 1 <= y_1 <= y_2 <= n}<br/>
 * {@code 1 <= x_1 <= x_2 <= n}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 3}<br/>
 * {@literal .*..}<br/>
 * {@literal *.**}<br/>
 * {@literal **..}<br/>
 * {@literal ****}<br/>
 * {@literal 2 2 3 4}<br/>
 * {@literal 3 1 3 1}<br/>
 * {@literal 1 1 2 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 1}<br/>
 * {@literal 2}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ForestQueries algorithm = readInputData();
        final long[] treesCount = algorithm.executeQueries();

        final String outputResult = createOutputResult(treesCount);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final long[] treesCount) {
        final StringBuilder sb = new StringBuilder(treesCount.length * 7);
        for (long treeCount : treesCount) {
            sb.append(treeCount).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static ForestQueries readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int forestSize = reader.nextInt();
            final int queriesCount = reader.nextInt();

            final long[][] prefixSums = new long[forestSize + 1][forestSize + 1];
            for (int i = 1; i <= forestSize; i++) {
                for (int j = 1; j <= forestSize; j++) {
                    final char forestItem = reader.nextChar();
                    final long prevPrefixSum = prefixSums[i][j - 1];
                    prefixSums[i][j] = forestItem == '.' ? prevPrefixSum : prevPrefixSum + 1;
                }
            }

            final int[][] queries = new int[queriesCount][4];
            for (int i = 0; i < queriesCount; i++) {
                queries[i][0] = reader.nextInt();
                queries[i][1] = reader.nextInt();
                queries[i][2] = reader.nextInt();
                queries[i][3] = reader.nextInt();
            }

            return new ForestQueries(prefixSums, queries);
        }
    }

    static class ForestQueries {

        private final long[][] prefixSums;
        private final int[][] queries;

        ForestQueries(long[][] prefixSums, int[][] queries) {
            this.prefixSums = prefixSums;
            this.queries = queries;
        }

        long[] executeQueries() {

            final long[] result = new long[this.queries.length];

            for (int i = 0; i < this.queries.length; i++) {
                final int[] query = this.queries[i];

                final int startIndexInOneLevel = query[1];
                final int endIndexInOneLevel = query[3];
                for (int q = query[0]; q <= query[2]; q++) {
                    final long[] prefixSumsOneLevel = this.prefixSums[q];
                    final long countOfTreesInSquareOfOneLevel = prefixSumsOneLevel[endIndexInOneLevel] - prefixSumsOneLevel[startIndexInOneLevel - 1];

                    result[i] += countOfTreesInSquareOfOneLevel;
                }
            }

            return result;
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

        char nextChar() throws IOException {
            byte c = read();
            while (c != '.' && c != '*') {
                c = read();
            }

            return (char) c;
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
