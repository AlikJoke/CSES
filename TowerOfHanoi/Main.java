import java.util.Scanner;

/**
 * <h1>Tower of Hanoi</h1><br/>
 * The Tower of Hanoi game consists of three stacks (left, middle and right) and {@literal n} round disks of
 * different sizes. Initially, the left stack has all the disks, in increasing order of size from top to bottom.<br/>
 * The goal is to move all the disks to the right stack using the middle stack. On each move you can move
 * the uppermost disk from a stack to another stack. In addition, it is not allowed to place a larger disk
 * on a smaller disk.<br/>
 * Task is to find a solution that minimizes the number of moves.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has an integer {@literal n}: the number of disks.<br/>
 * <i><b>Output</b></i>:<br/>
 * First print an integer {@literal k}: the minimum number of moves.<br/>
 * After this, print {@literal k} lines that describe the moves. Each line has two integers {@literal a} and {@literal b}:
 * move a disk from stack {@literal a} to stack {@literal b}.
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

    private static final int LEFT_STACK_ID = 1;
    private static final int MIDDLE_STACK_ID = 2;
    private static final int RIGHT_STACK_ID = 3;

    public static void main(String[] args) {
        final int disksCount = readDisksCount();

        final String result = move(disksCount);
        System.out.println(result);
    }

    private static int readDisksCount() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextInt();
        }
    }
    private static int power(final int base, final int degree) {
        return degree == 0 ? 1 : base * power(base, degree - 1);
    }

    private static String move(final int disksCount) {
        final int movesCount = power(2, disksCount) - 1;

        final StringBuilder acc = new StringBuilder(movesCount * 4 + 10);
        acc.append(movesCount).append(System.lineSeparator());

        move(disksCount, LEFT_STACK_ID, MIDDLE_STACK_ID, RIGHT_STACK_ID, acc);

        return acc.toString();
    }

    private static void move(
            final int diskNumber,
            final int leftStack,
            final int middleStack,
            final int rightStack,
            final StringBuilder acc) {

        if (diskNumber == 1) {
            appendMoveInfo(leftStack, rightStack, acc);
            return;
        }

        move(diskNumber - 1, leftStack, rightStack, middleStack, acc);
        appendMoveInfo(leftStack, rightStack, acc);
        move(diskNumber - 1, middleStack, leftStack, rightStack, acc);
    }

    private static void appendMoveInfo(
            final int sourceStack,
            final int targetStack,
            final StringBuilder acc) {
        acc.append(sourceStack)
                .append(' ').append(targetStack)
                .append(System.lineSeparator());
    }
}
