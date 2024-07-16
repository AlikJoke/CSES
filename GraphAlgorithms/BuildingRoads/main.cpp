#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * Building Roads.
 * Byteland has n cities, and m roads between them. The goal is to construct new roads so that there is a route between any two cities.
 * Task is to find out the minimum number of roads required, and also determine which roads should be built.
 *
 * Input
 * The first input line has two integers n and m: the number of cities and roads. The cities are numbered 1,2,...,n.
 * After that, there are m lines describing the roads. Each line has two integers a and b: there is a road between those cities.
 * A road always connects two different cities, and there is at most one road between any two cities.
 *
 * Output
 * First print an integer k: the number of required roads.
 * Then, print k lines that describe the new roads. Print any valid solution.
 *
 * Constraints
 * 1 <= n <= 10^5
 * 1 <= m <= 2*10^5
 * 1 <= a,b <= n
 *
 * Example
 * Input:
 * 4 2
 * 1 2
 * 3 4
 *
 * Output:
 * 1
 * 2 3
 * 
 * @author Alik
 */
class CityNode {

    public:

        CityNode(int id) : id(id), visited(false), relatedCities(vector<CityNode*>()) {}

        void addRelatedCity(CityNode* relatedCity) {
            relatedCities.push_back(relatedCity);
        }

		vector<CityNode*>& getRelatedCities() {
			return relatedCities;
		}
		
		bool isVisited() {
			return visited;
		}
		
		void setVisited(bool visited) {
			this->visited = visited;
		}
		
		int getId() {
			return id;
		}

    private:
        int id;
		bool visited;
        vector<CityNode*> relatedCities;
};

class BuildingRoads {
	public:
		BuildingRoads(vector<CityNode*>& cities) : cities(cities) {}

		vector<pair<int, int>> computeConnectingRoads() {
			vector<pair<int, int>> connectingRoads;
			int lastVisitedCityId = -1;
			for (unsigned int i = 0; i < cities.size(); ++i) {
				CityNode* city = cities[i];
				
				if (city == nullptr) {
					if (lastVisitedCityId != -1) {
						connectingRoads.push_back({ lastVisitedCityId, i + 1 });
					}
					lastVisitedCityId = i + 1;
					continue;
				}
				
				if (city->isVisited()) {
					continue;
				}
				
				visitLinkedCities(city);
				
				if (lastVisitedCityId != -1) {
					connectingRoads.push_back({ lastVisitedCityId, city->getId() });
				}
				
				lastVisitedCityId = city->getId();
			}
			
			return connectingRoads;
		}

		~BuildingRoads() {
		    for (auto x : cities) {
			    delete x;
			}
		}

	private:
		vector<CityNode*> cities;

		void visitLinkedCities(CityNode* city) {

			queue<CityNode*> nextCity;

			nextCity.push(city);

			while (!nextCity.empty()) {
				CityNode* fromCity = nextCity.front();
				nextCity.pop();
				
				if (fromCity->isVisited()) {
					continue;
				}
				
				fromCity->setVisited(true);

				for (CityNode* relatedCity : fromCity->getRelatedCities()) {
					if (relatedCity->isVisited()) {
						continue;
					}
					
					nextCity.push(relatedCity);
				}
			}
		}
};

BuildingRoads* createAlgorithm() {

    int citiesCount, roadsCount;
    cin >> citiesCount >> roadsCount;

    vector<CityNode*> cities(citiesCount, nullptr);
    for (int i = 0; i < roadsCount; ++i) {
		int firstCityId;
		int secondCityId;
		cin >> firstCityId >> secondCityId;
		
		CityNode* firstCityNode = cities[firstCityId - 1];
		if (firstCityNode == nullptr) {
			firstCityNode = new CityNode(firstCityId);
			cities[firstCityId - 1] = firstCityNode;
		}
		
		CityNode* secondCityNode = cities[secondCityId - 1];
		if (secondCityNode == nullptr) {
			secondCityNode = new CityNode(secondCityId);
			cities[secondCityId - 1] = secondCityNode;
		}
		
		firstCityNode->addRelatedCity(secondCityNode);
		secondCityNode->addRelatedCity(firstCityNode);
    }
	
    return new BuildingRoads(cities);
}

void writeOutputResult(vector<pair<int, int>>& connectingRoads) {
	cout << connectingRoads.size() << endl;
	for (unsigned int i = 0; i < connectingRoads.size(); ++i) {
		pair<int, int> road = connectingRoads[i];
		cout << road.first << ' ' << road.second << endl;
	}
}

int main() {

    BuildingRoads* algorithm = createAlgorithm();
    vector<pair<int, int>> connectingRoads = algorithm->computeConnectingRoads();

	writeOutputResult(connectingRoads);

	delete algorithm;

    return 0;
}
