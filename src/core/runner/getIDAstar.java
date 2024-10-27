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
    static List<String> resultPath = new ArrayList<>(); // 用于保存结果路径

    static class Node {
        int[] mat = new int[20];
        int pos;

        // 计算 h(n)
        int H() {
            int ans = 0;
            for (int i = 0; i < 15; ++i) {
                ans += Math.abs(xx[mat[i]] - (i >> 2)) + Math.abs(yy[mat[i]] - (i & 3));
            }
            return ans;
        }

        // 判断解的存在性
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

    // 防止从走过来的地方再退回去
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
        if (step + h > bound) return step + h; // 更新 f(n)
        if (h == 0) { // 到达最终状态
            System.out.println("Steps: " + step);
            System.out.println("Path: " + resultPath);
            flg = 1;
            return step;
        }
        int ret = 127, pos = a.pos, x = pos >> 2, y = pos & 3;
        int dx, dy, tar, ht, tmp;
        for (int i = 0; i < 4; ++i) { // 向四个方向拓展
            dx = x + u[i];
            dy = y + p[i];
            if (dx < 0 || dy < 0 || dx > 3 || dy > 3 || !ok(i, las)) continue;
            tar = (dx << 2) | dy; // 计算拓展出新节点的一维坐标
            a.mat[pos] = a.mat[tar];
            a.mat[tar] = 0; // swap 操作
            a.pos = tar;
            ht = h - (Math.abs(xx[a.mat[pos]] - dx) + Math.abs(yy[a.mat[pos]] - dy))
                    + Math.abs(xx[a.mat[pos]] - x) + Math.abs(yy[a.mat[pos]] - y); // 计算新的 h 值
            resultPath.add(directions[i]); // 记录方向
            tmp = dfs(step + 1, ht, i);
            if (flg == 1) return tmp; // 找到路径
            if (ret > tmp) ret = tmp; // 更新 bound
            resultPath.remove(resultPath.size() - 1); // 回溯时移除最后一步
            a.mat[tar] = a.mat[pos]; // 回溯
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
                a.pos = i; // 记录 0 的位置
            } else {
                a.mat[i] = k;
                xx[k] = i >> 2; // 保存 k 的二维坐标
                yy[k] = i & 3;  // i % 4
            }
        }
//        if (!a.check()) { // 判断解的存在性
//            System.out.println("No");
//            return;
//        }

        // 从目标状态向初始状态更新
        for (int i = 0; i < 16; ++i) {
            a.mat[i] = i + 1;
        }
        a.mat[15] = 0;
        a.pos = 15;
        long start = System.currentTimeMillis();
        // IDA* 部分
        for (bound = a.H(); bound <= 80; bound = dfs(0, a.H(), 4)) {
            if (flg == 1) break;
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start) + "ms");
    }
}
