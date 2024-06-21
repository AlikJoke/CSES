import java.util.Scanner;

/**
 * <h1>Gray Code</h1><br/>
 * A Gray code is a list of all {@code 2^n} bit strings of length {@code n}, where any two successive strings differ in
 * exactly one bit (i.e., their Hamming distance is one).<br/>
 * Task is to create a Gray code for a given length {@code n}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print {@code 2^n} lines that describe the Gray code. Print any valid solution.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 16}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 00}<br/>
 * {@literal 01}<br/>
 * {@literal 11}<br/>
 * {@literal 10}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final int bitStringsLength = readBitStringsLength();

        final GrayCode algorithm = new GrayCode(bitStringsLength);
        final int[] numbersSequence = algorithm.computeGrayNumbersSequence();

        final String result = createOutputResultString(numbersSequence, bitStringsLength);
        System.out.println(result);
    }

    private static String createOutputResultString(final int[] sequence, final int bitStringsLength) {

        final StringBuilder sb = new StringBuilder((bitStringsLength + 1) * sequence.length);

        for (int grayNumber : sequence) {
            final String formattedBinaryRepresentation =formatAsBinaryNumberString(grayNumber, bitStringsLength);
            sb.append(formattedBinaryRepresentation).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static int readBitStringsLength() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static String formatAsBinaryNumberString(final int number, final int expectedLength) {
        final String binaryRepresentation = Integer.toBinaryString(number);
        return String.format("%" + expectedLength + "s", binaryRepresentation).replace(' ', '0');
    }

    private static int computeGrayCode(final int n) {
        return n ^ (n >> 1);
    }

    private static class GrayCode {

        private final int bitStringsLength;

        GrayCode(final int bitStringsLength) {
            this.bitStringsLength = bitStringsLength;
        }

        int[] computeGrayNumbersSequence() {

            final int countOfStrings = (int) Math.pow(2, bitStringsLength);

            final int[] numbers = new int[countOfStrings];
            for (int i = 0; i < countOfStrings; i++) {
                final int grayNumber = computeGrayCode(i);

                numbers[i] = grayNumber;
            }

            return numbers;
        }
    }
}
