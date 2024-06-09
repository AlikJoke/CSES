import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * <h1>Missing number</h1><br/>
 * Given all numbers between {@literal 1,2,...,n} except one. Task is to find the missing number.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}.<br/>
 * The second line contains {@literal n-1} numbers. Each number is distinct and between {@literal 1} and {@literal n} (inclusive).<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the missing number.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 2 <= n <= 2 * 10^5}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2 3 1 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 4}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final boolean[] sequenceTable = createSequenceTable();

        for (int i = 1; i < sequenceTable.length; i++) {
            if (!sequenceTable[i]) {
                System.out.print(i);
                break;
            }
        }
    }

    private static boolean[] createSequenceTable() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int sequenceLength = reader.nextInt();

            final boolean[] numbersTable = new boolean[sequenceLength + 1];
            for (int i = 0; i < sequenceLength - 1; i++) {
                final int numberFromSequence = reader.nextInt();
                numbersTable[numberFromSequence] = true;
            }

            return numbersTable;
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
