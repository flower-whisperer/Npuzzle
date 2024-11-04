package stud.g01.solver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private int[][] initialState;
    private static int n;
    public int solutionSize = 0;
    //mytry
    static int[] xx = new int[20], yy = new int[20];
    static int bound, flg = 0;
    static int[] u = {0, 0, 1, -1};
    static int[] p = {1, -1, 0, 0};
    static myNode mynode = new myNode();
    static String[] directions = {"Right", "Left", "Down", "Up"};
    static List<String> resultPath = new ArrayList<>(); // ���ڱ�����·��
    static final int[] tileSubsets = {-1, 1, 0, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 2, 2};
    static final int[] tilePositions = {-1, 0, 0, 1, 2, 1, 2, 0, 1, 3, 4, 2, 3, 5, 4, 5};
    public static final byte[] costTable0 = new byte[40960],
            costTable1 = new byte[16777216],
            costTable2 = new byte[16777216];
    static class myNode {
        int[] mat = new int[20];
        int pos;

        // ���� h(n)
//        int H() {
//            //������
//            int ans = 0;
//            for (int i = 0; i < 15; ++i) {
//                ans += Math.abs(xx[mat[i]] - (i >> 2)) + Math.abs(yy[mat[i]] - (i & 3));
//            }
//            return ans;
//        }
        int H_disjoint(){
            int index0 = 0, index1 = 0, index2 = 0;
            for (int i = n - 1; i >= 0; --i) {
                for (int j = n - 1; j >= 0; --j) {
                    int p = i * n + j;
                    final int tile = mat[p];
                    if (mat[p] != 0) {
                        final int subsetNumber = tileSubsets[tile];
                        switch (subsetNumber) {
                            case 2:
                                index2 |= (i * n + j) << (tilePositions[tile] << 2);//pos*(2^(tilePositions[tile]*4))
                                break;
                            case 1:
                                index1 |= (i * n + j) << (tilePositions[tile] << 2);
                                break;
                            default:
                                index0 |= (i * n + j) << (tilePositions[tile] << 2);
                                break;
                        }
                    }
                }
            }
            return costTable0[index0] + costTable1[index1] + costTable2[index2];
        }

    }

    public IdAStar(Frontier frontier) {
        super(frontier);
    }
    public static void readBinaryFile(String filePath,byte[] array) {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            // ��ȡ�ļ����ݵ��ֽ�����
            fis.read(array);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Deque<Node> search(Problem problem) {
//       �����ida*������д��ʹ�����Ű汾1104
        //�����idaֻ������4��������
        //��ʼ��
        readBinaryFile("resources/cost0.bin",costTable0);
        readBinaryFile("resources/cost1.bin",costTable1);
        readBinaryFile("resources/cost2.bin",costTable2);
        this.initialState = ((PuzzleBoard) problem.getInitialState()).getPuzzle_board();
        this.n = problem.getSize();
        for(int i = 0 ; i < n; i ++){
            for(int j = 0; j < n; j++){
                int index = i * n + j;
                int k = initialState[i][j];
                if(initialState[i][j] == 0) {
                    mynode.pos = index;
                    mynode.mat[index] = 0;
                }
                else{
                    mynode.mat[index] = k;
                    xx[k] = index >> 2; // ���� k �Ķ�ά����
                    yy[k] = index & 3;  // i % 4
                }
            }
        }


        // ��Ŀ��״̬���ʼ״̬���£���ʹ��disjointʱ�����ע�͵�
//        for (int i = 0; i < 16; ++i) {
//            mynode.mat[i] = i + 1;
//        }
//        mynode.mat[15] = 0;
//        mynode.pos = 15;

        // IDA* ����
        flg = 0;
        for (bound = mynode.H_disjoint(); bound <= 80; bound = dfs(0, mynode.H_disjoint(), 4)) {
            if (flg == 1) break;
        }

        return null;
    }
    static boolean ok(int x, int y) {
        if (x > y) {
            int temp = x;
            x = y;
            y = temp;
        }
        if (x == 0 && y == 1) return false;
        if (x == 2 && y == 3) return false;
        return true;
    }
     int dfs(int step, int h, int las) {
        if (step + h > bound) return step + h; // ���� f(n)
        if (h == 0) { // ��������״̬
            solutionSize = step;
//            System.out.println("Path: " + resultPath);
            flg = 1;
            return step;
        }
        int ret = 127, pos = mynode.pos, x = pos >> 2, y = pos & 3;
        int dx, dy, tar, ht, tmp;
        for (int i = 0; i < 4; ++i) { // ���ĸ�������չ
            dx = x + u[i];
            dy = y + p[i];
            //ͳ�����ɽڵ�
            nodesGenerated ++;
            if (dx < 0 || dy < 0 || dx > 3 || dy > 3 || !ok(i, las) ) continue;
            tar = (dx << 2) | dy; // ������չ���½ڵ��һά����
            mynode.mat[pos] = mynode.mat[tar];
            mynode.mat[tar] = 0; // swap ����
            mynode.pos = tar;
//            ht = h - (Math.abs(xx[mynode.mat[pos]] - dx) + Math.abs(yy[mynode.mat[pos]] - dy))
//                    + Math.abs(xx[mynode.mat[pos]] - x) + Math.abs(yy[mynode.mat[pos]] - y); // �����µ� h ֵ
            // disjoint H����
            ht = mynode.H_disjoint();
            resultPath.add(directions[i]); // ��¼����
            //ͳ����չ�ڵ�
            nodesExpanded++;
            tmp = dfs(step + 1, ht, i);
            if (flg == 1) return tmp; // �ҵ�·��
            if (ret > tmp) ret = tmp; // ���� bound
            resultPath.remove(resultPath.size() - 1); // ����ʱ�Ƴ����һ��
            mynode.mat[tar] = mynode.mat[pos]; // ����
            mynode.mat[pos] = 0;
            mynode.pos = pos;

        }
        return ret;
    }
}

