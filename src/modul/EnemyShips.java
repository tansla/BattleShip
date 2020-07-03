package modul;

import java.util.HashMap;
import java.util.Map;

public class EnemyShips extends BaseField {

    private  Coordinate myShot;
    private Coordinate lastSuccesShot;
    private boolean isInjured;

    private Map<Integer, Integer> listOfShips = new HashMap<>();


    protected static final int SEARCH4 = -4;
    protected static final int SEARCH3 = -3;
    protected static final int SEARCH2 = -2;

    // Метод определяющий стратегию поиска кораблей на чужом поле
    public void initStrategy() {
        isInjured = false;

        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                setCellValue(new Coordinate(i,j), UNKNOWN);
            }
        }

        generateGrid(2,SEARCH2);
        generateGrid(3,SEARCH3);
        generateGrid(4,SEARCH4);

    }

    private void generateGrid(int step, int value) {
        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                if ((i + j + 1) % step == 0) setCellValue(new Coordinate(i,j), value);
            }
        }
    }



    private int findWhichShipDown(Coordinate c, boolean isHorizontal) {
        Coordinate copyC = new Coordinate(c.getX(),c.getY());
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        if (isHorizontal) {
            // направо
            while (getCellValue(c.getRight()) == DECK_HIT) {
                shipLength++;
                c = c.getRight();
            }
            //налево
            while (getCellValue(copyC.getLeft()) == DECK_HIT) {
                shipLength++;
                copyC = copyC.getLeft();
            }
        } else {
            // вниз
            while (getCellValue(c.getDown()) == DECK_HIT) {
                shipLength++;
                c = c.getDown();
            }
            //вверх
            while (getCellValue(copyC.getUp()) == DECK_HIT) {
                shipLength++;
                copyC = copyC.getUp();
            }
        }
        return shipLength ;
    }

    private void setEmptyNextToInjuredCell(Coordinate c){
        if(getCellValue(c.getLeftUp()) < EMPTY) setCellValue(c.getLeftUp(), EMPTY);
        if(getCellValue(c.getRightUp()) < EMPTY) setCellValue(c.getRightUp(), EMPTY);
        if(getCellValue(c.getLeftDown()) < EMPTY) setCellValue(c.getLeftDown(), EMPTY);
        if(getCellValue(c.getRightDown()) < EMPTY) setCellValue(c.getRightDown(), EMPTY);
    }

    private void setEmptyAroundDeadShip(Coordinate c, int length, boolean isHorizontal) {

        if (length == 1){
            if(getCellValue(c.getRight()) < EMPTY) setCellValue(c.getRight(),  EMPTY);
            if(getCellValue(c.getLeft()) < EMPTY) setCellValue(c.getLeft(), EMPTY);
            if(getCellValue(c.getUp()) < EMPTY) setCellValue(c.getUp(), EMPTY);
            if(getCellValue(c.getDown()) < EMPTY) setCellValue(c.getDown(), EMPTY);

        } else if(isHorizontal) {
            // horizontal ship
            Coordinate copyC = new Coordinate(c.getX(),c.getY());
            // to the right
            do {
                c = c.getRight();
                if (getCellValue(c) < EMPTY) {
                    setCellValue(c, EMPTY);
                }
            } while (getCellValue(c) == DECK_HIT);
            //to the left
            do {
                copyC = copyC.getLeft();
                if (getCellValue(copyC) < EMPTY) {
                    setCellValue(copyC, EMPTY);
                }
            } while (getCellValue(copyC) == DECK_HIT);

        } else {
            // vertical ship
            Coordinate copyC = new Coordinate(c.getX(),c.getY());
            // up
            do {
                c = c.getUp();
                if (getCellValue(c) < EMPTY) {
                    setCellValue(c, EMPTY);
                }
            } while (getCellValue(c) == DECK_HIT);
            //down
            do {
                copyC = copyC.getDown();
                if (getCellValue(copyC) < EMPTY) {
                    setCellValue(copyC, EMPTY);
                }
            } while (getCellValue(copyC) == DECK_HIT);
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

                lastSuccesShot = new Coordinate(myShot.getX(),myShot.getY());
                setEmptyNextToInjuredCell(myShot);
                break;
            case SHIP_DOWN:
                setCellValue(myShot, DECK_HIT);
                isInjured = false;

                // check which ship is down
                int lengthOfShipHorizontal = findWhichShipDown(myShot,true);
                int lentghOfShipVertical = findWhichShipDown(myShot,false);
                int lentghOfShip =  Math.max(lengthOfShipHorizontal,lentghOfShipVertical);
                boolean isHorizontal = lengthOfShipHorizontal > lentghOfShipVertical;

                // add this info to the map type of ship - count of down ship
                if(listOfShips.containsKey(lentghOfShip)) {
                    listOfShips.put(lentghOfShip,listOfShips.get(lentghOfShip) +1);
                }
                listOfShips.put(lentghOfShip,1);

                // set EMPTY around dead ship
                setEmptyNextToInjuredCell(myShot);
                setEmptyAroundDeadShip(myShot,lentghOfShip,isHorizontal);

                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + shot);
        }
    }




    private int chooseStrategy(){
       if (!listOfShips.containsKey(4)) return SEARCH4;
       if (!listOfShips.containsKey(3)) return SEARCH3;
       if (listOfShips.containsKey(3) && listOfShips.get(3) <2) return SEARCH2;
       if (!listOfShips.containsKey(2)) return SEARCH2;
       return UNKNOWN;
    }

    /** MakeShot - Метод возвращающий координаты того места, куда вы хотите сделать выстрел.

      Например, Вы стреляете в ячейку  5(строка) 3(столбец), тогда должен получиться массив,
                                который заполнен так:
                                myShot[0] = 3 - значение по X(номер столбца)
                                myShot[1] = 5  - значение по Y(номер строки)
     */
    public int[] makeShot(){

        if(isInjured) {
            //  search next to cell
            // looking right
            myShot = new Coordinate(lastSuccesShot.getX(),lastSuccesShot.getY());
            do{
                myShot = myShot.getRight();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            } while (getCellValue(myShot) > EMPTY );
            // looking left
            myShot = new Coordinate(lastSuccesShot.getX(),lastSuccesShot.getY());
            do{
                myShot = myShot.getLeft();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }

            } while (getCellValue(myShot) > EMPTY ) ;

            // looking up
            myShot = new Coordinate(lastSuccesShot.getX(),lastSuccesShot.getY());
            do{
                myShot = myShot.getUp();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }

            } while (getCellValue(myShot) > EMPTY ) ;

            // looking down
            myShot = new Coordinate(lastSuccesShot.getX(),lastSuccesShot.getY());
            do {
                myShot = myShot.getDown();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            } while (getCellValue(myShot) > EMPTY ) ;

        } else {
            // looking for the next cell from the field
            int myStrategy = chooseStrategy();

            // полный перебор клеток - что осталось
            if(myStrategy == UNKNOWN) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        if(getCellValue( new Coordinate(i,j))  < EMPTY){
                            myShot = new Coordinate(i,j);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                    }
                }
            }

            // поиск 4-3-2
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if(getCellValue(new Coordinate(i,j)) == myStrategy) {
                        myShot = new Coordinate(i,j);
                        return new int[]{myShot.getX(), myShot.getY()};
                    }
                }
            }

        }

        return new int[]{myShot.getX(), myShot.getY()};
    }



}
