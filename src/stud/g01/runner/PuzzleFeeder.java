package stud.g01.runner;

import core.problem.Problem;
import core.runner.EngineFeeder;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.queue.EvaluationType;
import core.solver.queue.Frontier;
import core.solver.queue.Node;
import stud.g01.problem.npuzzle.NPuzzleProblem;
import stud.g01.problem.npuzzle.PuzzleBoard;
import stud.g01.queue.PqFrontier;
import stud.queue.ListFrontier;


import java.util.ArrayList;
//Fix Me   //Fix Me
public class PuzzleFeeder extends EngineFeeder {
    @Override
    public ArrayList<Problem> getProblems(ArrayList<String> problemLines) {
        //problem lines的格式分析过，就是阶数、初始状态，最终状态

        ArrayList<Problem> problems = new ArrayList<>();
        for (String line : problemLines){
            //把每一行抽出来作为一个问题看待
            NPuzzleProblem problem = getPuzzle(line);
            problems.add(problem);
        }
        return problems;

    }
    private NPuzzleProblem getPuzzle(String problemLine) {
        //读入初始状态
        //通过空格分割字符串，转换为二维维数组形式存储棋盘（一维也行，写二维方便写函数）
        String[] cells = problemLine.split(" ");
        int size = Integer.parseInt(cells[0]);
        //这里不能用一个二维数组同时
        int[][] puzzle_board_init = new int[size][size];
        int[][] puzzle_board_goal = new int[size][size];
        //读入初始状态
        int k = 1;
        for(int i = 0 ;i < size; i++)
        {
            for (int j = 0; j < size; j++) {
                puzzle_board_init[i][j] = Integer.parseInt(cells[k++]) ;
            }
        }
        PuzzleBoard initialState = new PuzzleBoard(puzzle_board_init,size);

        //读入目标状态
        k = 1 + size * size;
        for(int i = 0 ;i < size; i++)
        {
            for (int j = 0; j < size; j++) {
                puzzle_board_goal[i][j] = Integer.parseInt(cells[k++]) ;
            }
        }
        PuzzleBoard goal = new PuzzleBoard(puzzle_board_goal,size);

        //通过 <初始状态，目标状态，size>构建一个问题
        return new NPuzzleProblem(initialState, goal, size);
    }
    @Override

    public Frontier getFrontier(EvaluationType type) {
        //这里可以选择不同的frontier
        return new PqFrontier();
//        return new ListFrontier(Node.evaluator(type));
    }
    @Override
    public Predictor getPredictor(HeuristicType type) {
        return PuzzleBoard.predictor(type);
    }
}
