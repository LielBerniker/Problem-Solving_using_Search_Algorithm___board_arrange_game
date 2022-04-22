

public class Game_Board {
    private String Algo_Name;
    private boolean Print_Info;
    private int Board_Size;
    private Game_Node_State First_State;
    private Game_Node_State Goal_State;
    private int Node_counter;

    public Game_Board(String Algo_name, String Print_Info, String Board_Size, String Board_State, String Goal_State){
        this.Algo_Name = Algo_name;
        this.Node_counter=0;
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
        this.First_State = new Game_Node_State(Board_State,this.Board_Size);
        this.Goal_State= new Game_Node_State(Goal_State,this.Board_Size);
    }

    public String getAlgo_Name() {
        return Algo_Name;
    }


    public boolean isPrint_Info() {
        return Print_Info;
    }


    public int getBoard_Size() {
        return Board_Size;
    }

    public Game_Node_State getFirst_State() {
        return First_State;
    }

    public Game_Node_State getGoal_State() {
        return Goal_State;
    }


    public int getNode_counter() {
        return Node_counter;
    }

    public void add_Node_counter(int node_counter) {
        this.Node_counter += node_counter;
    }
}
