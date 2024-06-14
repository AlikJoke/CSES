import java.util.Scanner;

/**
 * <h1>Bit Strings</h1><br/>
 * Task is to calculate the number of bit strings of length {@literal n}.
 * For example, if {@code n=3}, the correct answer is {@literal 8}, because the possible bit strings are {@literal 000},
 * {@literal 001}, {@literal 010}, {@literal 011}, {@literal 100}, {@literal 101}, {@literal 110}, and {@literal 111}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@literal n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the result modulo {@code 10^9+7}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * <i>Output</i>:<br/>
 * {@literal 8}<br/>
 *
 * @author Alik
 *
 * @implNote The number of bit strings of a given length is always equal to {@code 2^length}. Since the length
 * can be greater than {@literal 64} (the number of bits in the {@link Long} type), it is necessary to use division
 * of numbers in binary representation. Knowing the number of bits in the number containing the number of bit
 * strings, and the number of bits in the representation of the number whose remainder is to be obtained,
 * we can perform sequential division of these two numbers using bitwise shift and subtraction
 * (thus simulating binary division).
 */
public class Main {

    private static final int MODULO = 1_000_000_000 + 7;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            final int length = scanner.nextInt();
            final long result = computeResult(length);

            System.out.println(result);
            /*
             * Implementation via BigInteger:
             * System.out.println(BigInteger.valueOf(2).pow(length).mod(BigInteger.valueOf(MODULO)).longValue());
             */
        }
    }

    private static long computeResult(final int bitStringsLength) {

        final int countOfBitsInBinaryRepresentation = bitStringsLength + 1;

        final String moduloBinaryRepresentationString = Integer.toBinaryString(MODULO);
        final char[] moduloBinaryRepresentationChars = moduloBinaryRepresentationString.toCharArray();

        if (countOfBitsInBinaryRepresentation < moduloBinaryRepresentationChars.length) {
            final long resultCount = (long) Math.pow(2, countOfBitsInBinaryRepresentation - 1);
            return resultCount % MODULO;
        }

        long acc = (long) Math.pow(2, moduloBinaryRepresentationChars.length - 1);
        int currentBitIndex = moduloBinaryRepresentationChars.length;
        while (currentBitIndex < countOfBitsInBinaryRepresentation) {

            if (acc < MODULO) {
                acc <<= 1;
                currentBitIndex++;
            }

            if (acc >= MODULO) {
                acc -= MODULO;
            }
        }

        return acc;
    }
}
