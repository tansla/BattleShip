package modul;

import java.util.Random;

public class Field {
    //    private static int[][] field= new int[10][10];
    protected int[][] myField = new int[10][10];


    protected static final int EMPTY = 0;
    protected static final int DECK = 1;
    protected static final int INJURED = 2;

    public Field() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                myField[i][j] = EMPTY;
            }
        }
    }


    public void print() {
        System.out.println("---------- start ---------");
        for (int i = 0; i < 10; i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print( i+" :");
            for (int j = 0; j < 10; j++) {
                System.out.print("\t" + myField[j][i]);
            }
            System.out.println(":\t" + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        System.out.println("----------- end ----------");
    }

    private boolean isInside(int x, int y) {
        return ((x >= 0 && x <=9) && (y >= 0 && y <=9));
    }

    private boolean checkPlace(int x, int y) {
        if (!isInside(x,y)) return false;
        for (int tryX = x-1; tryX <= x+1; tryX++) {
            for (int tryY = y-1; tryY <= y+1; tryY++) {
                if(isInside(tryX,tryY)) {
                    if (this.myField[tryX][tryY] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean checkShip(Coordinates[] ship) {
        for (Coordinates eachShip: ship ) {
            if(!checkPlace(eachShip.x,eachShip.y) ) return false;
        }
        return true;
    }

    protected void fillCoordinates(Coordinates[] ship) {
        for(Coordinates eachShip: ship) {
            this.myField[eachShip.x][eachShip.y] = DECK;
        }
    }


    //3. Метод отвечающий противнику на его выстрел "Мимо"-(0), "Ранен"-(1), "Убит"-(2),
    protected int checkWhatInPlace(int x, int y){
        if(isInside(x,y)) {
            return  this.myField[x][y];
        }
        return 0;
    }

    private boolean isShipDown(int x, int y, boolean isHorizontal) {
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        int shipPoints = INJURED; // тк текущая клетка подбита - сохраняем ее статус 2
        if(isHorizontal) {
            // направо
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x +i,y) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + checkWhatInPlace(x + i, y);
                }
            }
            //налево
            for(int i = 1; i <= 3; i++){
                if(checkWhatInPlace(x -i,y) == EMPTY) {
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
        if(shipPoints == shipLength * INJURED) { // все клетки подбиты
            return true;
        }
        return false;
        }

    public int responseToShot(int[] shot ) {
        int result = 0;
        int x,y;
        //todo check args
        if (shot == null || shot.length != 2) {
            throw new IllegalArgumentException();
        }

        x = shot[0];
        y = shot[1];
        if (!isInside(x,y)) {
            throw new IllegalArgumentException();
        }

        if (checkWhatInPlace(x,y) == EMPTY) {
            return 0;
        }
        if(isShipDown(x, y, true)  &&  isShipDown(x,y,false)) {
            result = 2;
        }
        else  {
            result = 1;
        }
        myField[shot[0]][shot[1]] = INJURED; // статус подбит
        return result;

    }


    //5. Метод возвращающий Ваше поле с кораблями после игры
    public  int[][] getMap() {
        return this.myField;
    }




}





