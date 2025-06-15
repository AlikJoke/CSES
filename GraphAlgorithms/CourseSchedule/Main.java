import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <a href="https://cses.fi/problemset/task/1679/"><h1>Course Schedule</h1></a><br/>
 * You have to complete {@code n} courses. There are {@code m} requirements of the form "course {@code a} has
 * to be completed before course {@code b}". Task is to find an order in which you can complete the courses.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code m}: the number of courses and requirements. The courses
 * are numbered {@code 1},{@code 2},...,{@code n}.<br/>
 * After this, there are {@code m} lines describing the requirements. Each line has two integers {@code a} and {@code b}:
 * course {@code a} has to be completed before course {@code b}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print an order in which you can complete the courses. You can print any valid order that includes all the courses.<br/>
 * If there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 10^5}<br/>
 * {@code 1 <= m <= 2*10^5}<br/>
 * {@code 1 <= a,b <= n}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5 3}<br/>
 * {@literal 1 2}<br/>
 * {@literal 3 1}<br/>
 * {@literal 4 5}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3 4 1 5 2}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE_LABEL = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {
        final CourseSchedule algorithm = createAlgorithm();
        final Integer[] sortedCourses = algorithm.execute();

        final String outputResult = createOutputResult(sortedCourses);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final Integer[] path) {
        if (path.length == 0) {
            return IMPOSSIBLE_LABEL;
        }

        final StringBuilder sb = new StringBuilder(path.length * 5);
        for (int i = path.length - 1; i >= 0; i--) {
            sb.append(path[i] + 1).append(' ');
        }

        return sb.toString();
    }

    private static CourseSchedule createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int coursesCount = reader.nextInt();
            final int requirementsCount = reader.nextInt();

            @SuppressWarnings("unchecked")
            final List<Integer>[] adjTable = new ArrayList[coursesCount];
            for (int i = 0; i < requirementsCount; i++) {
                final int from = reader.nextInt() - 1;
                final int to = reader.nextInt() - 1;

                List<Integer> cityAdj = adjTable[from];
                if (cityAdj == null) {
                    adjTable[from] = cityAdj = new ArrayList<>(Math.min(coursesCount, 100));
                }

                cityAdj.add(to);
            }

            return new CourseSchedule(adjTable);
        }
    }

    private static class CourseSchedule {

        private final List<Integer>[] adjTable;
        private final Deque<Integer> sortedOrder;
        private final boolean[] visited;
        private final int[] path;

        CourseSchedule(final List<Integer>[] adjTable) {
            this.adjTable = adjTable;
            this.sortedOrder = new LinkedList<>();
            this.visited = new boolean[adjTable.length];
            this.path = new int[adjTable.length];
            Arrays.fill(this.path, -1);
        }

        Integer[] execute() {

            for (int i = 0; i < this.adjTable.length; i++) {
                if (!visited[i]) {

                    if (this.adjTable[i] == null) {
                        this.visited[i] = true;
                        this.sortedOrder.offerFirst(i);
                        continue;
                    }

                    this.path[i] = i;
                    if (!sort(i)) {
                        return new Integer[0];
                    } else {
                        this.sortedOrder.add(i);
                        this.path[i] = -1;
                    }
                }
            }

            return this.sortedOrder.toArray(new Integer[0]);
        }

        private boolean sort(final int currentNode) {
            this.visited[currentNode] = true;
            final List<Integer> adjNodes = this.adjTable[currentNode];
            if (adjNodes == null) {
                return true;
            }

            for (final Integer adjNode : adjNodes) {

                if (!visited[adjNode]) {
                    this.path[adjNode] = currentNode;
                    final boolean result = sort(adjNode);
                    this.sortedOrder.add(adjNode);
                    this.path[adjNode] = -1;

                    if (!result) {
                        return false;
                    }
                } else if (this.path[adjNode] != -1) {
                    return false;
                }
            }

            return true;
        }
    }

    private static class Reader implements AutoCloseable {

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