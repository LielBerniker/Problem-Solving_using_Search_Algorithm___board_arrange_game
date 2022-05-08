import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Ex1 {
    /* this class is the main class to get the input , process it , and use the algorithms to calculate it */
    static Game_Board board; // board information
    static Search_Algorithms algorithm; // the algorithms
    static StringBuilder full_path; // contain the full path
    static Game_Node_State last_node; // the last node

    public static void main(String[] args) throws IOException {
        /**
         * main function
         * @return
         */
        run_algorithm();
    }

    public static void get_board_information() {
        /**
         * this function scan the input txt file and get information from it
         * with this information create the Board_Game
         * @return
         */
        Scanner scanner = null;
        int line_count = 0, cur_size;
        String algorithm_name = "", open = "", board_size = "", goal_state = "", current_state = "";
        // read the a txt file
        try {
            File myObj = new File("input.txt");
            scanner = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // while there is more lines to the txt file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch (line_count) {
                case 0:
                    algorithm_name = line;
                    break;
                case 1:
                    open = line;
                    break;
                case 2:
                    board_size = line;
                    break;
                    // get the start state of the board
                case 3:
                    if (board_size.equals("small")) {
                        cur_size = 3;
                    } else {
                        cur_size = 5;
                    }
                    current_state += line + ',';
                    for (int i = 1; i < cur_size; i++) {
                        line = scanner.nextLine();
                        current_state += line + ',';
                    }
                    break;
                // get the goal state of the board
                case 4:
                    line = scanner.nextLine();
                    if (board_size.equals("small")) {
                        cur_size = 3;
                    } else {
                        cur_size = 5;
                    }
                    goal_state += line + ',';
                    for (int i = 1; i < cur_size; i++) {
                        line = scanner.nextLine();
                        goal_state += line + ',';
                    }
                    break;
                default:
                    break;
            }
            line_count++;
        }
        // create the board with the information
        board = new Game_Board(algorithm_name, open, board_size, current_state, goal_state);
    }

    private static void run_algorithm() throws IOException {
        /**
         * run first the read input function
         * then create an algorithm and run the wright algorithm
         * @return
         */
        get_board_information();
        algorithm = new Search_Algorithms(board);
        last_node = algorithm.runAlgorithm();
        findPath();
        createOutput();
    }

    private static void findPath() {
        /**
         * find the full path of the goal node by the previous node
         * crate a string of the full path
         * @return
         */
        Game_Node_State current_node;
        full_path = new StringBuilder();
        // check if there is a goal node
        if (!algorithm.isFind_goal()) {
            full_path.append("no path");
        } else {
            current_node = last_node;
            // go all the way to the first node and get the full path
            while (current_node.getPrev_node() != null) {
                full_path.insert(0, current_node.getPath());
                current_node = current_node.getPrev_node();
            }
            full_path.deleteCharAt(full_path.length() - 1);
            full_path.deleteCharAt(full_path.length() - 1);
        }
    }

    private static void createOutput() throws IOException {
        /**
         * this function creates a txt file by the information from the algorithem , elapsed time
         * number of nodes created ,the full path , and the cost
         * @return
         */
        boolean find_goal = algorithm.isFind_goal();
        String cost = "";
        // check if the algorithm reach the goal node
        if (find_goal == false) {
            cost = "inf";
        } else {
            cost = String.valueOf(last_node.getCost());
        }
        String all_info = full_path + "\nNum: " + algorithm.getNode_counter() + "\nCost: " + cost + "\n" + (algorithm.getElapsedTime() / 1000F) + " seconds\n";
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
