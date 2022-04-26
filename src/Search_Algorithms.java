import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

enum Result_DFS{
    GOAL,
    FAIL,
    CUTOFF;
    }
public class Search_Algorithms {
    private Game_Board board;
    private int node_counter;
    private static final int MAX_VAL = Integer.MAX_VALUE;
    private long elapsedTime;

    public Search_Algorithms(Game_Board board)
    {
        this.elapsedTime = 0;
        this.node_counter = 0;
        this.board = board;
    }

    public void BFS() throws IOException {
        this.setElapsedTime(System.currentTimeMillis());
        Game_Node_State start_node,goal_node,current_node_state;
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
            List<Game_Node_State> all_possible_children = this.find_all_children(current_node_state, frontier_nodes);
            this.addNode_counter(all_possible_children.size());
            for (Game_Node_State current_child:all_possible_children ) {
                if((!explored_nodes.containsKey(current_child.get_node_unique_key())))
                {
                    if(current_child.equals(goal_node))
                    {

                        long start  = this.getElapsedTime();
                        this.setElapsedTime(System.currentTimeMillis() -start);
                        this.board.setGoal_State(current_child);
                        create_output(true);
                        return;
                    }
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.get_node_unique_key(),current_child);

                }
            }
        }
        long start  = this.getElapsedTime();
        this.setElapsedTime(System.currentTimeMillis() -start);
        create_output(false);
    }
    public void DFID() throws IOException {
        Game_Node_State start_node,goal_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        this.setElapsedTime(System.currentTimeMillis());
        for (int i = 0; i <MAX_VAL; i++) {
            Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();
            Result_DFS limited_dfs_results = Limited_DFS(start_node,goal_node,i,frontier_nodes);
            if(limited_dfs_results != Result_DFS.CUTOFF)
            {
                if(limited_dfs_results == Result_DFS.GOAL)
                {
                    long start  = this.getElapsedTime();
                    this.setElapsedTime(System.currentTimeMillis() -start);
                    create_output(true);
                    return;
                }
            }
        }
        long start  = this.getElapsedTime();
        this.setElapsedTime(System.currentTimeMillis() -start);
        create_output(false);
    }
    public Result_DFS Limited_DFS(Game_Node_State current_node,Game_Node_State goal_node,int limit,Hashtable<String,Game_Node_State> frontier_nodes ) throws IOException {
        if(current_node.equals(goal_node))
        {
            this.board.setGoal_State(current_node);
            return Result_DFS.GOAL;
        }
        else if(limit == 0)
        {
           return Result_DFS.CUTOFF;
        }
        else
        {
            frontier_nodes.put(current_node.get_node_unique_key(),current_node);
            boolean is_cutoff = false;
            List<Game_Node_State> all_possible_children = this.find_all_children(current_node, frontier_nodes);
            for (Game_Node_State current_child:all_possible_children) {
                 this.addNode_counter(1);
                 Result_DFS result = Limited_DFS(current_child,goal_node,limit-1,frontier_nodes);
                 if(result == Result_DFS.CUTOFF)
                     is_cutoff = true;
                 else if(result !=Result_DFS.FAIL)
                     return result;
                 }
            frontier_nodes.remove(current_node);
            if(is_cutoff)
                return Result_DFS.CUTOFF;
            else
                return Result_DFS.FAIL;

            }
        }
        public void A_STAR() throws IOException {

            this.setElapsedTime(System.currentTimeMillis());
            Game_Node_State start_node,goal_node,current_node_state;
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
                frontier_nodes.remove(current_node_state);
                if(current_node_state.equals(goal_node))
                {

                    long start  = this.getElapsedTime();
                    this.setElapsedTime(System.currentTimeMillis() -start);
                    this.board.setGoal_State(current_node_state);
                    create_output(true);
                    return;
                }
                explored_nodes.put(current_node_state.get_node_unique_key(),current_node_state);
                List<Game_Node_State> all_possible_children = this.find_all_children_A_STAR(current_node_state, frontier_nodes,all_states);
                this.addNode_counter(all_possible_children.size());
                for (Game_Node_State current_child:all_possible_children) {
                    if((!explored_nodes.containsKey(current_child.get_node_unique_key())))
                    {

                        all_states.add(current_child);
                        frontier_nodes.put(current_child.get_node_unique_key(),current_child);

                    }
                }
            }
            long start  = this.getElapsedTime();
            this.setElapsedTime(System.currentTimeMillis() -start);
            create_output(false);
        }


    public List<Game_Node_State> find_all_children(Game_Node_State current_node,Hashtable<String,Game_Node_State> frontier_nodes)
    {
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(current_node.is_empty_block(i,j) )
                {
                    if(current_node.can_move_ball_from_above(i,j))
                    {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node,Direction.UP,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_up.get_node_unique_key()))
                        {possible_children.add(current_node_state_up);}
                    }
                    if(current_node.can_move_ball_from_below(i,j))
                    {
                        Game_Node_State current_node_state_down = new Game_Node_State(current_node,Direction.DOWN,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_down.get_node_unique_key()))
                        {possible_children.add(current_node_state_down);}
                    }
                    if(current_node.can_move_ball_from_the_right(i,j))
                    {
                        Game_Node_State current_node_state_right = new Game_Node_State(current_node,Direction.RIGHT,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_right.get_node_unique_key()))
                        {possible_children.add(current_node_state_right);}
                    }
                    if(current_node.can_move_ball_from_the_left(i,j))
                    {
                        Game_Node_State current_node_state_left = new Game_Node_State(current_node,Direction.LEFT,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_left.get_node_unique_key()))
                        {possible_children.add(current_node_state_left);}
                    }}
            }
        }
       return possible_children;
    }
    public List<Game_Node_State> find_all_children_A_STAR(Game_Node_State current_node,Hashtable<String,Game_Node_State> frontier_nodes,Queue<Game_Node_State> all_states)
    {
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(current_node.is_empty_block(i,j) ) {
                    if (current_node.can_move_ball_from_above(i, j)) {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node, Direction.UP, size, i, j);
                        if (!frontier_nodes.containsKey(current_node_state_up.get_node_unique_key())) {
                            possible_children.add(current_node_state_up);
                        } else {
                            check_better_node(current_node_state_up, frontier_nodes, all_states);
                        }
                    }

                    if(current_node.can_move_ball_from_below(i,j))
                    {
                        Game_Node_State current_node_state_down = new Game_Node_State(current_node,Direction.DOWN,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_down.get_node_unique_key()))
                        {possible_children.add(current_node_state_down);}
                        else{check_better_node(current_node_state_down,frontier_nodes,all_states); }
                    }

                    if(current_node.can_move_ball_from_the_right(i,j))
                    {
                        Game_Node_State current_node_state_right = new Game_Node_State(current_node,Direction.RIGHT,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_right.get_node_unique_key()))
                        {possible_children.add(current_node_state_right);}
                        else{check_better_node(current_node_state_right,frontier_nodes,all_states); }
                    }

                    if(current_node.can_move_ball_from_the_left(i,j))
                    {
                        Game_Node_State current_node_state_left = new Game_Node_State(current_node,Direction.LEFT,size,i,j);
                        if(!frontier_nodes.containsKey(current_node_state_left.get_node_unique_key()))
                        {possible_children.add(current_node_state_left);}
                        else{check_better_node(current_node_state_left,frontier_nodes,all_states); }
                    }

                    }
                  }
            }

        return possible_children;
    }
    public void check_better_node(Game_Node_State current_node,Hashtable<String,Game_Node_State> frontier_nodes,Queue<Game_Node_State> all_states)
    {
        Game_Node_State old_node = frontier_nodes.get(current_node.get_node_unique_key());

        //if child.f() > olderChild.f()
    /*    if( old_node > current_node )*/
        if(1==1){
            // replace child and olderChild
            all_states.remove(old_node);
            all_states.add(current_node);
            frontier_nodes.put(current_node.get_node_unique_key(), current_node);
    }
    }
    public int getNode_counter() {
        return node_counter;
    }

    public void addNode_counter(int node_counter) {
        this.node_counter += node_counter;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void create_output( boolean find_goal ) throws IOException {
        String balls_movement = "";
        String cost = "";
        if(find_goal==false)
        {
             balls_movement = "no path";
             cost = "inf";
        }
        else {
            String path = this.board.getGoal_State().getPath();
            balls_movement = path.substring(0, path.length() - 2);
            cost = String.valueOf(this.board.getGoal_State().getCost());
        }
        String all_info = balls_movement + "\nNum: " + this.getNode_counter() + "\nCost: " + cost + "\n" + (this.getElapsedTime() / 1000F) + " seconds\n";
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(all_info);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
