package modul;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static modul.Direction.*;

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
        for(Direction d:Direction.values()) {
            if(findCellValue(c.findNextToByDirection(d))!= EMPTY
                    && isInside(c.findNextToByDirection(d))) return false;
        }
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

    private int[] calcShipPointsByDirection(Coordinate c,Direction d){
        Coordinate tmpC = c;
        int[] shipPoints = new int[2];
        while (findCellValue(tmpC.findNextToByDirection(d)) != EMPTY) {
            tmpC = tmpC.findNextToByDirection(d);
            shipPoints[0]++;
            shipPoints[1] = shipPoints[1] + findCellValue(tmpC);
        }
        return shipPoints;
    }



    /** 3. Метод отвечающий противнику на его выстрел "Мимо"-(0), "Ранен"-(1), "Убит"-(2),
     * @param c
     * @param isHorizontal
     * @return 0 = Мимо; 1 = Ранен; 2 = Убит
     */
    private boolean isShipDown(Coordinate c, boolean isHorizontal) {
        int[] shipPoints = new int[2];
        int[] tmpShipPoints;
        shipPoints[0] = 1;
        shipPoints[1] = findCellValue(c);
        if(isHorizontal) {
            // направо
            for (Direction d:new Direction[] {RIGHT,LEFT}) {
                tmpShipPoints = calcShipPointsByDirection(c,d);
                shipPoints[0] += tmpShipPoints[0];
                shipPoints[1] += tmpShipPoints[1];
            }
        } else {
            for (Direction d:new Direction[] {DOWN,UP}) {
                tmpShipPoints = calcShipPointsByDirection(c,d);
                shipPoints[0] += tmpShipPoints[0];
                shipPoints[1] += tmpShipPoints[1];
            }
        }
        return shipPoints[1] == shipPoints[0] * DECK_HIT;
    }

    public int responseToShot(int[] shot ) {
        int result = 0;

        // check args
        if (shot == null || shot.length != 2) {
            throw new IllegalArgumentException();
        }

        Coordinate c = new Coordinate(shot[0],shot[1]);

        if (findCellValue(c) == EMPTY) {
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
