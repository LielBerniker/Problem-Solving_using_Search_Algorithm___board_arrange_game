import java.util.ArrayDeque;
import java.util.Queue;

public class Fix_Me_Board {
    private String Algo_Name;
    private boolean Print_Info;
    private int Board_Size;
    private Board_State Board_Balls_State;
    private Board_State Goal_State;

    public Fix_Me_Board(String Algo_name,String Print_Info,String Board_Size,String Board_State,String Goal_State){
        this.Algo_Name = Algo_name;
        if(Board_Size.equals("small"))
        {
            this.Board_Size = 3;
        }
        else {
            this.Board_Size = 5;
        }
        if(Print_Info.equals("with open"))
        {
            this.Print_Info = true;
        }
        else {
            this.Print_Info = false;
        }
        this.Board_Balls_State = new Board_State(Board_State,this.Board_Size);
        this.Goal_State= new Board_State(Goal_State,this.Board_Size);
    }

    public String getAlgo_Name() {
        return Algo_Name;
    }

    public Board_State getBoard_Balls_State() {
        return Board_Balls_State;
    }

    public void setBoard_Balls_State(Board_State board_Balls_State) {
        Board_Balls_State = board_Balls_State;
    }
    public void BFS()
    {
        Queue<Board_State> all_states = new ArrayDeque<>();
        all_states.add(this.Board_Balls_State);
        Board_State current_state;
        while(!all_states.isEmpty())
        {
            current_state = all_states.poll();
            if(current_state.getCurrent_state().equals(this.Goal_State))
            {
                System.out.println("finish");
            }
            for (int i = 0; i < this.Board_Size; i++) {
                for (int j = 0; j < this.Board_Size; j++) {
                     if(!is_ball_well_located(current_state,i,j))
                     {
                        if()
                     }
                }
            }
        }
    }



}
