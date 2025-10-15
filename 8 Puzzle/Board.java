import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int n;

    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], n);
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hammingDistance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int expected = row * n + col + 1;
                if (tiles[row][col] != 0 && tiles[row][col] != expected) {
                    hammingDistance++;
                }
            }
        }
        return hammingDistance;
    }

    // Returns the sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = tiles[row][col];
                if (value != 0) {
                    int goalRow = (value - 1) / n;
                    int goalCol = (value - 1) % n;
                    manhattanDistance += Math.abs(row - goalRow) + Math.abs(col - goalCol);
                }
            }
        }
        return manhattanDistance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board board = (Board) y;
        return n == board.n && Arrays.deepEquals(tiles, board.tiles);
    }

    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        int blankRow = -1, blankCol = -1;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    break;
                }
            }
        }

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directions) {
            int newRow = blankRow + direction[0];
            int newCol = blankCol + direction[1];
            if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
                int[][] newTiles = copyTiles();
                newTiles[blankRow][blankCol] = newTiles[newRow][newCol];
                newTiles[newRow][newCol] = 0;
                neighbors.add(new Board(newTiles));
            }
        }
        return neighbors;
    }

    public Board twin() {
        int[][] newTiles = copyTiles();
        if (newTiles[0][0] != 0 && newTiles[0][1] != 0) {
            swap(newTiles, 0, 0, 0, 1);
        }
        else {
            swap(newTiles, 1, 0, 1, 1);
        }
        return new Board(newTiles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int[] row : tiles) {
            for (int tile : row) {
                sb.append(String.format("%2d ", tile));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int[][] copyTiles() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            copy[i] = Arrays.copyOf(tiles[i], n);
        }
        return copy;
    }

    private void swap(int[][] tiles, int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    public static void main(String[] args) {
    }
}

