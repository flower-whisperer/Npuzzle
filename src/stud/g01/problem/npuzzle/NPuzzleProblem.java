package stud.g01.problem.npuzzle;

import core.problem.Action;
import core.problem.Problem;
import core.problem.State;
import core.solver.queue.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class NPuzzleProblem extends Problem {


    public NPuzzleProblem(State initialState, State goal) {
        super(initialState, goal);
    }

    public NPuzzleProblem(State initialState, State goal, int size) {
        super(initialState, goal, size);
    }

    //是否可解与输入的逆序数有关，这里偷懒直接判断为可解
    @Override
    public boolean solvable() {
        return true;
    }

    @Override
    public int stepCost(State state, Action action) {
        //这里一开始是0，导致更新不动
        return 1;
    }

    @Override
    public boolean applicable(State state, Action action) {
        //这里的action就是一个char，不妨选择最垃圾的写法
        //首先找到棋盘上0的位置，然后把char映射到数组
        //然后判断是否出界即可
        //按照N E S W 顺序
        Map<Character, int[]> directionsMap = ((Move)action).getDirectionsMap();
        int currentx = -1;
        int currenty = -1;
        PuzzleBoard pb = (PuzzleBoard) state;
        int size = pb.getSize();
        int[][] grid = pb.getPuzzle_board();
        //找到0所在的行与列
        for(int i = 0; i < size; i ++)
            for(int j = 0; j < size; j ++)
                if (grid[i][j] == 0) {
                    currentx = i;
                    currenty = j;
                    break;
                }
        //通过char拿到偏移
        int[] offset = directionsMap.get(((Move)action).getDirection());
        int actionx = currentx + offset[0];
        int actiony = currenty + offset[1];

        return actionx >= 0 && actionx < size && actiony >= 0 && actiony < size;
    }


    @Override
    public void showSolution(Deque<Node> path) {
        System.out.println("这里是结果展示______________________________________________________________");
        int k = 0;
        for(Node node : path){
            System.out.println("第"+k+"步操作：");
            k = k + 1;
            PuzzleBoard puzzleBoard = (PuzzleBoard) node.getParent().getState();
            puzzleBoard.draw();
        }
        this.goal.draw();
        System.out.println("结束——————————————————————————————————————————————————");

    }
}

