import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1642/"><h1>Sum Of Four Values</h1></a>
 * Given an array of {@code n} integers, and task is to find four values (at distinct positions) whose sum is {@code x}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the array size and the target sum.<br/>
 * The second line has {@code n} integers {@code a_1},{@code a_2},...,{@code a_n}: the array values.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print four integers: the positions of the values.<br/>
 * If there are several solutions, you may print any of them. If there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 1000}<br/>
 * {@code 0 <= x,a_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 15}<br/>
 * {@literal 3 2 5 8 1 3 2 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2 4 6 7}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE_LABEL = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {

        final SumOfFourValues algorithm = createAlgorithm();
        final int[] positions = algorithm.execute();

        final String outputResult = createOutputResult(positions);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] values) {
        if (values.length == 0) {
            return IMPOSSIBLE_LABEL;
        }

        return values[0] + " " + values[1] + " " + values[2] + " " + values[3];
    }

    private static SumOfFourValues createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int targetSum = reader.nextInt();

            final SumOfFourValues.IntNumber[] sequence = new SumOfFourValues.IntNumber[arraySize];

            int countOfElementsLessThanTargetSum = 0;
            for (int i = 0; i < arraySize; i++) {
                final int number = reader.nextInt();
                if (number >= targetSum) {
                    countOfElementsLessThanTargetSum++;
                }

                sequence[i] = new SumOfFourValues.IntNumber(number, i);
            }

            return new SumOfFourValues(targetSum, sequence, countOfElementsLessThanTargetSum);
        }
    }

    private static class SumOfFourValues {

        private final IntNumber[] sequence;
        private final long targetSum;
        private final int countOfElementsLessThanTargetSum;

        SumOfFourValues(long targetSum, IntNumber[] sequence, int countOfElementsLessThanTargetSum) {
            this.sequence = sequence;
            this.targetSum = targetSum;
            this.countOfElementsLessThanTargetSum = countOfElementsLessThanTargetSum;
        }

        int[] execute() {

            if (this.sequence.length < 4) {
                return new int[0];
            }

            Arrays.sort(this.sequence);

            final long maxSumOfTwo = this.targetSum - this.sequence[0].value - this.sequence[1].value;

            final int capacity = this.sequence.length - this.countOfElementsLessThanTargetSum;
            final Map<Long, Set<Pair>> sum2terms = new HashMap<>(capacity * capacity);
            for (int i = 0; i < this.sequence.length && this.sequence[i].value < maxSumOfTwo; i++) {

                for (int j = i + 1; j < this.sequence.length && this.sequence[j].value < maxSumOfTwo; j++) {
                    final long sumOfTwo = this.sequence[i].value + this.sequence[j].value;
                    if (sumOfTwo >= this.targetSum) {
                        break;
                    }

                    sum2terms.computeIfAbsent(sumOfTwo, k -> new HashSet<>()).add(new Pair(this.sequence[i], this.sequence[j]));

                    final Long leftValue = this.targetSum - sumOfTwo;
                    final Set<Pair> leftValuePositionPairs = sum2terms.get(leftValue);
                    if (leftValuePositionPairs == null) {
                        continue;
                    }

                    for (final Pair leftValuePositionPair : leftValuePositionPairs) {
                        if (leftValuePositionPair.first.position == this.sequence[i].position
                                || leftValuePositionPair.second.position == this.sequence[i].position
                                || leftValuePositionPair.first.position == this.sequence[j].position
                                || leftValuePositionPair.second.position == this.sequence[j].position) {
                            continue;
                        }

                        final int[] result = new int[4];
                        result[0] = this.sequence[i].position + 1;
                        result[1] = this.sequence[j].position + 1;
                        result[2] = leftValuePositionPair.first.position + 1;
                        result[3] = leftValuePositionPair.second.position + 1;

                        return result;
                    }
                }
            }

            return new int[0];
        }

        private static class Pair {
            private final IntNumber first;
            private final IntNumber second;

            private Pair(IntNumber first, IntNumber second) {
                this.first = first;
                this.second = second;
            }
        }

        private static class IntNumber implements Comparable<IntNumber> {

            private final Integer value;
            private final int position;

            private IntNumber(Integer value, int position) {
                this.value = value;
                this.position = position;
            }

            @Override
            public int compareTo(IntNumber o) {
                return this.value.compareTo(o.value);
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