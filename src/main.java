import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class main {
    public static void main(String[] args) throws IOException {
      Game_Board board_information = get_board_information();
      run_algorithm(board_information);
    }
    public static Game_Board get_board_information()
    {
        Scanner scanner = null;
        int line_count = 0,cur_size;
        String algorithm_name="",open="",board_size="",goal_state="",current_state="";
        try {
            File myObj = new File("input.txt");
            scanner = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            switch(line_count) {
                case 0:
                    algorithm_name = line;
                    break;
                case 1:
                    open = line;
                    break;
                case 2:
                    board_size = line;
                    break;
                case 3:
                    if(board_size.equals("small")){
                        cur_size = 3;}
                    else{
                        cur_size = 5;
                    }
                    current_state += line+',';
                    for (int i = 1; i <cur_size; i++) {
                        line = scanner.nextLine();
                        current_state += line+',';
                    }
                    break;
                case 4:
                    line = scanner.nextLine();
                    if(board_size.equals("small")){
                        cur_size = 3;}
                    else{
                        cur_size = 5;
                    }
                        goal_state += line+',';
                        for (int i = 1; i <cur_size; i++) {
                            line = scanner.nextLine();
                           goal_state  += line+',';
                        }
                    break;
                default:
                    break;
            }
            line_count++;
        }
     Game_Board board_information = new Game_Board(algorithm_name,open,board_size,current_state,goal_state);
        return board_information;
    }
    public static void run_algorithm(Game_Board board ) throws IOException {
        Search_Algorithms search_algo = new Search_Algorithms(board);
        switch(board.getAlgo_Name()) {
            case "BFS":
                search_algo.BFS();
                break;
            case "DFID":
                search_algo.DFID();
                break;
            case "A*":
                break;
            case "IDA*":
                break;
            case "DFBnB":
                break;
            default:
                break;
        }
    }
}
