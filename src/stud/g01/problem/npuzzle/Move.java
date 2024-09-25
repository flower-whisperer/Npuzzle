package stud.g01.problem.npuzzle;

import core.problem.Action;
import stud.problem.pathfinding.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Move extends Action {
//    一切从简，不再写方向类了，直接用一个char表示一次移动
    //具体是这样的，direction是N，则对应需要与#交换的位置偏移是(-1,0)
    private final char direction;
    Map<Character, int[]> directionsMap = new HashMap<>();


    public Move(char direction) {
        this.direction = direction;
        directionsMap.put('N',new int[]{-1,0});
        directionsMap.put('E',new int[]{0,1});
        directionsMap.put('S',new int[]{1,0});
        directionsMap.put('W',new int[]{0,-1});
    }
    public char getDirection(){return direction;}

    public Map<Character, int[]> getDirectionsMap() {
        return directionsMap;
    }

    @Override
    public void draw() {
        System.out.println(this);
    }

    @Override
    public int stepCost() {
//        在npuzzle问题中的f(n),表示从初始状态到当前状态的耗散值
//        这个值使用移动次数来表示，所以无论往哪个方向移动，cost都是1
        return 1;
    }

}
