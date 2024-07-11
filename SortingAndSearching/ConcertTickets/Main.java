import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <h1>Concert Tickets</h1><br/>
 * There are {@code n} concert tickets available, each with a certain price. Then, {@code m} customers arrive, one after another.<br/>
 * Each customer announces the maximum price they are willing to pay for a ticket, and after this, they will get a ticket with
 * the nearest possible price such that it does not exceed the maximum price.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains integers {@code n} and {@code m}: the number of tickets and the number of customers.<br/>
 * The next line contains {@code n} integers {@code h_1},{@code h_2},...,{@code h_n}: the price of each ticket.<br/>
 * The last line contains {@code m} integers {@code t_1},{@code t_2},...,{@code t_m}: the maximum price for each customer in the order they arrive.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print, for each customer, the price that they will pay for their ticket. After this, the ticket cannot be purchased again.<br/>
 * If a customer cannot get any ticket, print {@literal -1}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,m <= 2*10^5}<br/>
 * {@code 1 <= h_i,t_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 3}<br/>
 * {@literal 5 3 7 8 5}<br/>
 * {@literal 4 8 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 8}<br/>
 * {@literal -1}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ConcertTickets algorithm = readInputData();
        final int[] eachCustomerPaid = algorithm.computeEachCustomerPaid();

        final String outputResult = createOutputData(eachCustomerPaid);
        System.out.println(outputResult);
    }

    private static String createOutputData(final int[] eachCustomerPaid) {
        final StringBuilder sb = new StringBuilder(eachCustomerPaid.length * 5);
        for (int customerPaid : eachCustomerPaid) {
            sb.append(customerPaid).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static ConcertTickets readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int ticketsCount = reader.nextInt();
            final int customersCount = reader.nextInt();

            final NavigableMap<Integer, Integer> ticketPrices = new TreeMap<>();

            for (int i = 0; i < ticketsCount; i++) {
                final int ticketPrice = reader.nextInt();
                ticketPrices.compute(ticketPrice, (k, v) -> v == null ? 1 : v + 1);
            }

            final int[] customersMaxPrices = new int[customersCount];
            for (int i = 0; i < customersCount; i++) {
                customersMaxPrices[i] = reader.nextInt();
            }

            return new ConcertTickets(ticketPrices, customersMaxPrices);
        }
    }

    private static class ConcertTickets {

        private final NavigableMap<Integer, Integer> ticketPrices;
        private final int[] customersMaxPrices;

        ConcertTickets(NavigableMap<Integer, Integer> ticketPrices, int[] customersMaxPrices) {
            this.ticketPrices = ticketPrices;
            this.customersMaxPrices = customersMaxPrices;
        }

        int[] computeEachCustomerPaid() {

            final int[] result = new int[this.customersMaxPrices.length];

            for (int i = 0; i < this.customersMaxPrices.length; i++) {
                final int customerMaxPrice = this.customersMaxPrices[i];

                final Map.Entry<Integer, Integer> ticketsCountWithSuchPrice = this.ticketPrices.floorEntry(customerMaxPrice);
                if (ticketsCountWithSuchPrice == null) {
                    result[i] = -1;
                    continue;
                }

                result[i] = ticketsCountWithSuchPrice.getKey();
                if (ticketsCountWithSuchPrice.getValue() == 1) {
                    this.ticketPrices.remove(ticketsCountWithSuchPrice.getKey());
                } else {
                    ticketPrices.put(ticketsCountWithSuchPrice.getKey(), ticketsCountWithSuchPrice.getValue() - 1);
                }
            }

            return result;
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
