#include <cstdio>
#include <vector>
#include <string>
#include <algorithm>

using namespace std;

#define YES_ANSWER "YES"
#define NO_ANSWER "NO"

/**
 * Polygon Area.
 * Task is to calculate the area of a given polygon.
 * The polygon consists of n vertices (x_1,y_1),(x_2,y_2),...,(x_n,y_n). The vertices (x_i,y_i) and (x_{i+1},y_{i+1})
 * are adjacent for i=1,2,...,n-1, and the vertices (x_1,y_1) and (x_n,y_n) are also adjacent.
 *
 * Input
 * The first input line has an integer n: the number of vertices.
 * After this, there are n lines that describe the vertices. The ith such line has two integers x_i and y_i.
 * May assume that the polygon is simple, i.e., it does not intersect itself.
 *
 * Output
 * Print one integer: 2 * a where the area of the polygon is a (this ensures that the result is an integer).
 *
 * Constraints
 * 3 <= n <= 1000
 * -10^9 <= x_i,y_i <= 10^9
 *
 * Example
 * Input:
 * 4
 * 1 1
 * 4 2
 * 3 5
 * 1 4
 *
 * Output:
 * 16
 *
 * @author Alik
 */
class PolygonArea {
	public:
		PolygonArea(vector<vector<long>>& vertices) : vertices(vertices) {}

		long computeArea() {
		    // via Gauss area formula
		    long result = 0;
		    for (unsigned int i = 1; i < vertices.size() - 1; ++i) {
		        result += vertices[i][0] * (vertices[i + 1][1] - vertices[i - 1][1]);
		    }

			return abs(result);
		}

	private:
		vector<vector<long>> vertices;
};

PolygonArea* createAlgorithm() {

    unsigned int verticesCount;
    scanf("%d", &verticesCount);

    vector<vector<long>> vertices(verticesCount + 2, vector<long>(2));
    for (unsigned int i = 1; i < verticesCount + 1; ++i) {
        scanf("%ld %ld", &vertices[i][0], &vertices[i][1]);
    }

    vertices[0][1] = vertices[verticesCount][1];
    vertices[verticesCount + 1][1] = vertices[1][1];

    return new PolygonArea(vertices);
}

int main() {

    PolygonArea* algorithm = createAlgorithm();
    long area = algorithm->computeArea();

	printf("%ld", area);

	delete algorithm;

    return 0;
}