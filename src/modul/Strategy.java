package modul;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Strategy {
    private final Iterator<Coordinate> iterator;
    private List<Coordinate> coordinateList = new LinkedList<>();

    public  Strategy(int step) {
        for (int i = 0; i < BaseField.FIELD_LENGTH; i++) {
            for (int j = 0; j < BaseField.FIELD_LENGTH; j++) {
                if ((i + j + 1) % step == 0) {
                    coordinateList.add(new Coordinate(i, j));
                }
            }
        }

        iterator = coordinateList.iterator();
/*
        int square = BaseField.FIELD_LENGTH * BaseField.FIELD_LENGTH;
        for (int i = 0; i < square; i += step) {
            int x = i / BaseField.FIELD_LENGTH;
            int y = i % BaseField.FIELD_LENGTH;
            System.out.println(""+x+":"+y);
            }

 */
    }

    public Coordinate findNext(){
        return iterator.hasNext()?iterator.next():null;
    }

}
