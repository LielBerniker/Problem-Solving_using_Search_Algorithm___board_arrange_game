import java.lang.reflect.Array;
import java.util.Arrays;

public class Game_Node_State implements Comparable<Game_Node_State>{
    private char[][] Prev_State;
    private char[][] Current_State;
    private String Path;
    private int Board_size;
    private int Cost;


    public Game_Node_State(String cur_state, int board_size){
        this.Current_State = new char[board_size][board_size];
        this.Prev_State= new char[board_size][board_size];
        this.Path ="";
        this.Cost =0;
        this.Board_size = board_size;
        int k =0;
        for (int i = 0; i < this.Board_size; i++) {
            for (int j = 0; j < this.Board_size; j++) {
                this.Current_State[i][j] = cur_state.charAt(k);
                this.Prev_State[i][j] = cur_state.charAt(k);
                k = k+2;
            }
        }
    }
    public Game_Node_State(Game_Node_State present_state, Direction direct, int board_size, int row , int col){
        char temp_char;
        this.Board_size = board_size;
        this.Current_State = new char[board_size][board_size];
        this.Prev_State= present_state.getCurrent_state();
        for (int i = 0; i < this.Board_size; i++) {
            for (int j = 0; j < this.Board_size; j++) {
                this.Current_State[i][j] = present_state.getPrev_state()[i][j];
            }
        }
        temp_char = this.Current_State[row][col];
        this.Cost = present_state.Cost+ get_cost_by_color(temp_char);
        set_board_state(row,col,direct,temp_char);

    }
    private void set_board_state(int row, int col,Direction direct,char ball)
    {
        String current_move="";
        switch(direct) {
            case UP:
                current_move = "(" + (row+1) +"," +(col+1) + "):" + this.Current_State[row][col]+ ":("+ (row+2) +"," + (col+1) +")--" ;
                this.Current_State[row][col] = this.Current_State[row-1][col];
                this.Current_State[row-1][col] = ball;
                break;
            case DOWN:
                current_move = "(" + (row+1) +"," +(col+1) + "):" + this.Current_State[row][col]+ ":("+ (row) +"," + (col+1) +")--" ;
                this.Current_State[row][col] = this.Current_State[row+1][col];
                this.Current_State[row+1][col] = ball;
                break;
            case LEFT:
                current_move = "(" + (row+1) +"," +(col+1) + "):" + this.Current_State[row][col]+ ":("+ (row+1) +"," + (col) +")--" ;
                this.Current_State[row][col] = this.Current_State[row][col-1];
                this.Current_State[row][col-1] = ball;
                break;
            case RIGHT:
                current_move = "(" + (row+1) +"," +(col+1) + "):" + this.Current_State[row][col]+ ":("+ (row+2) +"," + (col+2) +")--" ;
                this.Current_State[row][col] = this.Current_State[row][col+1];
                this.Current_State[row][col+1] = ball;
                break;
            default:
                break;
        }
        this.Path = this.Path + current_move;
    }
    public boolean can_ball_move_up(int row)
    {
      if(row-1<0 )
          return false;
      return true;
    }
    public boolean can_ball_move_down(int row)
    {
        if(row+1>=this.Board_size )
            return false;
        return true;
    }
    public boolean can_ball_move_right(int col)
    {
        if(col+1>=this.Board_size )
            return false;
        return true;
    }
    public boolean can_ball_move_left(int col)
    {
        if(col-1<0 )
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
    public boolean contain_ball(int row, int col)
    {
        if(this.getCurrent_state()[row][col] != '_')
        {return true;}
        return false;
    }
   public String get_node_unique_key()
   {
       return Arrays.deepToString(this.getCurrent_state());
   }

    public char[][] getPrev_state() {
        return Prev_State;

    }
    public int getBoard_size() {
        return Board_size;
    }

    public char[][] getCurrent_state() {
        return Current_State;
    }

    public String getPath() {
        return Path;
    }

    public int getCost() {
        return Cost;
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
