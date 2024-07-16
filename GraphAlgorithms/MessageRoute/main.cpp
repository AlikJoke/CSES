#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * Message Route.
 * Syrjälä's network has n computers and m connections. Task is to find out if Uolevi can send a message to Maija,
 * and if it is possible, what is the minimum number of computers on such a route.
 *
 * Input
 * The first input line has two integers n and m: the number of computers and connections.
 * Then, there are m lines describing the connections. Each line has two integers a and b: there is a connection between those computers.
 * Every connection is between two different computers, and there is at most one connection between any two computers.
 *
 * Output
 * If it is possible to send a message, first print k: the minimum number of computers on a valid route.
 * After this, print an example of such a route. Print any valid solution.
 * If there are no routes, print "IMPOSSIBLE".
 *
 * Constraints
 * 2 <= n <= 10^5
 * 1 <= m <= 2*10^5
 * 1 <= a,b <= n
 *
 * Example
 * Input:
 * 5 5
 * 1 2
 * 1 3
 * 1 4
 * 2 3
 * 5 4
 *
 * Output:
 * 3
 * 1 4 5
 * 
 * @author Alik
 */
class ComputerNode {

    public:

        ComputerNode(int id) : id(id), visited(false), relatedComputers(vector<ComputerNode*>()), visitedFrom(nullptr) {}

        void addRelatedComputer(ComputerNode* relatedComputer) {
            relatedComputers.push_back(relatedComputer);
        }

		vector<ComputerNode*>& getRelatedComputers() {
			return relatedComputers;
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
		
		void setVisitedFrom(ComputerNode* visitedFrom) {
			this->visitedFrom = visitedFrom;
		}
		
		ComputerNode* getVisitedFrom() {
			return visitedFrom;
		}

    private:
        int id;
		bool visited;
        vector<ComputerNode*> relatedComputers;
		ComputerNode* visitedFrom;
};

class MessageRoute {
	public:
		MessageRoute(vector<ComputerNode*>& computers) : computers(computers) {}

		vector<int> computeRouteToLastComputer() {
			if (computers[0] == nullptr) {
				return vector<int>(0);
			}
			return findPathToLastComputer(computers[0], computers.size());
		}

		~MessageRoute() {
		    for (auto x : computers) {
			    delete x;
			}
		}

	private:
		vector<ComputerNode*> computers;

		vector<int> findPathToLastComputer(ComputerNode* startComputer, int lastComputerId) {

			queue<ComputerNode*> nextComputer;
			queue<ComputerNode*> prevComputer;
			
			nextComputer.push(startComputer);
			
			while (!nextComputer.empty()) {
				ComputerNode* fromComputer = nextComputer.front();
				nextComputer.pop();
				
				ComputerNode* visitedFrom = prevComputer.front();
				if (visitedFrom) {
					prevComputer.pop();
				}
				
				if (fromComputer->isVisited()) {
					continue;
				}
				
				fromComputer->setVisited(true);
				fromComputer->setVisitedFrom(visitedFrom);
				
				if (fromComputer->getId() == lastComputerId) {
					return createTracePath(fromComputer);
				}
				
				for (ComputerNode* relatedComputer : fromComputer->getRelatedComputers()) {
					if (relatedComputer->isVisited()) {
						continue;
					}

					nextComputer.push(relatedComputer);
					prevComputer.push(fromComputer);
				}
			}
			
			return vector<int>(0);
		}
		
		vector<int> createTracePath(ComputerNode* lastComputerInPath) {
			vector<int> path;
			while (lastComputerInPath) {
				path.push_back(lastComputerInPath->getId());
				lastComputerInPath = lastComputerInPath->getVisitedFrom();
			}
			
			return path;
		}
};

MessageRoute* createAlgorithm() {

    int computersCount, connectionsCount;
    cin >> computersCount >> connectionsCount;

    vector<ComputerNode*> computers(computersCount, nullptr);
    for (int i = 0; i < connectionsCount; ++i) {
		int firstComputerId;
		int secondComputerId;
		cin >> firstComputerId >> secondComputerId;
		
		ComputerNode* firstComputerNode = computers[firstComputerId - 1];
		if (firstComputerNode == nullptr) {
			firstComputerNode = new ComputerNode(firstComputerId);
			computers[firstComputerId - 1] = firstComputerNode;
		}
		
		ComputerNode* secondComputerNode = computers[secondComputerId - 1];
		if (secondComputerNode == nullptr) {
			secondComputerNode = new ComputerNode(secondComputerId);
			computers[secondComputerId - 1] = secondComputerNode;
		}
		
		firstComputerNode->addRelatedComputer(secondComputerNode);
		secondComputerNode->addRelatedComputer(firstComputerNode);
    }
	
    return new MessageRoute(computers);
}

void writeOutputResult(vector<int>& route) {
	if (route.empty()) {
		cout << "IMPOSSIBLE" << endl;
		return;
	}
	
	cout << route.size() << endl;
	for (auto computerIt = route.rbegin(); computerIt != route.rend(); ++computerIt) { 
        cout << *computerIt << ' '; 
    }
	
	cout << endl;
}

int main() {

    MessageRoute* algorithm = createAlgorithm();
    vector<int> route = algorithm->computeRouteToLastComputer();

	writeOutputResult(route);

	delete algorithm;

    return 0;
}
