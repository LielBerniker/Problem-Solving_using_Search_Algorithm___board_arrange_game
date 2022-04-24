import java.lang.reflect.Array;
import java.util.Arrays;

public class Game_Node_State implements Comparable<Game_Node_State>{
    private char[][] Current_State;
    private String Path;
    private int Board_size;
    private int Cost;


    public Game_Node_State(String cur_state, int board_size){
        this.Current_State = new char[board_size][board_size];
        this.Path ="";
        this.Cost =0;
        this.Board_size = board_size;
        int k =0;
        for (int i = 0; i < this.Board_size; i++) {
            for (int j = 0; j < this.Board_size; j++) {
                this.Current_State[i][j] = cur_state.charAt(k);
                k = k+2;
            }
        }
    }
    public Game_Node_State(Game_Node_State prev_node, Direction direct, int board_size, int row , int col){
        char temp_char;
        this.Path ="";
        this.Board_size = board_size;
        this.Current_State = new char[board_size][board_size];
        for (int i = 0; i < this.Board_size; i++) {
            for (int j = 0; j < this.Board_size; j++) {
                this.Current_State[i][j] = prev_node.getCurrent_state()[i][j];
            }
        }
        temp_char = this.Current_State[row][col];
        set_board_state(row,col,direct,prev_node);

    }
    private void set_board_state(int row, int col,Direction direct,Game_Node_State prev_node)
    {
        int current_cost =0;
        String current_move="";
        switch(direct) {
            case UP:

                this.Current_State[row][col] = this.Current_State[row-1][col];
                this.Current_State[row-1][col] = '_';
                current_move = "(" +(row+2) +"," + (col+1)  + "):" + this.Current_State[row][col]+ ":("+ (row+1) +"," +(col+1) +")--" ;
                break;
            case DOWN:
                this.Current_State[row][col] = this.Current_State[row+1][col];
                this.Current_State[row+1][col] = '_';
                current_move = "(" + (row) +"," + (col+1) + "):" + this.Current_State[row][col]+ ":("+ (row+1) +"," +(col+1) +")--" ;
                break;
            case LEFT:
                this.Current_State[row][col] = this.Current_State[row][col-1];
                this.Current_State[row][col-1] = '_';
                current_move = "(" + (row+1) +"," + (col) + "):" + this.Current_State[row][col]+ ":("+ (row+1) +"," +(col+1) +")--" ;
                break;
            case RIGHT:
                this.Current_State[row][col] = this.Current_State[row][col+1];
                this.Current_State[row][col+1] = '_';
                current_move = "(" + (row+1) +"," + (col+2)  + "):" + this.Current_State[row][col]+ ":("+ (row+1) +"," +(col+1) +")--" ;
                break;
            default:
                break;
        }
        this.Cost = prev_node.Cost+ get_cost_by_color(this.Current_State[row][col]);
        this.Path = prev_node.getPath() + current_move;

    }
    public boolean can_move_ball_from_above(int row, int col)
    {
      if( row-1<0  || this.getCurrent_state()[row-1][col]=='_')
          return false;
      return true;
    }
    public boolean can_move_ball_from_below(int row, int col)
    {
        if(row+1>=this.Board_size|| this.getCurrent_state()[row+1][col]=='_')
            return false;
        return true;
    }
    public boolean can_move_ball_from_the_right(int row,int col)
    {
        if(col+1>=this.Board_size || this.getCurrent_state()[row][col+1]=='_')
            return false;
        return true;
    }
    public boolean can_move_ball_from_the_left(int row,int col)
    {
        if(col-1<0 || this.getCurrent_state()[row][col-1]=='_')
            return false;
        return true;
    }
    private int get_cost_by_color(char color)
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
    public boolean is_empty_block(int row, int col)
    {
        if(this.getCurrent_state()[row][col] == '_')
        {return true;}
        return false;
    }
   public String get_node_unique_key()
   {
       return Arrays.deepToString(this.getCurrent_state());
   }

    public int getBoard_size() {
        return Board_size;
    }

    public char[][] getCurrent_state() {
        return this.Current_State;
    }

    public String getPath() {
        return this.Path;
    }

    public int getCost() {
        return this.Cost;
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
}
