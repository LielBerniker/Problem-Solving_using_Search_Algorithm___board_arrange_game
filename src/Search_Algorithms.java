import java.util.*;

public class Search_Algorithms {
    Game_Board board;

    public Search_Algorithms(Game_Board board)
    {
        this.board = board;
    }
    public void BFS()
    {
        Game_Node_State start_node,goal_node,current_node_state;
        int current_node_count =0;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        Queue<Game_Node_State> all_states = new ArrayDeque<>();
        Hashtable<String,Game_Node_State> frontier_nodes = new Hashtable<>();
        Hashtable<String,Game_Node_State> explored_nodes = new Hashtable<>();

        all_states.add(start_node);
        frontier_nodes.put(start_node.get_node_unique_key(),start_node);

        while(!all_states.isEmpty())
        {
            current_node_state = all_states.poll();

            explored_nodes.put(current_node_state.get_node_unique_key(),current_node_state);
            List<Game_Node_State> all_possible_children = this.find_all_children(current_node_state);
            this.board.add_Node_counter(all_possible_children.size());
            for (Game_Node_State current_child:all_possible_children ) {
                if((!explored_nodes.containsKey(current_child.get_node_unique_key())) && (!frontier_nodes.containsKey(current_child.get_node_unique_key())))
                {
                    if(current_child.equals(goal_node))
                    {
                        System.out.println("finish");
                    }
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.get_node_unique_key(),current_child);

                }
            }
        }
    }
    public List<Game_Node_State> find_all_children(Game_Node_State current_node)
    {
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(current_node.contain_ball(i,j))
                {
                    if(current_node.can_ball_move_up(i))
                    {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node,Direction.UP,size,i,j);
                        possible_children.add(current_node_state_up);
                    }
                    if(current_node.can_ball_move_down(i))
                    {
                        Game_Node_State current_node_state_down = new Game_Node_State(current_node,Direction.DOWN,size,i,j);
                        possible_children.add(current_node_state_down);
                    }
                    if(current_node.can_ball_move_right(j))
                    {
                        Game_Node_State current_node_state_right = new Game_Node_State(current_node,Direction.RIGHT,size,i,j);
                        possible_children.add(current_node_state_right);
                    }
                    if(current_node.can_ball_move_left(j))
                    {
                        Game_Node_State current_node_state_left = new Game_Node_State(current_node,Direction.LEFT,size,i,j);
                        possible_children.add(current_node_state_left);
                    }}
            }
        }
       return possible_children;
    }
}
