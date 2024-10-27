package core.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

 class FifteenPuzzle {
    static int[] xx = new int[20], yy = new int[20];
    static int bound, flg;
    static int[] u = {0, 0, 1, -1};
    static int[] p = {1, -1, 0, 0};
    static String[] directions = {"Right", "Left", "Down", "Up"};
    static Node a = new Node();
    static List<String> resultPath = new ArrayList<>(); // ���ڱ�����·��

    static class Node {
        int[] mat = new int[20];
        int pos;

        // ���� h(n)
        int H() {
            int ans = 0;
            for (int i = 0; i < 15; ++i) {
                ans += Math.abs(xx[mat[i]] - (i >> 2)) + Math.abs(yy[mat[i]] - (i & 3));
            }
            return ans;
        }

        // �жϽ�Ĵ�����
//        boolean check() {
//            int tot = 0;
//            for (int i = 0; i < 16; ++i) {
//                if (mat[i] == 0) continue;
//                for (int j = 0; j < i; ++j) {
//                    if (mat[j] < mat[i]) ++tot;
//                }
//            }
//            tot += Math.abs((pos >> 2) - 3) + Math.abs((pos & 3) - 3);
//            return (tot & 1) == 0;
//        }
    }

    // ��ֹ���߹����ĵط����˻�ȥ
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

    static int dfs(int step, int h, int las) {
        if (step + h > bound) return step + h; // ���� f(n)
        if (h == 0) { // ��������״̬
            System.out.println("Steps: " + step);
            System.out.println("Path: " + resultPath);
            flg = 1;
            return step;
        }
        int ret = 127, pos = a.pos, x = pos >> 2, y = pos & 3;
        int dx, dy, tar, ht, tmp;
        for (int i = 0; i < 4; ++i) { // ���ĸ�������չ
            dx = x + u[i];
            dy = y + p[i];
            if (dx < 0 || dy < 0 || dx > 3 || dy > 3 || !ok(i, las)) continue;
            tar = (dx << 2) | dy; // ������չ���½ڵ��һά����
            a.mat[pos] = a.mat[tar];
            a.mat[tar] = 0; // swap ����
            a.pos = tar;
            ht = h - (Math.abs(xx[a.mat[pos]] - dx) + Math.abs(yy[a.mat[pos]] - dy))
                    + Math.abs(xx[a.mat[pos]] - x) + Math.abs(yy[a.mat[pos]] - y); // �����µ� h ֵ
            resultPath.add(directions[i]); // ��¼����
            tmp = dfs(step + 1, ht, i);
            if (flg == 1) return tmp; // �ҵ�·��
            if (ret > tmp) ret = tmp; // ���� bound
            resultPath.remove(resultPath.size() - 1); // ����ʱ�Ƴ����һ��
            a.mat[tar] = a.mat[pos]; // ����
            a.mat[pos] = 0;
            a.pos = pos;
        }
        return ret;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < 16; ++i) {
            int k = scanner.nextInt();
            if (k == 0) {
                a.pos = i; // ��¼ 0 ��λ��
            } else {
                a.mat[i] = k;
                xx[k] = i >> 2; // ���� k �Ķ�ά����
                yy[k] = i & 3;  // i % 4
            }
        }
//        if (!a.check()) { // �жϽ�Ĵ�����
//            System.out.println("No");
//            return;
//        }

        // ��Ŀ��״̬���ʼ״̬����
        for (int i = 0; i < 16; ++i) {
            a.mat[i] = i + 1;
        }
        a.mat[15] = 0;
        a.pos = 15;
        long start = System.currentTimeMillis();
        // IDA* ����
        for (bound = a.H(); bound <= 80; bound = dfs(0, a.H(), 4)) {
            if (flg == 1) break;
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start) + "ms");
    }
}
