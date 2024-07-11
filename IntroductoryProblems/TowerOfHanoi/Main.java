import java.util.Scanner;

/**
 * <h1>Tower of Hanoi</h1><br/>
 * The Tower of Hanoi game consists of three stacks (left, middle and right) and {@code n} round disks of
 * different sizes. Initially, the left stack has all the disks, in increasing order of size from top to bottom.<br/>
 * The goal is to move all the disks to the right stack using the middle stack. On each move you can move
 * the uppermost disk from a stack to another stack. In addition, it is not allowed to place a larger disk
 * on a smaller disk.<br/>
 * Task is to find a solution that minimizes the number of moves.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@code n}: the number of disks.<br/>
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@code k}: the minimum number of moves.<br/>
 * After this, print {@code k} lines that describe the moves. Each line has two integers {@code a} and {@code b}:
 * move a disk from stack {@code a} to stack {@code b}.
 * <i><b>Constraints</b></i>:<br/>
 * {@code 1 <= n <= 16}<br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal 2}<br/>
 * <i>Output</i>:<br/>
 * {@literal 3}<br/>
 * {@literal 1 2}<br/>
 * {@literal 1 3}<br/>
 * {@literal 2 3}<br/>
 *
 * @author Alik
 *
 * @implNote Recursively move all disks except the lowest one from the original stack to the auxiliary stack.
 * After this, move the lowest disk to the target stack. The next step is to move all disks except the bottom
 * one from the auxiliary stack to the left one (originally the original one). The remaining disk on the
 * auxiliary stack is moved to the target stack. In this way, roles are rotated between stacks.
 */
public class Main {

    public static void main(String[] args) {
        final int disksCount = readDisksCount();

        final TowerOfHanoi algorithm = new TowerOfHanoi(disksCount);
        final int[][] result = algorithm.move();

        final StringBuilder acc = new StringBuilder(result.length * 4 + 10);
        acc.append(result.length).append(System.lineSeparator());

        for (int[] oneMoveInfo : result) {
            appendMoveInfo(oneMoveInfo[0], oneMoveInfo[1], acc);
        }

        System.out.println(acc);
    }

    private static void appendMoveInfo(
            final int sourceStack,
            final int targetStack,
            final StringBuilder acc) {
        acc.append(sourceStack)
                .append(' ').append(targetStack)
                .append(System.lineSeparator());
    }

    private static int readDisksCount() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }

    private static class TowerOfHanoi {

        private static final int LEFT_STACK_ID = 1;
        private static final int MIDDLE_STACK_ID = 2;
        private static final int RIGHT_STACK_ID = 3;

        private final int disksCount;

        TowerOfHanoi(final int disksCount) {
            this.disksCount = disksCount;
        }

        int[][] move() {
            final int movesCount = power(2, disksCount) - 1;

            final int[][] result = new int[movesCount][2];
            move(disksCount, LEFT_STACK_ID, MIDDLE_STACK_ID, RIGHT_STACK_ID, result, 0);

            return result;
        }

        private int power(final int base, final int degree) {
            return degree == 0 ? 1 : base * power(base, degree - 1);
        }

        private int move(
                final int diskNumber,
                final int leftStack,
                final int middleStack,
                final int rightStack,
                final int[][] moves,
                final int moveIndex) {

            if (diskNumber == 1) {
                moves[moveIndex] = new int[] { leftStack, rightStack };
                return moveIndex + 1;
            }

            int currentMoveIndex = move(diskNumber - 1, leftStack, rightStack, middleStack, moves, moveIndex);
            moves[currentMoveIndex++] = new int[] { leftStack, rightStack };
            return move(diskNumber - 1, middleStack, leftStack, rightStack, moves, currentMoveIndex);
        }
    }
}
