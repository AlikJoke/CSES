import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * <h1>Restaurant Customers</h1><br/>
 * Given the arrival and leaving times of {@code n} customers in a restaurant.<br/>
 * What was the maximum number of customers in the restaurant at any time?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of customers.<br/>
 * After this, there are {@code n} lines that describe the customers. Each line has two integers {@code a} and {@code b}:
 * the arrival and leaving times of a customer.<br/>
 * May assume that all arrival and leaving times are distinct.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the maximum number of customers.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= a < b <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 5 8}<br/>
 * {@literal 2 4}<br/>
 * {@literal 3 9}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final RestaurantCustomers algorithm = readInputData();
        final int maxNumberOfCustomersInAnyTime = algorithm.computeMaxNumberOfCustomersInAnyTime();

        System.out.println(maxNumberOfCustomersInAnyTime);
    }

    private static RestaurantCustomers readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int customersCount = reader.nextInt();

            final RestaurantCustomers.VisitTime[] customersVisitTimes = new RestaurantCustomers.VisitTime[customersCount * 2];
            for (int i = 0, j = 0; i < customersCount; i++) {
                customersVisitTimes[j++] = new RestaurantCustomers.VisitTime(reader.nextInt(), i);
                customersVisitTimes[j++] = new RestaurantCustomers.VisitTime(reader.nextInt(), i);
            }

            return new RestaurantCustomers(customersVisitTimes);
        }
    }

    private static class RestaurantCustomers {

        private final SplittableRandom random;
        private final RestaurantCustomers.VisitTime[] customersVisitTimes;

        RestaurantCustomers(RestaurantCustomers.VisitTime[] customersVisitTimes) {
            this.random = new SplittableRandom();
            this.customersVisitTimes = customersVisitTimes;
        }

        int computeMaxNumberOfCustomersInAnyTime() {
            quickSort(this.customersVisitTimes, 0, this.customersVisitTimes.length - 1);

            final boolean[] openedVisits = new boolean[this.customersVisitTimes.length >> 1];

            int maxCountOfOpenedVisits = 0;
            int currentCountOfOpenedVisits = 0;
            for (final VisitTime visitTime : this.customersVisitTimes) {
                if (openedVisits[visitTime.label]) {
                    openedVisits[visitTime.label] = false;
                    currentCountOfOpenedVisits--;
                } else {
                    openedVisits[visitTime.label] = true;
                    maxCountOfOpenedVisits = Math.max(++currentCountOfOpenedVisits, maxCountOfOpenedVisits);
                }
            }

            return maxCountOfOpenedVisits;
        }

        private void quickSort(final RestaurantCustomers.VisitTime[] visitTimes, final int leftBound, final int rightBound) {
            final RestaurantCustomers.VisitTime median = visitTimes[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (visitTimes[i].time < median.time) {
                    i++;
                }

                while (visitTimes[j].time > median.time) {
                    j--;
                }

                if (i <= j) {
                    final RestaurantCustomers.VisitTime t = visitTimes[i];
                    visitTimes[i] = visitTimes[j];
                    visitTimes[j] = t;

                    i++;
                    j--;
                }
            }

            if (j > leftBound) {
                quickSort(visitTimes, leftBound, j);
            }

            if (i < rightBound) {
                quickSort(visitTimes, i, rightBound);
            }
        }

        private static class VisitTime {

            private final int time;
            private final int label;

            private VisitTime(int time, int label) {
                this.time = time;
                this.label = label;
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
