#include <iostream>
#include <vector>
using namespace std;

/**
 * Tree Diameter.
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
class TreeDiameter {
    public:
        TreeDiameter(vector<vector<int>>& adj) : adj(adj), onPath(adj.size(), 0) {}
    
        int computeDiameter() {
            if (adj[0].empty()) {
                return 0;
            }
    
            dfs(0, 1);
    
            pair<int, int> farthestFromRootNodeData = findFarthestNodeIndex();
    
            dfs(farthestFromRootNodeData.first, 1);
    
            return findFarthestNodeIndex().second;
        }
    
    private:
        vector<vector<int>>& adj;
        vector<int> onPath;
    
        pair<int, int> findFarthestNodeIndex() {
            int maxPathLength = 0;
            int farthestFromRootNodeIndex = 0;
            for (unsigned int i = 0; i < onPath.size(); ++i) {
                if (maxPathLength < onPath[i]) {
                    maxPathLength = onPath[i];
                    farthestFromRootNodeIndex = i;
                }
    
                onPath[i] = 0;
            }
    
            return make_pair(farthestFromRootNodeIndex, maxPathLength - 1);
        }
    
        void dfs(int currentNode, int level) {
            onPath[currentNode] = level;
    
            for (int relation : adj[currentNode]) {
                if (onPath[relation] != 0) {
                    continue;
                }
                dfs(relation, level + 1);
            }
        }
};


int main() {
    int nodesCount;
    cin >> nodesCount;
    
    vector<vector<int>> adj(nodesCount);
    for (int i = 1; i < nodesCount; ++i) {
        int node;
        int linkedNode;
        cin >> node >> linkedNode;
        
        adj[node - 1].push_back(linkedNode - 1);
        adj[linkedNode - 1].push_back(node - 1);
    }
 
    TreeDiameter algorithm(adj);
    int diameter = algorithm.computeDiameter();

    cout << diameter << endl;

    return 0;
}