import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * <h1>Sum of Two Values</h1><br/>
 * Given an array of {@code n} integers, and task is to find two values (at distinct positions) whose sum is {@code x}.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has two integers {@code n} and {@code x}: the array size and the target sum.<br/>
 * The second line has {@code n} integers {@code a_1},{@code a_2},...,{@code a_n}: the array values.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print two integers: the positions of the values.<br/>
 * If there are several solutions, you may print any of them. If there are no solutions, print {@literal IMPOSSIBLE}.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 0 <= x,a_i <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 4 8}<br/>
 * {@literal 2 7 5 1}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2 4}<br/>
 *
 * @author Alik
 */
public class Main {

    private static final String IMPOSSIBLE = "IMPOSSIBLE";

    public static void main(String[] args) throws Exception {
        final SumOfTwoValues algorithm = createAlgorithm();
        final int[] targetElementPositions = algorithm.computeTargetElementPositions();

        if (targetElementPositions == null) {
            System.out.println(IMPOSSIBLE);
        } else {
            System.out.println(targetElementPositions[0] + " " + targetElementPositions[1]);
        }
    }

    private static SumOfTwoValues createAlgorithm() throws Exception {
        try (Reader reader = new Reader()) {
            final int arraySize = reader.nextInt();
            final int targetSum = reader.nextInt();

            int nearestToTargetSumValue = -1;
            final SumOfTwoValues.Item[] array = new SumOfTwoValues.Item[arraySize];
            for (int i = 0; i < arraySize; i++) {
                final int value = reader.nextInt();
                array[i] = new SumOfTwoValues.Item(i + 1, value);

                if (value < targetSum) {
                    nearestToTargetSumValue = Math.max(nearestToTargetSumValue, value);
                }
            }

            return new SumOfTwoValues(array, targetSum, nearestToTargetSumValue);
        }
    }

    private static class SumOfTwoValues {

        private final SplittableRandom random;

        private final Item[] array;
        private final int targetSum;
        private final int nearestToTargetSumValue;

        SumOfTwoValues(Item[] array, int targetSum, int nearestToTargetSumValue) {
            this.array = array;
            this.targetSum = targetSum;
            this.nearestToTargetSumValue = nearestToTargetSumValue;
            this.random = new SplittableRandom();
        }

        int[] computeTargetElementPositions() {
            quickSort(this.array, 0, this.array.length - 1);

            if (this.array.length == 1 || this.array[0].value >= this.targetSum || nearestToTargetSumValue == -1) {
                return null;
            }

            final HashTable hashTable = new HashTable(this.array);
            final SumOfTwoValues.Item nearestValueItem = hashTable.get(this.nearestToTargetSumValue);
            if (nearestValueItem == null) {
                return null;
            }

            for (int i = nearestValueItem.index; i > 0; i--) {
                final Item value = this.array[i];
                final int valuePosition = markValueItemAsUsed(value);

                final int leftValue = this.targetSum - value.value;
                if (leftValue > value.value) {
                    break;
                }

                final SumOfTwoValues.Item leftValueItem = hashTable.get(leftValue);
                if (leftValueItem != null && (leftValueItem != value || leftValueItem.count > 0)) {
                    return new int[] { leftValueItem.sourcePositions.getFirst(), valuePosition };
                }
            }

            return null;
        }

        private int markValueItemAsUsed(final Item valueItem) {
            valueItem.count--;
            return valueItem.sourcePositions.removeLast();
        }

        private void quickSort(final Item[] a, final int leftBound, final int rightBound) {
            final Item auxiliaryItem = a[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];
            final int auxiliaryItemValue = auxiliaryItem.value;

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i].value < auxiliaryItemValue) {
                    i++;
                }

                while (a[j].value > auxiliaryItemValue) {
                    j--;
                }

                if (i <= j) {
                    final Item t = a[i];
                    a[i] = a[j];
                    a[j] = t;

                    a[j].index = j;
                    a[i].index = i;

                    i++;
                    j--;
                }
            }

            if (j > leftBound) {
                quickSort(a, leftBound, j);
            }

            if (i < rightBound) {
                quickSort(a, i, rightBound);
            }
        }

        static class Item {
            private final List<Integer> sourcePositions;
            private final int value;

            private int index;
            private int count;

            Item(int sourcePosition, int value) {
                this.sourcePositions = new ArrayList<>();
                this.sourcePositions.add(sourcePosition);

                this.value = value;
                this.index = sourcePosition - 1;
                this.count = 1;
            }
        }

        private static class HashTable {

            private final Node[] table;

            HashTable(SumOfTwoValues.Item[] table) {
                this.table = new Node[table.length << 1];
                for (SumOfTwoValues.Item item : table) {
                    final int hash = hash(item.value);
                    Node existingNode = this.table[hash];
                    if (existingNode != null) {
                        while (true) {
                            if (existingNode.value.value == item.value) {
                                existingNode.value.count++;
                                existingNode.value.sourcePositions.addAll(item.sourcePositions);
                                existingNode.value.index = Math.max(existingNode.value.index, item.index);
                                break;
                            } else if (existingNode.nextNode == null) {
                                existingNode.nextNode = new Node(item);
                                break;
                            } else {
                                existingNode = existingNode.nextNode;
                            }
                        }
                    } else {
                        this.table[hash] = new Node(item);
                    }
                }
            }

            SumOfTwoValues.Item get(final int value) {
                Node node = this.table[hash(value)];
                while (node != null) {
                    if (node.value.value == value) {
                        return node.value;
                    }

                    node = node.nextNode;
                }

                return null;
            }

            private int hash(final int key) {

                int hash = ((key >>> 16) ^ key) * 0x45d9f3b;
                hash = ((hash >>> 16) ^ hash) * 0x45d9f3b;
                hash = (hash >>> 16) ^ hash;

                return (table.length - 1) & (hash ^ (hash >>> 16));
            }

            private static class Node {

                private final SumOfTwoValues.Item value;
                private Node nextNode;

                private Node(Item value) {
                    this.value = value;
                }
            }
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
