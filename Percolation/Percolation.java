/* *****************************************************************************
 *  Name:              SinceOctober
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private WeightedQuickUnionUF uf;
    private int n;
    private int openSitesCount;

    // Create an n-by-n grid
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than 0");

        this.n = n;
        this.grid = new boolean[n * n];
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.openSitesCount = 0;
    }

    private int index(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Index out of bounds");

        if (isOpen(row, col)) return;

        int idx = index(row, col);
        grid[idx] = true;
        openSitesCount++;

        boolean topRow = row == 1;
        boolean bottomRow = row == n;

        if (topRow) uf.union(idx, n * n);
        if (bottomRow) uf.union(idx, n * n + 1);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : directions) {
            int adjRow = row + d[0];
            int adjCol = col + d[1];
            if (adjRow > 0 && adjRow <= n && adjCol > 0 && adjCol <= n && isOpen(adjRow, adjCol)) {
                uf.union(idx, index(adjRow, adjCol));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Index out of bounds");
        return grid[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Index out of bounds");
        return uf.find(index(row, col)) == uf.find(n * n);
    }

    public int numberOfOpenSites() {
        return openSitesCount;
    }

    public boolean percolates() {
        return uf.find(n * n) == uf.find(n * n + 1);
    }
}
