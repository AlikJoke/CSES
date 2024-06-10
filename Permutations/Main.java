import java.util.Scanner;

/**
 * <h1>Permutations</h1><br/>
 * A permutation of integers {@literal 1},{@literal 2},...,{@literal n} is called <i>beautiful</i> if there are no
 * adjacent elements whose difference is {@literal 1}.<br/>
 * Given {@literal n}, construct a beautiful permutation if such a permutation exists.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line contains an integer {@literal n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print a beautiful permutation of {@literal 1},{@literal 2},...,{@literal n}. If there are several solutions,
 * print any of them. If there are no solutions, print {@literal NO SOLUTION}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 4 2 5 3 1}
 *
 * @author Alik
 *
 * @implNote The implementation calculates the median of the sequence (the middle element). For elements to the
 * left from the beginning of the sequence to the median, a permutation is performed in steps of 1 (i.e., every other element)
 * with the elements of the sequence after the median (symmetrically). The median itself, if the length of the
 * sequence was odd, is rearranged to the first position of the sequence, and the part of the sequence from
 * position 1 is moved forward (array shift by one character). If the length was even, then the two numbers in
 * the middle of the sequence are moved to the ends of the sequence (what was closer to the beginning of the
 * array is moved to the beginning, and what was closer to the end is moved to the end),
 * and the subsequences are shifted by one position.
 */
public class Main {

    private static final String NO_SOLUTION = "NO SOLUTION";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            final int sequenceLength = scanner.nextInt();
            final int[] sequence = new int[sequenceLength];

            fillSequence(sequence);

            final String outputSequence = composeOutputSequenceString(sequence);
            System.out.print(outputSequence);
        }
    }

    private static String composeOutputSequenceString(final int[] sequence) {

        final StringBuilder sb = new StringBuilder(sequence.length * 10);
        sb.append(sequence[0]).append(' ');

        for (int i = 1; i < sequence.length; i++) {
            if (Math.abs(sequence[i - 1] - sequence[i]) == 1) {
                return NO_SOLUTION;
            }

            sb.append(sequence[i]).append(' ');
        }

        return sb.toString();
    }

    private static void replaceMidOfSequence(final int[] sequence, final int median) {

        final boolean isSequenceLengthEven = sequence.length % 2 == 0;
        if (isSequenceLengthEven) {
            final int medianPrev = sequence[median - 1];
            final int medianNext = sequence[median];
            System.arraycopy(sequence, 0, sequence, 1, median - 1);
            System.arraycopy(sequence, median + 1, sequence, median, sequence.length - median - 1);

            sequence[0] = medianPrev;
            sequence[sequence.length - 1] = medianNext;
        } else {
            System.arraycopy(sequence, 0, sequence, 1, median - 1);
            sequence[0] = median;
        }
    }

    private static void fillSequence(final int[] sequence) {

        final boolean isSequenceLengthEven = sequence.length % 2 == 0;
        final int median = (isSequenceLengthEven ? sequence.length : sequence.length + 1) >> 1;

        for (int i = 0; i < median; i++) {
            final int symmetricNumberPositionFromEndOfSeq = sequence.length - i;

            if (i % 2 == 1) {
                sequence[i] = i + 1;
                sequence[symmetricNumberPositionFromEndOfSeq - 1] = symmetricNumberPositionFromEndOfSeq;
            } else {
                sequence[i] = symmetricNumberPositionFromEndOfSeq;
                sequence[symmetricNumberPositionFromEndOfSeq - 1] = i + 1;
            }
        }

        replaceMidOfSequence(sequence, median);
    }
}