#include <vector>
#include <iostream>
using std::vector;

/**
 * Subordinates.
 * Given the structure of a company, task is to calculate for each employee the number of their subordinates.
 *
 * Input:
 * The first input line has an integer n: the number of employees.
 * The employees are numbered 1,2,...,n, and employee 1 is the general director of the company.
 * After this, there are n-1 integers: for each employee 2,3,...,n direct boss in the company.
 * Output:
 * Print n integers: for each employee 1,2,...,n the number of their subordinates.
 * Constraints:
 * 1 <= n <= 2*10^5
 *
 * Example
 * Input:
 * 5
 * 1 1 2 3
 * Output:
 * 4 1 1 0 0
 *
 * @author Alik
 * Note: I tried to do this in Java, but two tests failed with TLE.
 */
class Tree {
    
    class Node {
        public:
            int id;
            vector<Node*> children;
            
            Node(int id) : id(id) {}
    
            vector<Node*>& getChildren() {
                return children;
            }
    };
    
    public:
        Tree(int employeeCount) : nodes(employeeCount) {
            nodes[0] = new Node(0);
        }

        void addNode(int i, int parentId) {
            Node* node = findOrCreateNode(i);
            Node* parent = findOrCreateNode(parentId);
            parent->children.push_back(node);
        }

        vector<int> countNestedChildren() {
            vector<int> result(nodes.size(), 0);
            countNestedChildren(nodes[0], result);
            return result;
        }

        ~Tree() {
            for (auto x : nodes) {
                delete x;
            }
        }

    private:
        vector<Node*> nodes;

        int countNestedChildren(Node* startNode, vector<int>& counts) {
            if (startNode->children.empty()) {
                counts[startNode->id] = 0;
                return 0;
            }
    
            int result = 0;
            for (Node* child : startNode->children) {
                result += countNestedChildren(child, counts) + 1;
            }
    
            counts[startNode->id] = result;
            return result;
        }
    
        Node* findOrCreateNode(int id) {
            if (nodes[id] == nullptr) {
                nodes[id] = new Node(id);
            }
            return nodes[id];
        }
};

class Subordinates {
    public:
        Subordinates(Tree* tree) : tree(tree) {}

        vector<int> computeSubordinates() {
            return tree->countNestedChildren();
        }

        ~Subordinates() {
            delete tree;
        }

    private:
        Tree* tree;
};

void writeOutputResult(vector<int> result) {
    for (auto x : result) {
        std::cout << x << ' ';
    }
}

Subordinates* createAlgorithm() {
    int employeeCount;
    std::cin >> employeeCount;

    Tree* tree = new Tree(employeeCount);
    for (int i = 1; i < employeeCount; ++i) {
        int parent;
        std::cin >> parent;
        tree->addNode(i, parent - 1);
    }

    return new Subordinates(tree);
}

int main()
{
    Subordinates* algorithm = createAlgorithm();
    vector<int> result = algorithm->computeSubordinates();

    writeOutputResult(result);

    delete algorithm;

    return 0;
}