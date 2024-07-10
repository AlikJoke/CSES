import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

/**
 * <h1>Room Allocation</h1><br/>
 * There is a large hotel, and {@code n} customers will arrive soon. Each customer wants to have a single room.<br/>
 * You know each customer's arrival and departure day. Two customers can stay in the same room if the departure
 * day of the first customer is earlier than the arrival day of the second customer.<br/>
 * What is the minimum number of rooms that are needed to accommodate all customers? And how can the rooms be allocated?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of customers.<br/>
 * Then there are {@code n} lines, each of which describes one customer. Each line has two integers {@code a} and {@code b}:
 * the arrival and departure day.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print first an integer {@code k}: the minimum number of rooms required.<br/>
 * After that, print a line that contains the room number of each customer in the same order as in the input.
 * The rooms are numbered {@literal 1},{@literal 2},...,{@literal k}. You can print any valid solution.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= a <= b <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 1 2}<br/>
 * {@literal 2 4}<br/>
 * {@literal 4 4}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 * {@literal 1 2 1}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final RoomAllocation algorithm = readInputData();
        final RoomAllocation.Result allocationResult = algorithm.computeRoomAllocation();

        final String outputResult = createOutputResult(allocationResult);
        System.out.println(outputResult);
    }

    private static String createOutputResult(final RoomAllocation.Result allocationResult) {
        final StringBuilder sb = new StringBuilder(allocationResult.allocation.length * 7);
        sb.append(allocationResult.getMinRequiredRooms()).append(System.lineSeparator());
        for (int room : allocationResult.getAllocation()) {
            sb.append(room).append(' ');
        }

        return sb.toString();
    }

    private static RoomAllocation readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int customersCount = reader.nextInt();

            final RoomAllocation.VisitTime[] customersTimes = new RoomAllocation.VisitTime[customersCount];
            for (int i = 0; i < customersCount; i++) {
                customersTimes[i] = new RoomAllocation.VisitTime(reader.nextInt(), reader.nextInt(), i);
            }

            return new RoomAllocation(customersTimes);
        }
    }

    static class RoomAllocation {

        private final SplittableRandom random;
        private final VisitTime[] customersTimes;

        RoomAllocation(VisitTime[] customersTimes) {
            this.customersTimes = customersTimes;
            this.random = new SplittableRandom();
        }

        Result computeRoomAllocation() {
            int minRequiredRooms = 0;

            final int[] result = new int[this.customersTimes.length];

            quickSort(this.customersTimes, 0, this.customersTimes.length - 1);
            final MinHeap heap = new MinHeap(this.customersTimes.length);

            for (final VisitTime customerTimes : this.customersTimes) {

                Room targetRoom = heap.peek();
                if (targetRoom == null || targetRoom.occupiedTo >= customerTimes.arrivalDay) {
                    targetRoom = new Room(++minRequiredRooms);
                } else {
                    targetRoom = heap.pollFirst();
                }

                targetRoom.occupiedTo = customerTimes.departureDay;

                heap.offer(targetRoom);

                result[customerTimes.index] = targetRoom.id;
            }

            return new Result(minRequiredRooms, result);
        }

        private void quickSort(final VisitTime[] a, final int leftBound, final int rightBound) {
            final VisitTime auxiliaryItem = a[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];
            final int auxiliaryItemValue = auxiliaryItem.arrivalDay;

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i].arrivalDay < auxiliaryItemValue) {
                    i++;
                }

                while (a[j].arrivalDay > auxiliaryItemValue) {
                    j--;
                }

                if (i <= j) {
                    final VisitTime t = a[i];

                    if (a[i].arrivalDay != a[j].arrivalDay || a[i].departureDay > a[j].departureDay) {
                        a[i] = a[j];
                        a[j] = t;
                    }

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

        static class Result {

            private final int minRequiredRooms;
            private final int[] allocation;

            Result(int minRequiredRooms, int[] allocation) {
                this.minRequiredRooms = minRequiredRooms;
                this.allocation = allocation;
            }

            public int getMinRequiredRooms() {
                return minRequiredRooms;
            }

            public int[] getAllocation() {
                return allocation;
            }
        }

        static class Room {
            private final int id;
            private int occupiedTo;

            Room(int id) {
                this.id = id;
            }
        }

        static class VisitTime {

            private final int arrivalDay;
            private final int departureDay;
            private final int index;

            VisitTime(int arrivalDay, int departureDay, int index) {
                this.arrivalDay = arrivalDay;
                this.departureDay = departureDay;
                this.index = index;
            }
        }

        private static class MinHeap {

            private final List<Room> table;

            MinHeap(int size) {
                this.table = new ArrayList<>(size);
            }

            Room peek() {
                return this.table.isEmpty() ? null : this.table.get(0);
            }

            Room pollFirst() {
                final Room node = this.table.get(0);
                if (node == null) {
                    return null;
                }

                final int lastValueIndex = this.table.size() - 1;
                final Room lastValue = this.table.get(lastValueIndex);

                this.table.set(0, lastValue);
                this.table.remove(lastValueIndex);

                heapify(0);

                return node;
            }

            void offer(Room value) {

                this.table.add(value);

                int indexOfValue = this.table.size() - 1;

                while (indexOfValue != 0) {
                    final int parentIndex = (indexOfValue - 1) / 2;
                    final Room parent = this.table.get(parentIndex);
                    if (parent.occupiedTo > value.occupiedTo) {
                        this.table.set(parentIndex, value);
                        this.table.set(indexOfValue, parent);
                    }

                    indexOfValue = parentIndex;
                }
            }

            private void heapify(final int rootIndex) {
                final int leftIndex = leftChildIndex(rootIndex);
                final int rightIndex = rightChildIndex(rootIndex);

                int currentNodeIndex = rootIndex;
                if (leftIndex < this.table.size() && this.table.get(leftIndex).occupiedTo < this.table.get(currentNodeIndex).occupiedTo) {
                    currentNodeIndex = leftIndex;
                }

                if (rightIndex < this.table.size() && this.table.get(rightIndex).occupiedTo < this.table.get(currentNodeIndex).occupiedTo) {
                    currentNodeIndex = rightIndex;
                }

                if (currentNodeIndex != rootIndex) {
                    final Room valueToReplace = this.table.get(currentNodeIndex);
                    this.table.set(currentNodeIndex, this.table.set(rootIndex, valueToReplace));

                    heapify(currentNodeIndex);
                }
            }

            private int leftChildIndex(final int index) {
                return index * 2 + 1;
            }

            private int rightChildIndex(final int index) {
                return index * 2 + 2;
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
