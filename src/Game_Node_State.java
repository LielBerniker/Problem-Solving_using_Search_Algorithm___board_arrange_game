import java.awt.*;
import java.util.Arrays;

public class Game_Node_State implements Comparable<Game_Node_State> {
    /* this class contains an information about a single state in the board game (node)*/
    private char[][] current_State; // the state in the board
    private String path; // path that move from the prev state
    Game_Node_State prev_node; // prv node
    private int board_size;
    private int cost; //  cost base on the prev node cost
    private int depth; // depth
    private int heuristic; // heuristic value
    private int iteration; // number of iteration
    private String unique_key; // unique key base on the board state
    private boolean out;


    public Game_Node_State(String cur_state, int board_size) {
        /**
         * get a current state as string and initiate the information base on it and the board size
         * @param cur_state
         * @param board_size
         */
        this.current_State = new char[board_size][board_size];
        this.path = "";
        this.iteration = 0;
        this.depth = 0;
        this.heuristic = 0;
        this.prev_node = null;
        this.cost = 0;
        this.out = false;
        this.board_size = board_size;
        int k = 0;
        // initiate the char matrix by the string
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                this.current_State[i][j] = cur_state.charAt(k);
                k = k + 2;
            }
        }
        this.unique_key = getNode_unique_key();
    }

    public Game_Node_State(Game_Node_State prev_node, Direction direct, int board_size, int row, int col) {
        /**
         * get a prev state as string and initiate the information base on it
         * and about whichdirectionn row and column
         * @param prev_node
         * @param direct
         * @param board_size
         * @param row
         * @param col
         */
        this.board_size = board_size;
        this.heuristic = 0;
        this.iteration = 0;
        this.depth = prev_node.getDepth() + 1;
        this.prev_node = prev_node;
        this.out = false;
        // initiate the char matrix
        this.current_State = new char[board_size][board_size];
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                this.current_State[i][j] = prev_node.getCurrent_state()[i][j];
            }
        }
        // set the move by the direction
        setBoard_state(row, col, direct, prev_node);
        this.unique_key = getNode_unique_key();

    }

    private void setBoard_state(int row, int col, Direction direct, Game_Node_State prev_node) {
        /**
         *  set the path and the char matrix by the move direction, and set the cost
         *  @param direct
         *  @param prev_node
         * @param row
         * @param col
         * @return
         */
        char current_ball;
        switch (direct) {
            case UP:
                // move a ball from above to the empty block
                this.current_State[row][col] = this.current_State[row - 1][col];
                this.current_State[row - 1][col] = '_';
                current_ball = this.current_State[row][col];
                this.path = "(" + (row) + "," + (col + 1) + "):" + current_ball + ":(" + (row + 1) + "," + (col + 1) + ")--";
                break;
            case DOWN:
                // move a ball from below to the empty block
                this.current_State[row][col] = this.current_State[row + 1][col];
                this.current_State[row + 1][col] = '_';
                current_ball = this.current_State[row][col];
                this.path = "(" + (row + 2) + "," + (col + 1) + "):" + current_ball + ":(" + (row + 1) + "," + (col + 1) + ")--";
                break;
            case LEFT:
                // move a ball from the left to the empty block
                this.current_State[row][col] = this.current_State[row][col - 1];
                this.current_State[row][col - 1] = '_';
                current_ball = this.current_State[row][col];
                this.path = "(" + (row + 1) + "," + (col) + "):" + current_ball + ":(" + (row + 1) + "," + (col + 1) + ")--";
                break;
            case RIGHT:
                // move a ball from the right to the empty block
                this.current_State[row][col] = this.current_State[row][col + 1];
                this.current_State[row][col + 1] = '_';
                current_ball = this.current_State[row][col];
                this.path = "(" + (row + 1) + "," + (col + 2) + "):" + current_ball + ":(" + (row + 1) + "," + (col + 1) + ")--";
                break;
            default:
                break;
        }
        this.cost = prev_node.cost + getCost_by_color(this.current_State[row][col]);

    }

    public boolean canMoveBallFromAbove(int row, int col) {
        /**
         * @param row
         * @param col
         * @return if can move ball from above return true else false
         */
        // if there is a row above and there is  a ball above
        if (row - 1 < 0 || this.getCurrent_state()[row - 1][col] == '_')
            return false;
        return true;
    }

    public boolean canMoveBallFromBelow(int row, int col) {
        /**
         * @param row
         * @param col
         * @return if can move ball from below return true else false
         */
        // if there is a row below and there is  a ball below
        if (row + 1 >= this.board_size || this.getCurrent_state()[row + 1][col] == '_')
            return false;
        return true;
    }

    public boolean canMoveBallFromRight(int row, int col) {
        /**
         * @param row
         * @param col
         * @return if can move ball from the right return true else false
         */
        // if there is a column from the right and there is a ball
        if (col + 1 >= this.board_size || this.getCurrent_state()[row][col + 1] == '_')
            return false;
        return true;
    }

    public boolean canMoveBallFromLeft(int row, int col) {
        /**
         * @param row
         * @param col
         * @return if can move ball from the left return true else false
         */
        // if there is a column from the left and there is a ball
        if (col - 1 < 0 || this.getCurrent_state()[row][col - 1] == '_')
            return false;
        return true;
    }

    public int getCost_by_color(char color) {
        /** return the cost base on the color char
         * @param color
         * @return color cost
         */
        int score = 0;
        switch (color) {
            case 'R':
            case 'Y':
                score = 1;
                break;
            case 'B':
                score = 2;
                break;
            case 'G':
                score = 10;
                break;
            default:
                break;
        }
        return score;
    }

    public boolean isBlock_empty(int row, int col) {
        /**
         * @param row
         * @param col
         * @return if block contain '_'
         */
        if (this.getCurrent_state()[row][col] == '_') {
            return true;
        }
        return false;
    }

    public Point[] findBalls_location_by_color_3X3(char current_color) {
        /** return all the balss location by the color the function gets
         * return a list of points contain the row and col of the blocks
         * for 2 balls
         * @param current_color
         * @return balls_location
         */
        int balls_amount = 2;
        Point[] balls_locate = balls_location(current_color, balls_amount);
        return balls_locate;
    }

    public Point[] findBalls_location_by_color_5X5(char current_color) {
        /** return all the balss location by the color the function gets
         * return a list of points contain the row and col of the blocks
         * for 4 balls
         * @param current_color
         * @return balls_location
         */
        int balls_amount = 4;
        Point[] balls_locate = balls_location(current_color, balls_amount);
        return balls_locate;
    }

    private Point[] balls_location(char current_color, int balls_amount) {
        /** return all the balss location by the color the function gets
         * return a list of points contain the row and col of the blocks
         *  do this by go over al the char matrix and compare to each block
         * for 2 balls
         * @param current_color
         * @param balls_amount
         * @return balls_location
         */
        Point[] balls_locate = new Point[balls_amount];
        int i = 0;
        for (int row = 0; row < this.getBoard_size(); row++) {
            for (int col = 0; col < this.getBoard_size(); col++) {
                // if the char in the block is equal to the current color
                if (this.getCurrent_state()[row][col] == current_color) {
                    Point ball_location = new Point(row, col);
                    balls_locate[i] = ball_location;
                    i++;
                }
            }
        }
        return balls_locate;
    }

    public String getNode_unique_key() {
        /** return the unique_key of a matrix by the function deepToString
         * @return unique_key
         */
        return Arrays.deepToString(this.current_State);
    }

    public String getUnique_key() {
        /** return the unique_key
         * @return unique_key
         */
        return this.unique_key;
    }

    public int getBoard_size() {
        /** return board size
         * @return board size
         */
        return board_size;
    }

    public char[][] getCurrent_state() {
        /** return the char matrix that represent the board state
         * @return current state of board matrix
         */
        return this.current_State;
    }

    public int getHeuristic() {
        /** return the Heuristic
         * @return Heuristic
         */
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        /** set the heuristic value
         * @param heuristic
         * @return
         */
        this.heuristic = heuristic;
    }

    public int getDepth() {
        /** return the depth
         * @return depth
         */
        return depth;
    }

    public int getIteration() {
        /** return iteration
         * @return iteration number
         */
        return iteration;
    }

    public void setIteration(int iteration) {
        /** set the iteration
         * @param iteration
         * @return
         */
        this.iteration = iteration;
    }

    public Game_Node_State getPrev_node() {
        /** return the prev node
         * @return prev node
         */
        return prev_node;
    }

    public boolean isOut() {
        /** return if the node is out
         * @return is out
         */
        return out;
    }

    public void setOut(boolean out) {
        /** set the out
         * @param out
         * @return
         */
        this.out = out;
    }

    public String getPath() {
        /** return the node path
         * @return path
         */
        return this.path;
    }

    public int getCost() {
        /** return the cost of the node
         * @return cost
         */
        return this.cost;
    }

    public String getNode_after_move(Direction direct, int row, int col) {
        /** return the unique key of the node , by make the move base on the direction and create a unique key
         * then return the matrix to the previous state
         * @param direct
         * @param row
         * @param col
         * @return unique key
         */
        String current_key = "";
        // check the direction and get the unique key base on the direction move
        switch (direct) {
            case UP:
                this.current_State[row][col] = this.current_State[row - 1][col];
                this.current_State[row - 1][col] = '_';
                current_key = this.getNode_unique_key();
                this.current_State[row - 1][col] = this.current_State[row][col];
                this.current_State[row][col] = '_';
                break;
            case DOWN:
                this.current_State[row][col] = this.current_State[row + 1][col];
                this.current_State[row + 1][col] = '_';
                current_key = this.getNode_unique_key();
                this.current_State[row + 1][col] = this.current_State[row][col];
                this.current_State[row][col] = '_';
                break;
            case LEFT:
                this.current_State[row][col] = this.current_State[row][col - 1];
                this.current_State[row][col - 1] = '_';
                current_key = this.getNode_unique_key();
                this.current_State[row][col - 1] = this.current_State[row][col];
                this.current_State[row][col] = '_';
                break;
            case RIGHT:
                this.current_State[row][col] = this.current_State[row][col + 1];
                this.current_State[row][col + 1] = '_';
                current_key = this.getNode_unique_key();
                this.current_State[row][col + 1] = this.current_State[row][col];
                this.current_State[row][col] = '_';
                break;
            default:
                break;
        }
        return current_key;
    }

    public boolean equalTo_prev(Direction direct, int row, int col) {
        /** return true if the unique key of the prev node is equal to the unique key
         * of the node that about to be crated
         * @param direct
         * @param row
         * @param col
         * @return unique key
         */
        // check if have a prev node
        if (this.getPrev_node() == null)
            return false;
        String temp_key_move = this.getNode_after_move(direct, row, col);
        String prev_key = this.getPrev_node().getUnique_key();
        // check if the keys are equal
        if (temp_key_move.equals(prev_key))
            return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        /** return true or false if the two object are equal
         * @param o
         * @return true if equal else false
         */
        //return true if the object is it self
        if (o == this) {
            return true;
        }

        // if the object is not Game_Node_State
        if (!(o instanceof Game_Node_State)) {
            return false;
        }
        String u_key = ((Game_Node_State) o).getUnique_key();
        if (!this.unique_key.equals(u_key))
            return false;
        return true;
    }

    public boolean currentIs_bigger(Game_Node_State o) {
        /** return true or false if the current object is bigger
         * @param o
         * @return
         */
        if (compareTo(o) == 1) {
            return true;
        }
        return false;
    }

    public int f_n() {
        /** return the f(n) value , which is the cost + heuristic
         * @return f(n)
         */
        return this.cost + this.heuristic;
    }

    @Override
    public int compareTo(Game_Node_State o) {
        /** a compare to function that return 1 or -1 base on the f(n) values of the nodes
         * @param o
         * @return
         */
        // compare the f(n) value
        if (this.f_n() > o.f_n()) {
            return 1;
        } else if (this.f_n() < o.f_n()) {
            return -1;
        } else {
            // compare which node is been create earlier
            if (this.getIteration() > o.getIteration()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public String toString() {
        /** return a string value of the current node with the node information
         * @return toString
         */
        return " <- " + Arrays.deepToString(this.current_State) + " cost:#" + this.cost + " H:" + this.heuristic;
    }
}
