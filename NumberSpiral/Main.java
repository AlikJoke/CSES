import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * <h1>Number Spiral</h1><br/>
 * A number spiral is an infinite grid whose upper-left square has number {@literal 1}.
 * Here are the first five layers of the spiral:<br/>
 * <img src="https://cses.fi/file/bba36f2601b99c7edc15865aa2a49e680a271075f30e86aa0e4e18d00a779c21"/><br/>
 * Task is to find out the number in row {@code y} and column {@code x}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code t}: the number of tests.<br/>
 * After this, there are {@code t} lines, each containing integers {@code y} and {@code x}.<br/>
 * <i><b>Output</b></i>:<br/>
 * For each test, print the number in row {@code y} and column {@code x}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= t <= 10^5}<br/>
 * {@code 1 <= y, x <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 2 3}<br/>
 * {@literal 1 1}<br/>
 * {@literal 4 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 8}<br/>
 * {@literal 1}<br/>
 * {@literal 15}<br/>
 *
 * @author Alik
 *
 * @implNote First, we determine the maximum from the coordinates and take the square from the previous coordinate value.
 * This will be a square in which the desired number certainly cannot be located.
 * From this square we take the maximum number - this will be the number from which we begin to look for what we are looking for.
 * In addition, we assign the current coordinate along {@code x = 1} if the resulting number is even (even squares are along {@code x = 1}),
 * and the current coordinate {@code y = 1} if the number is odd.
 * Next in the cycle we look for the required value in accordance with the laws of constructing a spiral on a two-dimensional plane,
 * until the current coordinates coincide with the desired ones.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final int[][] requestedCoordinates = readRequestedCoordinates();

        for (int[] coordinates : requestedCoordinates) {
            final long computedNumber = computeNumberByCoordinates(coordinates);
            System.out.println(computedNumber);
        }
    }

    private static long computeNumberByCoordinates(final int[] coordinates) {

        final int y = coordinates[0];
        final int x = coordinates[1];

        final int maxCoordinate = Math.max(x, y);
        long currentNumber = Math.max((long) (maxCoordinate - 1) * (maxCoordinate - 1), 1);

        int currentY;
        int currentX;
        if (currentNumber % 2 == 0) {
            currentX = 1;
            currentY = Math.max(maxCoordinate - 1, 1);
        } else {
            currentX = Math.max(maxCoordinate - 1, 1);
            currentY = 1;
        }

        boolean oneOfCoordinateDecrementedToStartPosition = currentNumber > 1;

        while (currentX != x || currentY != y) {

            if (currentX == currentY) {

                if (currentX == 1) {
                    currentX++;
                    currentNumber++;
                } else if (currentX % 2 == 0) {

                    if (currentY != y || currentX < x) {
                        currentNumber += currentX - 1;
                        currentX = 1;
                    } else {
                        currentNumber += currentX - x;
                        currentX = x;
                    }

                    oneOfCoordinateDecrementedToStartPosition = currentX == 1;
                } else {

                    if (currentX != x || currentY < y) {
                        currentNumber += currentY - 1;
                        currentY = 1;
                    } else {
                        currentNumber += currentY - y;
                        currentY = y;
                    }

                    oneOfCoordinateDecrementedToStartPosition = currentY == 1;
                }
            } else if (currentX > currentY) {
                if (oneOfCoordinateDecrementedToStartPosition) {
                    currentX++;
                    currentNumber++;
                    oneOfCoordinateDecrementedToStartPosition = false;
                } else if (currentX % 2 == 0) {

                    if (currentX != x || currentY > y) {
                        currentNumber += currentX - 1;
                        currentY = currentX;
                    } else {
                        currentNumber += Math.abs(currentY - y);
                        currentY = y;
                    }
                } else {

                    if (currentX != x || currentY < y) {
                        currentNumber += currentY - 1;
                        currentY = 1;
                    } else {
                        currentNumber += Math.abs(currentY - y);
                        currentY = y;
                    }

                    oneOfCoordinateDecrementedToStartPosition = currentY == 1;
                }
            } else if (oneOfCoordinateDecrementedToStartPosition) {
                currentY++;
                currentNumber++;
                oneOfCoordinateDecrementedToStartPosition = false;
            } else if (currentY % 2 == 0) {

                if (currentY != y || currentX > x) {
                    currentNumber += currentY - 1;
                    currentX = currentY;
                } else {
                    currentNumber += Math.abs(currentX - x);
                    currentX = x;
                }
            } else {
                if (currentY != y || currentX > x) {
                    currentNumber += currentY - 1;
                    currentX = currentY;
                } else {
                    currentNumber += Math.abs(currentX - x);
                    currentX = x;
                }
            }
        }

        return currentNumber;
    }

    private static int[][] readRequestedCoordinates() throws Exception {
        try (ConsoleReader reader = new ConsoleReader(" ")) {
            final int tests = reader.nextInt();

            final int[][] result = new int[tests][2];
            for (int i = 0; i < tests; i++) {
                result[i][0] = reader.nextInt();
                result[i][1] = reader.nextInt();
            }

            return result;
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
