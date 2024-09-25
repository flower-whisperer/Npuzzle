package stud.g01.problem.npuzzle;

import core.problem.Action;
import core.problem.State;
import core.solver.algorithm.heuristic.HeuristicType;
import core.solver.algorithm.heuristic.Predictor;
import stud.problem.pathfinding.Direction;
import stud.problem.pathfinding.Position;
import java.util.*;

import static core.solver.algorithm.heuristic.HeuristicType.*;
import static stud.problem.pathfinding.Direction.ROOT2;
import static stud.problem.pathfinding.Direction.SCALE;

public class PuzzleBoard extends State {
    private final int [][] puzzle_board;
    private final int size;
    //��cells������ʼ����״̬
    public PuzzleBoard(int[][] puzzle_board, int size)
    {
        this.puzzle_board = puzzle_board;
        this.size = size;
//        this.draw();
    }

    public int[][] getPuzzle_board() {
        return puzzle_board;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void draw() {
        for(int i = 0 ; i < this.size; i ++){
            for(int j = 0; j < this.size; j ++)
            {
                System.out.print(this.puzzle_board[i][j]);
                if(this.size == 3)
                    System.out.print(" ");
                else if(this.puzzle_board[i][j] < 10)
                    System.out.print("   ");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }
    //ģ��walkerFeeder���д�����ڼ̳�state�����������Ԥ��������
    //���ｫʵ������Ԥ����
    private static final EnumMap<HeuristicType, Predictor> predictors = new EnumMap<>(HeuristicType.class);
    static{
        predictors.put(MISPLACED,
                (state, goal) -> ((PuzzleBoard)state).misplacedTiles((PuzzleBoard)goal));
        predictors.put(MANHATTAN,
                (state, goal) -> ((PuzzleBoard)state).manhattanDistance((PuzzleBoard)goal));
    }
    public static Predictor predictor(HeuristicType type){
        return predictors.get(type);
    }
    //���㲻����λ������ʵ�ǳ��򵥣�����ͳ�Ʋ�������λ���ϵ����ָ���
    private int misplacedTiles(PuzzleBoard goal) {

        int ct = 0;
        int size = this.size;
        for(int i = 0 ; i < size; i ++)
        {
            for(int j = 0; j < size;j ++)
            {
                if(this.puzzle_board[i][j]!=0 && this.puzzle_board[i][j] != goal.puzzle_board[i][j])
                    ct = ct +1;
            }
        }
        return ct;
    }
    private int manhattanDistance(PuzzleBoard goal){
        int distance = 0;
        int size = this.size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = this.puzzle_board[i][j];
                if(value != 0){
                    // ����ǿ���λӦ���ڵ�λ��
                    int targetX = (value - 1) / size;
                    int targetY = (value - 1) % size;
                    distance += Math.abs(targetY - i) + Math.abs(targetY -j);
                }
            }
        }
        return distance;
    }
    @Override
    public State next(Action action) {
        char direction = ((Move)action).getDirection();
        int[] offsets = ((Move)action).getDirectionsMap().get(direction);
        //������ֱ�Ӹ�ֵ�ᵼ�±������puzzle_board�����ı䣬���������չ�ڵ�󸸽ڵ����ӽڵ���ͬ�����
        //��ֱ��ʹ����ķ���
        int[][] originpb = getPuzzle_board();
        int[][] pb = new int[size][size];

        int x = 0,y = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                pb[i][j] = originpb[i][j];
                if(originpb[i][j] == 0)
                {
                    x = i;
                    y = j;
                }
            }
//        boolean check  = true;
//        for (int i = 0; i < size; i ++)
//        {
//            check = check && Arrays.equals(pb[i],originpb[i]);
//
//        }
//        boolean check2 = Arrays.equals(pb[0],getPuzzle_board()[0]);
        //����
        int exchangex = x + offsets[0];
        int exchangey = y + offsets[1];
        int temp = pb[exchangex][exchangey];
        pb[exchangex][exchangey] = 0;
        pb[x][y] = temp;
        return new PuzzleBoard(pb,getSize());
    }


    @Override
    public Iterable<? extends Action> actions() {
        //�����ø�mapд�����ţ�����ô˵�أ����ܾ���
        Collection<Move> moves = new ArrayList<>();
        moves.add(new Move('N'));
        moves.add(new Move('E'));
        moves.add(new Move('S'));
        moves.add(new Move('W'));
        return moves;
    }
    //��Ҫ��дequals������hashcode������û����contains�������б�
    @Override
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj instanceof PuzzleBoard){
            PuzzleBoard another = (PuzzleBoard) obj;
            int [][]anpb = another.getPuzzle_board();
            int [][]pb = this.getPuzzle_board();
            for(int i = 0; i < size ; i++){
                for (int j = 0; j <size; j++) {
                    if(pb[i][j] != anpb[i][j]) return false;
                }
            }
            return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        if(getPuzzle_board() == null) return 0;
        int result = 1;
        int[][]pb = getPuzzle_board();
        for(int[] row : pb){
            if(row!=null){
                for(int e:row){
                    result = 31*result + Integer.hashCode(e);
                }
            }else{
                result = 31*result + Objects.hashCode(null);
            }
        }
        return result;
    }
}
