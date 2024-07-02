import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.SplittableRandom;

/**
 * <h1>Traffic Lights</h1><br/>
 * There is a street of length {@code x} whose positions are numbered {@literal 0},{@literal 1},...,{@literal x}.
 * Initially there are no traffic lights, but {@code n} sets of traffic lights are added to the street one after another.<br/>
 * Task is to calculate the length of the longest passage without traffic lights after each addition.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line contains two integers {@code x} and {@code n}: the length of the street and the number of sets of traffic lights.<br/>
 * Then, the next line contains {@code n} integers {@code p_1},{@code p_2},...,{@code p_n}: the position of each set of traffic lights. Each position is distinct.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print the length of the longest passage without traffic lights after each addition.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= x <= 10^9}<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 0 < p_i < x}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 8 3}<br/>
 * {@literal 3 6 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 5 3 3}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final TrafficLights algorithm = readInputData();
        final int[] longestDistances = algorithm.computeLongestDistances();

        final String outputResult = composeOutputResults(longestDistances);
        System.out.println(outputResult);
    }

    private static String composeOutputResults(final int[] longestDistances) {
        final StringBuilder outputAccumulator = new StringBuilder(longestDistances.length * 10);
        for (int result : longestDistances) {
            outputAccumulator.append(result).append(' ');
        }

        return outputAccumulator.toString();
    }

    private static TrafficLights readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int streetLength = reader.nextInt();
            final int trafficLightsCount = reader.nextInt();

            final TrafficLights.StreetPart[] partsWithTrafficLights = new TrafficLights.StreetPart[trafficLightsCount];

            for (int i = 0; i < trafficLightsCount; i++) {
                final int trafficLightPosition = reader.nextInt();
                final TrafficLights.StreetPart streetPart = new TrafficLights.StreetPart(trafficLightPosition);
                partsWithTrafficLights[i] = streetPart;
            }

            return new TrafficLights(streetLength, partsWithTrafficLights);
        }
    }

    static class TrafficLights {

        private final int streetLength;
        private final StreetPart[] streetPartsWithTrafficLights;
        private final SplittableRandom random;

        TrafficLights(final int streetLength, final StreetPart[] streetPartsWithTrafficLights) {
            this.streetLength = streetLength;
            this.streetPartsWithTrafficLights = streetPartsWithTrafficLights;
            this.random = new SplittableRandom();
        }

        int[] computeLongestDistances() {

            fillDistancesBetweenTrafficLights(this.streetPartsWithTrafficLights);

            int maxDistanceLength = computeLongestDistance(this.streetPartsWithTrafficLights);

            final int[] result = new int[this.streetPartsWithTrafficLights.length];
            for (int i = this.streetPartsWithTrafficLights.length - 1; i >= 0; i--) {
                result[i] = maxDistanceLength;

                final StreetPart partToRemoveTrafficLight = this.streetPartsWithTrafficLights[i];

                if (partToRemoveTrafficLight.nextPart != null) {
                    final StreetPart nextPartWithTrafficLight = partToRemoveTrafficLight.nextPart;
                    nextPartWithTrafficLight.prevPart = partToRemoveTrafficLight.prevPart;
                    nextPartWithTrafficLight.distanceToPrevTrafficLight += partToRemoveTrafficLight.distanceToPrevTrafficLight;
                    maxDistanceLength = Math.max(maxDistanceLength, nextPartWithTrafficLight.distanceToPrevTrafficLight);
                }

                if (partToRemoveTrafficLight.prevPart != null) {
                    final StreetPart prevPartWithTrafficLight = partToRemoveTrafficLight.prevPart;
                    prevPartWithTrafficLight.nextPart = partToRemoveTrafficLight.nextPart;
                    prevPartWithTrafficLight.distanceToNextTrafficLight += partToRemoveTrafficLight.distanceToNextTrafficLight;
                    maxDistanceLength = Math.max(maxDistanceLength, prevPartWithTrafficLight.distanceToNextTrafficLight);
                }
            }

            return result;
        }
        
        private int computeLongestDistance(final TrafficLights.StreetPart[] streetPartsWithTrafficLights) {
            int longestDistanceLength = 0; 
            for (TrafficLights.StreetPart part : streetPartsWithTrafficLights) {
                longestDistanceLength = Math.max(
                        longestDistanceLength,
                        Math.max(part.distanceToPrevTrafficLight, part.distanceToNextTrafficLight)
                );
            }

            return longestDistanceLength;
        }

        private void quickSort(final TrafficLights.StreetPart[] a, final int leftBound, final int rightBound) {
            final TrafficLights.StreetPart auxiliaryItem = a[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (a[i].position < auxiliaryItem.position) {
                    i++;
                }

                while (a[j].position > auxiliaryItem.position) {
                    j--;
                }

                if (i <= j) {
                    final TrafficLights.StreetPart t = a[i];
                    a[i] = a[j];
                    a[j] = t;

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

        private void fillDistancesBetweenTrafficLights(final TrafficLights.StreetPart[] streetPartsWithTrafficLights) {

            final StreetPart[] sortedTrafficLightsPositions = Arrays.copyOf(streetPartsWithTrafficLights, streetPartsWithTrafficLights.length);
            quickSort(sortedTrafficLightsPositions, 0, sortedTrafficLightsPositions.length - 1);

            final StreetPart startPartOfStreet = new StreetPart(0);
            StreetPart prevPartWithTrafficLight = startPartOfStreet;

            for (final StreetPart part : sortedTrafficLightsPositions) {
                part.distanceToPrevTrafficLight = part.position - prevPartWithTrafficLight.position;
                part.prevPart = prevPartWithTrafficLight == startPartOfStreet ? null : prevPartWithTrafficLight;

                prevPartWithTrafficLight.distanceToNextTrafficLight = part.distanceToPrevTrafficLight;
                prevPartWithTrafficLight.nextPart = part;

                prevPartWithTrafficLight = part;
            }

            prevPartWithTrafficLight.distanceToNextTrafficLight = this.streetLength - prevPartWithTrafficLight.position;
        }

        static class StreetPart {

            private final int position;
            private int distanceToPrevTrafficLight;
            private int distanceToNextTrafficLight;
            private StreetPart prevPart;
            private StreetPart nextPart;

            StreetPart(int position) {
                this.position = position;
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
