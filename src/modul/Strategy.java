package modul;

public class Strategy extends Field {

//    private int[][] enemyField = new int[10][10];

    private int[] myShot = new int[2];

    protected static final int SEARCH4 = -4;
    protected static final int SEARCH3 = -3;
    protected static final int SEARCH2 = -2;
    protected static final int UNKNOWN = -1;


    //MakeShot - Метод возвращающий координаты того места, куда вы хотите сделать выстрел.

    // Метод определяющий стратегию поиска кораблей на чужом поле
    public void initStrategy() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) myField[i][j] = UNKNOWN;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 2 == 0) myField[i][j] = SEARCH2;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 3 == 0) myField[i][j] = SEARCH3;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j + 1) % 4 == 0) myField[i][j] = SEARCH4;
            }
        }

    }

    // shotProcessing - Метод обрабатывающий ответ противника, после вашего выстрела

    public void shotProcessing(int shot) {
        /*
        Входной параметр: shot - может принимать следующие значения : "Мимо"-(0), "Ранен"-(1), "Убит"-(2)
         */
        switch (shot){
            case EMPTY:
                this.myField[myShot[0]][myShot[1]] = 0;
                break;
            case DECK:
                this.myField[myShot[0]][myShot[1]] = 2;
                break;
            case INJURED:
                this.myField[myShot[0]][myShot[1]] = 2;
                // todo: check which ship is down
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + shot);
        }
        myShot[0] = 0;
        myShot[1] = 0;
    }
}
