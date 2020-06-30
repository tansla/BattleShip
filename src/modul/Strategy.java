package modul;

import java.util.Map;

public class Strategy extends Field {

    private int[] myShot = new int[2];
    private int strategyStage;
    private boolean isInjured;

    protected static final int SEARCH4 = -4;
    protected static final int SEARCH3 = -3;
    protected static final int SEARCH2 = -2;
    protected static final int UNKNOWN = -1;

    private Map<Integer, Integer> listOfShips;


    private int findWhichShipDown(int[] myShot, boolean isHorizontal) {
        int x = myShot[0];
        int y = myShot[1];
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        int shipPoints = checkWhatInPlace(x, y); // тк текущая клетка подбита - сохраняем ее статус
        if (isHorizontal) {
            // направо
            for (int i = 1; i <= 3; i++) {
                if (checkWhatInPlace(x + i, y) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + checkWhatInPlace(x + i, y);
                }
            }
            //налево
            for (int i = 1; i <= 3; i++) {
                if (checkWhatInPlace(x - i, y) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + checkWhatInPlace(x - i, y);
                }
            }
        } else {
            // вниз
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x ,y+i) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + checkWhatInPlace(x , y+i);
                }
            }
            //вверх
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x ,y-i) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + checkWhatInPlace(x , y -i);
                }
            }
        }
        return shipPoints/shipLength * INJURED;
    }


    // shotProcessing - Метод обрабатывающий ответ противника, после вашего выстрела

    public void shotProcessing(int shot) {
        /*
        Входной параметр: shot - может принимать следующие значения : "Мимо"-(0), "Ранен"-(1), "Убит"-(2)
         */
        switch (shot){
            case EMPTY:
                this.myField[myShot[0]][myShot[1]] = EMPTY;
                isInjured = false;
                break;
            case DECK:
                this.myField[myShot[0]][myShot[1]] = INJURED;
                isInjured = true;
                break;
            case INJURED:
                this.myField[myShot[0]][myShot[1]] = INJURED;
                isInjured = true;
                // todo: check which ship is down
                int lentghOfShip = Math.max(findWhichShipDown(myShot,true)
                                            ,findWhichShipDown(myShot,false));
                if(listOfShips.containsKey(lentghOfShip)) {
                    listOfShips.put(lentghOfShip,listOfShips.get(lentghOfShip) +1);
                }
                listOfShips.put(lentghOfShip,1);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + shot);
        }
        myShot[0] = 0;
        myShot[1] = 0;
    }

    //todo MakeShot - Метод возвращающий координаты того места, куда вы хотите сделать выстрел.
    /*
    Например, Вы стреляете в ячейку  5(строка) 3(столбец), тогда должен получиться массив,
                                который заполнен так:
                                myShot[0] = 3 - значение по X(номер столбца)
                                myShot[1] = 5  - значение по Y(номер строки)
     */
    public int[] makeShot(){


        return myShot;
    }


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


}
