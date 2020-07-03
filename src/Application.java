import modul.MyRandomShips;
import modul.EnemyShips;

public class Application {

    public static void main(String[] args) {

        MyRandomShips playerOneField = new MyRandomShips();
        MyRandomShips playerTwoField = new MyRandomShips();

        playerOneField.initMap();
        playerTwoField.initMap();

        EnemyShips playerOneEnemyShips = new EnemyShips();
        EnemyShips playerTwoEnemyShips = new EnemyShips();

        playerOneEnemyShips.initStrategy();
        playerTwoEnemyShips.initStrategy();


        playerTwoField.print("playerTwoField");
        playerOneEnemyShips.print("playerOneEnemyField");

        /*

        try {
            for(int j=0;j<100;j++) {
                for (int i = 0; i < 50; i++) {
                    // Player One
                    int[] shotPlayerOne = playerOneEnemyShips.makeShot();
                    int shotResult = playerTwoField.responseToShot(shotPlayerOne);
                    playerOneEnemyShips.shotProcessing(shotResult);
                    System.out.println(j + "step " + i + ":" + shotPlayerOne[0] + "-" + shotPlayerOne[1] + " result " + shotResult);
                    if (i == 49) {
                        playerOneEnemyShips.print("playerOneEnemyField");
                    }
        }

        }
    } catch (Exception e){
            System.out.println(e.getMessage());

        }

         */
    }

}

