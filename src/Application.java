import modul.*;

import java.util.HashMap;
import java.util.Map;

import static modul.Direction.*;

public class Application {

    public static void main(String[] args) {


        //playerField.print("playerField");
       // playerEnemyShips.print("playerEnemyField");
/*

        Strategy str1 = new Strategy(1);
        int k =0;

        Coordinate c;
        while ((c = str1.findNext()) != null ){
            System.out.println(k++ + " - " + c);
        }


        for (int i = 0; i < BaseField.FIELD_LENGTH; i++) {
            for (int j = 0; j < BaseField.FIELD_LENGTH; j++) {
                if ((i + j + 1) % 4 == 0) {
                    System.out.println("i="+i+",j="+j);;
                }
            }
        }

*/


        try {
            for(int j=0;j<10;j++) {

                MyRandomShips playerField = new MyRandomShips();
                playerField.initMap();
                EnemyShips playerEnemyShips = new EnemyShips();
                playerEnemyShips.initStrategy();
               // playerField.print("Initial: " + j);
                //playerEnemyShips.print("Initial");

                for (int i = 0; i < 50; i++) {
                    // Player One
                    int[] shotPlayerOne = playerEnemyShips.makeShot();
                    int shotResult = playerField.responseToShot(shotPlayerOne);

                   //System.out.println(j + ".step " + i + ":" + shotPlayerOne[0] + "-" + shotPlayerOne[1] + " result " + shotResult);

                    playerEnemyShips.shotProcessing(shotResult);
                    //playerEnemyShips.print("playerOneEnemyField");



                }
                System.out.println(j);

                playerEnemyShips.print("playerOneEnemyField");
                System.out.println(playerEnemyShips.getListOfDeadShips());
        }
    } catch (Exception e){
          //  System.out.println(e.toString());
            e.printStackTrace();

        }






    }

}

