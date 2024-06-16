import java.util.Scanner;

/**
 * <h1>Creating Strings</h1><br/>
 * Given a string, task is to generate all different strings that can be created using its characters.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has a string of length {@literal n}. Each character is between {@literal a}–{@literal z}.<br/>
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@literal k}: the number of strings. Then print {@literal k} lines: the strings in alphabetical order.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 8}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal aba}<br/>
 * <i>Output</i>:<br/>
 * {@literal aab}<br/>
 * {@literal aba}<br/>
 * {@literal baa}<br/>
 *
 * @author Alik
 *
 * @implNote String generation is based on the recursive construction of a tree, at each node of which a particular character
 * is added. Initially, all unique characters are found, the number of their duplicates in the incoming string, after which
 * the characters are sorted in accordance with alphabetical order. Next, for each unique character, a recursive construction
 * of a string of a given length is performed. Each recursion step calculates the available characters to add to the base
 * string resulting from one of the unique characters, and adds one of the available characters to that string.
 * The addition occurs in a loop over the sorted array, so the generated lines will always be in alphabetical order.
 * After adding the next character, it is removed from the list of those available for adding to the line in this tree branch.
 */
public class Main {

    private static final char FIRST_ALPHABET_SYMBOL = 'a';
    private static final char LAST_ALPHABET_SYMBOL = 'z';

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            final String string = scanner.nextLine();

            final StringBuilder outputAcc = createOutputAccumulator(string.length());
            final int realCountOfStrings = generateAllStrings(string, outputAcc);

            System.out.println(realCountOfStrings);
            System.out.println(outputAcc);
        }
    }

    private static StringBuilder createOutputAccumulator(final int inputStringLength) {
        final int maxCountOfStringsIfAllSymbolsUnique = factorial(inputStringLength);
        return new StringBuilder((inputStringLength + 1) * maxCountOfStringsIfAllSymbolsUnique);
    }

    private static int generateAllStrings(final String inputString, final StringBuilder outputAccumulator) {

        final char[] chars = inputString.toCharArray();

        final int alphabetSize = LAST_ALPHABET_SYMBOL - FIRST_ALPHABET_SYMBOL + 1;
        final char[] uniqueSymbols = new char[alphabetSize];
        final int[] symbolsFrequencies = new int[alphabetSize];

        fillUniqueSymbolsFrequencies(chars, uniqueSymbols, symbolsFrequencies);

        final char[] sortedInputSymbols = createSortedInputSymbolsArray(chars.length, uniqueSymbols, symbolsFrequencies);

        return generateStringsFromUniqueSymbolsAsRoot(uniqueSymbols, sortedInputSymbols, outputAccumulator);
    }

    private static int generateStringsFromUniqueSymbolsAsRoot(
            final char[] uniqueSymbols,
            final char[] sortedInputStringSymbols,
            final StringBuilder outputAccumulator) {
        int countOfStrings = 0;
        for (final char initialChar : uniqueSymbols) {
            if (initialChar == 0) {
                continue;
            }

            final char[] sortedAvailableSymbols = copyExceptFirstOccurrenceOfSymbol(sortedInputStringSymbols, initialChar);
            countOfStrings += generateAllStringsInBranch(String.valueOf(initialChar), sortedAvailableSymbols, outputAccumulator);
        }

        return countOfStrings;
    }

    private static char[] copyExceptFirstOccurrenceOfSymbol(final char[] source, final char symbolToExclude) {
        final char[] result = new char[source.length - 1];

        boolean firstSymbolAlreadySkipped = false;
        int i = 0;
        for (char currentChar : source) {
            if (currentChar != symbolToExclude || firstSymbolAlreadySkipped) {
                result[i++] = currentChar;
            } else {
                firstSymbolAlreadySkipped = true;
            }
        }

        return result;
    }

    private static char[] createSortedInputSymbolsArray(
            final int inputStringLength,
            final char[] uniqueSymbols,
            final int[] symbolsFrequencies) {
        final char[] sortedInputSymbols = new char[inputStringLength];
        for (int i = 0, j = 0; i < symbolsFrequencies.length; i++) {
            int frequency = symbolsFrequencies[i];
            if (frequency == 0) {
                continue;
            }

            final char symbol = uniqueSymbols[i];
            while (frequency-- > 0) {
                sortedInputSymbols[j++] = symbol;
            }
        }

        return sortedInputSymbols;
    }

    private static void fillUniqueSymbolsFrequencies(
            final char[] inputStringChars,
            final char[] uniqueSymbols,
            final int[] symbolsFrequencies) {
        final int alphabetSize = uniqueSymbols.length;
        for (final char symbol : inputStringChars) {
            final int indexOfChar = alphabetSize - 1 - (LAST_ALPHABET_SYMBOL - symbol);

            uniqueSymbols[indexOfChar] = symbol;
            symbolsFrequencies[indexOfChar] = ++symbolsFrequencies[indexOfChar];
        }
    }

    private static int generateAllStringsInBranch(
            final String initialString,
            final char[] availableSymbols,
            final StringBuilder outputAccumulator) {
        if (availableSymbols.length == 0) {
            outputAccumulator.append(initialString).append(System.lineSeparator());

            return 1;
        }

        int countOfStringsInBranch = 0;
        for (int i = 0; i < availableSymbols.length; i++) {
            final char currentSymbol = availableSymbols[i];
            if (i > 0 && currentSymbol == availableSymbols[i - 1]) {
                continue;
            }

            final char[] nextAvailableSymbols = copyExceptElement(availableSymbols, i);
            countOfStringsInBranch += generateAllStringsInBranch(initialString + currentSymbol, nextAvailableSymbols, outputAccumulator);
        }

        return countOfStringsInBranch;
    }

    private static char[] copyExceptElement(final char[] source, final int excludedElementPosition) {

        final char[] result = new char[source.length - 1];

        System.arraycopy(
                source,
                0,
                result,
                0,
                excludedElementPosition
        );

        if (excludedElementPosition != source.length - 1) {
            System.arraycopy(
                    source,
                    excludedElementPosition + 1,
                    result,
                    excludedElementPosition,
                    result.length - excludedElementPosition
            );
        }

        return result;
    }

    private static int factorial(final int n) {
        return n == 0 ? 1 : n * factorial(n - 1);
    }
}
