import java.awt.*;
import java.util.*;
import java.util.List;

enum Result_DFS {
    GOAL,
    FAIL,
    CUTOFF
}

public class Search_Algorithms {
    private Game_Board board;
    private int node_counter;
    private long elapsed_time;
    private boolean find_goal;
    private static final int MAX_VAL = Integer.MAX_VALUE;

    class Block_Info {
        int row;
        int col;
        int dist;

        public Block_Info(int row, int col, int dist) {
            this.row = row;
            this.col = col;
            this.dist = dist;
        }
    }

    public Search_Algorithms(Game_Board board) {
        this.find_goal = false;
        this.elapsed_time = 0;
        this.node_counter = 0;
        this.board = board;
    }

    public Game_Node_State runAlgorithm() {
        String algo = this.board.getAlgo_Name();
        Game_Node_State final_node = null;
        switch (algo) {
            case "BFS":
                final_node = this.BFS();
                break;
            case "DFID":
                final_node = this.DFID();
                break;
            case "A*":
                final_node = this.A_STAR();
                break;
            case "IDA*":
                final_node = this.IDA_STAR();
                break;
            case "DFBnB":
                final_node = this.DFBnB();
                break;
            default:
                break;
        }
        return final_node;
    }

    private Game_Node_State BFS() {
        setTime(0);
        Game_Node_State start_node, goal_node, current_node_state;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        Queue<Game_Node_State> all_states = new ArrayDeque<>();
        Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();
        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        all_states.add(start_node);
        frontier_nodes.put(start_node.getNode_unique_key(), start_node);

        while (!all_states.isEmpty()) {
            openList(all_states);
            current_node_state = all_states.poll();

            explored_nodes.put(current_node_state.getNode_unique_key(), current_node_state);
            List<Game_Node_State> all_possible_children = this.findAll_children_BFS(current_node_state, frontier_nodes);
            this.addNode_counter(all_possible_children.size());
            for (Game_Node_State current_child : all_possible_children) {
                if ((!explored_nodes.containsKey(current_child.getNode_unique_key()))) {
                    if (current_child.equals(goal_node)) {
                        setTime(this.getElapsedTime());
                        this.setFind_goal(true);
                        return current_child;
                    }
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.getNode_unique_key(), current_child);

                }
            }
        }
        setTime(this.getElapsedTime());
        return this.board.getFirst_State();
    }

    private Game_Node_State DFID() {
        Game_Node_State start_node, goal_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        setTime(0);
        for (int i = 1; i < MAX_VAL; i++) {
            Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();
            Result_DFS limited_dfs_results = LimitedDFS(start_node, goal_node, i, frontier_nodes);
            if (limited_dfs_results != Result_DFS.CUTOFF) {
                if (limited_dfs_results == Result_DFS.GOAL) {
                    setTime(this.getElapsedTime());
                    this.setFind_goal(true);
                    return this.board.getGoal_State();
                }
            }
        }
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    private Result_DFS LimitedDFS(Game_Node_State current_node, Game_Node_State goal_node, int limit, Hashtable<String, Game_Node_State> frontier_nodes) {
        openList(frontier_nodes);
        if (current_node.equals(goal_node)) {
            this.board.setGoal_State(current_node);
            return Result_DFS.GOAL;
        } else if (limit == 0) {
            return Result_DFS.CUTOFF;
        } else {
            frontier_nodes.put(current_node.getNode_unique_key(), current_node);
            boolean is_cutoff = false;
            List<Game_Node_State> all_possible_children = this.findAll_children_BFS(current_node, frontier_nodes);
            for (Game_Node_State current_child : all_possible_children) {
                this.addNode_counter(1);
                Result_DFS result = LimitedDFS(current_child, goal_node, limit - 1, frontier_nodes);
                if (result == Result_DFS.CUTOFF)
                    is_cutoff = true;
                else if (result != Result_DFS.FAIL)
                    return result;
            }
            frontier_nodes.remove(current_node.getNode_unique_key());
            if (is_cutoff)
                return Result_DFS.CUTOFF;
            else
                return Result_DFS.FAIL;

        }
    }

    private Game_Node_State A_STAR() {

        this.setElapsedTime(System.currentTimeMillis());
        Game_Node_State start_node, goal_node, current_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        int iteration = 0;
        PriorityQueue<Game_Node_State> all_states = new PriorityQueue<>();
        Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();

        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();
        start_node.setHeuristic(calculateHeuristic_value(start_node));
        all_states.add(start_node);
        frontier_nodes.put(start_node.getNode_unique_key(), start_node);

        while (!all_states.isEmpty()) {
            openList(all_states);
            current_node = all_states.poll();
            frontier_nodes.remove(current_node.getNode_unique_key());
            if (current_node.equals(goal_node)) {

                setTime(this.getElapsedTime());
                this.setFind_goal(true);
                return current_node;
            }

            explored_nodes.put(current_node.getNode_unique_key(), current_node);
            List<Game_Node_State> all_possible_children = this.findAll_children(current_node);
            this.addNode_counter(all_possible_children.size());
            for (Game_Node_State current_child : all_possible_children) {
                current_child.setHeuristic(calculateHeuristic_value(current_child));
                current_child.setIteration(iteration);
                if ((!explored_nodes.containsKey(current_child.getNode_unique_key())) &&
                        (!frontier_nodes.containsKey(current_child.getNode_unique_key()))) {
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.getNode_unique_key(), current_child);
                } else if (frontier_nodes.containsKey(current_child.getNode_unique_key())) {
                    Game_Node_State old_node = frontier_nodes.get(current_node.getNode_unique_key());

                    //if child.f() > olderChild.f()
                    if (old_node.currentIs_bigger(current_child)) {
                        // replace child and olderChild
                        all_states.remove(old_node);
                        all_states.add(current_child);
                        frontier_nodes.put(current_node.getNode_unique_key(), current_child);
                    }
                }
            }
            iteration++;
        }
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    public Game_Node_State IDA_STAR() {
        this.setElapsedTime(System.currentTimeMillis());
        Game_Node_State start_node, goal_node, current_node;
        int threshold, minF;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();

        Stack<Game_Node_State> all_states = new Stack<>();

        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();
        start_node.setHeuristic(calculateHeuristic_value(start_node));
        threshold = start_node.getHeuristic();

        while (threshold != MAX_VAL) {
            openList(all_states);

            minF = MAX_VAL;

            all_states.push(start_node);
            explored_nodes.put(start_node.getNode_unique_key(), start_node);
            while (!all_states.isEmpty()) {
                openList(all_states);
                current_node = all_states.pop();
                if (current_node.isOut()) {
                    explored_nodes.remove(current_node.getNode_unique_key());
                } else {
                    current_node.setOut(true);
                    all_states.push(current_node);
                    List<Game_Node_State> all_possible_children = this.findAll_children(current_node);
                    for (Game_Node_State current_child : all_possible_children) {
                        this.addNode_counter(1);
                        current_child.setHeuristic(this.calculateHeuristic_value(current_child));
                        if (current_child.f_n() > threshold) {
                            minF = Math.min(minF, current_child.f_n());
                            continue;
                        }
                        Game_Node_State old_node = explored_nodes.get(current_child.getNode_unique_key());
                        if (old_node != null && old_node.isOut()) {
                            continue;
                        }
                        if (old_node != null && (!old_node.isOut())) {
                            old_node.setHeuristic(calculateHeuristic_value(old_node));
                            if (old_node.getHeuristic() > current_child.getHeuristic()) {
                                all_states.remove(old_node);
                                explored_nodes.remove(old_node.getNode_unique_key());
                            } else {
                                continue;
                            }
                        }
                        if (current_child.equals(goal_node)) {
                            setTime(this.getElapsedTime());
                            this.setFind_goal(true);
                            return current_child;
                        }
                        all_states.push(current_child);
                        explored_nodes.put(current_child.getNode_unique_key(), current_child);
                    }
                }
            }
            start_node.setOut(false);
            threshold = minF;
        }
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    private Game_Node_State DFBnB() {
        this.setElapsedTime(System.currentTimeMillis());
        Game_Node_State start_node, goal_node, current_node, result ;
        int threshold, minF;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        result = goal_node;
        Stack<Game_Node_State> all_states = new Stack<>();

        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        all_states.push(start_node);
        explored_nodes.put(start_node.getNode_unique_key(), start_node);
        threshold = MAX_VAL;
        while (!all_states.isEmpty()) {
            openList(all_states);
            current_node = all_states.pop();

            if (current_node.isOut()) {
                explored_nodes.remove(current_node.getNode_unique_key());
            } else {
                current_node.setOut(true);
                all_states.push(current_node);
                List<Game_Node_State> all_possible_children = this.getOrdered_children_by_f(current_node);
                this.addNode_counter(all_possible_children.size());
                Iterator<Game_Node_State> child_itr = all_possible_children.iterator();
                while (child_itr.hasNext()) {
                    Game_Node_State current_child = child_itr.next();
                    if (current_child.f_n() >= threshold) {
                        child_itr.remove();
                        while ((child_itr.hasNext()) && (all_possible_children.contains(child_itr.next()))) {
                            child_itr.remove();
                        }
                    } else if (explored_nodes.containsKey(current_child.getNode_unique_key())
                            && explored_nodes.get(current_child.getNode_unique_key()).isOut()) {
                        child_itr.remove();
                    } else if (explored_nodes.containsKey(current_child.getNode_unique_key())
                            && (!explored_nodes.get(current_child.getNode_unique_key()).isOut())) {
                        Game_Node_State old_child = explored_nodes.get(current_child.getNode_unique_key());
                        if (old_child.f_n() <= current_child.f_n()) {
                            child_itr.remove();
                        } else {
                            all_states.remove(old_child);
                            explored_nodes.remove(old_child.getNode_unique_key());
                        }
                    } else if (current_child.equals(goal_node)) {
                        threshold = current_child.f_n();
                        this.setFind_goal(true);
                        result = current_child;
                        while ((child_itr.hasNext()) && (all_possible_children.contains(child_itr.next()))) {
                            child_itr.remove();
                        }
                    }
                }
                Collections.reverse(all_possible_children);
                for (Game_Node_State cur_child : all_possible_children) {
                    all_states.push(cur_child);
                    explored_nodes.put(cur_child.getNode_unique_key(),cur_child);
                }
            }
        }
        setTime(this.getElapsedTime());
        return result;
    }


    private List<Game_Node_State> findAll_children_BFS(Game_Node_State current_node, Hashtable<String, Game_Node_State> frontier_nodes) {
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (current_node.isBlock_empty(i, j)) {
                    if (current_node.canMoveBallFromAbove(i, j)) {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node, Direction.UP, size, i, j);
                        if (!frontier_nodes.containsKey(current_node_state_up.getNode_unique_key()) && !current_node_state_up.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_up);
                        }
                    }
                    if (current_node.canMoveBallFromBelow(i, j)) {
                        Game_Node_State current_node_state_down = new Game_Node_State(current_node, Direction.DOWN, size, i, j);
                        if (!frontier_nodes.containsKey(current_node_state_down.getNode_unique_key()) && !current_node_state_down.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_down);
                        }
                    }
                    if (current_node.canMoveBallFromRight(i, j)) {
                        Game_Node_State current_node_state_right = new Game_Node_State(current_node, Direction.RIGHT, size, i, j);
                        if (!frontier_nodes.containsKey(current_node_state_right.getNode_unique_key()) && !current_node_state_right.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_right);
                        }
                    }
                    if (current_node.canMoveBallFromLeft(i, j)) {
                        Game_Node_State current_node_state_left = new Game_Node_State(current_node, Direction.LEFT, size, i, j);
                        if (!frontier_nodes.containsKey(current_node_state_left.getNode_unique_key()) && !current_node_state_left.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_left);
                        }
                    }
                }
            }
        }
        return possible_children;
    }

    private List<Game_Node_State> findAll_children(Game_Node_State current_node) {
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (current_node.isBlock_empty(i, j)) {
                    if (current_node.canMoveBallFromAbove(i, j)) {
                        Game_Node_State current_node_state_up = new Game_Node_State(current_node, Direction.UP, size, i, j);
                        if (!current_node_state_up.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_up);
                        }
                    }

                    if (current_node.canMoveBallFromBelow(i, j)) {
                        Game_Node_State current_node_state_down = new Game_Node_State(current_node, Direction.DOWN, size, i, j);
                        if (!current_node_state_down.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_down);
                        }
                    }

                    if (current_node.canMoveBallFromRight(i, j)) {
                        Game_Node_State current_node_state_right = new Game_Node_State(current_node, Direction.RIGHT, size, i, j);
                        if (!current_node_state_right.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_right);
                        }
                    }

                    if (current_node.canMoveBallFromLeft(i, j)) {
                        Game_Node_State current_node_state_left = new Game_Node_State(current_node, Direction.LEFT, size, i, j);
                        if (!current_node_state_left.equals(current_node.prev_node)) {
                            possible_children.add(current_node_state_left);
                        }
                    }

                }
            }
        }
        return possible_children;
    }


    public int getNode_counter() {
        return node_counter;
    }

    public void addNode_counter(int node_counter) {
        this.node_counter += node_counter;
    }

    public long getElapsedTime() {
        return elapsed_time;
    }

    public boolean isFind_goal() {
        return find_goal;
    }

    public void setFind_goal(boolean find_goal) {
        this.find_goal = find_goal;
    }

    private void setElapsedTime(long elapsedTime) {
        this.elapsed_time = elapsedTime;
    }

    private void openList(Object obj) {
        if (this.board.isPrint_Info()) {
            System.out.println(obj);
        }
    }

    private void setTime(long start) {
        long current_time = System.currentTimeMillis() - start;
        this.setElapsedTime(current_time);
    }

    private List<Game_Node_State> getOrdered_children_by_f(Game_Node_State current_node) {
        List<Game_Node_State> all_possible_children = this.findAll_children(current_node);
        for (Game_Node_State current_child : all_possible_children) {
            current_child.setHeuristic(calculateHeuristic_value(current_child));
        }
        Collections.sort(all_possible_children);
        return all_possible_children;
    }

    private int calculateHeuristic_value(Game_Node_State current_node) {
        return alternateMenhaden_algorithm(current_node);
    }

    private int alternateMenhaden_algorithm(Game_Node_State current_node) {
        int total_cost = 0;
        int size = this.board.getBoard_Size();
        char current_block;
        char[] balls_color;
        if (size == 3) {
            balls_color = new char[]{'R', 'B', 'G'};
        } else {
            balls_color = new char[]{'R', 'B', 'G', 'Y'};
        }
        for (char current_color : balls_color) {
            Point[] balls_in_goal = this.board.getGoal_State().findBalls_location_by_color(current_color);
            Point[] balls_in_current = current_node.findBalls_location_by_color(current_color);
            int color_cost = current_node.getCost_by_color(current_color);
            total_cost += findMin_cost(balls_in_goal, balls_in_current, color_cost);
        }
        return total_cost;
    }

    private int findMin_cost(Point[] balls_in_goal, Point[] balls_in_current, int color_cost) {
        int val;

        if (this.board.getBoard_Size() == 3) {
            int val1 = ballsDistance2(balls_in_current[0], balls_in_current[1], balls_in_goal[0], balls_in_goal[1]);
            int val2 = ballsDistance2(balls_in_current[1], balls_in_current[0], balls_in_goal[0], balls_in_goal[1]);
            val = color_cost * (Math.min(val1, val2));
        } else {
            int[] all_values = new int[6];
            all_values[0] = ballsDistance3(balls_in_current[0], balls_in_current[1], balls_in_current[2],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            all_values[1] = ballsDistance3(balls_in_current[0], balls_in_current[2], balls_in_current[1],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            all_values[2] = ballsDistance3(balls_in_current[1], balls_in_current[0], balls_in_current[2],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            all_values[3] = ballsDistance3(balls_in_current[1], balls_in_current[2], balls_in_current[0],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            all_values[4] = ballsDistance3(balls_in_current[2], balls_in_current[1], balls_in_current[0],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            all_values[5] = ballsDistance3(balls_in_current[2], balls_in_current[0], balls_in_current[1],
                    balls_in_goal[0], balls_in_goal[1], balls_in_goal[2]);
            val = color_cost * (getMin(all_values));
        }
        return val;
    }

    private int getMin(int[] inputArr) {
        int minVal = inputArr[0];
        for (int i = 1; i < inputArr.length; i++) {
            if (inputArr[i] < minVal) {
                minVal = inputArr[i];
            }
        }
        return minVal;
    }

    private int ballsDistance2(Point current_p1, Point current_p2, Point goal_p1, Point goal_p2) {
        int current_cost = minDistance(current_p1, goal_p1) + minDistance(current_p2, goal_p2);
        return current_cost;
    }

    private int ballsDistance3(Point current_p1, Point current_p2, Point current_p3
            , Point goal_p1, Point goal_p2, Point goal_p3) {
        int current_cost = minDistance(current_p1, goal_p1) + minDistance(current_p2, goal_p2)
                + minDistance(current_p3, goal_p3);
        return current_cost;
    }

    private int minDistance(Point start_p, Point end_p) {
        int size = this.board.getBoard_Size();
        Block_Info start = new Block_Info(start_p.x, start_p.y, 0);
        // applying BFS on matrix cells starting from source
        Queue<Block_Info> queue = new LinkedList<>();
        queue.add(start);

        boolean[][] visited = new boolean[size][size];
        visited[start.row][start.col] = true;

        while (queue.isEmpty() == false) {
            Block_Info current_block = queue.remove();

            // Destination found;
            if (current_block.row == end_p.x && current_block.col == end_p.y)
                return current_block.dist;

            // moving up
            if (isValid(current_block.row - 1, current_block.col, size, visited)) {
                queue.add(new Block_Info(current_block.row - 1, current_block.col, current_block.dist + 1));
                visited[current_block.row - 1][current_block.col] = true;
            }

            // moving down
            if (isValid(current_block.row + 1, current_block.col, size, visited)) {
                queue.add(new Block_Info(current_block.row + 1, current_block.col, current_block.dist + 1));
                visited[current_block.row + 1][current_block.col] = true;
            }

            // moving left
            if (isValid(current_block.row, current_block.col - 1, size, visited)) {
                queue.add(new Block_Info(current_block.row, current_block.col - 1, current_block.dist + 1));
                visited[current_block.row][current_block.col - 1] = true;
            }

            // moving right
            if (isValid(current_block.row, current_block.col + 1, size, visited)) {
                queue.add(new Block_Info(current_block.row, current_block.col + 1, current_block.dist + 1));
                visited[current_block.row][current_block.col + 1] = true;
            }
        }
        return -1;
    }

    // checking where it's valid or not
    private boolean isValid(int x, int y, int size, boolean[][] visited) {
        if (x >= 0 && y >= 0 && x < size
                && y < size && visited[x][y] == false) {
            return true;
        }
        return false;
    }


}
