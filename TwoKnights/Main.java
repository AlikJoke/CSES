import java.util.Scanner;

/**
 * <h1>Two Knights</h1><br/>
 * Task is to count for {@code k=1,2,...,n} the number of ways two knights can be placed on a {@literal k x k} chessboard
 * so that they do not attack each other.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * TThe only input line contains an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print {@code n} integers: the results.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10_000}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8}<br/>
 * <i>Output</i>:<br/>
 * {@literal 0}<br/>
 * {@literal 6}<br/>
 * {@literal 28}<br/>
 * {@literal 96}<br/>
 * {@literal 252}<br/>
 * {@literal 550}<br/>
 * {@literal 1056}<br/>
 * {@literal 1848}<br/>
 *
 * @author Alik
 *
 * @implNote First, we calculate the number of all possible placements of knights on the board (the combinatorial number
 * of placements on a symmetrical board). From the number of possible placements we subtract the number of positions from
 * which knights can attack each other. The number of positions from which knights can attack each other is calculated
 * by the number of rectangles of size {@code 2 * 3} in a square of the board (on one side the shift is by one cell, on the other by two).
 * Since there are two knights, we multiply the number of rectangles by {@literal 2}.
 */
public class Main {

    public static void main(String[] args) {
        final short chessboardSize = readChessboardSize();

        final TwoKnights algorithm = new TwoKnights(chessboardSize);
        final long[] resultPositions = algorithm.computeNonThreateningPositions();

        for (long positionsCount : resultPositions) {
            System.out.println(positionsCount);
        }
    }

    private static short readChessboardSize() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextShort();
        }
    }

    private static class TwoKnights {

        private final short chessboardSize;

        TwoKnights(final short chessboardSize) {
            this.chessboardSize = chessboardSize;
        }

        long[] computeNonThreateningPositions() {

            final long[] positions = new long[this.chessboardSize];

            for (short i = 1; i <= this.chessboardSize; i++) {
                final int firstKnightAllPositions = i * i;
                final int secondKnightAllPositions = firstKnightAllPositions - 1;

                final long allKnightsPositions = (long) firstKnightAllPositions * secondKnightAllPositions / 2;
                final int attackPositionsByOneKnight = 2 * (i - 1) * (i - 2);
                final int attackPositionsByKnights = attackPositionsByOneKnight * 2;

                final long resultPositions = allKnightsPositions - attackPositionsByKnights;

                positions[i - 1] = resultPositions;
            }

            return positions;
        }
    }
}
