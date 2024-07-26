#include <cstdio>
#include <vector>
#include <climits>

using namespace std;

#define NO_ROUTE -1

/**
 * Shortest Routes II.
 * There are n cities and m roads between them. Task is to process q queries where you have to determine 
 * the length of the shortest route between two given cities.
 *
 * Input
 * The first input line has three integers n, m and q: the number of cities, roads, and queries.
 * Then, there are m lines describing the roads. Each line has three integers a, b and c: there is a road between cities a and b whose length is c. 
 * All roads are two-way roads.
 * Finally, there are q lines describing the queries. Each line has two integers a and b: determine the length of the shortest route between cities a and b.
 *
 * Output
 * Print the length of the shortest route for each query. If there is no route, print -1 instead.
 *
 * Constraints
 * 1 <= n <= 500
 * 1 <= m <= n^2
 * 1 <= q <= 10^5
 * 1 <= a,b <= n
 * 1 <= c <= 10^9
 *
 * Example
 * Input:
 * 4 3 5
 * 1 2 5
 * 1 3 9
 * 2 3 3
 * 1 2
 * 2 1
 * 1 3
 * 1 4
 * 3 2
 *
 * Output:
 * 5
 * 5
 * 8
 * -1
 * 3
 * 
 * @author Alik
 */
class ShortestRoutesII {
	public:
		ShortestRoutesII(vector<vector<long>>& distances, vector<pair<unsigned int, unsigned int>>& queries) : distances(distances), queries(queries) {}

		vector<long> computeShortestRoutes() {
			for (unsigned int k = 0; k < distances.size(); ++k) {
			    for (unsigned int i = 0; i < distances.size(); ++i) {
			        if (distances[i][k] == LLONG_MAX) {
			            continue;
			        }

			        for (unsigned int j = 0; j < distances.size(); ++j) {
			            if (distances[k][j] == LLONG_MAX) {
                            continue;
                        }

			            if (distances[i][j] > distances[i][k] + distances[k][j]) {
			                distances[j][i] = distances[i][j] = distances[i][k] + distances[k][j];
			            }
			        }
			    }
	        }
			
			vector<long> requestedDistances(queries.size());
			for (unsigned int i = 0; i < queries.size(); ++i) {
				pair<int, int> query = queries[i];
				long distance = distances[query.first][query.second];
				requestedDistances[i] = distance == LLONG_MAX ? NO_ROUTE : distance;;
			}
			
			return requestedDistances;
		}

	private:
		vector<vector<long>> distances;
		vector<pair<unsigned int, unsigned int>> queries;
};

ShortestRoutesII* createAlgorithm() {

    unsigned int citiesCount, roadsCount, queriesCount;
    scanf("%d %d %d", &citiesCount, &roadsCount, &queriesCount);

    vector<vector<long>> distances(citiesCount, vector<long>(citiesCount, LLONG_MAX));
    for (unsigned int i = 0; i < citiesCount; ++i) {
	    distances[i][i] = 0;
	}

    for (unsigned int i = 0; i < roadsCount; ++i) {
		unsigned int firstCityId;
		unsigned int secondCityId;
		unsigned int distance;
		scanf("%d %d %d", &firstCityId, &secondCityId, &distance);

		distances[secondCityId - 1][firstCityId - 1] = distances[firstCityId - 1][secondCityId - 1] = distances[firstCityId - 1][secondCityId - 1] > distance ? distance : distances[firstCityId - 1][secondCityId - 1];
    }
	
	vector<pair<unsigned int, unsigned int>> queries(queriesCount);
    for (unsigned int i = 0; i < queriesCount; ++i) {
		int fromCity, toCity;
		scanf("%d %d", &fromCity, &toCity);
		queries[i] = { fromCity - 1, toCity - 1 };
	}
	
    return new ShortestRoutesII(distances, queries);
}

void writeOutputResult(vector<long>& distances) {
	for (auto distance : distances) {
		printf("%ld\n", distance);
	}
}

int main() {

    ShortestRoutesII* algorithm = createAlgorithm();
    vector<long> distances = algorithm->computeShortestRoutes();

	writeOutputResult(distances);

	delete algorithm;

    return 0;
}
