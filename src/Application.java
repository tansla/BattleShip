public class Application {

    public static void main(String[] args) {

        Map myMap = new Map();

        /*

        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {
                if ( (i+j+1)%4 ==0) {
                    myMap.myField[j][i] = 4;
                }
            }
        }

        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {
                if ( (i+j+1)%3 ==0) {
                    myMap.myField[j][i] = 3;
                }
            }
        }

        for(int i=0;i<10;i++) {
            for (int j = 0; j < 10; j++) {
                if ( (i+j+1)%2 ==0 ) {
                    myMap.myField[j][i] = 2;
                }
            }
        }


         */
        myMap.initMap();
        myMap.printMyField();

        System.out.println("Enemy Field");

        myMap.initStrategy();
        myMap.printEnemyField();




/*
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

