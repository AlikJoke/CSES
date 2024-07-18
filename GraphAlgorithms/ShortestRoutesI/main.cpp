#include <iostream>
#include <vector>
#include <queue>
#include <climits>

using namespace std;

/**
 * Shortest Routes I.
 * There are n cities and m flight connections between them. Task is to determine the length of the shortest route from Syrjälä to every city.
 *
 * Input
 * The first input line has two integers n and m: the number of cities and flight connections. The cities are numbered 1,2,...,n, and city 1 is Syrjälä.
 * After that, there are m lines describing the flight connections. Each line has three integers a, b and c: a flight begins at city a, 
 * ends at city b, and its length is c. Each flight is a one-way flight.
 * You can assume that it is possible to travel from Syrjälä to all other cities.
 *
 * Output
 * Print n integers: the shortest route lengths from Syrjälä to cities 1,2,...,n.
 * Then, print k lines that describe the new roads. Print any valid solution.
 *
 * Constraints
 * 1 <= n <= 10^5
 * 1 <= m <= 2*10^5
 * 1 <= a,b <= n
 * 1 <= c <= 10^9
 *
 * Example
 * Input:
 * 3 4
 * 1 2 6
 * 1 3 2
 * 3 2 3
 * 1 3 4
 *
 * Output:
 * 0 5 9
 * 
 * @author Alik
 */
class CityNode {

    public:

        CityNode(int id) : id(id), visited(false), relatedCities(vector<pair<CityNode*, int>>()) {}

        void addRelatedCity(CityNode* relatedCity, int distance) {
            relatedCities.push_back({ relatedCity, distance });
        }

		vector<pair<CityNode*, int>>& getRelatedCities() {
			return relatedCities;
		}
		
		int getId() {
			return id;
		}
		
		void setVisited(bool visited) {
		    this->visited = visited;
		}
		
		bool isVisited() {
		    return visited;
		}

    private:
        int id;
        bool visited;
        vector<pair<CityNode*, int>> relatedCities;
};

class ShortestRoutesI {
	public:
		ShortestRoutesI(vector<CityNode*>& cities) : cities(cities) {}

		vector<unsigned long> computeShortestRoutes() {
			vector<unsigned long> distances(cities.size());
			
			for (unsigned int i = 1; i < distances.size(); ++i) {
				distances[i] = LLONG_MAX;
			}
			
			distances[0] = 0;
			
			priority_queue<pair<unsigned long, int>, vector<pair<unsigned long, int>>, greater<pair<unsigned long, int>>> nextCityDistance;
			nextCityDistance.push({ 0, 0 });
			
			while (!nextCityDistance.empty()) {
				pair<unsigned long, int> currentCityDistance = nextCityDistance.top();
				nextCityDistance.pop();
				
				CityNode* currentCity = cities[currentCityDistance.second];
				if (currentCity -> isVisited()) {
					continue;
				}
				
                currentCity->setVisited(true);
				    
                unsigned long distanceToCurrentCity = distances[currentCityDistance.second];
				for (pair<CityNode*, int> linkedCityDistance : currentCity->getRelatedCities()) {
					int linkedCityId = linkedCityDistance.first->getId();
					
					unsigned long oldCityDistance = distances[linkedCityId];
					unsigned long newCityDistance = distanceToCurrentCity + linkedCityDistance.second;
					if (oldCityDistance > newCityDistance) {
						distances[linkedCityId] = newCityDistance;
						nextCityDistance.push({ newCityDistance, linkedCityId });
					}
				}
			}
			
			return distances;
		}

		~ShortestRoutesI() {
		    for (auto x : cities) {
			    delete x;
			}
		}

	private:
		vector<CityNode*> cities;
};

ShortestRoutesI* createAlgorithm() {

    int citiesCount, flightsCount;
    cin >> citiesCount >> flightsCount;

    vector<CityNode*> cities(citiesCount, nullptr);
    for (int i = 0; i < flightsCount; ++i) {
		int firstCityId;
		int secondCityId;
		int distance;
		cin >> firstCityId >> secondCityId >> distance;
		
		CityNode* firstCityNode = cities[firstCityId - 1];
		if (firstCityNode == nullptr) {
			firstCityNode = new CityNode(firstCityId - 1);
			cities[firstCityId - 1] = firstCityNode;
		}
		
		CityNode* secondCityNode = cities[secondCityId - 1];
		if (secondCityNode == nullptr) {
			secondCityNode = new CityNode(secondCityId - 1);
			cities[secondCityId - 1] = secondCityNode;
		}
		
		firstCityNode->addRelatedCity(secondCityNode, distance);
    }
	
    return new ShortestRoutesI(cities);
}

void writeOutputResult(vector<unsigned long>& distances) {
	for (auto distance : distances) {
		cout << distance << ' ';
	}
	
	cout << endl;
}

int main() {

    ShortestRoutesI* algorithm = createAlgorithm();
    vector<unsigned long> distances = algorithm->computeShortestRoutes();

	writeOutputResult(distances);

	delete algorithm;

    return 0;
}
