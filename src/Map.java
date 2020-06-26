import java.util.Random;

public class Map {
    //    private static int[][] field= new int[10][10];
    public int[][] field = new int[10][10];
    private static final int EMPTY = 0;
    private static final int DECK = 1;
    private static final int INJURED = 2;
    static final Coordinates[] STRATEGY4 = new Coordinates[24];

    public void initStrategy4() {
        int k = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 4 == 0) {
                    STRATEGY4[k] = new Coordinates(i, j);
                    k++;
                }
            }
        }
    }

    public Map() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = EMPTY;
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
                System.out.print("\t" + field[j][i]);
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
                    if (this.field[tryX][tryY] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkShip(Coordinates[] ship) {
        for (Coordinates eachShip: ship ) {
            if(!checkPlace(eachShip.x,eachShip.y) ) return false;
        }
        return true;
    }

    private void fillCoordinates(Coordinates[] ship) {
        for(Coordinates eachShip: ship) {
            this.field[eachShip.x][eachShip.y] = DECK;
        }
    }

    public void setShip(int length) {
        Random myRandom = new Random();
        Coordinates[] ship = new Coordinates[length];
        do {
            int x = myRandom.nextInt(10);
            int y = myRandom.nextInt(10);
            boolean isHorizontal = myRandom.nextBoolean();
            int coefficientHorizont  = (isHorizontal ? 1 : 0);
            ship[0] = new Coordinates(x, y);
            if(length >1) {
                for (int i = 1; i < length; i++) {
                    ship[i] = new Coordinates(x + i * coefficientHorizont , y+ i * (1 - coefficientHorizont));
                }
            }
        } while(!checkShip(ship));
        fillCoordinates(ship);
    }

    // 1. Метод расставляющий ваш флот на поле.
    public void initMap() {
        this.setShip(4);
        for (int i=0; i<2;i++){
            this.setShip(3);
        }
        for (int i=0; i<3;i++){
            this.setShip(2);
        }
        for (int i=0; i<4;i++){
            this.setShip(1);
        }
        // ДЛЯ ТЕСТОВ
        this.field[3][5] =1;
        this.field[3][6] =1;
        this.field[3][4] =1;
       // this.field[3][3] =1;

    }


    //3. Метод отвечающий противнику на его выстрел "Мимо"-(0), "Ранен"-(1), "Убит"-(2),
    private int checkWhatInPlace(int x,int y){
        if(isInside(x,y)) {
            return  this.field[x][y];
        }
        return 0;
    }

    private boolean isShipDown(int x, int y, boolean isHorizontal) {
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        int shipPoints = 2; // тк текущая клетка подбита - сохраняем ее статус 2
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
        if(shipPoints == shipLength * 2) { // все клетки подбиты
            return true;
        }
        return false;
        }

    public int responseToShot(int[] shot ) {
        int result = 0;
        int x,y;
        x = shot[0];
        y = shot[1];
        if (checkWhatInPlace(x,y) == EMPTY) {
            return 0;
        }
        if(isShipDown(x, y, true)  &&  isShipDown(x,y,false)) {
            result = 2;
        }
        else  {
            result = 1;
        }
        /*
        if (checkWhatInPlace(x + 1, y) == DECK ||
                    checkWhatInPlace(x, y + 1) == DECK ||
                    checkWhatInPlace(x - 1, y) == DECK ||
                    checkWhatInPlace(x, y - 1) == DECK) {
            result = 1;
        } else {
            result = 2; // если не мимо и не подбитых клеток нет - значит убит
        }

         */
        field[shot[0]][shot[1]] = INJURED; // статус подбит
        return result;

    }


    //5. Метод возвращающий Ваше поле с кораблями после игры
    public  int[][] getMap() {
        return this.field;
    }

}

    /*
     private boolean isInside(int x, int y) {
        return ((x >= 0 && x <=9) && (y >= 0 && y <=9));
    }

    private boolean checkPlace(int x, int y) {
        for (int tryX = x-1; tryX <= x+1; tryX++) {
            for (int tryY = y-1; tryY <= y+1; tryY++) {
                if(isInside(tryX,tryY)) {
                    if (this.coordinates[tryX][tryY] != 0) {
                        return false;
                    }
                }
            }
        }
        //this.coordinates[x][y] =1;
        return true;
    }

    public void setShipOne() {
        Random myRandom = new Random();
        int x,y;
        do {
            x = myRandom.nextInt(10);
            y = myRandom.nextInt(10);
        } while(!checkPlace(x, y));
        this.coordinates[x][y] = 1;
    }

    public void setShipTwo() {
        Random myRandom = new Random();
        int x,y,x1,y1;
        boolean isHorizontal;
        do {
            x = myRandom.nextInt(10);
            y = myRandom.nextInt(10);
            isHorizontal = myRandom.nextBoolean();
            x1 = x + (isHorizontal ? 1 : 0);
            y1 = y + (isHorizontal ? 0 : 1);
        } while( !checkPlace(x, y) || !checkPlace(x1 , y1)
                || !isInside(x,y) || !isInside(x1,y1));
        this.coordinates[x][y] = 1;
        this.coordinates[x1][y1] = 1;
    }


     */



