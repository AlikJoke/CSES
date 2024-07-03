import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Dynamic Range Sum Queries</h1><br/>
 * Given an array of {@code n} integers, task is to process {@code q} queries of the following types:<br/>
 * <ul>
 *     <li>1. update the value at position {@code k} to {@code u}</li>
 *     <li>2. what is the sum of values in range {@code [a,b]}?</li>
 * </ul><br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code q}: the number of values and queries.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the array values.<br/>
 * Finally, there are {@code q} lines describing the queries.
 * Each line has three integers: either {@literal 1 k u} or {@literal 2 a b}.<br/>
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
 * {@literal 8 4}<br/>
 * {@literal 3 2 4 5 1 1 5 3}<br/>
 * {@literal 2 1 4}<br/>
 * {@literal 2 5 6}<br/>
 * {@literal 1 3 1}<br/>
 * {@literal 2 1 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 14}
 * {@literal 2}
 * {@literal 11}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final DynamicRangeSumQueries algorithm = readInputData();
        final long[] sums = algorithm.executeQueries();

        final String outputResult = createOutputResult(sums);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final long[] sums) {
        final StringBuilder sb = new StringBuilder(sums.length * 5);
        for (long sum : sums) {
            sb.append(sum).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static DynamicRangeSumQueries readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int queriesCount = reader.nextInt();

            final long[] array = new long[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = reader.nextInt();
            }

            int sumQueriesCount = 0;
            final int[][] queries = new int[queriesCount][3];
            for (int i = 0; i < queriesCount; i++) {
                final int operation = reader.nextInt();
                queries[i][0] = operation;
                queries[i][1] = reader.nextInt() - 1;
                queries[i][2] = reader.nextInt() - (operation == 2 ? 1 : 0);

                sumQueriesCount += operation == 2 ? 1 : 0;
            }

            return new DynamicRangeSumQueries(array, queries, sumQueriesCount);
        }
    }

    static class DynamicRangeSumQueries {

        private final long[] array;
        private final int[][] queries;
        private final int sumQueriesCount;

        DynamicRangeSumQueries(long[] array, int[][] queries, int sumQueriesCount) {
            this.array = array;
            this.queries = queries;
            this.sumQueriesCount = sumQueriesCount;
        }

        long[] executeQueries() {

            final long[] result = new long[this.sumQueriesCount];

            final SegmentTree tree = new SegmentTree(this.array);
            for (int i = 0, sumQueryIndex = 0; i < this.queries.length; i++) {
                final int operation = this.queries[i][0];

                if (operation == 1) {
                    final int index = this.queries[i][1];
                    final int newValue = this.queries[i][2];

                    tree.set(index, newValue);
                } else {
                    final int fromIndex = this.queries[i][1];
                    final int toIndex = this.queries[i][2];

                    result[sumQueryIndex++] = tree.sum(fromIndex, toIndex);
                }
            }

            return result;
        }

        static class SegmentTree {

            private final int leavesCount;
            private final long[] segments;

            SegmentTree(long[] array) {
                this.leavesCount = array.length;
                this.segments = new long[this.leavesCount * 2];
                buildTree(array);
            }

            void set(int index, long newValue) {
                int indexInTree = this.leavesCount + index;
                this.segments[indexInTree] = newValue;

                do {
                    final int parentIndexInTree = indexInTree / 2;
                    newValue += this.segments[indexInTree + (indexInTree % 2 == 0 ? 1 : -1)];
                    this.segments[parentIndexInTree] = newValue;
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
