import java.util.Scanner;

/**
 * <h1>Grid Paths</h1><br/>
 * There are {@literal 88418} paths in a {@literal 7x7} grid from the upper-left square to the lower-left square.
 * Each path corresponds to a 48-character description consisting of characters {@literal D} (down), {@literal U} (up),
 * {@literal L} (left) and {@literal R} (right).<br/>
 * For example, the path<br/>
 * <img src="https://cses.fi/file/3624b569007eb03818b6611755f1bdbe4cdbd0a8334baf9fedc5365914bdd661"/><br/>
 * corresponds to the description {@literal DRURRRRRDDDLUULDDDLDRRURDDLLLLLURULURRUULDLLDDDD}.<br/>
 * Given a description of a path which may also contain characters {@literal ?} (any direction). Task is to calculate the
 * number of paths that match the description.<br/><br/>
 *
 * <i><b>Input</b></i>:<br/>
 * The only input line has a 48-character string of characters {@literal ?}, {@literal D}, {@literal U},
 * {@literal L} and {@literal R}.<br/>
 * <i><b>Output</b></i>:<br/>
 * Print one integer: the total number of paths.<br/><br/>
 *
 * <b>Example</b><br/>
 * <i>Input</i>:<br/>
 * {@literal ??????R??????U??????????????????????????LD????D?}<br/>
 * <i>Output</i>:<br/>
 * {@literal 201}
 *
 * @author Alik
 *
 * @implNote The implementation is based on a recursive traversal of the entire mesh, taking into account various
 * optimizations that reduce the number of recursive calls.
 */
public class Main {

    private static final int GRID_BOUND_INDEX = 6;
    private static final int PATH_LENGTH = 48;

    private static final char RIGHT_DIRECTION = 'R';
    private static final char LEFT_DIRECTION = 'L';
    private static final char DOWN_DIRECTION = 'D';
    private static final char UP_DIRECTION = 'U';
    private static final char MASK_DIRECTION = '?';

    public static void main(String[] args) {
        final String pathsMask = readPathsMask();
        final int paths = traversePaths(pathsMask);
        System.out.println(paths);
    }

    private static int traversePaths(final String pathsMask) {
        return traversePaths(0, 0, 0, new boolean[GRID_BOUND_INDEX + 1][GRID_BOUND_INDEX + 1], pathsMask);
    }

    private static int traversePaths(
            final int x,
            final int y,
            final int currentPathStep,
            final boolean[][] visitedSections,
            final String pathsMask) {

        if (currentPathStep == PATH_LENGTH) {
            return isEndReached(x, y) ? 1 : 0;
        }

        final char directionFromMask = pathsMask.charAt(currentPathStep);

        final int nextPathStep = currentPathStep + 1;

        if (directionFromMask != MASK_DIRECTION) {
            return traverse(x, y, directionFromMask, visitedSections, pathsMask, nextPathStep);
        } else if (y > 2 && visitedSections[x][y - 2] && !visitedSections[x][y - 1]
                && (x > 0 && visitedSections[x - 1][y - 1] || x < GRID_BOUND_INDEX && visitedSections[x + 1][y - 1])) {
            return traverse(x, y, UP_DIRECTION, visitedSections, pathsMask, nextPathStep);
        } else if (x > 0 && y < 5 && visitedSections[x][y + 2] && !visitedSections[x][y + 1]
                && (visitedSections[x - 1][y + 1] || visitedSections[x + 1][y + 1])){
            return traverse(x, y, DOWN_DIRECTION, visitedSections, pathsMask, nextPathStep);
        } else if (y > 0 && x > 2 && visitedSections[x - 2][y] && !visitedSections[x - 1][y] && visitedSections[x - 1][y - 1]) {
            return traverse(x, y, LEFT_DIRECTION, visitedSections, pathsMask, nextPathStep);
        } else {
            return traverse(x, y, RIGHT_DIRECTION, visitedSections, pathsMask, nextPathStep)
                    + traverse(x, y, LEFT_DIRECTION, visitedSections, pathsMask, nextPathStep)
                    + traverse(x, y, UP_DIRECTION, visitedSections, pathsMask, nextPathStep)
                    + traverse(x, y, DOWN_DIRECTION, visitedSections, pathsMask, nextPathStep);
        }
    }

    private static int traverse(
            final int x,
            final int y,
            final char direction,
            final boolean[][] visitedSections,
            final String pathsMask,
            final int nextPathStep) {
        visitedSections[x][y] = true;

        int result = 0;
        switch (direction) {
            case DOWN_DIRECTION: {
                final int nextY;
                if (y < GRID_BOUND_INDEX && canVisit(x, (nextY = y + 1), visitedSections, nextPathStep)) {
                    result = traversePaths(x, nextY, nextPathStep, visitedSections, pathsMask);
                }
                break;
            }
            case UP_DIRECTION: {
                final int nextY;
                if (y > 0 && y <= GRID_BOUND_INDEX && canVisit(x, (nextY = y - 1), visitedSections, nextPathStep)) {
                    result = traversePaths(x, nextY, nextPathStep, visitedSections, pathsMask);
                }
                break;
            }
            case RIGHT_DIRECTION: {
                final int nextX;
                if (x < GRID_BOUND_INDEX && canVisit((nextX = x + 1), y, visitedSections, nextPathStep)) {
                    result = traversePaths(nextX, y, nextPathStep, visitedSections, pathsMask);
                }
                break;
            }
            case LEFT_DIRECTION: {
                final int nextX;
                if (x > 0 && x <= GRID_BOUND_INDEX && canVisit((nextX = x - 1), y, visitedSections, nextPathStep)) {
                    result = traversePaths(nextX, y, nextPathStep, visitedSections, pathsMask);
                }
                break;
            }
        }

        visitedSections[x][y] = false;
        return result;
    }

    private static boolean isEndReached(final int x, final int y) {
        return y == GRID_BOUND_INDEX && x == 0;
    }

    private static boolean canVisit(final int x, final int y, final boolean[][] visitedSections, final int currentPathSection) {
        if (visitedSections[x][y] || isEndReached(x, y) && currentPathSection != PATH_LENGTH) {
            return false;
        }

        return !(x == GRID_BOUND_INDEX && (y > 0 && !visitedSections[x][y - 1] || y == 0 && visitedSections[x][y + 1])
                || x == 0 && y < GRID_BOUND_INDEX && visitedSections[x][y + 1]
                || y == 0 && (x > 0 && !visitedSections[x - 1][y])
                || y > 0 && x == GRID_BOUND_INDEX - 1 && !visitedSections[x + 1][y - 1] && visitedSections[x][y - 1]
                || y == GRID_BOUND_INDEX && (x < GRID_BOUND_INDEX && !visitedSections[x + 1][y] || x == GRID_BOUND_INDEX && visitedSections[x - 1][y]));
    }

    private static String readPathsMask() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }
}
