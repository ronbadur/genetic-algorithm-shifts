package dynamic.algorithm;

import dynamic.entities.Knapsack.KnapsackItem;

import java.util.Arrays;

class Knapsack {
    // A utility function that returns maximum of two integers
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    static int solve(int W, int wt[], int val[], int n, int visited[]) {
        // Base Case

        if (n == 0 || W == 0)
            return 0;

        // If weight of the nth item is more than Knapsack capacity W, then
        // this item cannot be included in the optimal solution
        if (wt[n - 1] > W) {

            return solve(W, wt, val, n - 1, visited);
        }
        // Return the maximum of two cases:
        // (1) nth item included
        // (2) not included
        else {

            int v1[] = new int[visited.length];
            System.arraycopy(visited, 0, v1, 0, v1.length);
            int v2[] = new int[visited.length];
            System.arraycopy(visited, 0, v2, 0, v2.length);
            v1[n - 1] = 1;

            int ans1 = val[n - 1] + solve(W - wt[n - 1], wt, val, n - 1, v1);
            int ans2 = solve(W, wt, val, n - 1, v2);
            if (ans1 > ans2) {
                System.arraycopy(v1, 0, visited, 0, v1.length);
                return ans1;
            } else {
                System.arraycopy(v2, 0, visited, 0, v2.length);
                return ans2;
            }
        }
    }


    // Returns the maximum value that can be put in a knapsack of capacity W
    static int solve(int W, int wt[], int val[], int n) {
        // Base Case
        if (n == 0 || W == 0)
            return 0;

        // If weight of the nth item is more than Knapsack capacity W, then
        // this item cannot be included in the optimal solution
        if (wt[n - 1] > W)
            return solve(W, wt, val, n - 1);

            // Return the maximum of two cases:
            // (1) nth item included
            // (2) not included
        else return max(val[n - 1] + solve(W - wt[n - 1], wt, val, n - 1),
                solve(W, wt, val, n - 1)
        );
    }

    static int solve(int W, KnapsackItem[] input, int[] visited) {
        int n = input.length;
        int[] wt = Arrays.stream(input).mapToInt(KnapsackItem::getWeight).toArray();
        int[] val = Arrays.stream(input).mapToInt(KnapsackItem::getValue).toArray();

        return Knapsack.solve(W, wt, val, n, visited);
    }
}
