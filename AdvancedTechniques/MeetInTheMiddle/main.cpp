#include <cstdio>
#include <bits/stdc++.h>

using namespace std;

#define MAX_SUM 1000000001

/**
 * Meet In The Middle.
 * You are given an array of n numbers. In how many ways can you choose a subset of the numbers with sum x?
 *
 * Input
 * The first input line has two numbers n and x: the array size and the required sum.
 * The second line has n integers t_1,t_2,...,t_n: the numbers in the array.
 *
 * Output
 * Print the number of ways you can create the sum x.
 *
 * Constraints
 * 1 <= n <= 40
 * 1 <= x <= 10^9
 * 1 <= t_i <= 10^9
 *
 * Example
 * Input:
 * 4 5
 * 1 2 3 2
 *
 * Output:
 * 3
 *
 * @author Alik
 */
class MeetInTheMiddle {
	public:
		MeetInTheMiddle(vector<int>& numbers, int x) : numbers(numbers), x(x) {}

		unsigned long execute() {
            unsigned int size = numbers.size();
            vector<unsigned long> part1 = computeSums(0, size / 2 - 1);
            vector<unsigned long> part2 = computeSums(size / 2, size - 1);
        
            sort(part1.begin(), part1.end());
            sort(part2.begin(), part2.end());

            unsigned long ways = 0;
            for (unsigned long s : part1) {
                auto low_iterator = lower_bound(part2.begin(), part2.end(), x - s);
            	auto high_iterator = upper_bound(part2.begin(), part2.end(), x - s);
            	ways += high_iterator - low_iterator;
            }
        
            return ways;
        }

	private:
		vector<int> numbers;
		int x;
        
        vector<unsigned long> computeSums(int from, int to) {
            int length = to - from + 1;
            vector<unsigned long> res;

            for (int i = 0; i < (1 << length); i++) {
                unsigned long sum = 0;
                for (int j = 0; j < length; j++) {
            	    if (i & (1 << j)) {
            	        sum += numbers[from + j];
            	    }
            	}

                if (sum <= MAX_SUM) {
           		    res.push_back(sum);
           		}
            }

            return res;
        }
};

MeetInTheMiddle* createAlgorithm() {

    unsigned int n;
    scanf("%d", &n);

    unsigned int x;
    scanf("%d", &x);

    vector<int> numbers(n);
    for (unsigned int i = 0; i < n; ++i) {
		scanf("%d", &numbers[i]);
    }

    return new MeetInTheMiddle(numbers, x);
}

int main() {

    MeetInTheMiddle* algorithm = createAlgorithm();
    unsigned long ways = algorithm->execute();

	printf("%ld\n", ways);

	delete algorithm;

    return 0;
}