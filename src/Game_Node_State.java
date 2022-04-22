public class Game_Node_State {
    private char[][] Prev_State;
    private char[][] Current_State;
    private String Path;
    private int Cost;

    public Game_Node_State(String cur_state, int board_size){
        this.Current_State = new char[board_size][board_size];
        this.Prev_State= new char[board_size][board_size];
        this.Path ="";
        this.Cost =0;
        int k =0;
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                this.Current_State[i][j] = cur_state.charAt(k);
                this.Prev_State[i][j] = cur_state.charAt(k);
                k = k+2;
            }
        }
    }
    public Game_Node_State(Game_Node_State present_state, Direction direct, int board_size, int row , int col){
        char temp_char;
        this.Current_State = new char[board_size][board_size];
        this.Prev_State= present_state.getCurrent_state();
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
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
    public boolean can_ball_move_up(int row,int col)
    {
      if(row-1<0 || this.Current_State[row-1][col]!='_' )
          return false;
      return true;
    }
    public boolean can_ball_move_down(int row,int col,int board_size)
    {
        if(row+1>=board_size || this.Current_State[row+1][col]!='_' )
            return false;
        return true;
    }
    public boolean can_ball_move_right(int row,int col,int board_size)
    {
        if(col+1>=board_size || this.Current_State[row][col+1]!='_' )
            return false;
        return true;
    }
    public boolean can_ball_move_left(int row,int col)
    {
        if(col-1<0 || this.Current_State[row][col-1]!='_' )
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


    public char[][] getPrev_state() {
        return Prev_State;
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
}
