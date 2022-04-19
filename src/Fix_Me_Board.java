public class Fix_Me_Board {
    private String Algo_Name;
    private boolean Print_Info;
    private int Board_Size;
    private String Board_Balls_State;
    private String Goal_State;

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
        this.Board_Balls_State = Board_State;
        this.Goal_State = Goal_State;
    }
}
