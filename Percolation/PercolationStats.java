/* *****************************************************************************
 *  Name:              SinceOctober
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;
    private int trials;
    private double mean, stddev, confLo, confHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trials must be greater than 0");

        this.trials = trials;
        this.thresholds = new double[trials];

        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            int openCount = 0;
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                    openCount++;
                }
            }
            thresholds[t] = (double) openCount / (n * n);
        }

        this.mean = StdStats.mean(thresholds);
        this.stddev = StdStats.stddev(thresholds);
        double margin = 1.96 * stddev / Math.sqrt(trials);
        this.confLo = mean - margin;
        this.confHi = mean + margin;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confLo;
    }

    public double confidenceHi() {
        return confHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
