#include <cstdio>
#include <vector>
#include <string>

using namespace std;

#define TOUCH "TOUCH"
#define LEFT "LEFT"
#define RIGHT "RIGHT"

/**
 * Point Location Test.
 * There is a line that goes through the points p_1=(x_1,y_1) and p_2=(x_2,y_2). There is also a point p_3=(x_3,y_3).
 * Task is to determine whether p_3 is located on the left or right side of the line or if it touches the line when
 * we are looking from p_1 to p_2.
 *
 * Input
 * The first input line has an integer t: the number of tests.
 * After this, there are t lines that describe the tests. Each line has six integers: x_1, y_1, x_2, y_2, x_3 and y_3.
 *
 * Output
 * For each test, print "LEFT", "RIGHT" or "TOUCH".
 *
 * Constraints
 * 1 <= t <= 10^5
 * -10^9 <= x_i,y_i,x_2,y_2,x_3,y_3 <= 10^9
 * x_1 != x_2 or y_1 != y_2
 *
 * Example
 * Input:
 * 3
 * 1 1 5 3 2 3
 * 1 1 5 3 4 1
 * 1 1 5 3 3 2
 *
 * Output:
 * LEFT
 * RIGHT
 * TOUCH
 *
 * @author Alik
 */
class PointLocationTest {
	public:
		PointLocationTest(vector<vector<long>>& coordinates) : coordinates(coordinates) {}

		vector<string> computeLocations() {
			vector<string> locations(coordinates.size());

			for (unsigned int i = 0; i < coordinates.size(); ++i) {
			    long xa = coordinates[i][0];
			    long ya = coordinates[i][1];

			    long xb = coordinates[i][2];
                long yb = coordinates[i][3];

                long x = coordinates[i][4];
                long y = coordinates[i][5];

                long result = (x - xa) * (yb - ya) - (y - ya) * (xb - xa);
                locations[i] = result == 0 ? TOUCH : result > 0 ? RIGHT : LEFT;
			}

			return locations;
		}

	private:
		vector<vector<long>> coordinates;
};

PointLocationTest* createAlgorithm() {

    unsigned int testsCount;
    scanf("%d", &testsCount);

    vector<vector<long>> coordinates(testsCount, vector<long>(6));
    for (unsigned int i = 0; i < testsCount; ++i) {
		scanf("%ld %ld %ld %ld %ld %ld", &coordinates[i][0], &coordinates[i][1], &coordinates[i][2], &coordinates[i][3], &coordinates[i][4], &coordinates[i][5]);
    }

    return new PointLocationTest(coordinates);
}

void writeOutputResult(vector<string> locations) {
	for (auto location : locations) {
        printf("%s\n", location.c_str());
    }
}

int main() {

    PointLocationTest* algorithm = createAlgorithm();
    vector<string> locations = algorithm->computeLocations();

	writeOutputResult(locations);

	delete algorithm;

    return 0;
}