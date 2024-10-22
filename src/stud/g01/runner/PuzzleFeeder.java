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
        //problem lines�ĸ�ʽ�����������ǽ�������ʼ״̬������״̬

        ArrayList<Problem> problems = new ArrayList<>();
        for (String line : problemLines){
            //��ÿһ�г������Ϊһ�����⿴��
            NPuzzleProblem problem = getPuzzle(line);
            problems.add(problem);
        }
        return problems;

    }
    private NPuzzleProblem getPuzzle(String problemLine) {
        //�����ʼ״̬
        //ͨ���ո�ָ��ַ�����ת��Ϊ��άά������ʽ�洢���̣�һάҲ�У�д��ά����д������
        String[] cells = problemLine.split(" ");
        int size = Integer.parseInt(cells[0]);
        //���ﲻ����һ����ά����ͬʱ
        int[][] puzzle_board_init = new int[size][size];
        int[][] puzzle_board_goal = new int[size][size];
        //�����ʼ״̬
        int k = 1;
        for(int i = 0 ;i < size; i++)
        {
            for (int j = 0; j < size; j++) {
                puzzle_board_init[i][j] = Integer.parseInt(cells[k++]) ;
            }
        }
        PuzzleBoard initialState = new PuzzleBoard(puzzle_board_init,size);

        //����Ŀ��״̬
        k = 1 + size * size;
        for(int i = 0 ;i < size; i++)
        {
            for (int j = 0; j < size; j++) {
                puzzle_board_goal[i][j] = Integer.parseInt(cells[k++]) ;
            }
        }
        PuzzleBoard goal = new PuzzleBoard(puzzle_board_goal,size);

        //ͨ�� <��ʼ״̬��Ŀ��״̬��size>����һ������
        return new NPuzzleProblem(initialState, goal, size);
    }
    @Override

    public Frontier getFrontier(EvaluationType type) {
        //�������ѡ��ͬ��frontier
        return new PqFrontier();
//        return new ListFrontier(Node.evaluator(type));
    }
    @Override
    public Predictor getPredictor(HeuristicType type) {
        return PuzzleBoard.predictor(type);
    }
}
