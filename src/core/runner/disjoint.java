package core.runner;

import java.util.*;

class NPuzzleSolver {

    // ���̵�Ŀ��״̬
    static final int[] GOAL_STATE = {
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 10, 11, 12,
            13, 14, 15, 0
    };

    // ����4x4���̵��ƶ�����
    static final int[] dx = {-1, 1, 0, 0}; // �ϣ��£�����
    static final int[] dy = {0, 0, -1, 1};

    // ��Եģʽ��7��Ŀ��λ��
    static final int[] edgePattern = {0, 1, 2, 3, 4, 8, 12};

    public static void main(String[] args) {
        // ��ʼ������� 15-puzzle ״̬
        int[] initialState = {
                    8,13,0,6,1,15,9,14,3,4,5,11,7,0,10,12
                 // ���ҵ�״̬
        };

        NPuzzleSolver solver = new NPuzzleSolver();
        solver.solve(initialState);
    }

    // ���15-puzzle�����������
    public void solve(int[] initialState) {
        System.out.println("��ʼ״̬��");
        printState(initialState);

        // ��ԭ���Ͻǵ�7������
        System.out.println("��ʼ��ԭ���Ͻǵ�7������...");
        solveEdgePattern(initialState);

        // ��ԭ���½ǵ�3x3����
        System.out.println("��ʼ��ԭ���½ǵ�3x3����...");
        solveCenterPattern(initialState);

        System.out.println("����״̬��");
        printState(initialState);
    }

    // ��ԭ���Ͻǵ�7������
    private void solveEdgePattern(int[] state) {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(state, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visited.add(Arrays.toString(current.state));

            if (checkEdgePattern(current.state)) {
                System.out.println("���Ͻ�7�������ѻ�ԭ");
                printState(current.state);
                return;
            }

            // ��ÿ�����ܵ��ƶ�����BFS
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

    // ��ԭ���½ǵ�3x3����
    private void solveCenterPattern(int[] state) {
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node(state, 0));

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visited.add(Arrays.toString(current.state));

            if (Arrays.equals(current.state, GOAL_STATE)) {
                System.out.println("���½�3x3�����ѻ�ԭ");
                printState(current.state);
                return;
            }

            // ��ÿ�����ܵ��ƶ�����BFS
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

    // �ƶ��ո񣨷�����/��/��/�ң�
    private boolean move(int[] state, int direction) {
        int zeroIndex = findZero(state);
        int x = zeroIndex / 4;
        int y = zeroIndex % 4;

        int newX = x + dx[direction];
        int newY = y + dy[direction];

        if (newX >= 0 && newX < 4 && newY >= 0 && newY < 4) {
            int newIndex = newX * 4 + newY;
            // �����ո������
            state[zeroIndex] = state[newIndex];
            state[newIndex] = 0;
            return true;
        }
        return false;
    }

    // ������Ͻǵ�7�������Ƿ��Ѿ���ԭ
    private boolean checkEdgePattern(int[] state) {
        for (int i = 0; i < edgePattern.length; i++) {
            if (state[edgePattern[i]] != GOAL_STATE[edgePattern[i]]) {
                return false;
            }
        }
        return true;
    }

    // �ҵ��ո��λ��
    private int findZero(int[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // ��ӡ����״̬
    private void printState(int[] state) {
        for (int i = 0; i < state.length; i++) {
            System.out.print(state[i] + "\t");
            if (i % 4 == 3) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // �ڲ����ʾ���̽ڵ�
    class Node {
        int[] state;
        int steps;

        Node(int[] state, int steps) {
            this.state = state.clone();
            this.steps = steps;
        }
    }
}
