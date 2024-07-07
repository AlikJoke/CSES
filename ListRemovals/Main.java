import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>List Removals</h1><br/>
 * Given a list consisting of {@code n} integers. Task is to remove elements from the list at given positions,
 * and report the removed elements.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the initial size of the list. During the process, the elements are numbered
 * {@literal 1},{@literal 2},...,{@literal k} where {@code k} is the current size of the list.<br/>
 * The second line has {@code n} integers {@code x_1},{@code x_2},...,{@code x_n}: the content of the list.<br/>
 * The last line has {@code n} integers {@literal p_1},{@literal p_2},...,{@literal p_n}: the positions of the elements to be removed.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the elements in the order they are removed.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/>
 * {@code 1 <= p_i <= n - i + 1}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 5}<br/>
 * {@literal 2 6 1 4 2}<br/>
 * {@literal 3 1 3 1 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 1 2 2 6 4}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final ListRemovals algorithm = readInputData();
        final int[] removedElements = algorithm.removeElements();

        final String outputResult = createOutputResult(removedElements);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] removedElements) {
        final StringBuilder sb = new StringBuilder(removedElements.length * 5);
        for (int element : removedElements) {
            sb.append(element).append(' ');
        }

        return sb.toString();
    }

    private static ListRemovals readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int listSize = reader.nextInt();

            final int[] list = new int[listSize];
            for (int i = 0; i < listSize; i++) {
                list[i] = reader.nextInt();
            }

            final int[] removalOrder = new int[listSize];
            for (int i = 0; i < listSize; i++) {
                removalOrder[i] = reader.nextInt();
            }

            return new ListRemovals(list, removalOrder);
        }
    }

    static class ListRemovals {

        private final int[] list;
        private final int[] removalOrder;

        ListRemovals(int[] list, int[] removalOrder) {
            this.list = list;
            this.removalOrder = removalOrder;
        }

        int[] removeElements() {

            final int[] result = new int[this.removalOrder.length];

            final IndicesSegmentTree tree = new IndicesSegmentTree(this.list);
            for (int i = 0; i < this.removalOrder.length; i++) {
                final int indexOfElementToRemove = this.removalOrder[i];
                result[i] = tree.remove(indexOfElementToRemove);
            }

            return result;
        }

        static class IndicesSegmentTree {

            private final int[] onPath;
            private final int leavesCount;
            private final Node[] segments;

            IndicesSegmentTree(int[] list) {
                final int height = (int) Math.ceil(Math.log(list.length) / Math.log(2));
                this.leavesCount = power(2, height);
                this.onPath = new int[height];
                this.segments = new Node[this.leavesCount * 2];
                buildTree(this.leavesCount, list);
            }

            int remove(int indexOfElementToRemove) {
                int currentNodeIndex = 1;

                int endRangeIndex = this.segments[currentNodeIndex].nodeValue;

                int countOfNodesOnPath = 0;
                while (currentNodeIndex < this.leavesCount) {

                    this.onPath[countOfNodesOnPath++] = currentNodeIndex;

                    final int indexOfLeftNode = 2 * currentNodeIndex;
                    final int indexOfRightNode = indexOfLeftNode + 1;
                    final Node rightChild = this.segments[indexOfRightNode];

                    final int leftRangeUpperIndex = endRangeIndex - rightChild.nodeValue;
                    if (indexOfElementToRemove <= leftRangeUpperIndex) {
                        endRangeIndex = leftRangeUpperIndex;
                        currentNodeIndex = indexOfLeftNode;
                    } else {
                        currentNodeIndex = indexOfRightNode;
                    }
                }

                final Node nodeToRemove = this.segments[currentNodeIndex];
                nodeToRemove.nodeValue = 0;

                for (int i = 0; i < countOfNodesOnPath; i++) {
                    final int indexOfNodeOnPath = this.onPath[i];
                    this.segments[indexOfNodeOnPath].nodeValue -= 1;
                }

                return nodeToRemove.sourceValue;
            }

            private int power(final int base, final int degree) {
                return degree == 0 ? 1 : base * power(base, degree - 1);
            }

            private void buildTree(int leavesCount, int[] list) {
                for (int i = 0; i < list.length; i++) {
                    this.segments[leavesCount + i] = new Node(list[i], 1);
                }

                for (int i = 0; i < leavesCount - list.length; i++) {
                    this.segments[leavesCount + list.length + i] = new Node(0, 0);
                }

                for (int i = leavesCount - 1; i > 0; i--) {
                    this.segments[i] = new Node(0, this.segments[2 * i].nodeValue + this.segments[2 * i + 1].nodeValue);
                }
            }

            static class Node {

                private final int sourceValue;
                private int nodeValue;

                public Node(int sourceValue, int nodeValue) {
                    this.sourceValue = sourceValue;
                    this.nodeValue = nodeValue;
                }
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
