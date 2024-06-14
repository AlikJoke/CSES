import java.util.Scanner;

/**
 * <h1>Bit Strings</h1><br/>
 * task is to calculate the number of bit strings of length {@literal n}.
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

            final int countOfBitsInBinaryRepresentation = length + 1;

            final String moduloBinaryRepresentationString = Integer.toBinaryString(MODULO);
            final char[] moduloBinaryRepresentationChars = moduloBinaryRepresentationString.toCharArray();

            if (countOfBitsInBinaryRepresentation < moduloBinaryRepresentationChars.length) {
                final long resultCount = (long) Math.pow(2, countOfBitsInBinaryRepresentation - 1);
                System.out.print(resultCount % MODULO);

                return;
            }

            long acc = (long) Math.pow(2, moduloBinaryRepresentationChars.length - 1);
            int index = moduloBinaryRepresentationChars.length;
            while (index < countOfBitsInBinaryRepresentation) {

                if (acc < MODULO) {
                    acc <<= 1;
                    index++;
                }

                if (acc >= MODULO) {
                    acc -= MODULO;
                }
            }

            System.out.println(acc);
            /*
             * Implementation via BigInteger:
             * System.out.println(BigInteger.valueOf(2).pow(length).mod(BigInteger.valueOf(MODULO)).longValue());
             */
        }
    }
}
