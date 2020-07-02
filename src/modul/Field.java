package modul;

public class Field {
    private int[][] field = new int[10][10];

    protected static final int FIELD_LENGTH = 10;

    protected static final int UNKNOWN = -1;
    protected static final int EMPTY = 0;
    protected static final int DECK_UNTOUCHED = 1;
    protected static final int DECK_HIT = 2;

    protected boolean isInside(int x, int y) {
        return ((x >= 0 && x < FIELD_LENGTH) && (y >= 0 && y < FIELD_LENGTH));
    }

    protected void setCellValue(int x, int y ,int value){
        if(!isInside(x,y)) {
            throw new IllegalArgumentException("Coordinates outside the Field");
        }
        field[x][y] = value;

    }

    protected int getCellValue(int x, int y){
        if(!isInside(x,y)) {
           return 0;
            // throw new IllegalArgumentException("Coordinates outside the Field");
        }
        return  this.field[x][y];
    }

    //5. Метод возвращающий Ваше поле с кораблями после игры
    public  int[][] getMap() {
        return this.field;
    }


    public void print(String title) {
        System.out.println("---------- "+title+" start ---------");
        for (int i = 0; i < FIELD_LENGTH; i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        for (int i = 0; i < FIELD_LENGTH; i++) {
            System.out.print( i+" :");
            for (int j = 0; j < FIELD_LENGTH; j++) {
                String printValue;
                switch (field[j][i]) {
                    case 0:
                        printValue = " . ";  // Пусто
                        break;
                    case 1:
                        printValue = "[+]";  // Палуба
                        break;
                    case 2:
                        printValue = "{X}";  // Подбитая палуба
                        break;
                    default:
                        printValue = String.valueOf(field[j][i]);
                }
                System.out.print("\t" + printValue);
            }
            System.out.println(" :\t" + i);
        }
        for (int i = 0; i < FIELD_LENGTH; i++) {
            System.out.print("\t" + i);
        }
        System.out.println();
        System.out.println("----------- "+title+" end ----------");
    }

}