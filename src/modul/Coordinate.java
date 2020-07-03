package modul;

public class Coordinate {

        private int x;
        private int y;

        public Coordinate(int x, int y) {
                this.x = x;
                this.y = y;
        }

        public int getX() {
                return x;
        }

        public int getY() {
                return y;
        }

        //крест вокруг клетки
        /**
         * * *
         x c *
         * * *
         */
        public Coordinate getLeft(){
                return new Coordinate(x-1, y);
        }
        /**
         * * *
         * c x
         * * *
         */
        public Coordinate getRight(){
                return new Coordinate(x+1, y);
        }
        /**
         * x *
         * c *
         * * *
         */
        public Coordinate getUp(){
                return new Coordinate(x, y-1);
        }
        /**
         * * *
         * c *
         * x *
         */
        public Coordinate getDown(){
                return new Coordinate(x, y+1);
        }

        // углы вокруг клетки

        /**
         x * *
         * c *
         * * *
         */
        public Coordinate getLeftUp(){
                return new Coordinate(x-1,y-1);
        }
        /**
         * * *
         * c *
         x * *
         */
        public Coordinate getLeftDown(){
                return new Coordinate(x+1,y-1);
        }
        /**
         * * x
         * c *
         * * *
         */
        public Coordinate getRightUp(){
                return new Coordinate(x+1,y+1);
        }
        /**
         * * *
         * c *
         * * x
         */
        public Coordinate getRightDown(){
                return new Coordinate(x+1,y-1);
        }

}
