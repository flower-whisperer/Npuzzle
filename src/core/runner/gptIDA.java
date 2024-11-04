import java.util.*;

class NPuzzle {
    private final int[][] goalState;
    private final int n;

    public NPuzzle(int n) {
        this.n = n;
        this.goalState = createGoalState(n);
    }

    private int[][] createGoalState(int n) {
        int[][] state = new int[n][n];
        int value = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                state[i][j] = (i == n - 1 && j == n - 1) ? 0 : value++;
            }
        }
        return state;
    }

    public void solve(int[][] initialState) {
        int depthLimit = 0;
        while (true) {
            List<int[][]> result = idAStar(initialState, depthLimit, new ArrayList<>());
            if (result != null) {
                int cnt = 0;
                for (int[][] state : result) {
                    System.out.println(cnt++);
                    printState(state);
                }
                System.out.println(cnt++);
                printState(goalState);
                break;
            }
            depthLimit++;
        }
    }

    private List<int[][]> idAStar(int[][] currentState, int depthLimit, List<int[][]> path) {
        if (Arrays.deepEquals(currentState, goalState)) {
            return new ArrayList<>(path);
        }

        if (h(currentState) + path.size() > depthLimit) {
            return null;
        }

        path.add(currentState);

        for (int[][] neighbor : getNeighbors(currentState)) {
            List<int[][]> result = idAStar(neighbor, depthLimit, path);
            if (result != null) {
                return result;
            }
        }

        path.remove(path.size() - 1);
        return null;
    }

    private int h(int[][] state) {
        int heuristic = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (state[i][j] != 0) {
                    int value = state[i][j] - 1;
                    heuristic += Math.abs(value / n - i) + Math.abs(value % n - j);
                }
            }
        }
        return heuristic;
    }

    private List<int[][]> getNeighbors(int[][] state) {
        List<int[][]> neighbors = new ArrayList<>();
        int zeroX = -1, zeroY = -1;

        // Find the position of the empty tile (0)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (state[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                }
            }
        }

        // Possible movements: up, down, left, right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = zeroX + dir[0];
            int newY = zeroY + dir[1];
            if (newX >= 0 && newX < n && newY >= 0 && newY < n) {
                int[][] newState = deepCopy(state);
                newState[zeroX][zeroY] = newState[newX][newY];
                newState[newX][newY] = 0;
                neighbors.add(newState);
            }
        }
        return neighbors;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[n][];
        for (int i = 0; i < n; i++) {
            copy[i] = Arrays.copyOf(original[i], n);
        }
        return copy;
    }

    private void printState(int[][] state) {
        for (int[] row : state) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        NPuzzle puzzle = new NPuzzle(3);
        int[][] initialState = {
//                {8, 13, 0, 6},
//                {1, 15, 9,14},
//                {3,4,5,11},
//                {7,2,10,12}
//                {15,14,13,12},
//                {11,10,9,8},
//                {7,6,5,4},
//                {3,1,2,0},

//                {5 ,0 ,8},
//                {4 ,2 ,1},
//                {7 ,3 ,6},
                {6 ,4 ,7 },
                {8 ,5 ,0},
                {3 ,2 ,1}
        };
        long start = System.currentTimeMillis();
        puzzle.solve(initialState);
        long end = System.currentTimeMillis();
        System.out.println("time cost:"+ (end - start)/1000/60 + "min" + (end - start)%1000 + "ms");
    }
}
