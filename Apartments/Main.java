import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SplittableRandom;
import java.util.StringTokenizer;

/**
 * <h1>Apartments</h1><br/>
 * There are {@code n} applicants and m free apartments. Task is to distribute the apartments so that as many applicants
 * as possible will get an apartment.<br/>
 * Each applicant has a desired apartment size, and they will accept any apartment whose size is close enough to the
 * desired size.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has three integers {@code n}, {@code m}, and {@code k}: the number of applicants,
 * the number of apartments, and the maximum allowed difference.<br/>
 * The next line contains {@code n} integers {@code a_1}, {@code a_2}, ..., {@code a_n}: the desired apartment
 * size of each applicant. If the desired size of an applicant is {@code x}, he or she will accept any apartment whose
 * size is between {@code x-k} and {@code x+k}.<br/>
 * The last line contains {@code m} integers {@code b_1}, {@code b_2}, ..., {@code b_m}: the size of each apartment.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of applicants who will get an apartment.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,m <= 2*10^5}<br/>
 * {@code 0 <= k <= 10^9}<br/>
 * {@code 1 <= a_i,b_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 3 5}<br/>
 * {@literal 60 45 80 60}<br/>
 * {@literal 30 60 75}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Apartments algorithm = readInputData();
        final int numberOfServedApplicants = algorithm.computeNumberOfServedApplicants();

        System.out.println(numberOfServedApplicants);
    }

    private static Apartments readInputData() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int applicantsCount = reader.nextInt();
            final int apartmentsCount = reader.nextInt();
            final int maxAllowedDifference = reader.nextInt();

            final int[] applicantsDesiredSizes = new int[applicantsCount];
            for (int i = 0; i < applicantsCount; i++) {
                applicantsDesiredSizes[i] = reader.nextInt();
            }

            final int[] apartmentsSizes = new int[apartmentsCount];
            for (int i = 0; i < apartmentsCount; i++) {
                apartmentsSizes[i] = reader.nextInt();
            }

            return new Apartments(applicantsDesiredSizes, apartmentsSizes, maxAllowedDifference);
        }
    }

    private static class Apartments {

        private final SplittableRandom random;

        private final int[] applicantsDesiredSizes;
        private final int[] apartmentsSizes;
        private final int maxAllowedDifference;

        Apartments(
                int[] applicantsDesiredSizes,
                int[] apartmentsSizes,
                int maxAllowedDifference) {
            this.applicantsDesiredSizes = applicantsDesiredSizes;
            this.apartmentsSizes = apartmentsSizes;
            this.maxAllowedDifference = maxAllowedDifference;
            this.random = new SplittableRandom();
        }

        int computeNumberOfServedApplicants() {
            quickSort(this.applicantsDesiredSizes, 0, this.applicantsDesiredSizes.length - 1);
            quickSort(this.apartmentsSizes, 0, this.apartmentsSizes.length - 1);

            int servedApplicants = 0;
            for (int i = 0, j = 0; i < this.apartmentsSizes.length && j < this.applicantsDesiredSizes.length; i++) {

                while (this.apartmentsSizes[i] > this.applicantsDesiredSizes[j] + this.maxAllowedDifference) {
                    if (++j >= this.applicantsDesiredSizes.length) {
                        return servedApplicants;
                    }
                }

                if (this.apartmentsSizes[i] >= this.applicantsDesiredSizes[j] - this.maxAllowedDifference
                        && this.apartmentsSizes[i] <= this.applicantsDesiredSizes[j++] + this.maxAllowedDifference) {
                    servedApplicants++;
                }
            }

            return servedApplicants;
        }

        private void quickSort(final int[] a, final int leftBound, final int rightBound) {
            final int median = a[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i] < median) {
                    i++;
                }

                while (a[j] > median) {
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
