import modul.MyRandomShips;
import modul.EnemyShips;

public class Application {

    public static void main(String[] args) {


        //playerField.print("playerField");
       // playerEnemyShips.print("playerEnemyField");



        try {
            for(int j=0;j<10;j++) {

                MyRandomShips playerField = new MyRandomShips();
                playerField.initMap();
                EnemyShips playerEnemyShips = new EnemyShips();
                playerEnemyShips.initStrategy();
                playerField.print("Initial");

                for (int i = 0; i < 50; i++) {
                    // Player One
                    int[] shotPlayerOne = playerEnemyShips.makeShot();
                    int shotResult = playerField.responseToShot(shotPlayerOne);
                    playerEnemyShips.shotProcessing(shotResult);

                    System.out.println(j + ".step " + i + ":" + shotPlayerOne[0] + "-" + shotPlayerOne[1] + " result " + shotResult);
                    if (i == 49) {
                        playerEnemyShips.print("playerOneEnemyField");
                    }


        }
                System.out.println(playerEnemyShips.getListOfDeadShips());
        }
    } catch (Exception e){
          //  System.out.println(e.toString());
            e.printStackTrace();

        }


    }

}

