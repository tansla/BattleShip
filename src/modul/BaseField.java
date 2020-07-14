package modul;

public class BaseField {

    protected static final int FIELD_LENGTH = 10;

    private final int[][] field = new int[FIELD_LENGTH][FIELD_LENGTH];

    protected static final int UNKNOWN = -1;
    protected static final int EMPTY = 0;
    protected static final int DECK_UNTOUCHED = 1;
    protected static final int DECK_HIT = 2;

    protected static final int SHIP_DOWN = 2;
    protected static final int SHIP_INJURED = 1;


    protected boolean isInside(Coordinate c) {
        return ((c.getX() >= 0 && c.getX() < FIELD_LENGTH) && (c.getY() >= 0 && c.getY() < FIELD_LENGTH));
    }

    protected void setCellValue(Coordinate c , int value){
        if(!isInside(c)) {
            throw new IllegalArgumentException("Coordinates outside the Field" + c.getX()+"-"+c.getY());
        }
        field[c.getX()][c.getY()] = value;

    }

    protected int findCellValue(Coordinate c){
        if(!isInside(c)) {
           return 0;
        }
        return  this.field[c.getX()][c.getY()];
    }

    //5. Метод возвращающий Ваше поле с кораблями после игры
    public int[][] getMap() {
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
                    case EMPTY:
                        printValue = " . ";  // Пусто
                        break;
                    case DECK_UNTOUCHED:
                        printValue = "[+]";  // Палуба
                        break;
                    case DECK_HIT:
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