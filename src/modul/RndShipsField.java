package modul;

import java.util.Random;

public class RndShipsField extends Field {

    private final Random myRandom = new Random();

    public void setShip(int length) {
        Coordinates[] ship = new Coordinates[length];
        do {
            int x = myRandom.nextInt(FIELD_LENGTH);
            int y = myRandom.nextInt(FIELD_LENGTH);
            boolean isHorizontal = myRandom.nextBoolean();
            int coefficientHorizon  = (isHorizontal ? 1 : 0);
            ship[0] = new Coordinates(x, y);
            for (int i = 1; i < length; i++) {
                ship[i] = new Coordinates(x + i * coefficientHorizon, y+ i * (1 - coefficientHorizon));
            }
        } while (!checkShip(ship));

        fillCoordinates(ship);
    }

    /**
     *
     * @param x
     * @param y
     * @return true = можно поставить корабль
     */
    private boolean checkPlace(int x, int y) {
        if (!isInside(x,y)) return false;
        for (int tryX = x-1; tryX <= x+1; tryX++) {
            for (int tryY = y-1; tryY <= y+1; tryY++) {
                if (this.getCellValue(tryX,tryY) != EMPTY && isInside(tryX,tryY)) {
                    return false;
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
            this.setCellValue(eachShip.x,eachShip.y,DECK_UNTOUCHED);
        }
    }


    // 1. Метод расставляющий ваш флот на поле.
    public void initMap() {
        for (int i = 0; i < FIELD_LENGTH; i++) {
            for (int j = 0; j < FIELD_LENGTH; j++) {
                this.setCellValue(i,j, EMPTY);
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
     * @param x
     * @param y
     * @param isHorizontal
     * @return 0 = Мимо; 1 = Ранен; 2 = Убит
     */
    private boolean isShipDown(int x, int y, boolean isHorizontal) {
        int shipLength = 1; // мы знаем что у нас подбит корабль и мы ищем остальные клетки
        int shipPoints = DECK_HIT; // тк текущая клетка подбита - сохраняем ее статус 2
        if(isHorizontal) {
            // направо
            for(int i = 1; i <= 3; i++){
                if(getCellValue(x + i, y) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + getCellValue(x + i, y);
                }
            }
            //налево
            for(int i = 1; i <= 3; i++){
                if(getCellValue(x - i, y) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + getCellValue(x - i, y);
                }
            }
        } else {
            // вниз
            for(int i = 1; i <= 3; i++){
                if(getCellValue(x,y + i) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + getCellValue(x , y + i);
                }
            }
            //вверх
            for(int i = 1; i <= 3; i++){
                if(getCellValue(x,y - i) == EMPTY) {
                    break;
                } else {
                    shipLength++;
                    shipPoints = shipPoints + getCellValue(x, y - i);
                }
            }
        }

        if (shipPoints == shipLength * DECK_HIT) { // все клетки подбиты
            return true;
        }
        return false;
    }

    public int responseToShot(int[] shot ) {
        int result = 0;
        int x,y;
        // check args
        if (shot == null || shot.length != 2) {
            throw new IllegalArgumentException();
        }

        x = shot[0];
        y = shot[1];
        if (!isInside(x,y)) {
            throw new IllegalArgumentException();
        }

        if (getCellValue(x,y) == EMPTY) {
            return 0;
        }
        if(isShipDown(x, y, true)  &&  isShipDown(x,y,false)) {
            result = 2;
        }
        else  {
            result = 1;
        }
        this.setCellValue(shot[0],shot[1],DECK_HIT); // статус подбит
        return result;

    }



}
