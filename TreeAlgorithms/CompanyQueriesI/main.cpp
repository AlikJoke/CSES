#include <iostream>
#include <vector>
#include <cmath>
#include <stack>

using namespace std;

#define MAX_DEPTH 20

/**
 * Company Queries I.
 * A company has n employees, who form a tree hierarchy where each employee has a boss, except for the general director.
 * Task is to process q queries of the form: who is employee x's boss k levels higher up in the hierarchy?
 *
 * Input:
 * The first input line has two integers n and q: the number of employees and queries.
 * The employees are numbered 1,2,...,n, and employee 1 is the general director.
 * The next line has n-1 integers e_2,e_3,...,e_n: for each employee 2,3,...,n their boss.
 * Finally, there are q lines describing the queries. Each line has two integers x and k:
 * who is employee x's boss k levels higher up?
 * Output:
 * Print the answer for each query. If such a boss does not exist, print -1.
 * Constraints:
 * 1 <= n,q <= 2*10^5
 * 1 <= e_i <= i - 1
 * 1 <= x, k <= n
 *
 * Example
 * Input:
 * 5 3
 * 1 1 3 3
 * 4 1
 * 4 2
 * 4 3
 * Output:
 * 3
 * 1
 * -1
 *
 * @author Alik
 */
class TreeNode {
    public:
        TreeNode(unsigned int id) : id(id), parent(nullptr) {}

        unsigned int getId() {
            return id;
        }

        void setParent(TreeNode* parent) {
            this->parent = parent;
        }

        TreeNode* getParent() {
            return parent;
        }

    private:
        unsigned int id;
        TreeNode* parent;
};

class CompanyQueriesI {
    public:
        CompanyQueriesI(vector<TreeNode*>& employees, vector<pair<unsigned int, unsigned int>>& queries) : employees(employees), queries(queries) {}

        vector<int> executeQueries() {
            vector<vector<TreeNode*>> parents(employees.size(), vector<TreeNode*>(MAX_DEPTH, nullptr));
            fillParentsTable(parents);

            return executeQueries(parents);
        }

        ~CompanyQueriesI() {
            for (auto x : employees) {
                delete x;
        }
    }

    private:
        vector<TreeNode*> employees;
        vector<pair<unsigned int, unsigned int>> queries;

        vector<int> executeQueries(vector<vector<TreeNode*>>& parents) {
            vector<int> bosses(queries.size(), -1);
            pair<unsigned int, unsigned int> lastQuery;

            for (unsigned int i = 0; i < queries.size(); ++i) {
                pair<unsigned int, unsigned int> query = queries[i];
                if (lastQuery == query) {
                    bosses[i] = bosses[i - 1];
                    continue;
                }

                TreeNode* employee = employees[query.first];

                TreeNode* targetBoss = findBossInHierarchy(parents, employee, query.second);
                bosses[i] = targetBoss ? targetBoss->getId() : -1;

                lastQuery = query;
            }

            return bosses;
        }

        TreeNode* findBossInHierarchy(vector<vector<TreeNode*>>& parents, TreeNode* employee, int level) {

            TreeNode* currentEmployee = employee;
            for (unsigned int i = 0; i < MAX_DEPTH && currentEmployee; ++i) {
                unsigned int distance = 1 << i;
                if (level & distance) {
                    currentEmployee = parents[currentEmployee->getId()][i];
                }
            }

            return currentEmployee == employee ? nullptr : currentEmployee;
        }

        void fillParentsTable(vector<vector<TreeNode*>>& parents) {

            for (TreeNode* currentEmployee : employees) {
                if (!currentEmployee) {
                    continue;
                }

                parents[currentEmployee->getId()][0] = currentEmployee->getParent();
                bool isRoot = !parents[currentEmployee->getId()][0];
                for (int i = 1; i < MAX_DEPTH; ++i) {
                    TreeNode* parent = isRoot ? nullptr : parents[currentEmployee->getId()][i - 1];
                    parents[currentEmployee->getId()][i] = parent ? parents[parent->getId()][i - 1] : nullptr;
                }
            }
        }
};

 CompanyQueriesI* createAlgorithm() {
     unsigned int employeeCount, queriesCount;
     cin >> employeeCount >> queriesCount;

     vector<TreeNode*> employees(employeeCount + 1, nullptr);
     for (unsigned int i = 2; i <= employeeCount; ++i) {
         int parent;
         cin >> parent;

         TreeNode* employee = employees[i];
         if (employee == nullptr) {
             employee = new TreeNode(i);
             employees[i] = employee;
         }

         TreeNode* parentNode = employees[parent];
         if (parentNode == nullptr) {
             parentNode = new TreeNode(parent);
             employees[parent] = parentNode;
         }

         employee->setParent(parentNode);
     }

     vector<pair<unsigned int, unsigned int>> queries(queriesCount);
     for (unsigned int i = 0; i < queriesCount; ++i) {
         unsigned int employee, level;
         cin >> employee >> level;
         queries[i] = { employee, level };
     }

     return new CompanyQueriesI(employees, queries);
 }

 void writeOutputResult(vector<int>& hierarchyBosses) {
 	for (int hierarchyBoss : hierarchyBosses) {
         cout << hierarchyBoss << endl;
     }
 }

 int main() {
     CompanyQueriesI* algorithm = createAlgorithm();
     vector<int> hierarchyBosses = algorithm->executeQueries();

     writeOutputResult(hierarchyBosses);

     delete algorithm;

     return 0;
 }