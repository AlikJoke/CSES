#include <iostream>
#include <vector>
#include <queue>

using namespace std;

/**
 * Building Teams.
 * There are n pupils in Uolevi's class, and m friendships between them. Task is to divide the pupils
 * into two teams in such a way that no two pupils in a team are friends. You can freely choose the sizes of the teams.
 *
 * Input
 * The first input line has two integers n and m: the number of pupils and friendships. The pupils are numbered 1,2,...,n.
 * Then, there are m lines describing the friendships. Each line has two integers a and b: pupils a and b are friends.
 * Every friendship is between two different pupils. You can assume that there is at most one friendship between any two pupils.
 *
 * Output
 * Print an example of how to build the teams. For each pupil, print "1" or "2" depending on to which team the
 * pupil will be assigned. Print any valid team.
 * If there are no solutions, print "IMPOSSIBLE".
 *
 * Constraints
 * 1 <= n <= 10^5
 * 1 <= m <= 2*10^5
 * 1 <= a,b <= n
 *
 * Example
 * Input:
 * 5 3
 * 1 2
 * 1 3
 * 4 5
 *
 * Output:
 * 1 2 2 1 2
 * 
 * @author Alik
 */
class PupilNode {

    public:
        PupilNode(int id) : id(id), team(0), friends(vector<PupilNode*>()) {}

        void addFriend(PupilNode* pupilFriend) {
            friends.push_back(pupilFriend);
        }

		vector<PupilNode*>& getFriends() {
			return friends;
		}
		
		int getTeam() {
			return team;
		}
		
		void setTeam(int team) {
			this->team = team;
		}
		
		int getId() {
			return id;
		}

    private:
        int id;
		int team;
        vector<PupilNode*> friends;
};

class BuildingTeams {
	public:
		BuildingTeams(vector<PupilNode*>& pupils) : pupils(pupils) {
			initPupilsWithoutFriends();
		}

		vector<PupilNode*> divideByTeams() {
			for (PupilNode* pupil : pupils) {
				if (pupil->getTeam() != 0) {
					continue;
				}
				
				if (!assignPupilAndHisFriendsToTeams(pupil)) {
					return vector<PupilNode*>(0, nullptr);
				}
			}
			return pupils;
		}

		~BuildingTeams() {
		    for (auto x : pupils) {
			    delete x;
			}
		}

	private:
		vector<PupilNode*> pupils;
		
		void initPupilsWithoutFriends() {
			for (unsigned int i = 0; i < pupils.size(); ++i) {
				if (!pupils[i]) {
					pupils[i] = new PupilNode(i + 1);
				}
			}
		}

		bool assignPupilAndHisFriendsToTeams(PupilNode* pupil) {

			queue<PupilNode*> nextPupil;
			nextPupil.push(pupil);
			
			pupil->setTeam(1);
				
			while (!nextPupil.empty()) {
				PupilNode* currentPupil = nextPupil.front();
				nextPupil.pop();
				
				for (PupilNode* pupilFriend : currentPupil->getFriends()) {
					if (pupilFriend->getTeam() == currentPupil->getTeam()) {
						return false;
					}
					
					if (pupilFriend->getTeam() != 0) {
						continue;
					}

					pupilFriend->setTeam(currentPupil->getTeam() == 1 ? 2 : 1);
					nextPupil.push(pupilFriend);
				}
			}
			
			return true;
		}
};

BuildingTeams* createAlgorithm() {

    int pupilsCount, friendshipsCount;
    cin >> pupilsCount >> friendshipsCount;

    vector<PupilNode*> pupils(pupilsCount, nullptr);
    for (int i = 0; i < friendshipsCount; ++i) {
		int firstPupilId;
		int secondPupilId;
		cin >> firstPupilId >> secondPupilId;
		
		PupilNode* firstPupilNode = pupils[firstPupilId - 1];
		if (firstPupilNode == nullptr) {
			firstPupilNode = new PupilNode(firstPupilId);
			pupils[firstPupilId - 1] = firstPupilNode;
		}
		
		PupilNode* secondPupilNode = pupils[secondPupilId - 1];
		if (secondPupilNode == nullptr) {
			secondPupilNode = new PupilNode(secondPupilId);
			pupils[secondPupilId - 1] = secondPupilNode;
		}
		
		firstPupilNode->addFriend(secondPupilNode);
		secondPupilNode->addFriend(firstPupilNode);
    }
	
    return new BuildingTeams(pupils);
}

void writeOutputResult(vector<PupilNode*>& pupils) {
	if (pupils.empty()) {
		cout << "IMPOSSIBLE" << endl;
		return;
	}
	
	for (auto pupil : pupils) {
        cout << pupil->getTeam() << ' '; 
    }
	
	cout << endl;
}

int main() {

    BuildingTeams* algorithm = createAlgorithm();
    vector<PupilNode*> dividedPupils = algorithm->divideByTeams();

	writeOutputResult(dividedPupils);

	delete algorithm;

    return 0;
}
