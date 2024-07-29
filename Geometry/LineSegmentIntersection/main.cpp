#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

#define YES_ANSWER "YES"
#define NO_ANSWER "NO"

/**
 * Line Segment Intersection.
 * There are two line segments: the first goes through the points (x_1,y_1) and (x_2,y_2), and the second goes
 * through the points (x_3,y_3) and (x_4,y_4).
 * Task is to determine if the line segments intersect, i.e., they have at least one common point.
 *
 * Input
 * The first input line has an integer t: the number of tests.
 * After this, there are t lines that describe the tests. Each line has eight integers x_1, y_1, x_2, y_2, x_3, y_3, x_4 and y_4.
 *
 * Output
 * For each test, print "YES" if the line segments intersect and "NO" otherwise.
 *
 * Constraints
 * 1 <= t <= 10^5
 * -10^9 <= x_i,y_i,x_2,y_2,x_3,y_3,x_4,y_4 <= 10^9
 * (x_1,y_1) != (x_2,y_2)
 * (x_3,y_3) != (x_4,y_4)
 *
 * Example
 * Input:
 * 5
 * 1 1 5 3 1 2 4 3
 * 1 1 5 3 1 1 4 3
 * 1 1 5 3 2 3 4 1
 * 1 1 5 3 2 4 4 1
 * 1 1 5 3 3 2 7 4
 *
 * Output:
 * NO
 * YES
 * YES
 * YES
 * YES
 *
 * @author Alik
 */
class LineSegmentIntersection {
	public:
		LineSegmentIntersection(vector<vector<long>>& coordinates) : coordinates(coordinates) {}

		vector<bool> compute() {
			vector<bool> intersections(coordinates.size());

			for (unsigned int i = 0; i < coordinates.size(); ++i) {
			    intersections[i] = isIntersected(coordinates[i]);
			}

			return intersections;
		}

	private:
		vector<vector<long>> coordinates;

		bool isIntersected(vector<long> linesCoordinates) {
		    long fxa = linesCoordinates[0];
            long fya = linesCoordinates[1];

            long fxb = linesCoordinates[2];
            long fyb = linesCoordinates[3];

            long sxa = linesCoordinates[4];
            long sya = linesCoordinates[5];

            long sxb = linesCoordinates[6];
            long syb = linesCoordinates[7];

            if (fxa == sxa && fxa == fxb && sxa == sxb) {
                return isOnLine(fya, sya, syb) || isOnLine(fyb, sya, syb) || isOnLine(sya, fya, fyb) || isOnLine(syb, fya, fyb);
            } else if (fya == sya && fya == fyb && sya == syb) {
                return isOnLine(fxa, sxa, sxb) || isOnLine(fxb, sxa, sxb) || isOnLine(sxa, fxa, fxb) || isOnLine(sxb, fxa, fxb);
            }

            long v1 = (fxb - fxa) * (syb - fya) - (fyb - fya) * (sxb - fxa);
            long v2 = (fxb - fxa) * (sya - fya) - (fyb - fya) * (sxa - fxa);

            long v3 = (sxb - sxa) * (fyb - sya) - (syb - sya) * (fxb - sxa);
            long v4 = (sxb - sxa) * (fya - sya) - (syb - sya) * (fxa - sxa);

            if (v1 == 0 && isIntersectedInBound({ fxa, fya }, { fxb, fyb }, { sxb, syb })
                || v2 == 0 && isIntersectedInBound({ fxa, fya }, { fxb, fyb }, { sxa, sya })
                || v3 == 0 && isIntersectedInBound({ sxa, sya }, { sxb, syb }, { fxb, fyb })
                || v4 == 0 && isIntersectedInBound({ sxa, sya }, { sxb, syb }, { fxa, fya })) {
                return true;
            }

            return (signum(v1) * signum(v2)) < 0 && (signum(v3) * signum(v4)) < 0;
		}

		bool isOnLine(long pointCoordinate, long startLineCoordinate, long endLineCoordinate) {
		    return pointCoordinate <= max(startLineCoordinate, endLineCoordinate) && pointCoordinate >= min(startLineCoordinate, endLineCoordinate);
		}

		int signum(long v) {
		    return (v > 0) ? 1 : ((v < 0) ? -1 : 0);
		}

		bool isIntersectedInBound(pair<long, long> a, pair<long, long> b, pair<long, long> p) {
            vector<pair<long, long>> v = { a, b, p };
            sort(
                v.begin(),
                v.end(),
                [](const pair<long, long> &p1, const pair<long, long> &p2) {
                    return p1.first == p2.first ? (p1.second < p2.second) : (p1.first < p2.first);
                }
            );
            return v[1] == p;
        }
};

LineSegmentIntersection* createAlgorithm() {

    unsigned int testsCount;
    scanf("%d", &testsCount);

    vector<vector<long>> coordinates(testsCount, vector<long>(8));
    for (unsigned int i = 0; i < testsCount; ++i) {
        for (unsigned int j = 0; j < 8; ++j) {
            scanf("%ld", &coordinates[i][j]);
	    }
    }

    return new LineSegmentIntersection(coordinates);
}

void writeOutputResult(vector<bool> intersections) {
	for (auto intersection : intersections) {
        printf("%s\n", intersection ? YES_ANSWER : NO_ANSWER);
    }
}

int main() {

    LineSegmentIntersection* algorithm = createAlgorithm();
    vector<bool> intersections = algorithm->compute();

	writeOutputResult(intersections);

	delete algorithm;

    return 0;
}