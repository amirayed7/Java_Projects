import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    // Create instance variables
    int[][] tiles;
    int n;
    int manhattan;
    int hamming;
    int blankPos;


    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = tiles;

        // Computing the Hamming and Manhatten distance
        this.hamming = hamming();
        this.manhattan = manhattan();

        //location of blank tile;
        for(int i = 0; i < n * n; i ++){
            if(tiles[i / n][i % n] == 0){
                blankPos = i + 1;
            }
        }
    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        //intialize distance to 0 and value of 1st tile to 1
        hamming = 0;
        int realValue = 1;

        //Iterate over each tile
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                // if its not a blank tile and not in its right place add 1 to hamming distance
                if(tiles[i][j] != realValue && tiles[i][j] != 0){
                    hamming ++;
                }
                //move to next value thats expected
                realValue ++;
            }
        }
        //return hamming distance
        return hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        //initialize distance to 0
        manhattan = 0;

        //Iterate over each tile
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                int Value = tiles[i][j];// The current tiles value
                if (Value != 0){
                    //calculate what the row and column should be then calculate the distance of
                    //that tile and add it to the total manhattan distance
                    int realRow = (Value - 1) / n;
                    int realCol = (Value - 1) % n;
                    manhattan += Math.abs(i - realRow) + Math.abs(j - realCol);
                }
            }
        }
        return manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        // Iterate over each tile
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                int Value = tiles[i][j];// current tiles value
                if (Value != 0){
                    // calculate the row and columns value that it should be
                    int realRow = (Value - 1) / n;
                    int realCol = (Value - 1) % n;
                    //if tile is not in correct position return false
                    if (i != realRow || j!= realCol){
                        return false;
                    }
                }
            }
        }
        //else return true
        return true;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        //create array to store the tiles values
        int[] values = new int[n * n - 1];
        int index = 0;

        //iterate over each tile
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                // if its not the blank tile then store it in the array
                if (tiles[i][j] != 0){
                    values[index++] = tiles[i][j];
                }
            }
        }
        //check if # of inversions is even which means its solvable
        return (Inversions.count(values) % 2) == 0;
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        //Queue to store the boards around
        LinkedQueue<Board> q = new LinkedQueue<>();

        // row and column index for the blank tile
        int blankRow = 0;
        int blankCol = 0;

        //find blank tile(i feel like i shouldve used blankPos here but it was not working)
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if(this.tiles[i][j] == 0){
                    blankRow = i;
                    blankCol = j;
                }
            }
        }
        //create the boards around it by moving the tile all four directions

        //down
        if(blankRow < (n - 1)){
            int[][] newTiles = cloneTiles();
            int temp = newTiles[blankRow][blankCol];
            newTiles[blankRow][blankCol] = newTiles[blankRow + 1][blankCol];
            newTiles[blankRow + 1][blankCol] = temp;
            q.enqueue(new Board(newTiles));
        }

        //up
        if(blankRow > 0){
            int[][] newTiles = cloneTiles();
            int temp = newTiles[blankRow][blankCol];
            newTiles[blankRow][blankCol] = newTiles[blankRow - 1][blankCol];
            newTiles[blankRow - 1][blankCol] = temp;
            q.enqueue(new Board(newTiles));
        }

        //left
        if(blankCol > 0){
            int[][] newTiles = cloneTiles();
            int temp = newTiles[blankRow][blankCol];
            newTiles[blankRow][blankCol] = newTiles[blankRow][blankCol - 1];
            newTiles[blankRow][blankCol - 1] = temp;
            q.enqueue(new Board(newTiles));
        }

        //right
        if(blankCol < (n - 1)){
            int[][] newTiles = cloneTiles();
            int temp = newTiles[blankRow][blankCol];
            newTiles[blankRow][blankCol] = newTiles[blankRow][blankCol + 1];
            newTiles[blankRow][blankCol + 1] = temp;
            q.enqueue(new Board(newTiles));
        }

        //return the queue that has all the boards around
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Board otherBoard = (Board) other;
        if(this.n != otherBoard.n){
            return false;// not equal
        }

        //check if tile on both boards are the same
        for (int i = 0; i < this.n; i++){
            for(int j = 0; j < this.n; j++){
                if(this.tiles[i][j] != otherBoard.tiles[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
