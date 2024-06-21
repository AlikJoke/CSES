import java.util.Scanner;

/**
 * <h1>Repetitions</h1><br/>
 * Given a DNA sequence: a string consisting of characters {@literal A}, {@literal C}, {@literal G}, and {@literal T}.
 * Task is to find the longest repetition in the sequence. This is a maximum-length substring containing only one type of character.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line contains a string of {@code n} characters.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the length of the longest repetition.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^6}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal ATTCGGGA}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final String dnaSequence = readDNASequence();

        final Repetitions algorithm = new Repetitions(dnaSequence);
        final int maxSequenceLengthOfAnyNucleotide = algorithm.computeMaxSequenceLengthOfAnyNucleotide();

        System.out.print(maxSequenceLengthOfAnyNucleotide);
    }

    private static String readDNASequence() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }

    private static class Repetitions {

        private final String dnaSequence;

        Repetitions(final String dnaSequence) {
            this.dnaSequence = dnaSequence;
        }

        int computeMaxSequenceLengthOfAnyNucleotide() {

            final int[] currentSeqLength = new int[4];
            final int[] maxSeqLength = new int[4];

            final char[] nucleotides = this.dnaSequence.toCharArray();
            for (char currentNucleotide : nucleotides) {
                final int currentNucleotideIdx = getNucleotideIdx(currentNucleotide);
                final int currentNucleotideSeqLength = currentSeqLength[currentNucleotideIdx] = ++currentSeqLength[currentNucleotideIdx];
                maxSeqLength[currentNucleotideIdx] = Math.max(maxSeqLength[currentNucleotideIdx], currentNucleotideSeqLength);

                for (int i = 0; i < currentSeqLength.length; i++) {
                    if (currentNucleotideIdx != i) {
                        currentSeqLength[i] = 0;
                    }
                }
            }

            return Math.max(maxSeqLength[0],
                    Math.max(maxSeqLength[1],
                            Math.max(maxSeqLength[2], maxSeqLength[3])
                    )
            );
        }

        private int getNucleotideIdx(final char nucleotide) {
            switch (nucleotide) {
                case 'A':
                    return 0;
                case 'T':
                    return 1;
                case 'C':
                    return 2;
                case 'G':
                    return 3;
                default:
                    throw new IllegalArgumentException("Unsupported nucleotide: " + nucleotide);
            }
        }
    }
}
