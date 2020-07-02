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

        playerTwoField.print("playerTwoField");
        playerOneEnemyField.print("playerOneEnemyField");



        for(int i =0; i < 50; i++) {
            // Player One
            int[] shotPlayerOne = playerOneEnemyField.makeShot();
            int shotResult = playerTwoField.responseToShot(shotPlayerOne);
            playerOneEnemyField.shotProcessing(shotResult);
            System.out.println("step "+ i + ":"+ shotPlayerOne[0]+ "-"+shotPlayerOne[1] + " result " +shotResult);
            playerOneEnemyField.print("playerOneEnemyField");

        }


    }
}

