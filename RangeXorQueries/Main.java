import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Range Xor Queries</h1><br/>
 * Given an array of {@code n} integers, task is to process {@code q} queries of the form: what is the xor sum of values in range {@code [a,b]}?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code q}: the number of values and queries.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the array values.<br/>
 * Finally, there are {@code q} lines describing the queries.
 * Each line has two integers {@code a} and {@code b}: what is the xor sum of values in range {@code [a,b]}?<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the result of each query.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,q <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/>
 * {@code 1 <= a <= b <= n}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 4}<br/>
 * {@literal 3 2 4 5 1 1 5 3}<br/>
 * {@literal 2 4}<br/>
 * {@literal 5 6}<br/>
 * {@literal 1 8}<br/>
 * {@literal 3 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}
 * {@literal 0}
 * {@literal 6}
 * {@literal 4}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final RangeXorQueries algorithm = readInputData();
        final int[] result = algorithm.executeQueries();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] result) {
        final StringBuilder sb = new StringBuilder(result.length * 5);
        for (int num : result) {
            sb.append(num).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static RangeXorQueries readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int queriesCount = reader.nextInt();

            final int[] array = new int[arraySize];
            for (int i = 0; i < arraySize; i++) {
                array[i] = reader.nextInt();
            }

            final int[][] queries = new int[queriesCount][2];
            for (int i = 0; i < queriesCount; i++) {
                queries[i][0] = reader.nextInt() - 1;
                queries[i][1] = reader.nextInt() - 1;
            }

            return new RangeXorQueries(array, queries);
        }
    }

    static class RangeXorQueries {

        private final int[] array;
        private final int[][] queries;

        RangeXorQueries(int[] array, int[][] queries) {
            this.array = array;
            this.queries = queries;
        }

        int[] executeQueries() {

            final int[] result = new int[this.queries.length];

            final SegmentTree tree = new SegmentTree(this.array);
            for (int i = 0; i < this.queries.length; i++) {
                final int leftIndex = this.queries[i][0];
                final int rightIndex = this.queries[i][1];

                result[i] = tree.xor(leftIndex, rightIndex);
            }

            return result;
        }

        static class SegmentTree {

            private final int leavesCount;
            private final int[] segments;

            SegmentTree(int[] array) {
                this.leavesCount = array.length;
                this.segments = new int[2 * leavesCount];
                fillSegments(this.segments, array);
            }

            int xor(int fromIndex, int toIndex) {

                int fromIndexInTree = this.leavesCount + fromIndex;
                int toIndexInTree = this.leavesCount + toIndex;

                int result = 0;
                while (fromIndexInTree <= toIndexInTree) {
                    if (fromIndexInTree % 2 == 1) {
                        result ^= this.segments[fromIndexInTree];
                        fromIndexInTree = fromIndexInTree / 2 + 1;
                    } else {
                        fromIndexInTree = fromIndexInTree / 2;
                    }

                    if (toIndexInTree % 2 == 1) {
                        toIndexInTree = toIndexInTree / 2;
                    } else {
                        result ^= this.segments[toIndexInTree];
                        toIndexInTree = toIndexInTree / 2 - 1;
                    }
                }

                return result;
            }

            private void fillSegments(final int[] segments, final int[] array) {

                System.arraycopy(array, 0, segments, this.leavesCount, segments.length - this.leavesCount);

                for (int i = this.leavesCount - 1; i > 0; i--) {
                    segments[i] = segments[2 * i] ^ segments[2 * i + 1];
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
