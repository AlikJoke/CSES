#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * Labyrinth.
 * Given a map of a labyrinth, and task is to find a path from start to end. You can walk left, right, up and down.
 *
 * Input
 * The first input line has two integers n and m: the height and width of the map.
 * Then there are n lines of m characters describing the labyrinth. Each character is . (floor), # (wall), A (start), or B (end). 
 * There is exactly one A and one B in the input.
 *
 * Output
 * First print "YES", if there is a path, and "NO" otherwise.
 * If there is a path, print the length of the shortest such path and its description as a string consisting of characters 
 * L (left), R (right), U (up), and D (down). You can print any valid solution.
 *
 * Constraints
 * 1 <= n,m <= 1000
 *
 * Example
 * Input:
 * 5 8
 * ########
 * #.A#...#
 * #.##.#B#
 * #......#
 * ########
 *
 * Output:
 * YES
 * 9
 * LDDRRRRRU
 * @author Alik
 */
class GraphNode {

    public:

        enum GraphNodeType {
			FLOOR,
            WALL,
            START,
            END,
			UNKNOWN
        };

        GraphNode(char type) : type(getType(type)), relations(4, nullptr) {}

        void addRelation(GraphNode* relatedNode, int directionIndex) {
            relations[directionIndex] = relatedNode;
        }
		
		vector<GraphNode*>& getRelations() {
			return relations;
		}
		
		GraphNodeType getType() {
			return type;
		}
		
		void setType(GraphNodeType type) {
			this->type = type;
		}

    private:
        GraphNodeType type;
        vector<GraphNode*> relations;
		
		GraphNodeType getType(char type) {
            switch (type) {
                case '.': return FLOOR;
                case '#': return WALL;
                case 'A': return START;
                case 'B': return END;
                default: return UNKNOWN;
            }
        }
};

class Labyrinth {
	public:
		Labyrinth(vector<vector<GraphNode*>>& mapNodes, GraphNode* startNode, GraphNode* endNode) :
			mapNodes(mapNodes), startNode(startNode), endNode(endNode) {
			linkNodes();
		}

		string computeShortestPath() {
			if (startNode == nullptr || endNode == nullptr ||
				!hasFloorsOrEndpointBeside(startNode, endNode) ||
				!hasFloorsOrEndpointBeside(endNode, startNode)) {
				return "";
			}

			return findShortestPathByBfs();
		}
		
		~Labyrinth() {
		    for (auto x : mapNodes) {
				for (auto y : x) {
			        delete y;
				}
			}
		}

	private:
		vector<vector<GraphNode*>> mapNodes;
		GraphNode* startNode;
		GraphNode* endNode;

		bool hasFloorsOrEndpointBeside(GraphNode* startNode, GraphNode* endpointNode) {
			for (GraphNode* relation : startNode->getRelations()) {
				if (relation == nullptr) {
					continue;
				}

				if (relation->getType() == GraphNode::FLOOR || relation == endpointNode) {
					return true;
				}
			}

			return false;
		}
		
		string findShortestPathByBfs() {
			
			queue<GraphNode*> nextNode;
			queue<vector<char>> nextPath;
			
			nextNode.push(startNode);
			nextPath.push(vector<char>());
			
			startNode->setType(GraphNode::WALL);
			
			vector<char> singlePath;
				
			while (!nextNode.empty()) {
				GraphNode* fromNode = nextNode.front();
				vector<char> path = nextPath.front();
				
				nextNode.pop();
					
				int relationsCount = 0;
				for (unsigned int i = 0; i < fromNode->getRelations().size(); ++i) {
					GraphNode* relation = fromNode->getRelations()[i];
					if (relation == nullptr || relation->getType() == GraphNode::WALL) {
						continue;
					}
					
					++relationsCount;
				}
				
				bool needToPushPath = false;
				if (!nextNode.empty() || relationsCount > 1) {
					nextPath.pop();
					singlePath = path;
					needToPushPath = true;
				}
				
				for (unsigned int i = 0; i < fromNode->getRelations().size(); ++i) {
					GraphNode* relation = fromNode->getRelations()[i];
					if (relation == nullptr || relation->getType() == GraphNode::WALL) {
						continue;
					}
					
					char directionAlias = from(i);
					
					if (relation == endNode) {
						return string(singlePath.begin(), singlePath.end()) + directionAlias;
					} else {
						relation->setType(GraphNode::WALL);
						nextNode.push(relation);
						
						if (relationsCount == 1) {
							singlePath.push_back(directionAlias);
							if (needToPushPath) {
								nextPath.push(singlePath);
							}
						} else {
							vector<char> currentPath = vector<char>(path.begin(), path.end());
							currentPath.push_back(directionAlias);
							nextPath.push(currentPath);
						}
					}
				}
			}
			
			return "";
		}

		void linkNodes() {
			for (unsigned int i = 0; i < mapNodes.size(); ++i) {
				for (unsigned int j = 0; j < mapNodes[i].size(); ++j) {
					GraphNode* node = mapNodes[i][j];

					if (i < mapNodes.size() - 1) {
						GraphNode* linkedNode = mapNodes[i + 1][j];
						if (linkedNode->getType() != GraphNode::WALL) {
							node->addRelation(linkedNode, 2);
						}
					}

					if (i > 0) {
						GraphNode* linkedNode = mapNodes[i - 1][j];
						if (linkedNode->getType() != GraphNode::WALL) {
							node->addRelation(linkedNode, 3);
						}
					}

					if (j < mapNodes[i].size() - 1) {
						GraphNode* linkedNode = mapNodes[i][j + 1];
						if (linkedNode->getType() != GraphNode::WALL) {
							node->addRelation(linkedNode, 1);
						}
					}

					if (j > 0) {
						GraphNode* linkedNode = mapNodes[i][j - 1];
						if (linkedNode->getType() != GraphNode::WALL) {
							node->addRelation(linkedNode, 0);
						}
					}
				}
			}
		}

		char from(int index) {
			switch (index) {
				case 0: return 'L';
				case 1: return 'R';
				case 2: return 'D';
				case 3: return 'U';
				default: return '-';
			}
		}
};

Labyrinth* createAlgorithm() {

    int heightOfTheMap, widthOfTheMap;
    cin >> heightOfTheMap >> widthOfTheMap;

    vector<vector<GraphNode*>> nodes(heightOfTheMap, vector<GraphNode*>(widthOfTheMap));
    GraphNode* startNode = nullptr;
    GraphNode* endNode = nullptr;

    for (int i = 0; i < heightOfTheMap; ++i) {
        for (int j = 0; j < widthOfTheMap; ++j) {
            char mapItemType;
            cin >> mapItemType;
            nodes[i][j] = new GraphNode(mapItemType);

            if (nodes[i][j]->getType() == GraphNode::START) {
                startNode = nodes[i][j];
			} else if (nodes[i][j]->getType() == GraphNode::END) {
                endNode = nodes[i][j];
            }
        }
    }

    return new Labyrinth(nodes, startNode, endNode);
}

void writeOutputResult(string shortestPath) {
	if (shortestPath.empty()) {
		cout << "NO" << endl; 
	} else {
		cout << "YES" << endl << shortestPath.length() << endl << shortestPath << endl;
	}
}

int main() {
	
    Labyrinth* labyrinth = createAlgorithm();
    string shortestPath = labyrinth->computeShortestPath();

	writeOutputResult(shortestPath);
	
	delete labyrinth;

    return 0;
}
