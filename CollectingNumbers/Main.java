import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * <h1>Collecting Numbers</h1><br/>
 * Given an array that contains each number between {@code 1}...{@code n} exactly once. Task is to collect
 * the numbers from {@code 1} to {@code n} in increasing order.<br/>
 * On each round, go through the array from left to right and collect as many numbers as possible.
 * What will be the total number of rounds?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the array size.<br/>
 * Then there are {@code n} integers: {@code x_1},{@code x_2},...,{@code x_n}: the numbers in the array.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of rounds.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 4 2 1 5 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final CollectingNumbers algorithm = readInputData();
        final int countOfRounds = algorithm.computeCountOfRounds();

        System.out.println(countOfRounds);
    }

    private static CollectingNumbers readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();

            final CollectingNumbers.Number[] numbers = new CollectingNumbers.Number[arraySize];
            for (int i = 0; i < arraySize; i++) {
                numbers[i] = new CollectingNumbers.Number(reader.nextInt(), i);
            }

            return new CollectingNumbers(numbers);
        }
    }

    static class CollectingNumbers {

        private final SplittableRandom random;
        private final Number[] numbers;

        CollectingNumbers(Number[] numbers) {
            this.numbers = numbers;
            this.random = new SplittableRandom();
        }

        int computeCountOfRounds() {
            quickSort(this.numbers, 0, this.numbers.length - 1);

            int countOfRearrangementRounds = 0;
            for (int i = 0; i < this.numbers.length; i++) {
                final Number number = this.numbers[i];
                if (number.sourcePosition != i && i == 0
                        || i > 0 && this.numbers[i - 1].sourcePosition > number.sourcePosition) {
                    countOfRearrangementRounds++;
                }
            }

            /*
             * If computed count of rearrangement rounds == 0 then all numbers were in the correct order
             * and not a single rearrangement was required. Шn this case, the passage through
             * the elements will be the only one.
             */
            return Math.max(countOfRearrangementRounds, 1);
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

        static class Number {

            private final int value;
            private final int sourcePosition;

            Number(int value, int sourcePosition) {
                this.value = value;
                this.sourcePosition = sourcePosition;
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
