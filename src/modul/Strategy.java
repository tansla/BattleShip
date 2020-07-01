package modul;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

public class Strategy extends Field {

    private int[] myShot = new int[2];
    private int[] lastSuccesShot = new int[2];
    private int strategyStage;
    private boolean isInjured;
  //  private String name;

    private Map<Integer, Integer> listOfShips = new HashMap<>();

    protected static final int DEAD = 2;
    protected static final int SEARCH4 = -4;
    protected static final int SEARCH3 = -3;
    protected static final int SEARCH2 = -2;
    protected static final int UNKNOWN = -1;

    // Метод определяющий стратегию поиска кораблей на чужом поле
    public void initStrategy() {
        strategyStage = SEARCH4;
        isInjured = false;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) myField[i][j] = UNKNOWN;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 2 == 0) myField[i][j] = SEARCH2;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 3 == 0) myField[i][j] = SEARCH3;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 4 == 0) myField[i][j] = SEARCH4;
            }
        }

    }



    private int findWhichShipDown(int[] myShot, boolean isHorizontal) {
        int x = myShot[0];
        int y = myShot[1];
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        int shipPoints = checkWhatInPlace(x, y); // тк текущая клетка подбита - сохраняем ее статус
        if (isHorizontal) {
            // направо
            for (int i = 1; i <= 3; i++) {
                if (checkWhatInPlace(x + i, y) <= EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + Math.max(EMPTY,checkWhatInPlace(x + i, y));
                }
            }
            //налево
            for (int i = 1; i <= 3; i++) {
                if (checkWhatInPlace(x - i, y) <= EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + Math.max(EMPTY,checkWhatInPlace(x - i, y));
                }
            }
        } else {
            // вниз
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x ,y+i) <= EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + Math.max(EMPTY,checkWhatInPlace(x , y+i));
                }
            }
            //вверх
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x ,y-i) <= EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + Math.max(EMPTY,checkWhatInPlace(x , y -i));
                }
            }
        }
        return shipLength ;
    }

    private void setEmptyNextToInjuredCell(int[] myShot){
        int x = myShot[0];
        int y = myShot[1];
        if(checkWhatInPlace(x-1,y-1) < 0) myField[x-1][y-1] = EMPTY;
        if(checkWhatInPlace(x-1,y+1) < 0) myField[x-1][y+1] = EMPTY;
        if(checkWhatInPlace(x+1,y+1) < 0) myField[x+1][y+1] = EMPTY;
        if(checkWhatInPlace(x+1,y-1) < 0) myField[x+1][y-1] = EMPTY;

    }


    // shotProcessing - Метод обрабатывающий ответ противника, после вашего выстрела

    public void shotProcessing(int shot) {
        /*
        Входной параметр: shot - может принимать следующие значения : "Мимо"-(0), "Ранен"-(1), "Убит"-(2)
         */
        int x = myShot[0];
        int y = myShot[1];
        switch (shot){
            case EMPTY:
                this.myField[myShot[0]][myShot[1]] = EMPTY;
                break;
            case DECK:
                this.myField[myShot[0]][myShot[1]] = INJURED;
                isInjured = true;
                lastSuccesShot[0] = myShot[0];
                lastSuccesShot[1] = myShot[1];
                setEmptyNextToInjuredCell(myShot);
                break;
            case DEAD:
                this.myField[myShot[0]][myShot[1]] = INJURED;
                isInjured = false;

                // check which ship is down
                int lengthOfShipHorizontal = findWhichShipDown(myShot,true);
                int lentghOfShipVertical = findWhichShipDown(myShot,false);
                int lentghOfShip =  Math.max(lengthOfShipHorizontal,lentghOfShipVertical);

                // add this info to the map type of ship - count of down ship
                if(listOfShips.containsKey(lentghOfShip)) {
                    listOfShips.put(lentghOfShip,listOfShips.get(lentghOfShip) +1);
                }
                listOfShips.put(lentghOfShip,1);

                // set EMPTY around dead ship
                setEmptyNextToInjuredCell(myShot);

                if (lentghOfShip == 1){
                    if(checkWhatInPlace(x-1,y) < 0) myField[x-1][y] = EMPTY;
                    if(checkWhatInPlace(x+1,y) < 0) myField[x+1][y] = EMPTY;
                    if(checkWhatInPlace(x,y+1) < 0) myField[x][y+1] = EMPTY;
                    if(checkWhatInPlace(x,y-1) < 0) myField[x][y-1] = EMPTY;

                } else if(lengthOfShipHorizontal > lentghOfShipVertical) {
                    // horizontal ship
                    int i = x;
                    // to the right
                    do {
                        i = i + 1;
                        if (checkWhatInPlace(i,y) < EMPTY) {
                            this.myField[i][y] = EMPTY;
                        }
                    } while (checkWhatInPlace(i,y) == INJURED);

                    //to the left
                    do {
                        i = i - 1;
                        if (checkWhatInPlace(i,y) < EMPTY) {
                            this.myField[i][y] = EMPTY;
                        }
                    } while (checkWhatInPlace(i,y) == INJURED);
                } else {
                    // vertical ship
                    // up
                    int j = y;
                    do {
                        j = j + 1;
                        if (checkWhatInPlace(x,j) < EMPTY) {
                            this.myField[x][j] = EMPTY;
                        }
                    } while (checkWhatInPlace(x,j) == INJURED);

                    //to the left
                    do {
                        j = j - 1;
                        if (checkWhatInPlace(x,j) < EMPTY) {
                            this.myField[x][j] = EMPTY;
                        }
                    } while (checkWhatInPlace(x,j) == INJURED);
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + shot);
        }
    }




    private int chooseStrategy(){
       if (!listOfShips.containsKey(4)) return SEARCH4;
       if (!listOfShips.containsKey(3)) return SEARCH3;
       if (listOfShips.containsKey(3) && listOfShips.get(3) <2) return SEARCH3;
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
            int x = lastSuccesShot[0];
            int y = lastSuccesShot[1];
            // looking right
            int i = x;
            while (checkWhatInPlace(i,y) == INJURED) {
                i = i + 1;
                if (checkWhatInPlace(i,y) < EMPTY) {
                    myShot[0] = i;
                    myShot[1] = y;
                    return myShot;
                }
            }

            // looking left
            while (checkWhatInPlace(i,y) == INJURED) {
                i = i - 1;
                if (checkWhatInPlace(i,y) < EMPTY) {
                    myShot[0] = i;
                    myShot[1] = y;
                    return myShot;
                }
            }

            // looking up
            int j = y;
            while (checkWhatInPlace(x,j) == INJURED) {
                j = j + 1;
                if (checkWhatInPlace(x,j) < EMPTY) {
                    myShot[0] = x;
                    myShot[1] = j;
                    return myShot;
                }
            }

            // looking down
            while (checkWhatInPlace(x,j) == INJURED) {
                j = j - 1;
                if (checkWhatInPlace(x,j) < EMPTY) {
                    myShot[0] = x;
                    myShot[1] = j;
                    return myShot;
                }
            }

        } else {
            // looking for the next cell from the field
            int myStrategy = chooseStrategy();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if(checkWhatInPlace(i,j) == myStrategy) {
                        //todo change strategy for UNKNOWN
                        myShot[0] = i;
                        myShot[1] = j;
                        return myShot;
                    }
                }
            }

        }

        return myShot;
    }



}
