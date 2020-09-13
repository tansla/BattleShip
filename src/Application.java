import modul.*;
import ru.homecredit.schoolonline.shared.SeaBattleInterface;


import static modul.Direction.*;

public class Application  {

    public static void main(String[] args) {

        try {

            Player player1 = new Player();
            Player player2 = new Player();

            player1.initMap();
            player2.initMap();

            int[] shot;

            for (int i=0;i<50;i++) {
                shot = player1.makeShot();
                player1.shotProcessing(player2.responseToShot(shot));

                shot = player2.makeShot();
                player2.shotProcessing(player1.responseToShot(shot));
            }

            player1.getMap();
            player2.getMap();

            System.out.println(player1.getListOfDeadShips());
            System.out.println(player2.getListOfDeadShips());

        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

}

