package modul;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import static modul.Direction.*;

public class EnemyShips extends BaseField {

    private  Coordinate myShot;
    private Coordinate lastSuccessShot;
    private boolean isInjured;

    private Map<Integer, Integer> listOfDeadShips = new HashMap<>();
    private List<Coordinate> listForSearch4 = new LinkedList<>();
    private List<Coordinate> listForSearch3 = new LinkedList<>();
    private List<Coordinate> listForSearch2 = new LinkedList<>();

    private final Direction[] straightDirections = {RIGHT,DOWN,LEFT,UP};


    protected static final int SEARCH4 = -4;
    protected static final int SEARCH3 = -3;
    protected static final int SEARCH2 = -2;
    protected static final int SEARCH1 = -1;

    // Метод определяющий стратегию поиска кораблей на чужом поле
    public void initStrategy() {
        isInjured = false;

        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                setCellValue(new Coordinate(i,j), UNKNOWN);
            }
        }
        listForSearch4 = generateStrategy(4);
        listForSearch3 = generateStrategy(3);
        listForSearch2 = generateStrategy(2);

    }

    private List generateStrategy(int step) {
        List listForSearch = new LinkedList<Coordinate>();
        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                if ((i + j + 1) % step == 0) {
                    listForSearch.add(new Coordinate(i,j));
                }
            }
        }
        return listForSearch;
    }

    private int calcShipLengthByDirection(Coordinate c, Direction d){
        int shipLength =0;
        while (findCellValue(c.findNextToByDirection(d)) == DECK_HIT) {
            shipLength++;
            c = c.findNextToByDirection(d);
        }
        return shipLength;
    }

    private int findWhichShipDown(Coordinate c, boolean isHorizontal) {
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        if (isHorizontal) {
            shipLength += calcShipLengthByDirection(c,RIGHT);
            shipLength += calcShipLengthByDirection(c,LEFT);
        } else {
            shipLength += calcShipLengthByDirection(c,DOWN);
            shipLength += calcShipLengthByDirection(c,UP);
        }
        return shipLength ;
    }

    /**
     *
     * @param c
     * @param checkValue
     * @param setValue
     */
    private void setCellValueWithCheck(Coordinate c, int checkValue, int setValue){
        if (findCellValue(c) < checkValue) {
            setCellValue(c, setValue);
        }
    }

    private void setEmptyNextToInjuredCell(Coordinate c) {
        setCellValueWithCheck(c.getLeftUp(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getRightUp(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getLeftDown(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getRightDown(), EMPTY, EMPTY);
    }

    private void setEmptyToDirection(Coordinate c, Direction d){
        do {
            c = c.findNextToByDirection(d);
            setCellValueWithCheck(c, EMPTY, EMPTY);
        } while (findCellValue(c) == DECK_HIT);
    }

    private void setEmptyAroundDeadShip(Coordinate c, int length, boolean isHorizontal) {

        if (length == 1){
            for(Direction d:straightDirections) {
                setCellValueWithCheck(c.findNextToByDirection(d),EMPTY,EMPTY);
            }
        } else if(isHorizontal) {
            // horizontal ship
            setEmptyToDirection(c, RIGHT);
            setEmptyToDirection(c, LEFT);
        } else {
            setEmptyToDirection(c, UP);
            setEmptyToDirection(c, DOWN);
        }
    }


    // shotProcessing - Метод обрабатывающий ответ противника, после вашего выстрела

    public void shotProcessing(int shot) {
        /*
        Входной параметр: shot - может принимать следующие значения : "Мимо"-(0), "Ранен"-(1), "Убит"-(2)
         */
        switch (shot){
            case EMPTY:
                setCellValue(myShot,EMPTY);
                break;
            case SHIP_INJURED:
                setCellValue(myShot, DECK_HIT);
                isInjured = true;

                lastSuccessShot = new Coordinate(myShot.getX(),myShot.getY());
                setEmptyNextToInjuredCell(myShot);
                break;
            case SHIP_DOWN:
                setCellValue(myShot, DECK_HIT);
                isInjured = false;

                // check which ship is down
                int lengthOfShipHorizontal = findWhichShipDown(myShot,true);
                int lengthOfShipVertical   = findWhichShipDown(myShot,false);
                int lengthOfShip =  Math.max(lengthOfShipHorizontal,lengthOfShipVertical);
                boolean isHorizontal = lengthOfShipHorizontal > lengthOfShipVertical;

                // add this info to the map type of ship - count of down ship
                Integer shipCountObj = listOfDeadShips.get(lengthOfShip);
                int shipCount = (shipCountObj == null) ? 1 : shipCountObj + 1;
                listOfDeadShips.put(lengthOfShip, shipCount);
                if(shipCount > 4) {
                    System.out.println("Dead " + lengthOfShip + " become " + shipCount);
                }
                // set EMPTY around dead ship
                setEmptyNextToInjuredCell(myShot);
                setEmptyAroundDeadShip(myShot,lengthOfShip,isHorizontal);

                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + shot);
        }
    }




    private int chooseStrategy(){
        //if(listForSearch4.isEmpty() && listForSearch2.isEmpty()) return SEARCH1;
        if (!listOfDeadShips.containsKey(4)) return SEARCH4;
        if (!listOfDeadShips.containsKey(3)) return SEARCH3;
        return (!listOfDeadShips.containsKey(2))?SEARCH2: SEARCH1;

       //if (!listOfDeadShips.containsKey(3)) return SEARCH3;
       //if (listOfDeadShips.containsKey(3) && listOfDeadShips.get(3) <2) return SEARCH2;
    }

    /** MakeShot - Метод возвращающий координаты того места, куда вы хотите сделать выстрел.

      Например, Вы стреляете в ячейку  5(строка) 3(столбец), тогда должен получиться массив,
                                который заполнен так:
                                myShot[0] = 3 - значение по X(номер столбца)
                                myShot[1] = 5  - значение по Y(номер строки)
     */

    private Coordinate lookForNextUnknownCell(Coordinate c, Direction d) {
        int cellValue;
        do {
            c = c.findNextToByDirection(d);
            cellValue = findCellValue(c);
            if (cellValue < EMPTY) {
                return c;
            }
        } while (cellValue > EMPTY);
        return null;
    }

    public int[] makeShot() {

        if (isInjured) {
            for (Direction d : straightDirections) {
                Coordinate nextShotCandidate = lookForNextUnknownCell(lastSuccessShot, d);
                if(nextShotCandidate != null) {
                    myShot = nextShotCandidate;
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            }
        } else {
            // looking for the next cell from the field
            int myStrategy = chooseStrategy();

            switch (myStrategy) {
                case SEARCH4:
                    while (!listForSearch4.isEmpty()){
                        if (findCellValue(listForSearch4.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch4.get(0);
                            listForSearch4.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                        listForSearch4.remove(0);
                    }
                case SEARCH3:
                    while (!listForSearch3.isEmpty()){
                        if (findCellValue(listForSearch3.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch3.get(0);
                            listForSearch3.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                        listForSearch3.remove(0);
                    }

                case SEARCH2:
                    while (!listForSearch2.isEmpty()){
                        if (findCellValue(listForSearch2.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch2.get(0);
                            listForSearch2.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                        listForSearch2.remove(0);
                    }

                default:
                    for (int i = 10; i > 0; i--) {
                        for (int j = 0; j < 10; j++) {
                            if (findCellValue(new Coordinate(i, j)) < EMPTY) {
                                myShot = new Coordinate(i, j);
                                return new int[]{myShot.getX(), myShot.getY()};
                            }
                        }
                    }

            }

        }
        return new int[]{0, 0};
    }


    public String getListOfDeadShips(){
        StringBuilder list = new StringBuilder();
        for (Map.Entry<Integer,Integer> entry : listOfDeadShips.entrySet()) {
            list.append(entry.getKey())
                .append(": ")
                .append(entry.getValue())
                .append('\n');
        }
        return list.toString();
    }
    
}
