package modul;

import java.util.Random;

public class MyRandomShips extends BaseField {

    private final Random myRandom = new Random();

    public void setShip(int length) {
        Coordinate[] ship = new Coordinate[length];

        do {
            int x = myRandom.nextInt(FIELD_LENGTH);
            int y = myRandom.nextInt(FIELD_LENGTH);
            boolean isHorizontal = myRandom.nextBoolean();
            ship[0] = new Coordinate(x, y);
            for (int i = 1; i < length; i++) {
                if(isHorizontal) {
                    ship[i] = ship[i-1].getRight();
                } else {
                    ship[i] = ship[i-1].getDown();
                }
            }
        } while (!isPlaceAvailable(ship));

        fillCoordinates(ship);
    }

    /**
     * пусто ли в клетке и нет ли конфликта по соседям
     * @param c
     * @return true = можно поставить корабль
     */
    private boolean isCellAvailable(Coordinate c) {
        if (!isInside(c)) return false;
        if(getCellValue(c.getLeft())!= EMPTY && isInside(c.getLeft())) return false;
        if(getCellValue(c.getLeftUp())!= EMPTY && isInside(c.getLeftUp())) return false;
        if(getCellValue(c.getUp())!= EMPTY && isInside(c.getUp())) return false;
        if(getCellValue(c.getRightUp())!= EMPTY && isInside(c.getRightUp())) return false;
        if(getCellValue(c.getRight())!= EMPTY && isInside(c.getRight())) return false;
        if(getCellValue(c.getRightDown())!= EMPTY && isInside(c.getRightDown())) return false;
        if(getCellValue(c.getDown())!= EMPTY && isInside(c.getDown())) return false;
        if(getCellValue(c.getLeftDown())!= EMPTY && isInside(c.getLeftDown())) return false;

        return true;
    }

    protected boolean isPlaceAvailable(Coordinate[] ship) {
        for (Coordinate eachShip: ship ) {
            if(!isCellAvailable(eachShip) ) return false;
        }
        return true;
    }

    protected void fillCoordinates(Coordinate[] ship) {
        for(Coordinate eachShip: ship) {
            this.setCellValue(eachShip,DECK_UNTOUCHED);
        }
    }


    // 1. Метод расставляющий ваш флот на поле.
    public void initMap() {
        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                this.setCellValue(new Coordinate(i,j), EMPTY);
            }
        }

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

    }

    /** 3. Метод отвечающий противнику на его выстрел "Мимо"-(0), "Ранен"-(1), "Убит"-(2),
     * @param c
     * @param isHorizontal
     * @return 0 = Мимо; 1 = Ранен; 2 = Убит
     */
    private boolean isShipDown(Coordinate c, boolean isHorizontal) {
        int shipLength = 1; // мы знаем что у нас подбита палуба
        int shipPoints = getCellValue(c); // тк текущая клетка подбита - сохраняем ее статус 2
        Coordinate copyC = new Coordinate(c.getX(),c.getY());
        if(isHorizontal) {
            // направо
            while (getCellValue(c.getRight()) != EMPTY) {
                c = c.getRight();
                shipLength++;
                shipPoints = shipPoints + getCellValue(c);
                }
            //налево
            while (getCellValue(copyC.getLeft()) != EMPTY) {
                copyC = copyC.getLeft();
                shipLength++;
                shipPoints = shipPoints + getCellValue(copyC);
            }
        } else {
            // вниз
            while (getCellValue(c.getDown()) != EMPTY) {
                c = c.getDown();
                shipLength++;
                shipPoints = shipPoints + getCellValue(c);
            }
            //вверх
            while (getCellValue(copyC.getUp()) != EMPTY) {
                copyC = copyC.getUp();
                shipLength++;
                shipPoints = shipPoints + getCellValue(copyC);
            }
        }

        return shipPoints == shipLength * DECK_HIT;

    }

    public int responseToShot(int[] shot ) {
        int result = 0;

        // check args
        if (shot == null || shot.length != 2) {
            throw new IllegalArgumentException();
        }

        Coordinate c = new Coordinate(shot[0],shot[1]);

        if (getCellValue(c) == EMPTY) {
            return EMPTY;
        } else {
            this.setCellValue(c,DECK_HIT); // статус подбит
        }

        if(isShipDown(c, true)  &&  isShipDown(c,false)) {
            result = SHIP_DOWN;
        }
        else  {
            result = SHIP_INJURED;
        }
        return result;

    }



}
