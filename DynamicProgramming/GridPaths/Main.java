import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Grid Paths</h1><br/>
 * Consider an {@code n * n} grid whose squares may have traps. It is not allowed to move to a square with a trap.<br/>
 * Task is to calculate the number of paths from the upper-left square to the lower-right square. Move right or down only.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the size of the grid.<br/>
 * After this, there are {@code n} lines that describe the grid. Each line has {@code n} characters: {@literal .} denotes
 * an empty cell, and {@literal *} denotes a trap.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the number of paths modulo 10^9+7.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 1000}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4}<br/>
 * {@literal . . . .}<br/>
 * {@literal . * . .}<br/>
 * {@literal . . . *}<br/>
 * {@literal * . . .}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws IOException {
        final GridPaths algorithm = createAlgorithm();
        final int paths = algorithm.computeNumberOfPaths();

        System.out.println(paths);
    }

    private static GridPaths createAlgorithm() throws IOException {
        try (Reader reader = new Reader()) {
            final int gridSize = reader.nextInt();

            final boolean[][] grid = new boolean[gridSize][gridSize];
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    grid[i][j] = reader.nextChar() == '.';
                }
            }

            return new GridPaths(grid);
        }
    }

    static class GridPaths {

        private static final int MODULO = 1_000_000_000 + 7;

        private final boolean[][] grid;

        GridPaths(boolean[][] grid) {
            this.grid = grid;
        }

        int computeNumberOfPaths() {

            if (!this.grid[0][0] || !this.grid[this.grid.length - 1][this.grid.length - 1]) {
                return 0;
            }

            final int[][] paths = new int[this.grid.length + 1][this.grid.length + 1];
            paths[0][1] = 1;

            for (int i = 1; i <= this.grid.length; i++) {

                for (int j = 1; j <= this.grid.length; j++) {
                    if (!this.grid[i - 1][j - 1]) {
                        continue;
                    }

                    paths[i][j] = (paths[i - 1][j] + paths[i][j - 1]) % MODULO;
                }
            }

            return paths[this.grid.length][this.grid.length];
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

        char nextChar() throws IOException {
            byte c = read();
            while (c != '.' && c != '*') {
                c = read();
            }

            return (char) c;
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
