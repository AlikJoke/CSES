import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SplittableRandom;
import java.util.StringTokenizer;

/**
 * <h1>Ferris Wheel</h1><br/>
 * There are {@code n} children who want to go to a Ferris wheel, and task is to find a gondola for each child.<br/>
 * Each gondola may have one or two children in it, and in addition, the total weight in a gondola may not exceed {@code x}.
 * You know the weight of every child.<br/>
 * What is the minimum number of gondolas needed for the children?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains two integers {@code n} and {@code x}: the number of children and the maximum allowed weight.<br/>
 * The next line contains {@code n} integers {@code p_1},{@code p_2},...,{@code p_n}: the weight of each child.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum number of gondolas.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 0 <= x <= 10^9}<br/>
 * {@code 1 <= p_i <= x}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 10}<br/>
 * {@literal 7 2 3 9}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final FerrisWheel algorithm = readInputData();
        final int minCountOfGondolas = algorithm.computeMinCountOfGondolas();

        System.out.println(minCountOfGondolas);
    }

    private static FerrisWheel readInputData() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int childrenCount = reader.nextInt();
            final int maxAllowedWeight = reader.nextInt();

            final int[] childrenWeights = new int[childrenCount];
            for (int i = 0; i < childrenCount; i++) {
                childrenWeights[i] = reader.nextInt();
            }

            return new FerrisWheel(childrenWeights, maxAllowedWeight);
        }
    }

    private static class FerrisWheel {

        private final SplittableRandom random;

        private final int[] childrenWeights;
        private final int maxAllowedWeight;

        FerrisWheel(int[] childrenWeights, int maxAllowedWeight) {
            this.childrenWeights = childrenWeights;
            this.maxAllowedWeight = maxAllowedWeight;
            this.random = new SplittableRandom();
        }

        int computeMinCountOfGondolas() {
            quickSort(this.childrenWeights, 0, this.childrenWeights.length - 1);

            int countOfGondolas = 0;
            for (int i = this.childrenWeights.length - 1, j = 0; i >= j; i--) {
                final int childrenWeight = this.childrenWeights[i];
                if (childrenWeight == this.maxAllowedWeight) {
                    countOfGondolas++;
                    continue;
                }

                if (j < i && (childrenWeight + this.childrenWeights[j]) <= this.maxAllowedWeight) {
                    j++;
                }

                countOfGondolas++;
            }

            return countOfGondolas;
        }

        private void quickSort(final int[] a, final int leftBound, final int rightBound) {
            final auxiliaryItem median = a[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i] < auxiliaryItem) {
                    i++;
                }

                while (a[j] > auxiliaryItem) {
                    j--;
                }

                if (i <= j) {
                    final int t = a[i];
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
    }

    private static class ConsoleReader implements AutoCloseable {
        private final BufferedReader reader;
        private final String delimiter;
        private StringTokenizer tokenizer;

        ConsoleReader(final String delimiter) {
            this.delimiter = delimiter;
            this.reader = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (this.tokenizer == null || !tokenizer.hasMoreElements()) {
                this.tokenizer = new StringTokenizer(this.reader.readLine(), this.delimiter);
            }
            return this.tokenizer.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        @Override
        public void close() throws Exception {
            this.reader.close();
        }
    }
}
