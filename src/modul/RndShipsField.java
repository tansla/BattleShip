package modul;

import java.util.Random;

public class RndShipsField extends Field {


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

    }


}
