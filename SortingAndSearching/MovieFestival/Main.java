import java.io.DataInputStream;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * <h1>Movie Festival</h1><br/>
 * In a movie festival {@code n} movies will be shown. You know the starting and ending time of each movie.
 * What is the maximum number of movies you can watch entirely?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The first input line has an integer {@code n}: the number of movies.<br/>
 * After this, there are {@code n} lines that describe the movies. Each line has two integers
 * {@code a} and {@code b}: the starting and ending times of a movie.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the maximum number of movies.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/>
 * {@code 1 <= a < b <= 10^9}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 3 5}<br/>
 * {@literal 4 9}<br/>
 * {@literal 5 8}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2}<br/>
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final MovieFestival algorithm = readInputData();
        final int maxNumberOfMovies = algorithm.computeMaxNumberOfMovies();

        System.out.println(maxNumberOfMovies);
    }

    private static MovieFestival readInputData() throws Exception {
        try (Reader reader = new Reader()) {
            final int moviesCount = reader.nextInt();

            final MovieFestival.MovieTime[] customersVisitTimes = new MovieFestival.MovieTime[moviesCount * 2];
            for (int i = 0, j = 0; i < moviesCount; i++) {
                customersVisitTimes[j++] = new MovieFestival.MovieTime(reader.nextInt(), i, false);
                customersVisitTimes[j++] = new MovieFestival.MovieTime(reader.nextInt(), i, true);
            }

            return new MovieFestival(customersVisitTimes);
        }
    }

    private static class MovieFestival {

        private final SplittableRandom random;
        private final MovieFestival.MovieTime[] movieTimes;

        MovieFestival(MovieFestival.MovieTime[] movieTimes) {
            this.random = new SplittableRandom();
            this.movieTimes = movieTimes;
        }

        int computeMaxNumberOfMovies() {
            quickSort(this.movieTimes, 0, this.movieTimes.length - 1);

            final int[] finishedMoviesBeforeNewMovie = new int[this.movieTimes.length >> 1];
            final boolean[] currentMovies = new boolean[finishedMoviesBeforeNewMovie.length];

            int maxCountOfFinishedMovies = 0;
            for (final MovieTime movieTime : this.movieTimes) {

                if (currentMovies[movieTime.label]) {
                    currentMovies[movieTime.label] = false;
                    finishedMoviesBeforeNewMovie[movieTime.label] = ++finishedMoviesBeforeNewMovie[movieTime.label];
                } else {
                    currentMovies[movieTime.label] = true;
                    finishedMoviesBeforeNewMovie[movieTime.label] = maxCountOfFinishedMovies;
                }

                maxCountOfFinishedMovies = Math.max(finishedMoviesBeforeNewMovie[movieTime.label], maxCountOfFinishedMovies);
            }

            return maxCountOfFinishedMovies;
        }

        private void quickSort(final MovieFestival.MovieTime[] movieTimes, final int leftBound, final int rightBound) {
            final MovieFestival.MovieTime auxiliaryItem = movieTimes[leftBound == rightBound ? leftBound : random.nextInt(leftBound, rightBound)];

            int i = leftBound;
            int j = rightBound;

            while (i <= j) {

                while (movieTimes[i].time < auxiliaryItem.time || movieTimes[i].time == auxiliaryItem.time && !auxiliaryItem.finishTime && movieTimes[i].finishTime) {
                    i++;
                }

                while (movieTimes[j].time > auxiliaryItem.time || movieTimes[j].time == auxiliaryItem.time && auxiliaryItem.finishTime && !movieTimes[j].finishTime) {
                    j--;
                }

                if (i <= j) {
                    final MovieFestival.MovieTime t = movieTimes[i];
                    movieTimes[i] = movieTimes[j];
                    movieTimes[j] = t;

                    i++;
                    j--;
                }
            }

            if (j > leftBound) {
                quickSort(movieTimes, leftBound, j);
            }

            if (i < rightBound) {
                quickSort(movieTimes, i, rightBound);
            }
        }

        private static class MovieTime {

            private final int time;
            private final int label;
            private final boolean finishTime;

            private MovieTime(int time, int label, boolean finishTime) {
                this.time = time;
                this.label = label;
                this.finishTime = finishTime;
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
