public class Board_State {
    private char[][] prev_state;
    private char[][] current_state;

    public Board_State(String cur_state, int board_size){
        this.current_state = new char[board_size][board_size];
        this.prev_state= new char[board_size][board_size];
        int k =0;
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                this.current_state[i][j] = cur_state.charAt(k);
                this.prev_state[i][j] = cur_state.charAt(k);
                k = k+2;
            }
        }
    }
    public boolean can_ball_move_up(int row,int col)
    {
      if(row-1<0 || this.current_state[row-1][col]!='_' )
          return false;
      return true;
    }
    public boolean can_ball_move_down(int row,int col,int board_size)
    {
        if(row+1>=board_size || this.current_state[row+1][col]!='_' )
            return false;
        return true;
    }
    public boolean can_ball_move_right(int row,int col,int board_size)
    {
        if(col+1>=board_size || this.current_state[row][col+1]!='_' )
            return false;
        return true;
    }
    public boolean can_ball_move_left(int row,int col)
    {
        if(col-1<0 || this.current_state[row][col-1]!='_' )
            return false;
        return true;
    }


    public char[][] getPrev_state() {
        return prev_state;
    }


    public char[][] getCurrent_state() {
        return current_state;
    }

}
