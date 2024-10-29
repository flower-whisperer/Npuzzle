package core.runner;


import java.io.*;
import java.util.*;

class PatternDatabaseGenerator {
    private static final int N = 4; // 4x4 puzzle
    private static final int[] test_state = {1,4,3,2, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
    private static final int[] GOAL_STATE = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
    static final int[] tileSubsets = {-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
    static final int[] tilePositions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
    private static final long[][] zobristTable = new long[N*N][N*N];
    private static final Random random = new Random();
    private static final int currentGenerateClass = 0;
    public static final byte[] costTable_15_puzzle_0 = new byte[40960],
            costTable_15_puzzle_1 = new byte[16777216],
            costTable_15_puzzle_2 = new byte[16777216];


    // 初始化Zobrist表
    static {
        for (int i = 0; i < N*N; i++) {
            for (int j = 0; j < N*N; j++) {
                zobristTable[i][j] = random.nextLong();
            }
        }
    }
    public static void main(String[] args) throws IOException {

        int[] pattern0 = {2,3,4,0}; // 模式0
        int[] pattern1 = {1,5,6,9,10,13,0};
        int[] pattern2 = {7,8,11,12,14,15,0};
        switch (currentGenerateClass){
            case 0:{
                Arrays.fill(costTable_15_puzzle_0, (byte) -1);
                generatePatternDatabase(pattern0,costTable_15_puzzle_0);
                saveArrayToBinaryFile(costTable_15_puzzle_0,"cost"+ currentGenerateClass+".bin");
                break;
            }
            case 1:{
                Arrays.fill(costTable_15_puzzle_1, (byte) -1);
                generatePatternDatabase(pattern1,costTable_15_puzzle_1);
                saveArrayToBinaryFile(costTable_15_puzzle_1,"cost"+ currentGenerateClass+".bin");
                break;
            }
            case 2:{
                Arrays.fill(costTable_15_puzzle_2, (byte) -1);
                generatePatternDatabase(pattern2,costTable_15_puzzle_2);
                saveArrayToBinaryFile(costTable_15_puzzle_2,"cost"+ currentGenerateClass+".bin");
            }
        }

//        generatePatternDatabase(pattern1,costTable_15_puzzle_1);
//        saveArrayToBinaryFile(costTable_15_puzzle_1,"cost"+ currentGenerateClass+".bin");
//        loadArrayFromBinaryFile("cost"+ currentGenerateClass+".bin",costTable_15_puzzle_1.length);
        System.out.println();

//        saveArray(costTable_15_puzzle_0,"costTable_15_puzzle_0.dat");


    }

    public static long getHash(int[] board,int[]pattern) {
        //用pattern控制搜索范围
        long hash = 0L;
        for(int ptnum : pattern){
            int index = findIndex(board,ptnum);
            hash ^= zobristTable[index][ptnum];
        }
        return hash;
    }
    // 将数组保存到二进制文件的方法
    public static void saveArrayToBinaryFile(byte[] array, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(array);
            System.out.println("数组已保存到文件: " + filePath);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误: " + e.getMessage());
        }
    }

    // 从二进制文件加载数组的方法
    public static byte[] loadArrayFromBinaryFile(String filePath, int length) {
        byte[] array = new byte[length];
        try (FileInputStream fis = new FileInputStream(filePath)) {
            fis.read(array);
            System.out.println("数组已从文件加载: " + filePath);
        } catch (IOException e) {
            System.out.println("读取文件时发生错误: " + e.getMessage());
        }
        return array;
    }
    public static void saveArray(byte[] array, String filePath) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            for (byte value : array) {
                dos.writeInt(value);
            }
        }
    }

    public static void loadArray(String filePath,final byte[] costTable) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            int length = (int) (new File(filePath).length() ); // 每个int占用4字节
            for (int i = 0; i < length; i++) {
                costTable[i] = dis.readByte();
            }
        }
    }

    public static void generatePatternDatabase(int[] pattern,byte[]patternClass) {

        Queue<Node> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();

        // 初始化队列，将目标状态作为起始节点
        Node startNode = new Node(Arrays.copyOf(GOAL_STATE, GOAL_STATE.length), 0, 15);
        queue.add(startNode);
        visited.add(stateToPatternKey(GOAL_STATE, pattern));
        patternClass[getIndex(GOAL_STATE,currentGenerateClass)] = 0;

        // BFS 遍历
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            List<Node> neighbors = generatePatternNeighbors(node, pattern);
            for (Node neighbor : neighbors) {
                Long key = stateToPatternKey(neighbor.state, pattern);
                if (!visited.contains(key)) {
                    visited.add(key);
//                    cost不会超过127
                    int index = getIndex(neighbor.state,currentGenerateClass);
                    if(patternClass[index] == (byte) -1)
                        patternClass[index] = (byte)(neighbor.cost);
                    else if (patternClass[index] > (byte)(neighbor.cost))
                        patternClass[index] = (byte)(neighbor.cost);
                    queue.add(neighbor);
                    System.out.println(visited.size());
                }
            }
        }

    }

    private static List<Node> generatePatternNeighbors(Node node, int[] pattern) {
        List<Node> neighbors = new ArrayList<>();
        int[] moves = {-1, 1, -N, N}; // left, right, up, down

        int emptyPos = node.emptyPos;
        int row = emptyPos / N;
        int col = emptyPos % N;

        for (int move : moves) {
            int newPos = emptyPos + move;
            if (newPos >= 0 && newPos < N * N && Math.abs((newPos / N) - row) + Math.abs((newPos % N) - col) == 1) {
                int[] newState = Arrays.copyOf(node.state, node.state.length);
                int newcost = node.cost;
                for(int num:pattern){
                    if(num == newState[newPos]) newcost++;
                }
                newState[emptyPos] = newState[newPos];
                newState[newPos] = 0;
                neighbors.add(new Node(newState, newcost, newPos));
            }
        }
        return neighbors;
    }

    private static boolean contains(int[] pattern, int num) {
        for (int p : pattern) {
            if (p == num) return true;
        }
        return false;
    }
    private static int getIndex(int[]state,int patternNum){
        int index=0;
        for (int i = N - 1; i >= 0; --i) {
            for (int j = N - 1; j >= 0; --j) {
                final int tile = state[i * N + j];
                int p = i * N + j;
                if (tile != 0) {
                    final int subsetNumber = tileSubsets[tile];
                    if (subsetNumber == patternNum) {
                        index |= p << (tilePositions[tile] << 2);
                    }
                }
            }
        }
        return index;
    }
    private static Long stateToPatternKey(int[] state, int[] pattern) {
//        int index=0;
//        for (int i = N - 1; i >= 0; --i) {
//            for (int j = N - 1; j >= 0; --j) {
//                final int tile = state[i * N + j];
//                int p = i * N + j;
//                if (tile != 0) {
//                    final int subsetNumber = tileSubsets[tile];
//                    if (subsetNumber == 1) {
//                        index |= p << (tilePositions[tile] << 2);
//                    }
//                }
//            }
//        }
        return  getHash(state,pattern);
    }

    private static int findIndex(int[] state, int num) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == num) return i;
        }
        return -1;
    }

    private static class Node {
        int[] state;
        int cost;
        int emptyPos;

        Node(int[] state, int cost, int emptyPos) {
            this.state = state;
            this.cost = cost;
            this.emptyPos = emptyPos;
        }
    }
}
