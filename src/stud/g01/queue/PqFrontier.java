package stud.g01.queue;

import core.solver.queue.Frontier;
import core.solver.queue.Node;
import stud.queue.QueueFrontier;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PqFrontier extends PriorityQueue<Node> implements Frontier {

    public PqFrontier(){
        super((Comparator<Node>)(node1,node2)->
            node1.evaluation() != node2.evaluation() ? node1.evaluation() - node2.evaluation():
                    node1.getHeuristic() - node2.getHeuristic()
        );
    }
    @Override
    public boolean contains(Node node) {
        return false;
    }

    @Override
    public boolean offer(Node node) {
        super.offer(node);
        return true;
    }

}
