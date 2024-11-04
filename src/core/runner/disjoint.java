package core.runner;

import java.util.*;

class NPuzzleSolver {

    // 棋盘的目标状态
    static final int[] GOAL_STATE = {
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 10, 11, 12,
            13, 14, 15, 0
    };

    // 定义4x4棋盘的移动方向
    static final int[] dx = {-1, 1, 0, 0}; // 上，下，左，右
    static final int[] dy = {0, 0, -1, 1};

    // 边缘模式的7个目标位置
    static final int[] edgePattern = {0, 1, 2, 3, 4, 8, 12};

    public static void main(String[] args) {
        // 初始化输入的 15-puzzle 状态
        int[] initialState = {
                    8,13,0,6,1,15,9,14,3,4,5,11,7,0,10,12
                 // 打乱的状态
        };

        NPuzzleSolver solver = new NPuzzleSolver();
        solver.solve(initialState);
    }

    // 解决15-puzzle问题的主流程
    public void solve(int[] initialState) {
        System.out.println("初始状态：");
        printState(initialState);

        // 还原左上角的7个数字
        System.out.println("开始还原左上角的7个数字...");
        solveEdgePattern(initialState);

        // 还原右下角的3x3区域
        System.out.println("开始还原右下角的3x3区域...");
        solveCenterPattern(initialState);

        System.out.println("最终状态：");
        printState(initialState);
    }

    // 还原左上角的7个数字
    private void solveEdgePattern(int[] state) {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(state, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visited.add(Arrays.toString(current.state));

            if (checkEdgePattern(current.state)) {
                System.out.println("左上角7个数字已还原");
                printState(current.state);
                return;
            }

            // 对每个可能的移动进行BFS
            for (int i = 0; i < 4; i++) {
                int[] nextState = current.state.clone();
                if (move(nextState, i)) {
                    if (!visited.contains(Arrays.toString(nextState))) {
                        queue.offer(new Node(nextState, current.steps + 1));
                    }
                }
            }
        }
    }

    // 还原右下角的3x3区域
    private void solveCenterPattern(int[] state) {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(state, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visited.add(Arrays.toString(current.state));

            if (Arrays.equals(current.state, GOAL_STATE)) {
                System.out.println("右下角3x3区域已还原");
                printState(current.state);
                return;
            }

            // 对每个可能的移动进行BFS
            for (int i = 0; i < 4; i++) {
                int[] nextState = current.state.clone();
                if (move(nextState, i)) {
                    if (!visited.contains(Arrays.toString(nextState))) {
                        queue.offer(new Node(nextState, current.steps + 1));
                    }
                }
            }
        }
    }

    // 移动空格（方向：上/下/左/右）
    private boolean move(int[] state, int direction) {
        int zeroIndex = findZero(state);
        int x = zeroIndex / 4;
        int y = zeroIndex % 4;

        int newX = x + dx[direction];
        int newY = y + dy[direction];

        if (newX >= 0 && newX < 4 && newY >= 0 && newY < 4) {
            int newIndex = newX * 4 + newY;
            // 交换空格和数字
            state[zeroIndex] = state[newIndex];
            state[newIndex] = 0;
            return true;
        }
        return false;
    }

    // 检查左上角的7个数字是否已经还原
    private boolean checkEdgePattern(int[] state) {
        for (int i = 0; i < edgePattern.length; i++) {
            if (state[edgePattern[i]] != GOAL_STATE[edgePattern[i]]) {
                return false;
            }
        }
        return true;
    }

    // 找到空格的位置
    private int findZero(int[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // 打印棋盘状态
    private void printState(int[] state) {
        for (int i = 0; i < state.length; i++) {
            System.out.print(state[i] + "\t");
            if (i % 4 == 3) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // 内部类表示棋盘节点
    class Node {
        int[] state;
        int steps;

        Node(int[] state, int steps) {
            this.state = state.clone();
            this.steps = steps;
        }
    }
}
