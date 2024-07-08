import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Hotel Queries</h1><br/>
 * There are {@code n} hotels on a street. For each hotel you know the number of free rooms. Task is to assign hotel rooms for
 * groups of tourists. All members of a group want to stay in the same hotel.<br/>
 * The groups will come to you one after another, and you know for each group the number of rooms it requires.
 * You always assign a group to the first hotel having enough rooms. After this, the number of free rooms in the hotel decreases.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains two integers {@code n} and {@code m}: the number of hotels and the number of groups.
 * The hotels are numbered {@literal 1},{@literal 2},...,{@literal n}.<br/>
 * The second line has {@code n} integers {@code h_1},{@code h_2},...,{@code h_n}: the number of free rooms in each hotel.<br/>
 * The last line has {@code m} integers {@literal r_1},{@literal r_2},...,{@literal r_m}: the number of rooms each group requires.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the assigned hotel for each group. If a group cannot be assigned a hotel, print {@literal 0} instead.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n,m <= 2*10^5}<br/>
 * {@code 1 <= h_i,r_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 5}<br/>
 * {@literal 3 2 4 1 5 5 2 6}<br/>
 * {@literal 4 4 7 1 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3 5 0 1 1}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final HotelQueries algorithm = readInputData();
        final int[] hotels = algorithm.computeHotels();

        final String outputResult = createOutputResult(hotels);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] hotels) {
        final StringBuilder sb = new StringBuilder(hotels.length * 6);
        for (int hotel : hotels) {
            sb.append(hotel).append(' ');
        }

        return sb.toString();
    }

    private static HotelQueries readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int hotelsCount = reader.nextInt();
            final int groupsCount = reader.nextInt();

            final int[] countOfRoomsInHotels = new int[hotelsCount];
            for (int i = 0; i < hotelsCount; i++) {
                countOfRoomsInHotels[i] = reader.nextInt();
            }

            final int[] roomsCountForGroups = new int[groupsCount];
            for (int i = 0; i < groupsCount; i++) {
                roomsCountForGroups[i] = reader.nextInt();
            }

            return new HotelQueries(countOfRoomsInHotels, roomsCountForGroups);
        }
    }

    static class HotelQueries {

        private final int[] countOfRoomsInHotels;
        private final int[] roomsCountForGroups;

        HotelQueries(int[] countOfRoomsInHotels, int[] roomsCountForGroups) {
            this.countOfRoomsInHotels = countOfRoomsInHotels;
            this.roomsCountForGroups = roomsCountForGroups;
        }

        int[] computeHotels() {

            final int[] result = new int[this.roomsCountForGroups.length];

            final MaxSegmentTree tree = new MaxSegmentTree(this.countOfRoomsInHotels);
            for (int i = 0; i < this.roomsCountForGroups.length; i++) {
                final int requiredRoomsCount = this.roomsCountForGroups[i];
                result[i] = tree.decreaseNearestCeil(requiredRoomsCount);
            }

            return result;
        }

        static class MaxSegmentTree {

            private final int[] onPath;
            private final int leavesCount;
            private final int[] segments;

            MaxSegmentTree(int[] array) {
                final int height = (int) Math.ceil(Math.log(array.length) / Math.log(2));
                this.leavesCount = power(2, height);
                this.onPath = new int[height];
                this.segments = new int[this.leavesCount * 2];
                buildMaxTree(this.leavesCount, array);
            }

            int decreaseNearestCeil(int value) {
                int currentNodeIndex = 1;

                final int rootMaxValue = this.segments[currentNodeIndex];
                if (rootMaxValue < value) {
                    return 0;
                }

                int countOfNodesOnPath = 0;
                while (currentNodeIndex < this.leavesCount) {

                    this.onPath[countOfNodesOnPath++] = currentNodeIndex;

                    final int indexOfLeftNode = 2 * currentNodeIndex;
                    final int indexOfRightNode = indexOfLeftNode + 1;
                    final int leftChild = this.segments[indexOfLeftNode];

                    if (value <= leftChild) {
                        currentNodeIndex = indexOfLeftNode;
                    } else {
                        currentNodeIndex = indexOfRightNode;
                    }
                }

                this.segments[currentNodeIndex] -= value;

                for (int i = this.onPath.length - 1; i >= 0; i--) {
                    final int indexOfNodeOnPath = this.onPath[i];
                    final int indexOfLeftChildNode = 2 * indexOfNodeOnPath;

                    final int leftChild = this.segments[indexOfLeftChildNode];
                    final int rightChild = this.segments[indexOfLeftChildNode + 1];

                    final int newMax = Math.max(leftChild, rightChild);
                    if (this.segments[indexOfNodeOnPath] == newMax) {
                        break;
                    }

                    this.segments[indexOfNodeOnPath] = newMax;
                }

                return currentNodeIndex - this.leavesCount + 1;
            }

            private void buildMaxTree(int leavesCount, int[] array) {
                System.arraycopy(array, 0, this.segments, this.leavesCount, array.length);

                for (int i = leavesCount - 1; i > 0; i--) {
                    this.segments[i] = Math.max(this.segments[2 * i], this.segments[2 * i + 1]);
                }
            }

            private int power(final int base, final int degree) {
                return degree == 0 ? 1 : base * power(base, degree - 1);
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
