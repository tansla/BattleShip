package ru.homecredit.schoolonline.shared;

public interface SeaBattleInterface {

    void initMap();
    int[] makeShot();
    int responseToShot(int[] shot);
    void shotProcessing(int shot);
    int[][] getMap();
}
