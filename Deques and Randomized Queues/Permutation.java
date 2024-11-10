import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected one command-line argument: k");
        }

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        // Read strings from standard input and add to the queue
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }

        // Dequeue and print exactly k items
        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
