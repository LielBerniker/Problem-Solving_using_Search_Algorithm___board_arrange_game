import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game_Board {
    /* this class contains a general information about the board in the game*/
    private String algo_Name; // name of the algorithm
    private boolean print_Info;// boolean that contain if to print information
    private int board_Size;// size of board
    private Game_Node_State first_State; // state to begin with
    private Game_Node_State goal_State;// goal state

    public Game_Board(String Algo_name, String Print_Info, String Board_Size, String Board_State, String Goal_State){
        /**
         * initiate the board by the information the function gets
         * @param Algo_name
         * @param Print_Info
         * @param Board_Size
         * @param Board_State
         * @param Goal_State
         */
        this.algo_Name = Algo_name;
        // size of board
        if(Board_Size.equals("small"))
        {
            this.board_Size = 3;
        }
        else {
            this.board_Size = 5;
        }
        // if to print to screen
        if(Print_Info.equals("with open"))
        {
            this.print_Info = true;
        }
        else {
            this.print_Info = false;
        }
        // set the First state and goal state
        this.first_State = new Game_Node_State(Board_State,this.board_Size);
        this.goal_State= new Game_Node_State(Goal_State,this.board_Size);
    }

    public String getAlgo_Name() {
        /** return the algorithm name
         * @return algo name
         */
        return algo_Name;
    }

    public boolean isPrint_Info() {
        /**
         * @return boolean that contain true if needed to print to screen else false
         */
        return print_Info;
    }

    public int getBoard_Size() {
        /** return the board size
         * @return board size
         */
        return board_Size;
    }

    public Game_Node_State getFirst_State() {
        /** return the first node
         * @return the first state(node)
         */
        return first_State;
    }

    public Game_Node_State getGoal_State() {
        /** return the goal node
         * @return goal node
         */
        return goal_State;
    }

    public void setGoal_State(Game_Node_State goal_State) {
        /** set the goal state(node)
         * @param goal_State
         * @return
         */
        this.goal_State = goal_State;
    }


}
