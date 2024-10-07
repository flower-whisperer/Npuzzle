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
     * ���캯��
     *
     * @param frontier Node�����һ�����ȶ��У�����ȷ��һ��״̬����Ӧ�Ľ���Ƿ���frontier�У�
     * @param predictor �����Ԥ�����������پ��룩
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
        //ida�㷨����Ҫ�洢���нڵ㣬ֻ������·���ڵ㣬���ʡ�ڴ棬����Ҫ���޶���
        Node root = problem.root(predictor);
        int threshold = root.getHeuristic();//�ѳ�ʼ״̬������������ֵ��Ϊ����

        while(true)
        {
            System.out.println(threshold);
            Pair<Deque<Node>,Integer> result = find(root,0,threshold,problem);
            if(result.getFirst() != null)
                return result.getFirst();
            if(result.getSecond() == null || result.getSecond() == Integer.MAX_VALUE)
                return null;//û�и�С��ֵ���޽�
            threshold = result.getSecond();
        }

    }
    public Pair<Deque<Node>,Integer> find(Node node,int g,int threshold,Problem problem){
        int f = g + node.getHeuristic();//���㵱ǰfֵ
        //������ֵ�����س�����ֵ����Сf���ڸ���
        if(f > threshold)
            return new Pair<>(null,f);
        //�ִ�Ŀ��״̬������·��
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
