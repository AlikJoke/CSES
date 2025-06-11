import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <a href="https://cses.fi/problemset/task/1753/"><h1>String Matching</h1><br/></a>
 * Given a string and a pattern, task is to count the number of positions where the pattern occurs in the string.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has a string of length {@code n}, and the second input line has a pattern of length {@code m}.
 * Both of them consist of characters {@literal a–z}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the number of occurrences.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,m <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal saippuakauppias}<br/>
 * {@literal pp}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final StringMatching algorithm = readInputData();
        final int count = algorithm.execute();

        System.out.println(count);
    }

    private static StringMatching readInputData() throws Exception {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            final String source = reader.readLine();
            final String template = reader.readLine();

            return new StringMatching(source, template);
        }
    }

    static class StringMatching {

        private final String source;
        private final String template;

        StringMatching(String source, String template) {
            this.source = source;
            this.template = template;
        }

        int execute() {
            final int sourceLength = this.source.length();
            final int templateLength = this.template.length();

            final int[] prefixes = createPrefixes();

            int count = 0;

            int sourceIndex = 0;
            int templateIndex = 0;

            while (sourceIndex < sourceLength) {
                if (this.template.charAt(templateIndex) == this.source.charAt(sourceIndex)) {
                    sourceIndex++;
                    templateIndex++;
                }

                if (templateIndex == templateLength) {
                    templateIndex = prefixes[templateIndex - 1];
                    count++;
                } else if (sourceIndex < sourceLength && this.template.charAt(templateIndex) != this.source.charAt(sourceIndex)) {
                    if (templateIndex != 0) {
                        templateIndex = prefixes[templateIndex - 1];
                    } else {
                        sourceIndex++;
                    }
                }
            }

            return count;
        }

        private int[] createPrefixes() {
            final int patternLength = this.template.length();
            final int[] prefixes = new int[patternLength];

            int length = 0;
            int i = 1;

            while (i < patternLength) {
                if (this.template.charAt(i) == this.template.charAt(length)) {
                    length++;
                    prefixes[i] = length;
                    i++;
                } else {
                    if (length != 0) {
                        length = prefixes[length - 1];
                    } else {
                        prefixes[i] = 0;
                        i++;
                    }
                }
            }
            return prefixes;
        }
    }
}