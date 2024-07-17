#include <iostream>
#include <vector>
#include <stack>

using namespace std;

/**
 * Round Trip.
 * Byteland has n cities and m roads between them. Your task is to design a round trip that begins in a city, goes through two 
 * or more other cities, and finally returns to the starting city. Every intermediate city on the route has to be distinct.
 *
 * Input
 * The first input line has two integers n and m: the number of cities and roads. The cities are numbered 1,2,...,n.
 * Then, there are m lines describing the roads. Each line has two integers a and b: there is a road between those cities.
 * Every road is between two different cities, and there is at most one road between any two cities.
 *
 * Output
 * First print an integer k: the number of cities on the route. Then print k cities in the order they will be visited. You can print any valid solution.
 * If there are no solutions, print "IMPOSSIBLE".
 *
 * Constraints
 * 2 <= n <= 10^5
 * 1 <= m <= 2*10^5
 * 1 <= a,b <= n
 *
 * Example
 * Input:
 * 5 6
 * 1 3
 * 1 2
 * 5 3
 * 1 5
 * 2 4
 * 4 5
 *
 * Output:
 * 4
 * 3 5 1 3
 * 
 * @author Alik
 */
class CityNode {

    public:

        CityNode(int id) : id(id), visited(false), linkedCities(vector<CityNode*>()), visitedFrom(nullptr) {}

        void addlinkedCity(CityNode* linkedCity) {
            linkedCities.push_back(linkedCity);
        }

		vector<CityNode*>& getLinkedCities() {
			return linkedCities;
		}
		
		int getId() {
			return id;
		}
		
		bool isVisited() {
			return visited;
		}
		
		void setVisited(bool visited) {
			this->visited = visited;
		}
		
		void setVisitedFrom(CityNode* visitedFrom) {
			this->visitedFrom = visitedFrom;
		}
		
		CityNode* getVisitedFrom() {
			return visitedFrom;
		}
		
    private:
        int id;
		bool visited;
        vector<CityNode*> linkedCities;
		CityNode* visitedFrom;
};

class RoundTrip {
	public:
		RoundTrip(vector<CityNode*>& cities) : cities(cities) {}

		vector<int> computeCitiesCycleRoute() {
			for (CityNode* city : cities) {
				if (!city || city->isVisited()) {
					continue;
				}
				
				vector<int> cycleRoute = tryFindCycleRouteFromCity(city);
				if (!cycleRoute.empty()) {
					return cycleRoute;
				}
			}
			
			return vector<int>(0);
		}

		~RoundTrip() {
		    for (auto x : cities) {
			    delete x;
			}
		}

	private:
		vector<CityNode*> cities;

		vector<int> tryFindCycleRouteFromCity(CityNode* startCity) {

			stack<CityNode*> nextCity;
			nextCity.push(startCity);
			
			while (!nextCity.empty()) {
				CityNode* currentCity = nextCity.top();
				nextCity.pop();
				
				if (currentCity->isVisited() && currentCity->getVisitedFrom()) {
				    return createTracePath(currentCity);
				}
				
				currentCity->setVisited(true);
				
				for (CityNode* linkedCity : currentCity->getLinkedCities()) {
				    if (currentCity->getVisitedFrom() && currentCity->getVisitedFrom() == linkedCity) {
				        continue;
				    }
				    
				    if (linkedCity->isVisited() && linkedCity->getVisitedFrom()) {
						linkedCity->setVisitedFrom(currentCity);
				        return createTracePath(linkedCity);
				    }
					
					linkedCity->setVisitedFrom(currentCity);
				
					nextCity.push(linkedCity);
				}
			}
			
			return vector<int>(0);
		}
		
		vector<int> createTracePath(CityNode* targetCity) {
			vector<int> path;
			do {
				path.push_back(targetCity->getId());
				targetCity = targetCity->getVisitedFrom();
			} while (targetCity && targetCity->getId() != path[0]);
			
			path.push_back(path[0]);
			
			return path;
		}
};

RoundTrip* createAlgorithm() {

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
		
		firstCityNode->addlinkedCity(secondCityNode);
		secondCityNode->addlinkedCity(firstCityNode);
    }
	
    return new RoundTrip(cities);
}

void writeOutputResult(vector<int>& route) {
	if (route.empty()) {
		cout << "IMPOSSIBLE" << endl;
		return;
	}
	
	cout << route.size() << endl;
	for (auto cityIt = route.rbegin(); cityIt != route.rend(); ++cityIt) { 
        cout << *cityIt << ' '; 
    }
	
	cout << endl;
}

int main() {

    RoundTrip* algorithm = createAlgorithm();
    vector<int> route = algorithm->computeCitiesCycleRoute();

	writeOutputResult(route);

	delete algorithm;

    return 0;
}