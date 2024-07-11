import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Range Update Queries</h1><br/>
 * Given an array of {@code n} integers, task is to process {@code q} queries of the following types:<br/>
 * <ul>
 *     <li>1. increase each value in range {@code [a,b]} by {@code u}</li>
 *     <li>2. what is the value at position {@code k}?</li>
 * </ul><br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code q}: the number of values and queries.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the array values.<br/>
 * Finally, there are {@code q} lines describing the queries.
 * Each line has four or two integers: either {@code 1 a b u} or {@code 2 k}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the result of each query of type 2.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,q <= 2*10^5}<br/>
 * {@code 1 <= x_i,u <= 10^9}<br/>
 * {@code 1 <= k <= n}<br/>
 * {@code 1 <= a <= b <= n}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 3 2 4 5 1 1 5 3}<br/>
 * {@literal 2 4}<br/>
 * {@literal 1 2 5 1}<br/>
 * {@literal 2 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}
 * {@literal 6}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final RangeUpdateQueries algorithm = readInputData();
        final long[] values = algorithm.executeQueries();

        final String outputResult = createOutputResult(values);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final long[] values) {
        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (long value : values) {
            sb.append(value).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static RangeUpdateQueries readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int queriesCount = reader.nextInt();

            final int[] array = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = reader.nextInt();
            }

            int positionQueries = 0;
            final int[][] queries = new int[queriesCount][4];
            for (int i = 0; i < queriesCount; i++) {
                final int operation = reader.nextInt();
                queries[i][0] = operation;
                queries[i][1] = reader.nextInt() - 1;
                if (operation == 1) {
                    queries[i][2] = reader.nextInt() - 1;
                    queries[i][3] = reader.nextInt();
                }

                positionQueries += operation == 2 ? 1 : 0;
            }

            return new RangeUpdateQueries(array, queries, positionQueries);
        }
    }

    static class RangeUpdateQueries {

        private final int[] array;
        private final int[][] queries;
        private final int positionQueries;

        RangeUpdateQueries(int[] array, int[][] queries, int positionQueries) {
            this.array = array;
            this.queries = queries;
            this.positionQueries = positionQueries;
        }

        long[] executeQueries() {

            final long[] result = new long[this.positionQueries];

            final long[] differences = createDifferencesArray();
            final SegmentTree tree = new SegmentTree(differences);
            for (int i = 0, positionQueryIndex = 0; i < this.queries.length; i++) {
                final int operation = this.queries[i][0];

                if (operation == 1) {
                    final int fromIndex = this.queries[i][1];
                    final int toIndex = this.queries[i][2];
                    final int increaseByValue = this.queries[i][3];

                    tree.increase(fromIndex, increaseByValue);
                    if (toIndex < this.array.length - 1) {
                        tree.increase(toIndex + 1, -increaseByValue);
                    }
                } else {
                    final int positionIndex = this.queries[i][1];
                    result[positionQueryIndex++] = tree.sum(0, positionIndex);
                }
            }

            return result;
        }

        private long[] createDifferencesArray() {
            final long[] differences = new long[this.array.length];
            for (int i = 0, prev = 0; i < this.array.length; prev = this.array[i], i++) {
                differences[i] = this.array[i] - prev;
            }

            return differences;
        }

        static class SegmentTree {

            private final int leavesCount;
            private final long[] segments;

            SegmentTree(long[] array) {
                this.leavesCount = array.length;
                this.segments = new long[this.leavesCount * 2];
                buildTree(array);
            }

            void increase(int index, int value) {
                int indexInTree = this.leavesCount + index;
                this.segments[indexInTree] += value;

                do {
                    final int parentIndexInTree = indexInTree / 2;
                    this.segments[parentIndexInTree] += value;
                    indexInTree = parentIndexInTree;
                } while (indexInTree > 0);
            }

            long sum(int fromIndex, int toIndex) {

                int fromIndexInTree = this.leavesCount + fromIndex;
                int toIndexInTree = this.leavesCount + toIndex;

                long result = 0;
                while (fromIndexInTree <= toIndexInTree) {
                    if (fromIndexInTree % 2 == 1) {
                        result += this.segments[fromIndexInTree];
                        fromIndexInTree = fromIndexInTree / 2 + 1;
                    } else {
                        fromIndexInTree = fromIndexInTree / 2;
                    }

                    if (toIndexInTree % 2 == 1) {
                        toIndexInTree = toIndexInTree / 2;
                    } else {
                        result += this.segments[toIndexInTree];
                        toIndexInTree = toIndexInTree / 2 - 1;
                    }
                }

                return result;
            }

            private void buildTree(long[] array) {

                System.arraycopy(array, 0, this.segments, this.leavesCount, this.segments.length - leavesCount);

                for (int i = this.leavesCount - 1; i > 0; i--) {
                    this.segments[i] = this.segments[2 * i] + this.segments[2 * i + 1];
                }
            }
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
