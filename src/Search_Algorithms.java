import java.awt.*;
import java.util.*;
import java.util.List;


public class Search_Algorithms {
    /* this class contains a various search algorithms*/
    private Game_Board board; // board information
    private int node_counter; // number of nodes that been created
    private long elapsed_time; // the time that pass from the beginning of thealgorithmm
    private boolean find_goal; // if the algorithm reach the goal
    private static final int MAX_VAL = Integer.MAX_VALUE;

    enum Result_DFS {
        /* contain a enum options from the result of the limited DFS*/
        GOAL,
        FAIL,
        CUTOFF
    }

    public Search_Algorithms(Game_Board board) {
        /**
         * get a Game Board and initiate the information base on it
         * @param board
         */
        this.find_goal = false;
        this.elapsed_time = 0;
        this.node_counter = 0;
        this.board = board;
    }

    public Game_Node_State runAlgorithm() {
        /**
         *  execute the algorithm base on the algorithm name
         * @return
         */
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
        /**
         *  execute the BFS algorithm
         * @return goal_node
         */
        // reset the time
        setTime(0);

        // initiate the first state node and the goal node
        Game_Node_State start_node, goal_node, current_node_state;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();

        // create a Queue and hashtable of nodes that has been visited
        Queue<Game_Node_State> all_states = new ArrayDeque<>();
        Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();

        // create a hashtable of nodes that has been explored
        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        // add the first node ass visited node
        all_states.add(start_node);
        frontier_nodes.put(start_node.getUnique_key(), start_node);

        // while their is still visited nodes to explore
        while (!all_states.isEmpty()) {

            // check if is needed to print open list
            openList(all_states);

            // get the front node in the Queue and add it to the explored nodes
            current_node_state = all_states.poll();
            explored_nodes.put(current_node_state.getUnique_key(), current_node_state);

            // find all allowed operators for this current node
            List<Game_Node_State> all_possible_children = this.findAll_children(current_node_state);

            // add to the number of nodes that have been created
            this.addNode_counter(all_possible_children.size());

            // go over all the allowed operators
            for (Game_Node_State current_child : all_possible_children) {

                // check if the current child is not have been visited or explored
                if ((!explored_nodes.containsKey(current_child.getUnique_key()))
                        && (!frontier_nodes.containsKey(current_child.getUnique_key()))) {

                    // if the current child is equal to the goal node stop the time an return the current node
                    if (current_child.equals(goal_node)) {
                        setTime(this.getElapsedTime());
                        this.setFind_goal(true);
                        return current_child;
                    }

                    // add the current child to the visited nodes Queue and hashtable
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.getUnique_key(), current_child);
                }
            }
        }

        // stop the time and return answer
        setTime(this.getElapsedTime());
        return this.board.getFirst_State();
    }

    private Game_Node_State DFID() {
        /**
         *  execute the DFID algorithm
         * @return goal_node
         */
        // initiate the first state node and the goal node
        Game_Node_State start_node, goal_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();

        // reset the time
        setTime(0);

        // run the for loop for the limit ' start from 1 to infinite
        for (int i = 1; i < MAX_VAL; i++) {

            // create the hash table
            Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();

            // run the limited DFS with the limit in the for loop (i)
            Result_DFS limited_dfs_results = LimitedDFS(start_node, goal_node, i, frontier_nodes);

            // if the result is not cut off and equal to the goal , stop the time and return goal node
            if (limited_dfs_results != Result_DFS.CUTOFF) {
                if (limited_dfs_results == Result_DFS.GOAL) {
                    setTime(this.getElapsedTime());
                    this.setFind_goal(true);
                    return this.board.getGoal_State();
                }
            }
        }

        // stop the time and return answer
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    private Result_DFS LimitedDFS(Game_Node_State current_node, Game_Node_State goal_node, int limit, Hashtable<String, Game_Node_State> frontier_nodes) {
        /**
         *  execute the Limited DFS algorithm
         * @return result
         */
        // check if is needed to print open list
        openList(frontier_nodes);

        // if current node is the goal node , stop time and set the goal node, then return goal
        if (current_node.equals(goal_node)) {
            this.board.setGoal_State(current_node);
            return Result_DFS.GOAL;

            // if the limit is 0 return cut off
        } else if (limit == 0) {
            return Result_DFS.CUTOFF;
        } else {
            // insert the current node to the hashtable
            frontier_nodes.put(current_node.getUnique_key(), current_node);
            boolean is_cutoff = false;

            // find all allowed operators for this current node
            List<Game_Node_State> all_possible_children = this.findAll_children(current_node);

            // add to the number of nodes that have been created
            this.addNode_counter(all_possible_children.size());

            // go over all the allowed operators
            for (Game_Node_State current_child : all_possible_children) {

                // if the current child is already in the hash table continue with the next ,
                // but if the older child got a bigger depth, remove the older child
                if (frontier_nodes.containsKey(current_child.getUnique_key())) {
                    if (frontier_nodes.get(current_child.getUnique_key()).getDepth() > current_child.getDepth()) {
                        frontier_nodes.remove(current_child.getUnique_key());
                    } else {
                        continue;
                    }
                }

                // call the limited dfs with a limit that is lower by 1
                Result_DFS result = LimitedDFS(current_child, goal_node, limit - 1, frontier_nodes);

                // if the result is cut off , then is cutoff variable is true , else return the result
                if (result == Result_DFS.CUTOFF)
                    is_cutoff = true;
                else if (result != Result_DFS.FAIL)
                    return result;
            }

            //remove current node from the hashtable
            frontier_nodes.remove(current_node.getUnique_key());

            //if it has been cut off return cut off else return it failed
            if (is_cutoff)
                return Result_DFS.CUTOFF;
            else
                return Result_DFS.FAIL;
        }
    }

    private Game_Node_State A_STAR() {
        /**
         *  execute the A* algorithm
         * @return goal_node
         */
        // reset the time
        setTime(0);

        // initiate the first state node and the goal node
        Game_Node_State start_node, goal_node, current_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        // set number of iteration
        int iteration = 0;

        // create a Priority Queue and hashtable of nodes that has been visited
        // the Priority is defined by the copmareTo function of the Game NodeState
        PriorityQueue<Game_Node_State> all_states = new PriorityQueue<>();
        Hashtable<String, Game_Node_State> frontier_nodes = new Hashtable<>();

        // create a hashtable of nodes that has been explored
        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        // set the Heuristic value for the node by the Heuristic function
        start_node.setHeuristic(calculateHeuristic_value(start_node));

        // add the first node ass visited node
        all_states.add(start_node);
        frontier_nodes.put(start_node.getUnique_key(), start_node);

        // while their is still visited nodes to explore
        while (!all_states.isEmpty()) {

            // check if is needed to print open list
            openList(all_states);

            // poll the first node from the priority queue and delete it from the hash table
            current_node = all_states.poll();
            frontier_nodes.remove(current_node.getUnique_key());

            // if the current node is equal to the goal node stop the time an return the current node
            if (current_node.equals(goal_node)) {

                setTime(this.getElapsedTime());
                this.setFind_goal(true);
                return current_node;
            }

            // add this node to the explored nodes
            explored_nodes.put(current_node.getUnique_key(), current_node);

            // find all allowed operators for this current node
            List<Game_Node_State> all_possible_children = this.findAll_children(current_node);

            // add to the number of nodes that have been created
            this.addNode_counter(all_possible_children.size());

            // go over all the allowed operators
            for (Game_Node_State current_child : all_possible_children) {
                // set the Heuristic value for the node by the Heuristic function, and set the iteration
                current_child.setHeuristic(calculateHeuristic_value(current_child));
                current_child.setIteration(iteration);

                // check if the current child is not have been visited or explored
                if ((!explored_nodes.containsKey(current_child.getUnique_key())) &&
                        (!frontier_nodes.containsKey(current_child.getUnique_key()))) {
                    // add the current node to the priority queue and hash table of visited nodes
                    all_states.add(current_child);
                    frontier_nodes.put(current_child.getUnique_key(), current_child);

                    // if the current node has been visited before ,if it does
                    // check which one is bigger by the compareTo function of the Game Node State
                } else if (frontier_nodes.containsKey(current_child.getUnique_key())) {
                    Game_Node_State old_node = frontier_nodes.get(current_child.getUnique_key());

                    //check which node is bigger, if the old node is bigger replace it by the new one
                    if (old_node.currentIs_bigger(current_child)) {
                        all_states.remove(old_node);
                        all_states.add(current_child);
                        frontier_nodes.put(current_node.getUnique_key(), current_child);
                    }
                }
            }
            iteration++;
        }

        // stop the time and return answer
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    public Game_Node_State IDA_STAR() {
        /**
         *  execute the IDA* algorithm
         * @return goal_node
         */
        // reset the time
        setTime(0);

        // initiate the first state node and the goal node
        Game_Node_State start_node, goal_node, current_node;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        int threshold, minF;

        // create a stack of nodes that has been visited
        Stack<Game_Node_State> all_states = new Stack<>();

        // create a hashtable of nodes that has been explored
        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        // set the Heuristic value for the node by the Heuristic function, and set the threshold by the start node
        start_node.setHeuristic(calculateHeuristic_value(start_node));
        threshold = start_node.getHeuristic();

        // while the threshold is different from infinity
        while (threshold != MAX_VAL) {
            openList(all_states);

            // set the min f(n) function to infinity
            minF = MAX_VAL;

            // push the current node to the stack , and add it to the explored hash table
            all_states.push(start_node);
            explored_nodes.put(start_node.getUnique_key(), start_node);

            // while their is still visited nodes to explore
            while (!all_states.isEmpty()) {

                // check if is needed to print open list
                openList(all_states);

                // pop the first node from the stack of visited nodes
                current_node = all_states.pop();

                // if the current node has been  marked as out , remove it from the explored nodes
                if (current_node.isOut()) {
                    explored_nodes.remove(current_node.getUnique_key());
                } else {

                    // mark the current node ass out and add it to the explored hash table
                    current_node.setOut(true);
                    all_states.push(current_node);

                    // find all allowed operators for this current node
                    List<Game_Node_State> all_possible_children = this.findAll_children(current_node);

                    // add to the number of nodes that have been created
                    this.addNode_counter(all_possible_children.size());

                    // go over all the allowed operators
                    for (Game_Node_State current_child : all_possible_children) {
                        // set the Heuristic value for the node by the Heuristic function, and set the iteration
                        current_child.setHeuristic(this.calculateHeuristic_value(current_child));

                        // if the current node f(n) value is bigger then the threshold ,
                        // then update the nin f and continue to the next allowed operator
                        if (current_child.f_n() > threshold) {
                            minF = Math.min(minF, current_child.f_n());
                            continue;
                        }
                        Game_Node_State old_node = explored_nodes.get(current_child.getUnique_key());
                        // if the current node has been explored and been marked as out before,
                        // then continue to the next allowed operator
                        if (old_node != null && old_node.isOut()) {
                            continue;
                        }
                        // if the current node has been explored and did not been marked as out before,
                        if (old_node != null && (!old_node.isOut())) {
                            old_node.setHeuristic(calculateHeuristic_value(old_node));

                            // if the current node f(n) is lower then the old node ,remove the old node from the stack
                            // and hash table, else continue to the next allowed operator
                            if (old_node.f_n() > current_child.f_n()) {
                                all_states.remove(old_node);
                                explored_nodes.remove(old_node.getUnique_key());
                            } else {
                                continue;
                            }
                        }

                        // if the current node is equal to the goal node stop the time an return the current node
                        if (current_child.equals(goal_node)) {
                            setTime(this.getElapsedTime());
                            this.setFind_goal(true);
                            return current_child;
                        }
                        // add the current node to the stack and to the hash table
                        all_states.push(current_child);
                        explored_nodes.put(current_child.getUnique_key(), current_child);
                    }
                }
            }
            start_node.setOut(false);
            // set the threshold to the minimum f(n) value
            threshold = minF;
        }
        // stop the time and return answer
        setTime(this.getElapsedTime());
        return this.board.getGoal_State();
    }

    private Game_Node_State DFBnB() {
        /**
         *  execute the DFBnB algorithm
         * @return goal_node
         */
        // reset the time
        setTime(0);

        // initiate the first state node and the goal node, and set the result
        Game_Node_State start_node, goal_node, current_node, result;
        int threshold;
        start_node = this.board.getFirst_State();
        goal_node = this.board.getGoal_State();
        result = goal_node;

        // create a stack of nodes that has been visited
        Stack<Game_Node_State> all_states = new Stack<>();

        // create a hashtable of nodes that has been explored
        Hashtable<String, Game_Node_State> explored_nodes = new Hashtable<>();

        // push the current node to the stack , and add it to the explored hash table
        all_states.push(start_node);
        explored_nodes.put(start_node.getUnique_key(), start_node);
        // set the threshold to infinity
        threshold = MAX_VAL;

        // while their is still visited nodes to explore
        while (!all_states.isEmpty()) {

            // check if is needed to print open list
            openList(all_states);

            // pop the first node from the stack of visited nodes
            current_node = all_states.pop();

            // if the current node marked as out remove ir from the hash table
            if (current_node.isOut()) {
                explored_nodes.remove(current_node.getUnique_key());
            } else {

                // marked current node as out and insert to the stack
                current_node.setOut(true);
                all_states.push(current_node);

                // // find all allowed operators for this current node, ordered by their f(n)
                List<Game_Node_State> all_possible_children = this.getOrdered_children_by_f(current_node);

                // add to the number of nodes that have been created
                this.addNode_counter(all_possible_children.size());

                // go over all the allowed operators with iterator
                Iterator<Game_Node_State> child_itr = all_possible_children.iterator();
                while (child_itr.hasNext()) {
                    Game_Node_State current_child = child_itr.next();

                    // if the current node f(n) value is greater then the threshold,
                    // remove the node and all the nodes after it in the ordered list
                    if (current_child.f_n() >= threshold) {
                        child_itr.remove();
                        while ((child_itr.hasNext()) && (all_possible_children.contains(child_itr.next()))) {
                            child_itr.remove();
                        }

                        // if the current node has been explored and marked as out remove the current node
                    } else if (explored_nodes.containsKey(current_child.getUnique_key())
                            && explored_nodes.get(current_child.getUnique_key()).isOut()) {
                        child_itr.remove();

                        // check if the current node has been explored and did not been marked as out
                    } else if (explored_nodes.containsKey(current_child.getUnique_key())
                            && (!explored_nodes.get(current_child.getUnique_key()).isOut())) {
                        Game_Node_State old_child = explored_nodes.get(current_child.getUnique_key());

                        // if the old node f(n) value is lower then the current node f(n), remove the current node
                        if (old_child.f_n() <= current_child.f_n()) {
                            child_itr.remove();
                            // remove the old mode from the stack and from the explored node hash table
                        } else {
                            all_states.remove(old_child);
                            explored_nodes.remove(old_child.getUnique_key());
                        }

                        // if the current node is equal to the goal node set the result to the current node
                        // ans set the threshold
                    } else if (current_child.equals(goal_node)) {
                        threshold = current_child.f_n();
                        this.setFind_goal(true);
                        result = current_child;

                        // remove the current node and all the nodes after it from the list
                        while ((child_itr.hasNext()) && (all_possible_children.contains(child_itr.next()))) {
                            child_itr.remove();
                        }
                    }
                }
                // insert the ordered children of the current node in a reverse order to the stack and the hash table
                for (int i = all_possible_children.size() - 1; i >= 0; i--) {
                    Game_Node_State current_node_rev = all_possible_children.get(i);
                    all_states.push(current_node_rev);
                    explored_nodes.put(current_node_rev.getUnique_key(), current_node_rev);
                }
            }
        }
        // stop the time and return answer
        setTime(this.getElapsedTime());
        return result;
    }

    private List<Game_Node_State> findAll_children(Game_Node_State current_node) {
        /**
         *  get a node and return all the possible operators from it ( all the moves )
         * @param current_node
         * @return all_operators
         */
        int size = this.board.getBoard_Size();
        List<Game_Node_State> possible_children = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // check if the current block is empty
                if (current_node.isBlock_empty(i, j)) {
                    // check if it can move a ball from above
                    if (current_node.canMoveBallFromAbove(i, j)) {
                        if (!current_node.equalTo_prev(Direction.UP, i, j)) {
                            Game_Node_State current_node_state_up = new Game_Node_State(current_node, Direction.UP, size, i, j);
                            possible_children.add(current_node_state_up);
                        }
                    }
                    // check if it can move a ball from below
                    if (current_node.canMoveBallFromBelow(i, j)) {
                        if (!current_node.equalTo_prev(Direction.DOWN, i, j)) {
                            Game_Node_State current_node_state_down = new Game_Node_State(current_node, Direction.DOWN, size, i, j);
                            possible_children.add(current_node_state_down);
                        }
                    }
                    // check if it can move a ball from the right
                    if (current_node.canMoveBallFromRight(i, j)) {
                        if (!current_node.equalTo_prev(Direction.RIGHT, i, j)) {
                            Game_Node_State current_node_state_right = new Game_Node_State(current_node, Direction.RIGHT, size, i, j);
                            possible_children.add(current_node_state_right);
                        }
                    }
                    // check if it can move a ball from the left
                    if (current_node.canMoveBallFromLeft(i, j)) {
                        if (!current_node.equalTo_prev(Direction.LEFT, i, j)) {
                            Game_Node_State current_node_state_left = new Game_Node_State(current_node, Direction.LEFT, size, i, j);
                            possible_children.add(current_node_state_left);
                        }
                    }

                }
            }
        }
        return possible_children;
    }

    public int getNode_counter() {
        /**
         *  return node conter
         * @return node_counter
         */
        return node_counter;
    }

    public void addNode_counter(int node_counter) {
        /**
         *  add to the node counter
         * @param node_counter
         * @return
         */
        this.node_counter += node_counter;
    }

    public long getElapsedTime() {
        /**
         *  return Elapsed Time
         * @return Elapsed Time
         */
        return elapsed_time;
    }

    public boolean isFind_goal() {
        /**
         *  return true or false if the algorithm reach the goal
         * @return is a goal
         */
        return find_goal;
    }

    public void setFind_goal(boolean find_goal) {
        /**
         *  set the find goal
         * @param find_goal
         * @return
         */
        this.find_goal = find_goal;
    }

    private void setElapsedTime(long elapsedTime) {
        /**
         *  set the elapse time
         * @param elapseTime
         * @return
         */
        this.elapsed_time = elapsedTime;
    }

    private void openList(Object obj) {
        /**
         *  print the details in the object the function gets
         * @param obj
         * @return
         */
        if (this.board.isPrint_Info()) {
            System.out.println(obj);
        }
    }

    private void setTime(long start) {
        /**
         *  set the time
         * @param start
         * @return
         */
        long current_time = System.currentTimeMillis() - start;
        this.setElapsedTime(current_time);
    }

    private List<Game_Node_State> getOrdered_children_by_f(Game_Node_State current_node) {
        /**
         * @param current_node
         * @return list of node, ordered by the comparator
         */
        List<Game_Node_State> all_possible_children = this.findAll_children(current_node);
        // set the heuristic value for all in the list
        for (Game_Node_State current_child : all_possible_children) {
            current_child.setHeuristic(calculateHeuristic_value(current_child));
        }
        // use the comprator to sort the list, sort by f(n) value
        Comparator<Game_Node_State> compare_f_n = getComparator_f_n();
        Collections.sort(all_possible_children, compare_f_n);
        return all_possible_children;
    }

    private Comparator<Game_Node_State> getComparator_f_n() {
        /**
         *  return the comparator
         * @return comparator
         */
        Comparator<Game_Node_State> compare_f_n
                = (node1, node2) -> Integer.compare(node1.f_n(), node2.f_n());
        return compare_f_n;
    }

    private int calculateHeuristic_value(Game_Node_State current_node) {
        /**
         *  add to the node counter
         * @param current_node
         * @return return the heuristic value
         */
        int total_cost = 0;
        int size = this.board.getBoard_Size();
        // calculate the heuristic by the board size
        if (size == 3) {
            total_cost = Manhattan_algorithm_3x3(current_node);
        } else {
            total_cost = Manhattan_algorithm_5x5(current_node);
        }
        return total_cost;
    }

    private int Manhattan_algorithm_3x3(Game_Node_State current_node) {
        /**
         *  go over all the balls color and get the minimum heuristic value for each color ,sum it and return
         * @param current_node
         * @return return the heuristic value
         */
        int total_cost = 0;
        char[] balls_color = new char[]{'R', 'B', 'G'};
        for (char current_color : balls_color) {
            Point[] balls_in_goal = this.board.getGoal_State().findBalls_location_by_color_3X3(current_color);
            Point[] balls_in_current = current_node.findBalls_location_by_color_3X3(current_color);
            // cost of the ball color
            int color_cost = current_node.getCost_by_color(current_color);
            total_cost += findMin_cost_3x3(balls_in_goal, balls_in_current, color_cost);
        }
        return total_cost;
    }

    private int Manhattan_algorithm_5x5(Game_Node_State current_node) {
        /**
         *  go over all the balls color and get the minimum heuristic value for each color ,sum it and return
         * @param current_node
         * @return return the heuristic value
         */
        int total_cost = 0;
        char[] balls_color = new char[]{'R', 'B', 'G', 'Y'};
        for (char current_color : balls_color) {
            Point[] balls_in_goal = this.board.getGoal_State().findBalls_location_by_color_5X5(current_color);
            Point[] balls_in_current = current_node.findBalls_location_by_color_5X5(current_color);
            int color_cost = current_node.getCost_by_color(current_color);
            total_cost += findMin_cost_5x5(balls_in_goal, balls_in_current, color_cost);
        }
        return total_cost;
    }

    private int findMin_cost_3x3(Point[] balls_in_goal, Point[] balls_in_current, int color_cost) {
        /**
         *  go over all The permutation of balls color and get the minimum heuristic value
         * @param  balls_in_goal
         * @param balls_in_current
         * @param color_cost
         * @return return the minimum heuristic value
         */
        int val;
        int val1 = ballsDistance2(balls_in_current[0], balls_in_current[1], balls_in_goal[0], balls_in_goal[1]);
        int val2 = ballsDistance2(balls_in_current[1], balls_in_current[0], balls_in_goal[0], balls_in_goal[1]);
        val = color_cost * (Math.min(val1, val2));
        return val;
    }

    private int findMin_cost_5x5(Point[] balls_in_goal, Point[] balls_in_current, int color_cost) {
        /**
         *  go over all The permutation of balls color and get the minimum heuristic value
         * @param  balls_in_goal
         * @param balls_in_current
         * @param color_cost
         * @return return the minimum heuristic value
         */
        int val;
        int k = 0;
        int min = MAX_VAL;
        // get the permutation for 4 balls
        int[] all_perm = all_permutations_blocks();
        for (int i = 0; i < 24; i++) {
            int[] cur_perm = new int[4];
            for (int j = 0; j < 4; j++) {
                cur_perm[j] = all_perm[j + k];
            }
            int temp_val = ballsDistance4(balls_in_goal, balls_in_current, cur_perm);
            // find min permutation value
            if (temp_val < min) {
                min = temp_val;
            }
            k = k + 4;
        }
        val = color_cost * min;

        return val;
    }

    private int ballsDistance2(Point current_p1, Point current_p2, Point goal_p1, Point goal_p2) {
        /**
         *  get the sum of min distances in the board by the locations
         * @param  current_p1
         * @param current_p2
         * @param goal_p1
         * @param goal_p2
         * @return return the minimum heuristic value
         */
        int current_cost = minDistance(current_p1, goal_p1) + minDistance(current_p2, goal_p2);
        return current_cost;
    }

    private int ballsDistance4(Point[] balls_in_goal, Point[] balls_in_cur, int[] perm) {
        /**
         *  get the sum of min distances in the board by the locations
         * @param balls_in_goal
         * @param balls_in_cur
         * @param perm
         * @return return the minimum heuristic value
         */
        int current_cost = 0;
        for (int i = 0; i < 4; i++) {
            int cur_index = perm[i];
            current_cost += minDistance(balls_in_cur[cur_index], balls_in_goal[i]);
        }
        return current_cost;
    }

    private int[] all_permutations_blocks() {
        /**
         * @return return all permutations for 4 balls
         */
        int[] permutations = {0, 1, 2, 3, 0, 1, 3, 2, 0, 2, 1, 3, 0, 2, 3, 1, 0, 3, 1, 2, 0, 3, 2, 1, 1, 0, 2, 3, 1, 0, 3, 2, 1, 2, 0, 3, 1, 2, 3, 0, 1, 3, 0, 2,
                1, 3, 2, 0, 2, 0, 1, 3, 2, 0, 3, 1, 2, 1, 0, 3, 2, 1, 3, 0, 2, 3, 0, 1, 2, 3, 1, 0, 3, 0, 1, 2, 3, 0, 2, 1, 3, 1, 0, 2, 3, 1, 2, 0, 3, 2, 0, 1, 3, 2, 1, 0};
        return permutations;
    }

    private int minDistance(Point start_p, Point end_p) {
        /**
         * @param start_p
         * @param end_p
         * @return return the distance in the baord by the rows al columns
         */
        
        return (Math.abs(start_p.x - end_p.x) + Math.abs(start_p.y - end_p.y));
    }

}
