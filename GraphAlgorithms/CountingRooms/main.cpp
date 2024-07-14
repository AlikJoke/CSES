#include <vector>
#include <iostream>
using namespace std;

/**
 * Counting Rooms.
 * Given a map of a building, and task is to count the number of its rooms. The size of the map is n * m squares,
 * and each square is either floor or wall. You can walk left, right, up, and down through the floor squares.
 *
 * Input
 * The first input line has two integers n and m: the height and width of the map.
 * Then there are n lines of m characters describing the map. Each character is either . (floor) or # (wall).
 *
 * Output
 * Print one integer: the number of rooms.
 *
 * Constraints
 * 1 <= n,m <= 1000
 *
 * Example
 * Input:
 * 5 8
 * ########
 * #..#...#
 * ####.#.#
 * #..#...#
 * ########
 *
 * Output:
 * 3
 * @author Alik
 */
class GraphNode {
    public:
        GraphNode(bool floor) : floor(floor) {}

        void addRelation(GraphNode* relatedNode) {
            relations.push_back(relatedNode);
        }

        void setFloor(bool floor) {
            this->floor = floor;
        }

        bool isFloor() {
            return floor;
        }

        vector<GraphNode*>& getRelations() {
            return relations;
        }

    private:
        bool floor;
        vector<GraphNode*> relations;
};

class CountingRooms {
    public:
        CountingRooms(vector<vector<GraphNode*>>& mapNodes, int firstFloorLevel) :
            mapNodes(mapNodes), firstFloorLevel(firstFloorLevel) {
            linkNodes();
        }

        int computeRoomsCount() {
            if (firstFloorLevel == -1) {
                return 0;
            }

            int roomsCount = 0;
            for (unsigned int i = firstFloorLevel; i < mapNodes.size(); ++i) {
                for (unsigned int j = 0; j < mapNodes[i].size(); ++j) {
                    GraphNode* node = mapNodes[i][j];
                    if (node->isFloor()) {
                        roomsCount++;
                        traverseFloors(node);
                    }
                }
            }

            return roomsCount;
        }

    private:
        vector<vector<GraphNode*>>& mapNodes;
        int firstFloorLevel;

        void traverseFloors(GraphNode* nodeToProcessing) {
            nodeToProcessing->setFloor(false);
            for (GraphNode* relation : nodeToProcessing->getRelations()) {
                if (relation->isFloor()) {
                    traverseFloors(relation);
                }
            }
        }

        void linkNodes() {
            for (unsigned int i = 0; i < mapNodes.size(); ++i) {
                for (unsigned int j = 0; j < mapNodes[i].size(); ++j) {
                    GraphNode* node = mapNodes[i][j];

                    if (i < mapNodes.size() - 1) {
                        GraphNode* linkedNode = mapNodes[i + 1][j];
                        if (linkedNode->isFloor()) {
                            node->addRelation(linkedNode);
                        }
                    }

                    if (i > 0) {
                        GraphNode* linkedNode = mapNodes[i - 1][j];
                        if (linkedNode->isFloor()) {
                            node->addRelation(linkedNode);
                        }
                    }

                    if (j < mapNodes[i].size() - 1) {
                        GraphNode* linkedNode = mapNodes[i][j + 1];
                        if (linkedNode->isFloor()) {
                            node->addRelation(linkedNode);
                        }
                    }

                    if (j > 0) {
                        GraphNode* linkedNode = mapNodes[i][j - 1];
                        if (linkedNode->isFloor()) {
                            node->addRelation(linkedNode);
                        }
                    }
                }
            }
        }
};

int main()
{
    int heightOfTheMap;
    cin >> heightOfTheMap;

    int widthOfTheMap;
    cin >> widthOfTheMap;

    vector<vector<GraphNode*>> mapNodes(heightOfTheMap, vector<GraphNode*>(widthOfTheMap));

    int firstFloorLevel = -1;
    for (int i = 0; i < heightOfTheMap; ++i) {
        for (int j = 0; j < widthOfTheMap; ++j) {
            char mapItem;
            cin >> mapItem;

            bool isFloor = mapItem == '.';

            mapNodes[i][j] = new GraphNode(isFloor);
            if (isFloor && firstFloorLevel == -1) {
                firstFloorLevel = i;
            }
        }
    }

    CountingRooms algorithm(mapNodes, firstFloorLevel);
    int roomsCount = algorithm.computeRoomsCount();

    cout << roomsCount << endl;

    for (auto x : mapNodes) {
        for (auto y : x) {
            delete y;
        }
    }

    return 0;
}