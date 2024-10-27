package stud.g01.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NPuzzleProblem extends Problem {

    private final long[][] zobristTable;
    private long currentHash;

    public void setCurrentHash(long currentHash) {
        this.currentHash = currentHash;
    }

    public NPuzzleProblem(State initialState, State goal, int size) {
        super(initialState, goal, size);
        this.zobristTable = new long[size * size][size * size]; // ����0��n*n��״̬
        generateZobristTable();
    }
    private void generateZobristTable() {
        Random rand = new Random();
        for (int i = 0; i < size * size; i++) {
            for (int j = 0; j < size * size; j++) {
                for (int k = 0; k < 2; k++) {
                    zobristTable[i][j] = rand.nextLong();
                }
            }
        }
    }
    //�Ƿ�ɽ���������������йأ�����͵��ֱ���ж�Ϊ�ɽ�
    @Override
    public boolean solvable() {
        return true;
    }

    @Override
    public int stepCost(State state, Action action) {
        //����һ��ʼ��0�����¸��²���
        return 1;
    }

    @Override
    public boolean applicable(State state, Action action) {
        //�����action����һ��char������ѡ����������д��
        //�����ҵ�������0��λ�ã�Ȼ���charӳ�䵽����
        //Ȼ���ж��Ƿ���缴��
        //����N E S W ˳��
        Map<Character, int[]> directionsMap = ((Move)action).getDirectionsMap();
        int currentx = -1;
        int currenty = -1;
        PuzzleBoard pb = (PuzzleBoard) state;
        int size = pb.getSize();
        int[][] grid = pb.getPuzzle_board();
        //�ҵ�0���ڵ�������
        for(int i = 0; i < size; i ++)
            for(int j = 0; j < size; j ++)
                if (grid[i][j] == 0) {
                    currentx = i;
                    currenty = j;
                    break;
                }
        //ͨ��char�õ�ƫ��
        int[] offset = directionsMap.get(((Move)action).getDirection());
        int actionx = currentx + offset[0];
        int actiony = currenty + offset[1];

        return actionx >= 0 && actionx < size && actiony >= 0 && actiony < size;
    }


    @Override
    public void showSolution(Deque<Node> path) {
        System.out.println("�����ǽ��չʾ______________________________________________________________");
        int k = 0;
        for(Node node : path){
            System.out.println("��"+k+"��������");
            k = k + 1;
            PuzzleBoard puzzleBoard = (PuzzleBoard) node.getParent().getState();
            puzzleBoard.draw();
        }
        this.goal.draw();
        System.out.println("��������������������������������������������������������������������������������������������������������");

    }
    public long[][] getZobristTable() {
        return zobristTable;
    }

    public long getCurrentHash() {
        return currentHash;
    }
}

