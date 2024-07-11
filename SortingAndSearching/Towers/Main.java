import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Towers</h1><br/>
 * Given {@code n} cubes in a certain order, and task is to build towers using them. Whenever two cubes are one
 * on top of the other, the upper cube must be smaller than the lower cube.<br/>
 * Process the cubes in the given order. Always either place the cube on top of an existing tower, or begin a new
 * tower. What is the minimum possible number of towers?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of cubes.<br/>
 * The next line contains {@code n} integers {@code k_1},{@code k_2},...,{@code k_n}: the sizes of the cubes.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the minimum number of towers.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= k_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 3 8 2 1 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Towers algorithm = readInputData();
        final int minTowersCount = algorithm.computeMinTowersCount();

        System.out.println(minTowersCount);
    }

    private static Towers readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int cubesCount = reader.nextInt();

            final int[] cubes = new int[cubesCount];
            for (int i = 0; i < cubesCount; i++) {
                cubes[i] = reader.nextInt();
            }

            return new Towers(cubes);
        }
    }

    static class Towers {

        private final int[] cubes;
        private final int[] towers;

        Towers(int[] cubes) {
            this.cubes = cubes;
            this.towers = new int[cubes.length];
        }

        int computeMinTowersCount() {
            int towersCount = 0;

            for (final int cube : this.cubes) {
                final int maxUsedCube = this.towers[Math.max(towersCount - 1, 0)];
                if (cube >= maxUsedCube) {
                    this.towers[towersCount++] = cube;
                    continue;
                }

                int nearestCubePosition = searchProbableNearest(this.towers, cube, 0, towersCount, -1);
                while (nearestCubePosition > 0 && (this.towers[nearestCubePosition - 1] > cube)) {
                    nearestCubePosition--;
                }

                this.towers[nearestCubePosition] = cube;
            }

            return towersCount;
        }

        private int searchProbableNearest(
                final int[] a,
                final int value,
                final int leftBound,
                final int rightBound,
                final int nearestPosition) {
            if (leftBound > rightBound) {
                return nearestPosition;
            }

            final int medianPosition = leftBound + ((rightBound - leftBound) >> 1);
            final int median = a[medianPosition];

            if (value >= median) {
                return leftBound == medianPosition ? nearestPosition : searchProbableNearest(a, value, medianPosition + 1, rightBound, nearestPosition);
            } else {
                return medianPosition == 0 ? medianPosition : searchProbableNearest(a, value, leftBound, medianPosition - 1, medianPosition);
            }
        }
    }

    static class Reader implements AutoCloseable {

        private static final int BUFFER_SIZE = 1 << 16;

        private final DataInputStream din;
        private final byte[] buffer;

        private int bufferPointer;
        private int bytesRead;

        Reader() {
            this.din = new DataInputStream(System.in);
            this.buffer = new byte[BUFFER_SIZE];
        }

        int nextInt() throws IOException {

            byte c = read();
            while (c <= ' ') {
                c = read();
            }

            final boolean negate = (c == '-');
            if (negate) {
                c = read();
            }

            int result = 0;
            do {
                result = result * 10 + c - '0';
            } while ((c = read()) >= '0' && c <= '9');

            return negate ? -result : result;
        }

        private void fillBuffer() throws IOException {
            this.bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
            if (this.bytesRead == -1) {
                this.buffer[0] = -1;
            }
        }

        private byte read() throws IOException {
            if (this.bufferPointer == this.bytesRead) {
                fillBuffer();
            }

            return this.buffer[this.bufferPointer++];
        }

        @Override
        public void close() throws IOException {
            this.din.close();
        }
    }
}
