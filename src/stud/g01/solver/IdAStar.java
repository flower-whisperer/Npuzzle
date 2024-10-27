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
        //����һЩ��Ҫ�ĳ�ʼ��,��Ϊ�������������װ�ڸ����ط���
        this.initialState = ((PuzzleBoard) problem.getInitialState()).getPuzzle_board();
        this.goalState = ((PuzzleBoard) problem.getGoal()).getPuzzle_board();
        this.zobristTable = ((NPuzzleProblem) problem).getZobristTable();
        this.n = problem.getSize();
        visited = new HashSet<>();

        nodesGenerated = 0;
        nodesExpanded = 0;
        System.out.println("������IDA�㷨");
        //�����ʼ״̬���̹�ϣֵ���е��鷳 �ȼ��ڡ�currentHash = computeHash(initialState);��
//        currentHash = computeHash(initialState);
        //��ȿ��ƣ�����������ڵ���Ѱ��
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

        //ԭ��java�к������Ե��ð���֮ǰ���Լ���ѭ��д
        if(Arrays.deepEquals(currentState,goalState))
            return new ArrayList<>(path);
        //��ǰ����ʽ����ֵ+pastCost ��������ֱ�ӷ���null��û�ҵ�
        if(h(currentState) + path.size() > depthLimit)
            return null;
        //�ѵ�ǰ״̬��ӵ�·����
        path.add(currentState);
        //����zobrist��ϣֵ
        currentHash = computeHash(currentState);
        //�����ѱ�����ϣset

        visited.add(currentHash);
        nodesExpanded++;
        //���������ҺϷ���next״̬
        for(int[][] neighbor:getNeighbors(currentState)){
            Long neighborHash = computeHash(neighbor);
            //��zobrist��ϣ�жϵ�ǰ״̬�Ƿ������
            if(!visited.contains(neighborHash)){
                nodesGenerated++;
                //���û�У�ȥ������ھӵ��ھ�
                List<int[][]>result = idaStar(neighbor,depthLimit,path);
                if(result != null) return result;
            }
        }
        //��������ھӶ�û�ҵ�result��˵�����״̬û���ˣ�ȥ�ұ��
        path.remove(path.size() - 1);
        return null;
    }
    private List<int[][]>getNeighbors(int[][]state){
        List<int[][]> neighbors = new ArrayList<>();
        //�ҡ�0�������λ��
        int zeroX = -1, zeroY = -1;
        for(int i = 0;i < n; i ++){
            for(int j = 0; j < n; j ++){
                if(state[i][j] == 0){
                    zeroX = i;
                    zeroY = j;
                }
            }
        }
        //�����ƶ�˳������Ӧ����puzzleboard���next��������������
        int [][]directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for(int[] dir : directions){
            //��0���������һ��λ��
            int newX = zeroX + dir[0];
            int newY = zeroY + dir[1];
            //�ж�λ�úϷ�
            if(newX >= 0 && newX < n && newY >= 0 && newY < n){
                //��ȿ�����ά���飬��ʵ����һ������ֵ����
                int[][] newState = deepCopy(state);
                //��Ŀ������0��λ�öԻ�
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
            //ԭ������ֱ�ӿ���һ�еģ����Ӧ�������ڴ��ƶ�ʵ�ֵģ�Ӧ�ñ�һ������ֵ��
            copy[i] = Arrays.copyOf(original[i],n);
        }
        return copy;
    }
    //�����ٺ�������������ʦ�����ˣ�̫����
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
    //����zobrist��ϣ����������ÿ��λ��ÿ�������ʼ��һ��long������ʹ������λ�õ�����ʾ����״̬
    //��һ��λ�ñ�ʾ���λ�ñ�ţ�0-15���ڶ���λ�ñ�ʾ��������ֵ������ֵ��ʾ��ǰ����״̬
    private long computeHash(int[][]state) {
        long hash = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                hash ^= zobristTable[i * n + j][state[i][j]]; // Zobrist��ϣ
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
