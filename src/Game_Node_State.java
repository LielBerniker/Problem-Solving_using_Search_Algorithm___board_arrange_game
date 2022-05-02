import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game_Node_State implements Comparable<Game_Node_State>{
    private char[][] current_State;
    private String path;
    private int depth;
    Game_Node_State prev_node;
    private int board_size;
    private int cost;
    private int heuristic;
    private int iteration;
    private boolean out;


    public Game_Node_State(String cur_state, int board_size){
        this.current_State = new char[board_size][board_size];
        this.path="";
        this.depth=0;
        this.iteration =0;
        this.heuristic = 0;
        this.prev_node = null;
        this.cost =0;
        this.out = false;
        this.board_size = board_size;
        int k =0;
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                this.current_State[i][j] = cur_state.charAt(k);
                k = k+2;
            }
        }
    }
    public Game_Node_State(Game_Node_State prev_node, Direction direct, int board_size, int row , int col){
        this.board_size = board_size;
        this.heuristic = 0;
        this.iteration =0;
        this.out = false;
        this.prev_node = prev_node;
        this.depth = prev_node.getDepth()+1;
        this.current_State = new char[board_size][board_size];
        for (int i = 0; i < this.board_size; i++) {
            for (int j = 0; j < this.board_size; j++) {
                this.current_State[i][j] = prev_node.getCurrent_state()[i][j];
            }
        }
        setBoard_state(row,col,direct,prev_node);

    }
    private void setBoard_state(int row, int col,Direction direct,Game_Node_State prev_node)
    {
        char current_ball;
        switch(direct) {
            case UP:
                this.current_State[row][col] = this.current_State[row-1][col];
                this.current_State[row-1][col] = '_';
                current_ball = this.current_State[row][col];
                this.path = "(" +(row) +"," + (col+1)  + "):" + current_ball+ ":("+ (row+1) +"," +(col+1) +")--";
                break;
            case DOWN:
                this.current_State[row][col] = this.current_State[row+1][col];
                this.current_State[row+1][col] = '_';
                current_ball = this.current_State[row][col];
                this.path="(" + (row+2) +"," + (col+1) + "):" +current_ball + ":("+ (row+1) +"," +(col+1) +")--";
                break;
            case LEFT:
                this.current_State[row][col] = this.current_State[row][col-1];
                this.current_State[row][col-1] = '_';
                current_ball = this.current_State[row][col];
                this.path="(" + (row+1) +"," + (col) + "):" + current_ball+ ":("+ (row+1) +"," +(col+1) +")--";
                break;
            case RIGHT:
                this.current_State[row][col] = this.current_State[row][col+1];
                this.current_State[row][col+1] = '_';
                current_ball = this.current_State[row][col];
                this.path="(" + (row+1) +"," + (col+2)  + "):" + current_ball+ ":("+ (row+1) +"," +(col+1) +")--";
                break;
            default:
                break;
        }
        this.cost = prev_node.cost+ getCost_by_color(this.current_State[row][col]);

    }
    public boolean canMoveBallFromAbove(int row, int col)
    {
      if( row-1<0  || this.getCurrent_state()[row-1][col]=='_')
          return false;
      return true;
    }
    public boolean canMoveBallFromBelow(int row, int col)
    {
        if(row+1>=this.board_size|| this.getCurrent_state()[row+1][col]=='_')
            return false;
        return true;
    }
    public boolean canMoveBallFromRight(int row,int col)
    {
        if(col+1>=this.board_size || this.getCurrent_state()[row][col+1]=='_')
            return false;
        return true;
    }
    public boolean canMoveBallFromLeft(int row,int col)
    {
        if(col-1<0 || this.getCurrent_state()[row][col-1]=='_')
            return false;
        return true;
    }
    public int getCost_by_color(char color)
    {
        int score=0;
        switch(color) {
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
    public boolean isBlock_empty(int row, int col)
    {
        if(this.getCurrent_state()[row][col] == '_')
        {return true;}
        return false;
    }
    public Point[] findBalls_location_by_color(char current_color)
    {
        int size = this.getBoard_size();
        int balls_amount,i=0;
        if(size == 3)
        {
           balls_amount = 2;
        }
        else{
            balls_amount = 4;
        }
        Point[] balls_location = new Point[balls_amount];
        for (int row = 0; row <this.getBoard_size(); row++) {
            for (int col = 0; col <this.getBoard_size() ; col++) {
                if(this.getCurrent_state()[row][col] == current_color)
                {
                    Point ball_location = new Point(row,col);
                    balls_location[i]=ball_location;
                    i++;
                }
            }
        }
        return balls_location;
    }

   public String getNode_unique_key()
   {
       return Arrays.deepToString(this.getCurrent_state());
   }

    public int getBoard_size() {
        return board_size;
    }

    public char[][] getCurrent_state() {
        return this.current_State;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public Game_Node_State getPrev_node() {
        return prev_node;
    }

    public void setPrev_node(Game_Node_State prev_node) {
        this.prev_node = prev_node;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public String getPath() {
        return this.path;
    }

    public int getCost() {
        return this.cost;
    }
    @Override
    public boolean equals(Object o)
    {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Game_Node_State)) {
            return false;
        }
        char[][] compared_node = ((Game_Node_State) o).getCurrent_state();
        for (int i = 0; i <this.getBoard_size() ; i++) {
            for (int j = 0; j < this.getBoard_size(); j++) {
                if(compared_node[i][j] != this.getCurrent_state()[i][j])
                    return false;
            }
        }
        return true;
    }
    public boolean currentIs_bigger(Game_Node_State o)
    {
        if(compareTo(o)==1)
        {
            return true;
        }
        return false;
    }
    public int f_n()
    {
        return this.cost+this.heuristic;
    }
    @Override
    public int compareTo(Game_Node_State o) {
        if(this.f_n()>o.f_n())
        {
            return 1;
        }
        else if(this.f_n()<o.f_n())
        {
            return -1;
        }
        else{
            if(this.getIteration()>o.getIteration())
            {
                return 1;
            }
            else if(this.getIteration()>o.getIteration())
            {
                return -1;
            }
            else{
                return 1;
            }
        }
    }
    @Override
    public String toString(){
        return " <- " + Arrays.deepToString(this.current_State)+"  #"+this.cost+" "+ this.heuristic;
    }
}
