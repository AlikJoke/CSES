import java.io.DataInputStream;
import java.io.IOException;

/**
 * <a href="https://cses.fi/problemset/task/1158/"><h1>Book Shop</h1></a><br/>
 * You are in a book shop which sells {@code n} different books. You know the price and number of pages of each book.<br/>
 * You have decided that the total price of your purchases will be at most {@code x}.
 * What is the maximum number of pages you can buy? You can buy each book at most once.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains two integers {@code n} and {@code x}: the number of books and the maximum total price.<br/>
 * The next line contains {@code n} integers {@code h_1},{@code h_2},...,{@code h_n}: the price of each book.<br/>
 * The last line contains {@code n} integers {@code s_1},{@code s_2},...,{@code s_n}: the number of pages of each book.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the maximum number of pages.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 1000}<br/>
 * {@code 1 <= x <= 10^5}<br/>
 * {@code 1 <= h_i, s_i <= 1000}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 10}<br/>
 * {@literal 4 8 5 3}<br/>
 * {@literal 5 12 8 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 13}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final BookShop algorithm = createAlgorithm();
        final int maxPages = algorithm.execute();

        System.out.println(maxPages);
    }

    private static BookShop createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int booksCount = reader.nextInt();
            final int availableSum = reader.nextInt();

            final int[] prices = new int[booksCount];
            final int[] pages = new int[booksCount];

            for (int i = 0; i < booksCount; i++) {
                prices[i] = reader.nextInt();
            }

            for (int i = 0; i < booksCount; i++) {
                pages[i] = reader.nextInt();
            }

            return new BookShop(availableSum, prices, pages);
        }
    }

    private static class BookShop {

        private final int availableSum;
        private final int[] prices;
        private final int[] pages;

        BookShop(int availableSum, int[] prices, int[] pages) {
            this.availableSum = availableSum;
            this.prices = prices;
            this.pages = pages;
        }

        int execute() {
            final int[] pages = new int[this.availableSum + 1];
            int maxPages = 0;

            for (int i = 0; i < this.prices.length; i++) {
                final int currentBookPrice = this.prices[i];

                for (int j = this.availableSum; j >= currentBookPrice; j--) {

                    pages[j] = Math.max(pages[j], pages[j - currentBookPrice] + this.pages[i]);
                    maxPages = Math.max(maxPages, pages[j]);
                }
            }

            return maxPages;
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