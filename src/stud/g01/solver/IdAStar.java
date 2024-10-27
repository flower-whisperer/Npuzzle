package stud.g01.solver;
import java.util.*;
import core.problem.Problem;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.algorithm.searcher.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;
import stud.g01.problem.npuzzle.NPuzzleProblem;
import stud.g01.problem.npuzzle.PuzzleBoard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class IdAStar extends AbstractSearcher {
    private int [][]initialState;
    private int [][]goalState;
    private long [][]zobristTable;
    private Set<Long> visited = new HashSet<>();
    private int n;
    private long currentHash;
    public int solutionSize = 0;
    public IdAStar(Frontier frontier) {
        super(frontier);
    }

    @Override
    public Deque<Node> search(Problem problem) {
        //进行一些必要的初始化,因为具体变量都被封装在各个地方了
        this.initialState = ((PuzzleBoard) problem.getInitialState()).getPuzzle_board();
        this.goalState = ((PuzzleBoard) problem.getGoal()).getPuzzle_board();
        this.zobristTable = ((NPuzzleProblem) problem).getZobristTable();
        this.n = problem.getSize();
        visited = new HashSet<>();

        nodesGenerated = 0;
        nodesExpanded = 0;
        System.out.println("这里是IDA算法");
        //计算初始状态棋盘哈希值，有点麻烦 等价于“currentHash = computeHash(initialState);”
//        currentHash = computeHash(initialState);
        //深度控制，这个变量用于迭代寻优
        int depthLimit = h(initialState);
        while(true){

            List<int[][]> result = idaStar(initialState,depthLimit,new ArrayList<>());
            if (result != null) {
                int cnt = 0;
                for (int[][] state : result) {
                    System.out.println(cnt++);
                    printState(state);
                }
                System.out.println(cnt++);
                printState(goalState);
                this.solutionSize = result.size();
                break;
            }
            depthLimit++;
        }

        return null;
    }
    private List<int[][]>idaStar(int[][] currentState, int depthLimit, List<int[][]>path){

        //原来java有函数可以调用啊，之前还自己用循环写
        if(Arrays.deepEquals(currentState,goalState))
            return new ArrayList<>(path);
        //当前启发式函数值+pastCost 大于门限直接返回null，没找到
        if(h(currentState) + path.size() > depthLimit)
            return null;
        //把当前状态添加到路径中
        path.add(currentState);
        //更新zobrist哈希值
        currentHash = computeHash(currentState);
        //创建已遍历哈希set

        visited.add(currentHash);
        nodesExpanded++;
        //上下左右找合法的next状态
        for(int[][] neighbor:getNeighbors(currentState)){
            Long neighborHash = computeHash(neighbor);
            //用zobrist哈希判断当前状态是否遍历过
            if(!visited.contains(neighborHash)){
                nodesGenerated++;
                //如果没有，去找这个邻居的邻居
                List<int[][]>result = idaStar(neighbor,depthLimit,path);
                if(result != null) return result;
            }
        }
        //如果所有邻居都没找到result，说明这个状态没用了，去找别的
        path.remove(path.size() - 1);
        return null;
    }
    private List<int[][]>getNeighbors(int[][]state){
        List<int[][]> neighbors = new ArrayList<>();
        //找“0”块儿的位置
        int zeroX = -1, zeroY = -1;
        for(int i = 0;i < n; i ++){
            for(int j = 0; j < n; j ++){
                if(state[i][j] == 0){
                    zeroX = i;
                    zeroY = j;
                }
            }
        }
        //遍历移动顺序，这里应该用puzzleboard里的next方法，但不管了
        int [][]directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for(int[] dir : directions){
            //“0”块儿的下一个位置
            int newX = zeroX + dir[0];
            int newY = zeroY + dir[1];
            //判断位置合法
            if(newX >= 0 && newX < n && newY >= 0 && newY < n){
                //深度拷贝二维数组，其实就是一个个赋值罢了
                int[][] newState = deepCopy(state);
                //把目标块儿与0的位置对换
                newState[zeroX][zeroY] = newState[newX][newY];
                newState[newX][newY] = 0;
                neighbors.add(newState);
            }
        }
        return neighbors;
    }
    private int[][] deepCopy(int[][] original){
        int[][] copy = new int[n][];
        for(int i = 0; i < n; i ++){
            //原来可以直接拷贝一行的，这个应该是用内存移动实现的，应该比一个个赋值快
            copy[i] = Arrays.copyOf(original[i],n);
        }
        return copy;
    }
    //曼哈顿函数，懒得用老师给的了，太深了
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
    //计算zobrist哈希，对棋盘上每个位置每个块儿初始化一个long整数，使用所有位置的异或表示棋盘状态
    //第一个位置表示块儿位置编号（0-15）第二个位置表示这个块儿的值，整个值表示当前棋盘状态
    private long computeHash(int[][]state) {
        long hash = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                hash ^= zobristTable[i * n + j][state[i][j]]; // Zobrist哈希
            }
        }
        return hash;
    }
    private void  printState(int[][] state){
        for(int[]row : state){
            for(int value : row){
                System.out.print(value);
                if(n == 3) System.out.print(" ");
                else if(value < 10) System.out.print("   ");
                else System.out.print("  ");
            }
            System.out.println();
        }

    }
}
