import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1641/"><h1>Sum Of Three Values</h1></a>
 * Given an array of {@code n} integers, and task is to find three values (at distinct positions) whose sum is {@code x}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the array size and the target sum.<br/>
 * The second line has {@code n} integers {@code a_1},{@code a_2},...,{@code a_n}: the array values.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print three integers: the positions of the values.<br/>
 * If there are several solutions, you may print any of them. If there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 5000}<br/>
 * {@code 0 <= x,a_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 8}<br/>
 * {@literal 2 7 5 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 1 3 4}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE_LABEL = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {
        final SumOfThreeValues algorithm = createAlgorithm();
        final int[] positions = algorithm.execute();

        final String outputResult = createOutputResult(positions);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] values) {
        if (values.length == 0) {
            return IMPOSSIBLE_LABEL;
        }

        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (int element : values) {
            sb.append(element).append(' ');
        }

        return sb.toString();
    }

    private static SumOfThreeValues createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int targetSum = reader.nextInt();

            final SumOfThreeValues.IntNumber[] sequence = new SumOfThreeValues.IntNumber[arraySize];
            final Map<Integer, Set<Integer>> number2position = new HashMap<>(arraySize, 1);

            for (int i = 0; i < arraySize; i++) {
                final SumOfThreeValues.IntNumber value = sequence[i] = new SumOfThreeValues.IntNumber(reader.nextInt(), i);
                if (value.value < targetSum - 1) {
                    number2position.computeIfAbsent(value.value, k -> new HashSet<>()).add(i);
                }
            }

            return new SumOfThreeValues(targetSum, sequence, number2position);
        }
    }

    private static class SumOfThreeValues {

        private final IntNumber[] sequence;
        private final long targetSum;
        private final Map<Integer, Set<Integer>> number2positions;

        SumOfThreeValues(long targetSum, IntNumber[] sequence, Map<Integer, Set<Integer>> number2positions) {
            this.sequence = sequence;
            this.targetSum = targetSum;
            this.number2positions = number2positions;
        }

        int[] execute() {

            if (this.sequence.length < 3) {
                return new int[0];
            }

            Arrays.sort(this.sequence);

            final long maxSumOfTwo = this.targetSum - this.sequence[0].value;

            for (int i = 0; i < this.sequence.length && this.sequence[i].value < maxSumOfTwo; i++) {

                for (int j = i + 1; j < this.sequence.length && this.sequence[j].value < maxSumOfTwo; j++) {
                    final long sumOfTwo = this.sequence[i].value + this.sequence[j].value;
                    if (sumOfTwo > maxSumOfTwo) {
                        break;
                    }

                    final Integer leftValue = Math.toIntExact(this.targetSum - sumOfTwo);
                    final Set<Integer> allPositionsOfLeftValue = this.number2positions.get(leftValue);

                    final boolean isLeftPositionEqual = allPositionsOfLeftValue != null
                            && allPositionsOfLeftValue.size() == 1 && allPositionsOfLeftValue.contains(this.sequence[i].position);
                    final boolean isRightPositionEqual = allPositionsOfLeftValue != null
                            && allPositionsOfLeftValue.size() == 1 && allPositionsOfLeftValue.contains(this.sequence[j].position);

                    final boolean applicablePositionOfThirdValueNotExist =
                            allPositionsOfLeftValue == null
                                    || (isLeftPositionEqual || isRightPositionEqual)
                                    || allPositionsOfLeftValue.size() == 2 &&  (allPositionsOfLeftValue.contains(this.sequence[i].position) && allPositionsOfLeftValue.contains(this.sequence[j].position));

                    if (applicablePositionOfThirdValueNotExist) {
                        continue;
                    }

                    final int[] result = new int[3];
                    result[0] = this.sequence[i].position + 1;
                    result[1] = this.sequence[j].position + 1;

                    allPositionsOfLeftValue.remove(this.sequence[i].position);
                    allPositionsOfLeftValue.remove(this.sequence[j].position);

                    result[2] = allPositionsOfLeftValue.iterator().next() + 1;

                    Arrays.sort(result);

                    return result;
                }
            }

            return new int[0];
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