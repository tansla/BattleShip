import modul.Field;
import modul.RndShipsField;
import modul.Strategy;

import java.lang.management.PlatformLoggingMXBean;

public class Application {

    public static void main(String[] args) {

        RndShipsField playerOneField = new RndShipsField();
        RndShipsField playerTwoField = new RndShipsField();

        playerOneField.initMap();
        playerTwoField.initMap();

        Strategy playerOneEnemyField = new Strategy();
        Strategy playerTwoEnemyField = new Strategy();

        playerOneEnemyField.initStrategy();
        playerTwoEnemyField.initStrategy();

       // System.out.println("---------- playerOneField ---------");
       // playerOneField.print();
        //System.out.println("---------- playerTwoField ---------");
        playerTwoField.print("playerTwoField");
        //System.out.println("---------- playerOneEnemyField ---------");
        playerOneEnemyField.print("playerOneEnemyField");



        for(int i =0; i < 50; i++) {
            // Player One
            int[] shotPlayerOne = playerOneEnemyField.makeShot();
            int shotResult = playerTwoField.responseToShot(shotPlayerOne);
            playerOneEnemyField.shotProcessing(shotResult);
            System.out.println("step "+ i + ":"+ shotPlayerOne[0]+ "-"+shotPlayerOne[1] + " result " +shotResult);
            //System.out.println("---------- playerOneEnemyField ---------");
            playerOneEnemyField.print("playerOneEnemyField");

        }
/*
        System.out.println("---------- playerOneEnemyField ---------");
        playerOneEnemyField.print();
        System.out.println("---------- playerTwoEnemyField ---------");
        playerTwoEnemyField.print();


 */







/*

        RndShipsField myField = new RndShipsField();

        myField.initMap();
        myField.print();

        System.out.println("Enemy Field");

        Strategy strategy = new Strategy();

        strategy.initStrategy();
        strategy.print();

        int[][] myMap2 = myMap.getMap();

        System.out.println("Start of the myMap2");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print("\t" + myMap2[i][j]);
            }
            System.out.println();
        }
        System.out.println("End of the myMap2");

 */

/*
        int[] shot1 = new int[2];
        shot1[0] = 3;
        shot1[1] = 5;

        int[] shot2 = new int[2];
        shot2[0] = 3;
        shot2[1] = 6;

        int[] shot3 = new int[2];
        shot3[0] = 3;
        shot3[1] = 3;

        int[] shot4 = new int[2];
        shot4[0] = 3;
        shot4[1] = 4;

        int resultShot = myMap.responseToShot(shot1);
        int resultShot2 = myMap.responseToShot(shot2);
        int resultShot3 = myMap.responseToShot(shot3);
        int resultShot4 = myMap.responseToShot(shot4);

        System.out.println(resultShot + " " + resultShot2 + " " + resultShot3 + " " + resultShot4);
        myMap.print();

*/

    }
}

