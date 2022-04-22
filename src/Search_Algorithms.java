import java.util.ArrayDeque;
import java.util.Queue;

public class Search_Algorithms {
    Game_Board board;

    public Search_Algorithms(Game_Board board)
    {
        this.board = board;
    }
    public void BFS()
    {
        int size = this.board.getBoard_Size();
        int current_node_count =0;
        Queue<Game_Node_State> all_states = new ArrayDeque<>();
        all_states.add(this.board.getFirst_State());
        Game_Node_State current_node_state;
        while(!all_states.isEmpty())
        {
            current_node_state = all_states.poll();
            if(current_node_state.getCurrent_state().equals(this.board.getGoal_State()))
            {
                System.out.println("finish");
            }
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                     current_node_count =0;
                     if(current_node_state.can_ball_move_up(i,j))
                     {
                         Game_Node_State current_node_state_up = new Game_Node_State(current_node_state,Direction.UP,size,i,j);
                         all_states.add(current_node_state_up);
                         current_node_count++;
                     }
                    if(current_node_state.can_ball_move_down(i,j,size))
                    {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node_state,Direction.DOWN,size,i,j);
                        all_states.add(current_node_state_up);
                        current_node_count++;
                    }
                    if(current_node_state.can_ball_move_right(i,j,size))
                    {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node_state,Direction.RIGHT,size,i,j);
                        all_states.add(current_node_state_up);
                        current_node_count++;
                    }
                    if(current_node_state.can_ball_move_left(i,j))
                    {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node_state,Direction.LEFT,size,i,j);
                        all_states.add(current_node_state_up);
                        current_node_count++;
                    }
                    this.board.add_Node_counter(current_node_count);
                }
            }
        }
    }
}