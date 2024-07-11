import java.util.Scanner;

/**
 * <h1>Josephus Problem II</h1><br/>
 * Consider a game where there are {@code n} children (numbered {@literal 1},{@literal 2},...,{@literal n}) in a circle.
 * During the game, every other child is removed from the circle until there are no children left.
 * In which order will the children be removed?<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@code n}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print {@code n} integers: the removal order.<br/>
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 2*10^5}<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 7}<br/>
 * <i>Output</i>:<br/>
 * {@literal 2 4 6 1 5 3 7}
 *
 * @author Alik
 */
public class Main {

    public static void main(String[] args) {
        final JosephusProblemII algorithm = readInputData();
        final int[] removalOrder = algorithm.computeRemovalOrder();

        final String result = createOutputResult(removalOrder);
        System.out.println(result);
    }

    private static String createOutputResult(final int[] removalOrder) {
        final StringBuilder sb = new StringBuilder(removalOrder.length * 7);
        for (int seqNumber : removalOrder) {
            sb.append(seqNumber).append(' ');
        }

        return sb.toString();
    }

    private static JosephusProblemII readInputData() {
        try (Scanner scanner = new Scanner(System.in)) {
            return new JosephusProblemII(scanner.nextInt(), scanner.nextInt());
        }
    }

    private static class JosephusProblemII {

        private final int childrenCount;
        private final int skipCount;

        JosephusProblemII(int childrenCount, int skipCount) {
            this.childrenCount = childrenCount;
            this.skipCount = skipCount;
        }

        int[] computeRemovalOrder() {
            final int[] removalOrder = new int[this.childrenCount];

            final IndicesSegmentTree tree = new IndicesSegmentTree(this.childrenCount);

            int removalCounter = 0;
            int nextToRemove = 0;
            while (removalCounter != this.childrenCount) {
                nextToRemove = (nextToRemove + this.skipCount) % (this.childrenCount - removalCounter);
                removalOrder[removalCounter++] = tree.remove(nextToRemove + 1);
            }

            return removalOrder;
        }

        static class IndicesSegmentTree {

            private final int[] onPath;
            private final int leavesCount;
            private final int[] segments;

            IndicesSegmentTree(int size) {
                final int height = (int) Math.ceil(Math.log(size) / Math.log(2));
                this.leavesCount = power(2, height);
                this.onPath = new int[height];
                this.segments = new int[this.leavesCount * 2];
                buildTree(this.leavesCount, size);
            }

            int remove(int indexOfElementToRemove) {
                int currentNodeIndex = 1;

                int endRangeIndex = this.segments[currentNodeIndex];

                int countOfNodesOnPath = 0;
                while (currentNodeIndex < this.leavesCount) {

                    this.onPath[countOfNodesOnPath++] = currentNodeIndex;

                    final int indexOfLeftNode = 2 * currentNodeIndex;
                    final int indexOfRightNode = indexOfLeftNode + 1;
                    final int rightChild = this.segments[indexOfRightNode];

                    final int leftRangeUpperIndex = endRangeIndex - rightChild;
                    if (indexOfElementToRemove <= leftRangeUpperIndex) {
                        endRangeIndex = leftRangeUpperIndex;
                        currentNodeIndex = indexOfLeftNode;
                    } else {
                        currentNodeIndex = indexOfRightNode;
                    }
                }

                this.segments[currentNodeIndex] = 0;

                for (int i = 0; i < countOfNodesOnPath; i++) {
                    final int indexOfNodeOnPath = this.onPath[i];
                    this.segments[indexOfNodeOnPath] -= 1;
                }

                return currentNodeIndex + 1 - this.leavesCount;
            }

            private int power(final int base, final int degree) {
                return degree == 0 ? 1 : base * power(base, degree - 1);
            }

            private void buildTree(int leavesCount, int size) {
                for (int i = 0; i < size; i++) {
                    this.segments[leavesCount + i] = 1;
                }

                for (int i = 0; i < leavesCount - size; i++) {
                    this.segments[leavesCount + size + i] = 0;
                }

                for (int i = leavesCount - 1; i > 0; i--) {
                    this.segments[i] = this.segments[2 * i] + this.segments[2 * i + 1];
                }
            }
        }
    }
}
