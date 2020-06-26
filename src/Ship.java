/*
package homeUniLevel2.battleShip;

import java.util.Random;

public class Ship {
    int countDeck;
    Coordinates[] coords;
    Ship(int count) { // конструктор
        countDeck =count;
        coords = new Coordinates[countDeck];
       // randomFillCoordindats(coords);
    }
    Ship() { // конструктор
        this(1);
    }

    public void fillCoordinates(Coordinates[] arr) {
        Random myRandom = new Random(); //.nextInt(11);
        int myRandomX = myRandom.nextInt(11);
        int myRandomY = myRandom.nextInt(11);
        int myRandomZ = myRandom.nextInt(1);
//        Random myRandomY = new Random().nextInt(11);
        for(int i=0; i< arr.length; i++){
            arr[i] = new Coordinates();
            arr[i].x = myRandomX + ((myRandomZ == 0)?1:0);
            arr[i].y = myRandomY + ((myRandomZ == 0)?0:1);
            arr[i].isDown = false;

        }
    }
}
*/