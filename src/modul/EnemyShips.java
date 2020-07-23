package modul;

import java.util.HashMap;
import java.util.Map;

import static modul.Direction.*;

public class EnemyShips extends BaseField {

    private  Coordinate myShot;
    private Coordinate lastSuccessShot;
    private boolean isInjured;

    private Map<Integer, Integer> listOfDeadShips = new HashMap<>();

    private Map<Integer, Strategy> myStrategy = new HashMap<>();

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
        for (int i =1; i<=4; i++){
            myStrategy.put(i,new Strategy(i));
        }
    }

    private int calcShipLengthByDirection(Coordinate c, Direction d){
        Coordinate tmpC = c;
        int shipLength =0;
        while (findCellValue(tmpC.findNextToByDirection(d)) == DECK_HIT) {
            shipLength++;
            tmpC = tmpC.findNextToByDirection(d);
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
        Coordinate tmpC = c;
        do {
            tmpC = tmpC.findNextToByDirection(d);
            setCellValueWithCheck(tmpC, EMPTY, EMPTY);
        } while (findCellValue(tmpC) == DECK_HIT);
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

                // set EMPTY around dead ship
                setEmptyNextToInjuredCell(myShot);
                setEmptyAroundDeadShip(myShot,lengthOfShip,isHorizontal);

                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + shot);
        }
    }

//todo: update how choose strategy
    private int chooseStrategy(){
        if (!listOfDeadShips.containsKey(4)) return 4;
        if (!listOfDeadShips.containsKey(3)) return 3;
        return (!listOfDeadShips.containsKey(2))?2: 1;
    }

    /** MakeShot - Метод возвращающий координаты того места, куда вы хотите сделать выстрел.

      Например, Вы стреляете в ячейку  5(строка) 3(столбец), тогда должен получиться массив,
                                который заполнен так:
                                myShot[0] = 3 - значение по X(номер столбца)
                                myShot[1] = 5  - значение по Y(номер строки)
     */

    private Coordinate lookForNextUnknownCell(Coordinate c, Direction d) {
        Coordinate tmpC = c;
        int cellValue;
        do {
            tmpC = tmpC.findNextToByDirection(d);
            cellValue = findCellValue(tmpC);
            if (cellValue < EMPTY) {
                return tmpC;
            }
        } while (cellValue > EMPTY);
        return null;
    }

    public int[] makeShot() {

        if (isInjured) {
            //todo if there are 2 injured cells - you know direction
            for (Direction d : straightDirections) {
                Coordinate nextShotCandidate = lookForNextUnknownCell(lastSuccessShot, d);
                if(nextShotCandidate != null) {
                    myShot = nextShotCandidate;
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            }
        } else {
            // looking for the next cell from the field
            int currentStrategy = chooseStrategy();
            do {
                    myShot = myStrategy.get(currentStrategy).findNext();
                } while(findCellValue(myShot) >=EMPTY);
                return new int[]{myShot.getX(), myShot.getY()};
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
