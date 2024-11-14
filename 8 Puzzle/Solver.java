import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private final boolean solvable;
    private final List<Board> solutionPath;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        solutionPath = new ArrayList<>();

        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();

            if (node.board.isGoal()) {
                solvable = true;
                for (SearchNode n = node; n != null; n = n.previous) {
                    solutionPath.add(0, n.board);
                }
                return;
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.previous == null || !neighbor.equals(node.previous.board)) {
                    pq.insert(new SearchNode(neighbor, node.moves + 1, node));
                }
            }
        }

        solvable = false;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return solvable ? solutionPath.size() - 1 : -1;
    }

    public Iterable<Board> solution() {
        return solvable ? solutionPath : null;
    }

    public static void main(String[] args) {
    }
}
