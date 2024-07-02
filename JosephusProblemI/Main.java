import java.util.Scanner;

/**
 * <h1>Josephus Problem I</h1><br/>
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
        final int childrenCount = readInputChildrenCount();

        final JosephusProblemI algorithm = new JosephusProblemI(childrenCount);
        final int[] removalOrder = algorithm.computeRemovalOrder();

        final String result = createOutputResult(removalOrder);
        System.out.println(result);
    }

    private static String createOutputResult(final int[] removalOrder) {
        final StringBuilder sb = new StringBuilder(removalOrder.length * 5);
        for (int seqNumber : removalOrder) {
            sb.append(seqNumber).append(' ');
        }

        return sb.toString();
    }

    private static int readInputChildrenCount() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static class JosephusProblemI {

        private final int childrenCount;

        JosephusProblemI(int childrenCount) {
            this.childrenCount = childrenCount;
        }

        int[] computeRemovalOrder() {
            final int[] removalOrder = new int[this.childrenCount];

            int removed = 0;
            Child currentChild = createCircularLinkedChildrenNodes();
            while (removed != this.childrenCount) {
                final Child childToRemoval = currentChild.next;
                currentChild.next = childToRemoval.next;
                currentChild = childToRemoval.next;

                removalOrder[removed++] = childToRemoval.id;
            }

            return removalOrder;
        }

        private Child createCircularLinkedChildrenNodes() {
            Child nextChild = null;
            Child lastChild = null;
            for (int i = this.childrenCount - 1; i >= 0; i--) {
                final Child currentChild = new Child(i + 1);
                if (nextChild != null) {
                    currentChild.next = nextChild;
                } else {
                    lastChild = currentChild;
                }

                nextChild = currentChild;
            }

            if (lastChild != null) {
                lastChild.next = nextChild;
            }

            return nextChild;
        }

        private static class Child {

            private final int id;
            private Child next;

            private Child(int id) {
                this.id = id;
            }
        }
    }
}
