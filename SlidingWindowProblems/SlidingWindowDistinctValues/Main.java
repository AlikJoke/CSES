import java.io.DataInputStream;
import java.io.IOException;

/**
 * <a href="https://cses.fi/problemset/task/3222/"><h1>Sliding Window Distinct Values</h1><br/></a>
 * Given an array of {@code n} integers. Task is to calculate the number of distinct values in each window of {@code k} elements, from left to right.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first line contains two integers {@code n} and {@code k}: the number of elements and the size of the window.<br/>
 * Then there are {@code n} integers {@literal x_1,x_2,...,x_n}: the contents of the array.<br/>
 *
 * <i><b>Output</b></i>:<br/>
 * Print {@code n-k+1} values: the numbers of distinct values.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= k <= n <= 2*10^5}<br/>
 * {@code 1 <= x_i <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 1 2 3 2 5 2 2 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3 2 3 2 2 1}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final SlidingWindowDistinctValues algorithm = readInputData();
        final int[] result = algorithm.execute();

        final String outputResult = createOutputResult(result);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final int[] values) {
        final StringBuilder sb = new StringBuilder(values.length * 5);
        for (long value : values) {
            sb.append(value).append(' ');
        }

        return sb.toString();
    }

    private static SlidingWindowDistinctValues readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int n = reader.nextInt();
            final int k = reader.nextInt();

            final int[] sequence = new int[n];
            for (int i = 0; i < n; i++) {
                sequence[i] = reader.nextInt();
            }

            return new SlidingWindowDistinctValues(n, k, sequence);
        }
    }

    static class SlidingWindowDistinctValues {

        private final int n;
        private final int k;
        private final int[] sequence;

        SlidingWindowDistinctValues(int n, int k, int[] sequence) {
            this.n = n;
            this.k = k;
            this.sequence = sequence;
        }

        int[] execute() {

            final int[] result = new int[this.n - this.k + 1];

            final IntHashSet set = new IntHashSet(this.k + 1);

            for (int i = 0; i < this.k; i++) {
                set.add(this.sequence[i]);
            }

            result[0] = set.size();

            for (int i = this.k; i < this.n; i++) {
                set.remove(this.sequence[i - this.k]);
                set.add(this.sequence[i]);

                result[i - this.k + 1] = set.size();
            }

            return result;
        }

        private static class IntHashSet {

            private final Node[] nodes;
            private int size;

            IntHashSet(final int capacity) {
                final int n = -1 >>> Integer.numberOfLeadingZeros(capacity - 1);
                final int size = (n >= 100_000) ? 100_000 : n + 1;
                this.nodes = new Node[size];
            }

            void add(final int value) {
                final int hash = hash(value);
                Node node = this.nodes[hash];
                if (node == null) {
                    this.nodes[hash] = node = new Node(value);
                    this.size++;
                }

                while (node.value != value) {
                    if (node.next == null) {
                        node = node.next = new Node(value, node);
                        this.size++;
                    } else {
                        node = node.next;
                    }
                }

                node.count++;
            }

            void remove(final int value) {
                final int hash = hash(value);
                Node node = this.nodes[hash];
                if (node == null) {
                    return;
                }

                while (node.value != value) {
                    node = node.next;
                    if (node == null) {
                        return;
                    }
                }

                if (--node.count == 0) {
                    if (node.prev != null) {
                        node.prev.next = node.next;
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }
                    } else {
                        this.nodes[hash] = node.next;
                        if (node.next != null) {
                            node.next.prev = null;
                        }
                    }

                    this.size--;
                }
            }

            int size() {
                return this.size;
            }

            private int hash(final int key) {

                int hash = ((key >>> 16) ^ key) * 0x45d9f3b;
                hash = ((hash >>> 16) ^ hash) * 0x45d9f3b;
                hash = (hash >>> 16) ^ hash;

                return (this.nodes.length - 1) & (hash ^ (hash >>> 16));
            }

            private static class Node {

                private final int value;
                private int count;
                private Node next;
                private Node prev;

                private Node(int value) {
                    this.value = value;
                }

                private Node(int value, Node prev) {
                    this.value = value;
                    this.prev = prev;
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
