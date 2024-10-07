package stud.g01.solver;

import core.problem.Problem;
import core.solver.algorithm.heuristic.Predictor;
import core.solver.algorithm.searcher.AbstractSearcher;
import core.solver.queue.Frontier;
import core.solver.queue.Node;

import java.util.Deque;

class Pair<T, U> {
    T first;
    U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
public class IdAStar extends AbstractSearcher {
    private final Predictor predictor;
    /**
     * 构造函数
     *
     * @param frontier Node对象的一个优先队列，可以确定一个状态所对应的结点是否在frontier中，
     * @param predictor 具体的预测器（曼哈顿距离）
     */
    public IdAStar(Frontier frontier, Predictor predictor) {

        super(frontier);
        this.predictor = predictor;
    }

    @Override
    public Deque<Node> search(Problem problem) {
        System.out.println("test");
        if(!problem.solvable()){
            return null;
        }
        //ida算法不需要存储所有节点，只存最终路径节点，因此省内存，不需要有限队列
        Node root = problem.root(predictor);
        int threshold = root.getHeuristic();//把初始状态的启发函数估值作为门限

        while(true)
        {
            System.out.println(threshold);
            Pair<Deque<Node>,Integer> result = find(root,0,threshold,problem);
            if(result.getFirst() != null)
                return result.getFirst();
            if(result.getSecond() == null || result.getSecond() == Integer.MAX_VALUE)
                return null;//没有更小阈值，无解
            threshold = result.getSecond();
        }

    }
    public Pair<Deque<Node>,Integer> find(Node node,int g,int threshold,Problem problem){
        int f = g + node.getHeuristic();//计算当前f值
        //超过阈值，返回超过阈值的最小f用于更新
        if(f > threshold)
            return new Pair<>(null,f);
        //抵达目标状态，构建路径
        if(problem.goal(node.getState()))
            return new Pair<>(generatePath(node),null);
        int min = Integer.MAX_VALUE;
        for(Node child : problem.childNodes(node,predictor)){
            Pair<Deque<Node>,Integer> result = find(child,g+1,threshold,problem);
            if(result.getFirst() != null)
                return result;
            if(result.getSecond() < min)
                min = result.getSecond();
        }
        return new Pair<>(null,min);
    }
}
