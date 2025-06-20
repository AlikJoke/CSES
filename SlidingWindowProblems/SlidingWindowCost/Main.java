import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;
import java.util.TreeSet;

/**
 * <a href="https://cses.fi/problemset/task/1077/"><h1>Sliding Window Cost</h1><br/></a>
 * You are given an array of {@code n} integers. Your task is to calculate for each window of {@code k} elements, from left to right, the minimum total cost of making all elements equal.<br/>
 * You can increase or decrease each element with cost {@code x} where {@code x} is the difference between the new and the original value. The total cost is the sum of such costs.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of elements and the size of the window.<br/>
 * Then there are {@code n} integers {@literal x_1,x_2,...,x_n}: the contents of the array.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@code n-k+1} values: the costs.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 2 4 3 5 8 1 2 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2 2 5 7 7 1}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowMedian algorithm = readInputData();
        final long[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final long[] values) {
        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (long value : values) {
            sb.append(value).append(' ');
        }

        return sb.toString();
    }

    private static SlidingWindowMedian readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final SlidingWindowMedian.Number[] sequence = new SlidingWindowMedian.Number[n];
            for (int i = 0; i < n; i++) {
                sequence[i] = new SlidingWindowMedian.Number(reader.nextInt(), i);
            }

            return new SlidingWindowMedian(n, k, sequence);
        }
    }

    static class SlidingWindowMedian {

        private final int n;
        private final int k;
        private final Number[] sequence;
        private final SplittableRandom random;

        SlidingWindowMedian(int n, int k, Number[] sequence) {
            this.n = n;
            this.k = k;
            this.sequence = sequence;
            this.random = new SplittableRandom();
        }

        long[] execute() {

            final long[] result = new long[this.n - this.k + 1];

            if (this.k == 1) {
                for (int i = 0; i < this.n; i++) {
                    result[i] = 0;
                }

                return result;
            }

            final TreeSet<Number> part1 = new TreeSet<>();
            final TreeSet<Number> part2 = new TreeSet<>();

            final Number[] initialWindow = new Number[this.k];
            System.arraycopy(this.sequence, 0, initialWindow, 0, this.k);
            quickSort(initialWindow, 0, initialWindow.length - 1);

            final int medianPosition = this.k % 2 == 0 ? this.k / 2 - 1 : this.k / 2;
            Integer median = initialWindow[medianPosition].value;

            long sumOfPart1 = 0;
            long sumOfPart2 = 0;
            for (int i = 0; i < this.k; i++) {
                if (i <= medianPosition) {
                    sumOfPart1 += initialWindow[i].value;
                } else {
                    sumOfPart2 += initialWindow[i].value;
                }

                (i <= medianPosition ? part1 : part2).add(initialWindow[i]);
            }

            result[0] = ((long) median * part1.size() - sumOfPart1) + (sumOfPart2 - (long) median * part2.size());

            for (int i = this.k; i < this.n; i++) {

                final Number numberToRemove = this.sequence[i - this.k];
                final Number numberToAdd = this.sequence[i];

                if (part1.remove(numberToRemove)) {
                    sumOfPart1 -= numberToRemove.value;
                    if (!part1.isEmpty() && part1.last().compareTo(numberToAdd) >= 0) {
                        part1.add(numberToAdd);
                        sumOfPart1 += numberToAdd.value;
                    } else {
                        final Number firstInPart2 = part2.first();
                        if (firstInPart2.compareTo(numberToAdd) >= 0) {
                            part1.add(numberToAdd);
                            sumOfPart1 += numberToAdd.value;
                        } else {
                            final Number numberToMove = part2.pollFirst();
                            part1.add(numberToMove);
                            sumOfPart1 += numberToMove.value;
                            part2.add(numberToAdd);
                            sumOfPart2 = sumOfPart2 - numberToMove.value + numberToAdd.value;
                        }
                    }
                } else {
                    part2.remove(numberToRemove);
                    sumOfPart2 -= numberToRemove.value;
                    final Number lastInPart1 = part1.last();
                    if (!part2.isEmpty() && part2.first().compareTo(numberToAdd) >= 0) {
                        if (lastInPart1.compareTo(numberToAdd) > 0) {
                            part1.add(numberToAdd);
                            final Number numberToMove = part1.pollLast();
                            sumOfPart1 = sumOfPart1 + numberToAdd.value - numberToMove.value;
                            part2.add(numberToMove);
                            sumOfPart2 += numberToMove.value;
                        } else {
                            part2.add(numberToAdd);
                            sumOfPart2 += numberToAdd.value;
                        }
                    } else {
                        if (lastInPart1.compareTo(numberToAdd) <= 0) {
                            part2.add(numberToAdd);
                            sumOfPart2 += numberToAdd.value;
                        } else {
                            final Number numberToMove = part1.pollLast();
                            part2.add(numberToMove);
                            part1.add(numberToAdd);

                            sumOfPart2 += numberToMove.value;
                            sumOfPart1 = sumOfPart1 + numberToAdd.value - numberToMove.value;
                        }
                    }
                }

                result[i - this.k + 1] = ((long) part1.last().value * part1.size() - sumOfPart1) + (sumOfPart2 - (long) part1.last().value * part2.size());
            }

            return result;
        }

        private void quickSort(final Number[] a, final int leftBound, final int rightBound) {
            final Number auxiliaryItem = a[leftBound == rightBound ? leftBound : this.random.nextInt(leftBound, rightBound)];
            final int auxiliaryNumber = auxiliaryItem.value;

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i].value < auxiliaryNumber) {
                    i++;
                }

                while (a[j].value > auxiliaryNumber) {
                    j--;
                }

                if (i <= j) {
                    final Number t = a[i];
                    a[i] = a[j];
                    a[j] = t;

                    i++;
                    j--;
                }
            }

            if (j > leftBound) {
                quickSort(a, leftBound, j);
            }

            if (i < rightBound) {
                quickSort(a, i, rightBound);
            }
        }

        static class Number implements Comparable<Number> {

            private final Integer value;
            private final int sourcePosition;

            Number(Integer value, int sourcePosition) {
                this.value = value;
                this.sourcePosition = sourcePosition;
            }

            @Override
            public int compareTo(Number o) {
                final int valueComparisonResult = Integer.compare(this.value, o.value);
                return valueComparisonResult == 0 ? Integer.compare(this.sourcePosition, o.sourcePosition) : valueComparisonResult;
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
