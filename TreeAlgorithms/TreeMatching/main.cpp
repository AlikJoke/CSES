#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * Tree Matching.
 * Given a tree consisting of n nodes. The diameter of a tree is the maximum distance between two nodes.
 * Task is to determine the diameter of the tree.
 *
 * Input:
 * The first input line contains an integer n: the number of nodes. The nodes are numbered 1,2,...,n.
 * Then there are n-1 lines describing the edges. Each line contains two integers a and b: there is an edge between nodes a and b.
 * Output:
 * Print one integer: the diameter of the tree.
 * Constraints:
 * 1 <= n <= 2*10^5
 * 1 <= a,b <= n
 *
 * Example
 * Input:
 * 5
 * 1 2
 * 1 3
 * 3 4
 * 3 5
 * Output:
 * 3
 *
 * @author Alik
 */
class TreeNode {
    public:
        TreeNode(unsigned int id) : id(id), visited(false) {}

        void addEdge(TreeNode* child) {
            edges.push_back(child);
        }

        vector<TreeNode*>& getEdges() {
            return edges;
        }

        void setVisited(bool visited) {
            this->visited = visited;
        }

        bool isVisited() {
            return visited;
        }

        unsigned int getId() {
            return id;
        }

    private:
        unsigned int id;
        bool visited;
        vector<TreeNode*> edges;
};

class TreeMatching {
    public:
        TreeMatching(vector<TreeNode*>& treeNodes) : treeNodes(treeNodes) {}

        unsigned int computeNumberOfMatchingPairs() {
            if (treeNodes.size() == 1) {
                return 0;
            }

            return computeNumberOfMatchingPairs(treeNodes[0], nullptr);
        }

        ~TreeMatching() {
            for (auto x : treeNodes) {
                delete x;
            }
        }

    private:
        vector<TreeNode*> treeNodes;

        unsigned int computeNumberOfMatchingPairs(TreeNode* currentNode, TreeNode* prev) {

            unsigned int result = 0;

            for (TreeNode* node : currentNode->getEdges()) {

                if (node == prev) {
                    continue;
                }

                result += computeNumberOfMatchingPairs(node, currentNode);
            }

            if (prev && !currentNode->isVisited() && !prev->isVisited()) {
                result++;
                currentNode->setVisited(true);
                prev->setVisited(true);
            }

            return result;
        }
};

TreeMatching* createAlgorithm() {
    int nodesCount;
    cin >> nodesCount;

    vector<TreeNode*> nodes(nodesCount, nullptr);
    for (int i = 0; i < nodesCount - 1; ++i) {
        int nodeId;
        int linkedNodeId;
        cin >> nodeId >> linkedNodeId;

        TreeNode* node = nodes[nodeId - 1];
        if (node == nullptr) {
     	    node = new TreeNode(nodeId - 1);
        	nodes[nodeId - 1] = node;
        }

        TreeNode* linkedNode = nodes[linkedNodeId - 1];
        if (linkedNode == nullptr) {
     	    linkedNode = new TreeNode(linkedNodeId - 1);
        	nodes[linkedNodeId - 1] = linkedNode;
        }

        node->addEdge(linkedNode);
        linkedNode->addEdge(node);
    }

    return new TreeMatching(nodes);
}

int main() {
    TreeMatching* algorithm = createAlgorithm();
    unsigned int pairsCount = algorithm->computeNumberOfMatchingPairs();

    cout << pairsCount << endl;

    delete algorithm;

    return 0;
}