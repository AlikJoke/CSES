#include <cstdio>
#include <vector>
#include <climits>

using namespace std;

#define HAS_CYCLE -1

/**
 * High Score.
 * You play a game consisting of n rooms and m tunnels. Initial score is 0, and each tunnel increases your score by x where
 * x may be both positive or negative. You may go through a tunnel several times.
 * Task is to walk from room 1 to room n. What is the maximum score you can get?
 *
 * Input
 * The first input line has two integers n and m: the number of rooms and tunnels. The rooms are numbered 1,2,...,n.
 * Then, there are m lines describing the tunnels. Each line has three integers a, b and x: the tunnel starts at room a,
 * ends at room b, and it increases your score by x. All tunnels are one-way tunnels.
 * You can assume that it is possible to get from room 1 to room n.
 *
 * Output
 * Print one integer: the maximum score you can get. However, if you can get an arbitrarily large score, print -1.
 *
 * Constraints
 * 1 <= n <= 2500
 * 1 <= m <= 5000
 * 1 <= a,b <= n
 * -10^9 <= x <= 10^9
 *
 * Example
 * Input:
 * 4 5
 * 1 2 3
 * 2 4 -1
 * 1 3 -2
 * 3 4 7
 * 1 4 4
 *
 * Output:
 * 5
 *
 * @author Alik
 */
class GraphEdge {

    public:
        GraphEdge(unsigned int source, unsigned int destination, long weight) : source(source), destination(destination), weight(weight) {}

		unsigned int getSource() {
			return source;
		}

		unsigned int getDestination() {
			return destination;
		}

		long getWeight() {
		    return weight;
		}

    private:
        unsigned int source;
		unsigned int destination;
        long weight;
};

class GraphNode {
    public:
        GraphNode() : visited(false), finishReached(false) {}

		bool isFinishReached() {
		    return finishReached;
		}

		void setFinishReached(bool finishReached) {
		    this->finishReached = finishReached;
		}

		bool isVisited() {
		    return visited;
		}

		void setVisited(bool visited) {
		    this->visited = visited;
		}

		vector<GraphNode*> getLinkedNodes() {
			return linkedNodes;
		}

		void addLinkedNode(GraphNode* linkedNode) {
		    linkedNodes.push_back(linkedNode);
		}

    private:
        bool visited;
        bool finishReached;
		vector<GraphNode*> linkedNodes;
};

class HighScore {
	public:
		HighScore(vector<GraphNode*>& rooms, vector<GraphEdge*>& tunnels) : rooms(rooms), tunnels(tunnels) {}

        ~HighScore() {
            for (auto x : rooms) {
                delete x;
            }
            for (auto x : tunnels) {
                delete x;
            }
        }

		long computeMaxScore() {
            rooms[0]->setFinishReached(true);
		    for (GraphNode* room : rooms) {
		        dfs(room);
		    }

		    vector<long> maxScores(rooms.size(), LLONG_MIN);
		    for (unsigned int i = 1; i < rooms.size(); ++i) {
		        maxScores[i] = LLONG_MIN;
		    }

		    maxScores[0] = 0;

	        for (unsigned int i = 1; i < rooms.size(); ++i) {
	            fillScores(maxScores);
	        }

            long maxScore = maxScores[rooms.size() - 1];

            vector<long> initialMaxScores(maxScores);
			fillScores(maxScores);

            if (maxScores[rooms.size() - 1] != maxScore) {
                return HAS_CYCLE;
            }

			for (unsigned int i = 0; i < maxScores.size(); ++i) {
			    if (maxScores[i] != initialMaxScores[i] && rooms[i]->isFinishReached()) {
                    return HAS_CYCLE;
			    }
			}

			return maxScore;
		}

	private:
	    vector<GraphNode*> rooms;
		vector<GraphEdge*> tunnels;

		void fillScores(vector<long>& maxScores) {
		    for (GraphEdge* tunnel : tunnels) {
                unsigned int source = tunnel->getSource();
                unsigned int destination = tunnel->getDestination();
                long score = tunnel->getWeight();

                if (maxScores[source] == LLONG_MIN) {
                    continue;
                }

                long newScore = maxScores[source] + score;
                if (maxScores[destination] < newScore) {
                    maxScores[destination] = newScore;
                }
            }
		}

		void dfs(GraphNode* start) {
		    start->setVisited(true);

		    for (GraphNode* linkedNode : start->getLinkedNodes()) {

                if (linkedNode == rooms[rooms.size() - 1]) {
                    linkedNode->setFinishReached(true);
                }

                if (linkedNode->isFinishReached()) {
                    start->setFinishReached(true);
                }

		        if (linkedNode->isVisited()) {
                    continue;
                }

		        dfs(linkedNode);

		        if (linkedNode->isFinishReached()) {
		            start->setFinishReached(true);
		        }
		    }
		}
};

HighScore* createAlgorithm() {

    unsigned int roomsCount, tunnelsCount;
    scanf("%d %d", &roomsCount, &tunnelsCount);

    vector<GraphEdge*> tunnels(tunnelsCount);
    vector<GraphNode*> rooms(roomsCount);
    for (unsigned int i = 0; i < tunnelsCount; ++i) {
		unsigned int sourceRoom;
		unsigned int targetRoom;
		long score;
		scanf("%d %d %ld", &sourceRoom, &targetRoom, &score);

		tunnels[i] = new GraphEdge(sourceRoom - 1, targetRoom - 1, score);

		GraphNode* destination = rooms[targetRoom - 1];
		if (destination == nullptr) {
            destination = new GraphNode();
        	rooms[targetRoom - 1] = destination;
        }

        GraphNode* source = rooms[sourceRoom - 1];
		if (source == nullptr) {
		    source = new GraphNode();
		    rooms[sourceRoom - 1] = source;
		}

		source->addLinkedNode(destination);
    }

    return new HighScore(rooms, tunnels);
}

int main() {

    HighScore* algorithm = createAlgorithm();
    long maxScore = algorithm->computeMaxScore();

	printf("%ld\n", maxScore);

	delete algorithm;

    return 0;
}