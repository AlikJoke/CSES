import java.io.DataInputStream;
import java.io.IOException;

/**
 * <h1>Playlist</h1><br/>
 * Given a playlist of a radio station since its establishment. The playlist has a total of {@code n} songs.<br/>
 * What is the longest sequence of successive songs where each song is unique?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains an integer {@code n}: the number of songs.<br/>
 * Then there are {@code n} integers: {@code k_1},{@code k_2},...,{@code k_n}: the id number of each song.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the length of the longest sequence of unique songs.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= k_i <= 10^9}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8}<br/>
 * {@literal 1 2 1 3 2 7 4 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final Playlist algorithm = readInputData();
        final int uniqueSongsLongestSeries = algorithm.computeLongestSeriesOfUniqueSongs();

        System.out.println(uniqueSongsLongestSeries);
    }

    private static Playlist readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int songsCount = reader.nextInt();

            final int[] songs = new int[songsCount];
            for (int i = 0; i < songsCount; i++) {
                songs[i] = reader.nextInt();
            }

            return new Playlist(songs);
        }
    }

    static class Playlist {

        private final int[] songs;

        Playlist(int[] songs) {
            this.songs = songs;
        }

        int computeLongestSeriesOfUniqueSongs() {

            int longestSeries = 0;
            int currentSeriesStartIndex = 0;

            final IntHashTable songs2index = new IntHashTable(Math.max(1, this.songs.length >> 1));

            for (int i = 0; i < this.songs.length; i++) {

                final int song = this.songs[i];
                int lastGlobalSequencePositionOfSong = songs2index.get(song);
                if (lastGlobalSequencePositionOfSong != -1) {
                    currentSeriesStartIndex = Math.max(currentSeriesStartIndex, lastGlobalSequencePositionOfSong + 1);
                }

                songs2index.put(song, i);
                longestSeries = Math.max(longestSeries, i + 1 - currentSeriesStartIndex);
            }

            return longestSeries;
        }

        private static class IntHashTable {

            private final Node[] table;

            IntHashTable(int size) {
                this.table = new Node[size];
            }

            void put(final int key, final int value) {
                final int hash = hash(key);
                Node node = this.table[hash];
                Node lastNode = null;
                while (node != null) {
                    if (node.key == key) {
                        node.value = value;
                        return;
                    }

                    lastNode = node;
                    node = node.nextNode;
                }

                if (lastNode == null) {
                    this.table[hash] = new Node(key, value);
                } else {
                    lastNode.nextNode = new Node(key, value);
                }
            }

            int get(final int key) {
                final int hash = hash(key);
                Node node = this.table[hash];
                while (node != null) {
                    if (node.key == key) {
                        return node.value;
                    }

                    node = node.nextNode;
                }

                return -1;
            }

            private int hash(final int key) {

                int hash = ((key >>> 16) ^ key) * 0x45d9f3b;
                hash = ((hash >>> 16) ^ hash) * 0x45d9f3b;
                hash = (hash >>> 16) ^ hash;

                return (this.table.length - 1) & (hash ^ (hash >>> 16));
            }

            private static class Node {

                private final int key;
                private int value;
                private Node nextNode;

                private Node(int key, int value) {
                    this.key = key;
                    this.value = value;
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
