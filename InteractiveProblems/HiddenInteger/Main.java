import java.io.DataInputStream;
import java.io.IOException;

/**
 * <a href="https://cses.fi/problemset/task/3112/"><h1>Hidden Integer</h1><br/></a><br/>
 * There is a hidden integer {@code x}. Task is to find the value of {@code x}.<br/>
 * To do this, you can ask questions: you can choose an integer {@code y} and you will be told if {@code y < x}.<br/><br/>
 *
 * <i><b>Interaction</b></i><br/>
 * This is an interactive problem. Your code will interact with the grader using standard input and output.
 * You can start asking questions right away.<br/>
 * On your turn, you can print one of the following:
 * <ul>
 *     <li>"? y", where {@code 1 <= y <= 10^9}: ask if {@code y < x}. The grader will return {@literal YES} if {@code y < x} and {@literal NO} otherwise.</ul>
 *     <li>"!\ x": report that the hidden integer is {@code x}. Your program must terminate after this.</ul>
 * </ul>
 * Each line should be followed by a line break. You must make sure the output gets flushed after printing each line.<br/>
 *
 * <i><b>Constraints</b></i><br/>
 * <ul>
 *     <li>{@code 1 <= x <= 10^9}</li>
 *     <li>you can ask at most 30 questions of type ?</li>
 * </ul>
 *
 * <b>Example</b><br/>
 * {@literal ? 3}<br/>
 * {@literal YES}<br/>
 * {@literal ? 6}<br/>
 * {@literal YES}<br/>
 * {@literal ? 7}<br/>
 * {@literal NO}<br/>
 * {@literal ! 7}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final HiddenInteger algorithm = new HiddenInteger();
        algorithm.execute();
    }

    static class HiddenInteger {

        private static final int MAX_VALUE = 1_000_000_000;

        void execute() throws IOException {

            int median = MAX_VALUE / 2;
            int right = MAX_VALUE;
            int left = 1;
            try (Reader reader = new Reader()) {

                while (true) {

                    if (median == right && median == left) {
                        System.out.println("!" + median);
                        return;
                    }

                    System.out.println("? " + median);
                    if (reader.next()) {
                        left = median + 1;
                    } else {
                        if (median == left) {
                            System.out.println("! " + median);
                            return;
                        }

                        right = median;
                    }
                    median = (right + left) >> 1;
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

            boolean next() throws IOException {

                byte c = read();
                while (c <= ' ') {
                    c = read();
                }

                boolean result = false;
                do {
                    result |= c == 'Y';
                } while ((c = read()) >= 'A' && c <= 'Z');

                return result;
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
}
