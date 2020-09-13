package modul;

import ru.homecredit.schoolonline.shared.SeaBattleInterface;

public class Player implements SeaBattleInterface {

    MyRandomShips playerField;
    EnemyShips playerEnemyShips;


    public Player() {
    }

    @Override
    public void initMap() {
        playerField = new MyRandomShips();
        playerField.initMap();
        playerEnemyShips = new EnemyShips();
        playerEnemyShips.initStrategy();
    }

    @Override
    public int[] makeShot() {
        return playerEnemyShips.makeShot();
    }

    @Override
    public int responseToShot(int[] shot) {
        return playerField.responseToShot(shot);
    }

    @Override
    public void shotProcessing(int shot) {
         playerEnemyShips.shotProcessing(shot);
    }

    @Override
    public int[][] getMap() {
        return playerField.getMap();
    }

    public  String getListOfDeadShips(){
        return playerEnemyShips.getListOfDeadShips();
    }
}
