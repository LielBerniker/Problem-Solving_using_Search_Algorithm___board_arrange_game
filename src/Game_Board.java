import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game_Board {
    private String algo_Name;
    private boolean print_Info;
    private int board_Size;
    private Game_Node_State first_State;
    private Game_Node_State goal_State;

    public Game_Board(String Algo_name, String Print_Info, String Board_Size, String Board_State, String Goal_State){
        this.algo_Name = Algo_name;
        if(Board_Size.equals("small"))
        {
            this.board_Size = 3;
        }
        else {
            this.board_Size = 5;
        }
        if(Print_Info.equals("with open"))
        {
            this.print_Info = true;
        }
        else {
            this.print_Info = false;
        }
        this.first_State = new Game_Node_State(Board_State,this.board_Size);
        this.goal_State= new Game_Node_State(Goal_State,this.board_Size);
    }

    public String getAlgo_Name() {
        return algo_Name;
    }


    public boolean isPrint_Info() {
        return print_Info;
    }


    public int getBoard_Size() {
        return board_Size;
    }

    public Game_Node_State getFirst_State() {
        return first_State;
    }

    public Game_Node_State getGoal_State() {
        return goal_State;
    }




    public void setGoal_State(Game_Node_State goal_State) {
        this.goal_State = goal_State;
    }


}
