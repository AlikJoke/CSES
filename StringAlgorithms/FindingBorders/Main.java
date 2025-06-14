import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://cses.fi/problemset/task/1732/"><h1>Finding Borders</h1><br/></a>
 * A border of a string is a prefix that is also a suffix of the string but not the whole string. For example,
 * the borders of {@literal abcababcab} are {@literal ab} and {@literal abcab}.<br/>
 * Task is to find all border lengths of a given string.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has a string of length {@code n} consisting of characters {@literal a–z}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print all border lengths of the string in increasing order.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal abcababcab}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2 5}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final FindingBorders algorithm = readInputData();
        final List<Integer> borders = algorithm.execute();

        final String outputResult = createOutputResult(borders);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final List<Integer> borders) {
        final StringBuilder sb = new StringBuilder(borders.size() * 5);
        for (Integer border : borders) {
            sb.append(border).append(' ');
        }

        return sb.toString();
    }

    private static FindingBorders readInputData() throws Exception {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final String text = reader.readLine();
            return new FindingBorders(text);
        }
    }

    static class FindingBorders {

        private static final int Q = 1_000_000_007;
        private static final int X = 3;

        private final String text;
        private final long[] powers;
        private final int maxPrefixLength;

        FindingBorders(String text) {
            this.text = text;
            this.maxPrefixLength = text.length() - 1;
            this.powers = new long[this.maxPrefixLength];
        }

        List<Integer> execute() {
            computePowers(this.powers.length - 1);

            final List<Integer> result = new ArrayList<>();

            long hashPrefix = 0, hashSuffix = 0;
            for (int i = 0; i < this.maxPrefixLength; i++) {
                final char prefixChar = this.text.charAt(i);
                final char suffixChar = this.text.charAt(this.maxPrefixLength - i);

                hashPrefix = (hashPrefix + prefixChar * this.powers[i]) % Q;
                hashSuffix = (hashSuffix * X + suffixChar) % Q;

                if (hashPrefix == hashSuffix) {
                    result.add(i + 1);
                }
            }

            Collections.sort(result);

            return result;
        }

        private void computePowers(final int degree) {
            int currentDegree = 0;
            this.powers[currentDegree] = 1;
            while (currentDegree != degree) {
                this.powers[currentDegree + 1]  = (X * this.powers[++currentDegree - 1]) % Q;
            }
        }
    }
}