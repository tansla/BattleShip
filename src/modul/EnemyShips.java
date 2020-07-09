package modul;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class EnemyShips extends BaseField {

    private  Coordinate myShot;
    private Coordinate lastSuccessShot;
    private boolean isInjured;

    private Map<Integer, Integer> listOfDeadShips = new HashMap<>();
    private List<Coordinate> listForSearch4 = new LinkedList<>();
    private List<Coordinate> listForSearch3 = new LinkedList<>();
    private List<Coordinate> listForSearch2 = new LinkedList<>();




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


       // generateGrid(2,SEARCH2);
       // generateGrid(3,SEARCH3);
       // generateGrid(4,SEARCH4);



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

    /**
     *
     * @param c
     * @param checkValue
     * @param setValue
     */
    private void setCellValueWithCheck(Coordinate c, int checkValue, int setValue){
        if (getCellValue(c) < checkValue) {
            setCellValue(c, setValue);
        }
    }

    private void setEmptyNextToInjuredCell(Coordinate c) {
        setCellValueWithCheck(c.getLeftUp(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getRightUp(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getLeftDown(), EMPTY, EMPTY);
        setCellValueWithCheck(c.getRightDown(), EMPTY, EMPTY);
    }

    private void setEmptyAroundDeadShip(Coordinate c, int length, boolean isHorizontal) {

        if (length == 1){
            setCellValueWithCheck(c.getRight(), EMPTY, EMPTY);
            setCellValueWithCheck(c.getLeft(), EMPTY, EMPTY);
            setCellValueWithCheck(c.getUp(), EMPTY, EMPTY);
            setCellValueWithCheck(c.getDown(), EMPTY, EMPTY);

        } else if(isHorizontal) {
            // horizontal ship
            Coordinate copyC = new Coordinate(c.getX(),c.getY());
            // to the right
            do {
                c = c.getRight();
                setCellValueWithCheck(c, EMPTY, EMPTY);
            } while (getCellValue(c) == DECK_HIT);
            //to the left
            do {
                copyC = copyC.getLeft();
                setCellValueWithCheck(copyC, EMPTY, EMPTY);
            } while (getCellValue(copyC) == DECK_HIT);

        } else {
            // vertical ship
            Coordinate copyC = new Coordinate(c.getX(),c.getY());
            // up
            do {
                c = c.getUp();
                setCellValueWithCheck(c, EMPTY, EMPTY);
            } while (getCellValue(c) == DECK_HIT);
            //down
            do {
                copyC = copyC.getDown();
                setCellValueWithCheck(copyC, EMPTY, EMPTY);
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
    public int[] makeShot() {

        if (isInjured) {
            //  search next to cell
            // looking right
            myShot = new Coordinate(lastSuccessShot.getX(), lastSuccessShot.getY());
            do {
                myShot = myShot.getRight();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            } while (getCellValue(myShot) > EMPTY);
            // looking left
            myShot = new Coordinate(lastSuccessShot.getX(), lastSuccessShot.getY());
            do {
                myShot = myShot.getLeft();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }

            } while (getCellValue(myShot) > EMPTY);

            // looking up
            myShot = new Coordinate(lastSuccessShot.getX(), lastSuccessShot.getY());
            do {
                myShot = myShot.getUp();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }

            } while (getCellValue(myShot) > EMPTY);

            // looking down
            myShot = new Coordinate(lastSuccessShot.getX(), lastSuccessShot.getY());
            do {
                myShot = myShot.getDown();
                if (getCellValue(myShot) < EMPTY) {
                    return new int[]{myShot.getX(), myShot.getY()};
                }
            } while (getCellValue(myShot) > EMPTY);

        } else {
            // looking for the next cell from the field
            int myStrategy = chooseStrategy();

            switch (myStrategy) {
                case SEARCH4:
                    while (!listForSearch4.isEmpty()){
                        if (getCellValue(listForSearch4.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch4.get(0);
                            listForSearch4.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                    }
                case SEARCH3:
                    while (!listForSearch3.isEmpty()){
                        if (getCellValue(listForSearch3.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch3.get(0);
                            listForSearch3.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                    }

                case SEARCH2:
                    while (!listForSearch2.isEmpty()){
                        if (getCellValue(listForSearch2.get(0))==UNKNOWN)
                        {
                            myShot = listForSearch2.get(0);
                            listForSearch2.remove(0);
                            return new int[]{myShot.getX(), myShot.getY()};
                        }
                    }

                default:
                    for (int i = 10; i > 0; i--) {
                        for (int j = 0; j < 10; j++) {
                            if (getCellValue(new Coordinate(i, j)) < EMPTY) {
                                myShot = new Coordinate(i, j);
                                return new int[]{myShot.getX(), myShot.getY()};
                            }
                        }
                    }

            }


            // полный перебор клеток - что осталось



            /*
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

             */


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
