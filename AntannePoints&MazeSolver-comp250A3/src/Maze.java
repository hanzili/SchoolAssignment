import java.io.*;

public class Maze {
    private final int dimension = 15;
    public int counter = 0;

    // Checks if we can move to (x,y)
    boolean canMove(char maze[][], boolean found, int x, int y) {
        if(found) {
            return (x >= 0 && x < dimension && y >= 0 && y < dimension && (maze[x][y] == '.' || maze[x][y] == '0'));
        } else {
            return (x >= 0 && x < dimension && y >= 0 && y < dimension && (maze[x][y] == '.' || maze[x][y] == 'k'));
        }
    }


    boolean solveMaze(char maze[][]) {

        if (!solveMazeUtil(maze, false, 0, 1)) {
            System.out.print("Solution doesn't exist\n");
            return false;
        }

        return true;
    }

    // mark the track on the maze
    char[][] markPoint(char maze[][], boolean found, int x, int y) {
        char[][] markedMaze = copyMaze(maze);
        boolean atK = (markedMaze[x][y] == 'k');
        if (atK) {
            return markedMaze;
        }
        if(found) {
            markedMaze[x][y] = '1';
        } else {
            markedMaze[x][y] = '0';
        }
        return markedMaze;
    }


    // update found
    boolean checkFound(char[][] maze,boolean found, int x, int y) {
        if (!found) {
            return maze[x][y]=='k'||maze[x][y]=='1';
        }
        return true;
    }

    // make a new maze and copy the elements from the parameter and return this maze
    static char[][] copyMaze(char[][] maze){
        char[][] copy = new char[15][15];
        for(int row = 0; row <= 14; row++) {
            for (int col = 0; col <= 14; col++) {
                copy[row][col] = maze[row][col];
            }
        }
        return copy;
    }

    // alter the whole maze
    void copyWholeMaze(char[][] old, char[][] copy) {
        for(int row = 0; row <= 14; row++) {
            for (int col = 0; col <= 14; col++) {
                copy[row][col] = old[row][col];
            }
        }
    }

    // A recursive function to solve Maze problem
    boolean solveMazeUtil(char maze[][], boolean found, int x, int y) {
        // please do not delete/modify the next line!
        counter++;

        // Insert your solution here and modify the return statement.
        boolean atEnd = (x==14 && y==13);
        char[][] markedMaze = markPoint(maze,found,x,y);
        found = checkFound(maze, found, x, y);
        boolean moveRight = y+1<=14 && canMove(maze, found, x,y+1);
        boolean moveLeft = y-1>=0 && canMove(maze, found, x,y-1);
        boolean moveUp = x-1>=0 && canMove(maze,found,x-1,y);
        boolean moveDown = x+1<=14 && canMove(maze, found, x+1,y);

        // if we can move in the limited vision, keep moving
        // until reaching the end with key or get blocked
        // if get blocked, mark that side as false
        if (moveRight) {
            if (! solveMazeUtil(markedMaze, found, x, y + 1)) {
                moveRight = false;
            }
        }
        if (moveLeft) {
            if (! solveMazeUtil(markedMaze, found, x, y-1)){
                moveLeft = false;
            }
        }
        if (moveUp) {
            if (! solveMazeUtil(markedMaze, found, x-1, y)) {
                moveUp = false;
            }
        }
        if (moveDown) {
            if(! solveMazeUtil(markedMaze, found, x+1, y)){
                moveDown = false;
            }
        }

        // base case
        // return true if we can get to the end with a key from the start
        if (found && atEnd) {
            copyWholeMaze(markedMaze,maze);
            return true;
        }
        // if there is no way to go
        if (!moveRight && !moveLeft && !moveUp && !moveDown && !atEnd) {
            return false;
        }
        // if you reach the end without the key, return false
        if (atEnd) {
            return false;
        }
        // mark the true maze, when you find a valid path
        copyWholeMaze(markedMaze,maze);
        return true;
    }


    //Loads maze from text file
    char[][] loadMaze(String directory) throws IOException{
        char[][] maze = new char[dimension][dimension];

        try (BufferedReader br = new BufferedReader(new FileReader(directory))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                for (int col = 0; col < line.length(); col++){
                    maze[row][col] = line.charAt(col);
                }
                row++;
            }
        }
        return maze;

    }

    //Prints maze
    private static void printMaze(char[][] maze) {
        for (int i = 0; i < maze[0].length; i++) {
            for (int j = 0; j < maze[0].length; j++)
                System.out.print(" " + maze[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }


    public static void main(String args[]) {
        Maze m = new Maze();
        for (int i = 0; i < 4; i++) {
            try {
                char[][] myMaze = m.loadMaze("/Users/hanzili/Desktop/A3/src/mazes/m"+i+".txt");
                System.out.println("\nMaze "+i);
                Maze.printMaze(myMaze);
                if(m.solveMaze(myMaze)){
                    Maze.printMaze(myMaze);
                }
            } catch (Exception e){
                System.out.print("File was not found.");
            }

        }
    }
}