import java.lang.reflect.Array;
import java.util.*;

public class Main {
    private static int choice;
    private static int n;
    private static int h = 0;
    private static int stepsClimbed = 0;
    private static int randomRestarts = 0;
    private static int stepsClimbedAfterLastRestart = 0;
    private static int stepsClimbedTotal = 0;
    private static int stuck = 0;
    private static HashSet<String> past = new HashSet<>();

    public static void main(String[] args) {
        {
            int totalStepsS = 0;
            int totalStepsF = 0;
            int test = 300;
            int success = 0;
            int presentH;
            Scanner s = new Scanner(System.in);
            System.out.println("Select:\n1.regular HCS\n2.HCS with sideways move\n3.Random-restart\n4.Random-restart and sideways");
            choice = s.nextInt();
            System.out.println("Enter the number of Queens :");
            n = s.nextInt();
            for (int i = 0; i < test; i++) {
//                System.out.println("Solution to " + n + " queens using hill climbing:");
                //Creating the initial Board
                Tile[] present = generate();
                System.out.println("initial board state:");
                printBoard(present);
                presentH = findH(present);
                // test if the present board is the solution board
                while (presentH != 0) {
                    //  Get the next board
                    // printState(presentBoard);
                    present = nextBoard(present);
                    if (!past.contains(Arrays.toString(present))) {
                        stuck = 0;
                    }
                    past.add(Arrays.toString(present));
                    printBoard(present);
                    if (choice == 1 && presentH == h) {
                        break;
                    }
                    if (choice == 2 || choice == 4) {
                        if (presentH == h) {
                            stuck++;
                            System.out.println(stuck);
                        }

                        if (stuck > 100) {
                            break;
                        }
                    }
                    presentH = h;
                }
                if (h == 0) {
                    System.out.println("Success!");
                    success++;
                    totalStepsS = stepsClimbed + totalStepsS;
                } else {
                    System.out.println("Failed");
                    totalStepsF = stepsClimbed + totalStepsF;
                }
                //Printing the solution
                past.clear();
                stuck = 0;
                stepsClimbed = 0;
                System.out.println("final board state:");
                printBoard(present);
                System.out.println("\nTotal number of Steps Climbed: " + stepsClimbedTotal);
                System.out.println("Steps Climbed after last restart: " + stepsClimbedAfterLastRestart);
                stepsClimbedAfterLastRestart=0;
            }
            if (choice == 3 || choice == 4) {
                System.out.println("Number of random restarts: " + randomRestarts/test);
            }
            System.out.println("Success rate " + (double) success / test);
            System.out.println("\nAvg steps on success: " + totalStepsS/success);
            //if(test-success==0)

            System.out.println("\nAvg steps on failure " + totalStepsF/(test-success));
        }
    }

    //This function is used to generate the intial board state
    public static Tile[] generate() {
        Tile[] init = new Tile[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            init[i] = new Tile(r.nextInt(n), i);
        }
        return init;
    }

    //This function is used to print the Board
    private static void printBoard(Tile[] board) {
        int[][] temp = new int[n][n];
        for (int i = 0; i < n; i++) {
            temp[board[i].getRow()][board[i].getColumn()] = 1;
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(temp[i][j] + " ");
            }
            System.out.println();
        }

    }

    //This function compares all of the tiles to find the heuristic value
    public static int findH(Tile[] board) {
        int h = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = i + 1; j < board.length; j++) {
                if (board[i].ifConflict(board[j])) {
                    h++;
                }
            }
        }
        return h;
    }

    //This function is to find the next board state
    public static Tile[] nextBoard(Tile[] current) {
        Tile[] next = new Tile[n];
        Tile[] tmp = new Tile[n];
        int currentH = findH(current);
        int bestH = currentH;
        int tempH;
        boolean t = true;
        for (int i = 0; i < n; i++) {
            //save the current board as the best board and temp board
            next[i] = new Tile(current[i].getRow(), current[i].getColumn());
            tmp[i] = next[i];
        }
        //check each column
        for (int i = 0; i < n; i++) {
            if (i > 0)
                tmp[i - 1] = new Tile(current[i - 1].getRow(), current[i - 1].getColumn());
            tmp[i] = new Tile(0, tmp[i].getColumn());
            //check each row
            for (int j = 0; j < n; j++) {
                //get the heuristic of temp
                tempH = findH(tmp);
                //check if temp is better than best
                if (tempH < bestH) {
                    bestH = tempH;
                    //make the temp the best
                    for (int k = 0; k < n; k++) {
                        next[k] = new Tile(tmp[k].getRow(), tmp[k].getColumn());
                    }
                    t = false;
                }

                //sideways
                if (choice == 2 || choice == 4 && t) {
                    if (tempH <= bestH) {
                        if (!past.contains(Arrays.toString(tmp))) {
                            bestH = tempH;
                            //make the temp the best
                            for (int k = 0; k < n; k++) {
                                next[k] = new Tile(tmp[k].getRow(), tmp[k].getColumn());
                            }
                            t = false;
                        }

                    }
                }
                //move queen
                if (tmp[i].getRow() != n - 1) {
                    tmp[i].move();
                }
            }
        }
        //Random restart
        if (bestH == currentH && choice == 3) {
            randomRestarts++;
            stepsClimbedAfterLastRestart = 0;
            next = generate();
            h = findH(next);

        } else if (bestH == currentH && choice == 4 && stuck > 99) {
            randomRestarts++;
            stepsClimbedAfterLastRestart = 0;
            next = generate();
            h = findH(next);


        } else {
            h = bestH;
        }
        stepsClimbed++;
        stepsClimbedTotal++;
        stepsClimbedAfterLastRestart++;
        return next;


    }
}
