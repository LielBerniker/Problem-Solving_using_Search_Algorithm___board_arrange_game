import java.lang.reflect.Array;
import java.util.Arrays;

public class Game_Node_State implements Comparable<Game_Node_State>{
    private char[][] current_State;
    private String path;
    Game_Node_State prev_node;
    private int board_size;
    private int cost;
    private int heuristic;


    public Game_Node_State(String cur_state, int board_size){
        this.current_State = new char[board_size][board_size];
        this.path="";
        this.heuristic = 0;
        this.prev_node = null;
        this.cost =0;
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
        this.prev_node = prev_node;
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
    private int getCost_by_color(char color)
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

    public Game_Node_State getPrev_node() {
        return prev_node;
    }

    public void setPrev_node(Game_Node_State prev_node) {
        this.prev_node = prev_node;
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
    @Override
    public int compareTo(Game_Node_State o) {
        return 0;
    }
    @Override
    public String toString(){
        return  Arrays.deepToString(this.current_State)+"  #"+this.cost+ this.heuristic;
    }
}
