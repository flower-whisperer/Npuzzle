package stud.g01.problem.npuzzle;

import core.problem.Action;
import stud.problem.pathfinding.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Move extends Action {
//    һ�дӼ򣬲���д�������ˣ�ֱ����һ��char��ʾһ���ƶ�
    //�����������ģ�direction��N�����Ӧ��Ҫ��#������λ��ƫ����(-1,0)
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
//        ��npuzzle�����е�f(n),��ʾ�ӳ�ʼ״̬����ǰ״̬�ĺ�ɢֵ
//        ���ֵʹ���ƶ���������ʾ�������������ĸ������ƶ���cost����1
        return 1;
    }

}
