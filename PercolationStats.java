import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
//import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double sum = 0;
    private double dev = 0;
    private int t;

    public PercolationStats(int n, int trials) {  // perform trials independent experiments on an n-by-n grid
        t = trials;
        for (int i = 0; i < trials; ++i) {
            Percolation perc = new Percolation(n);
            WeightedQuickUnionUF wqu = new WeightedQuickUnionUF((n + 2) * (n + 2));

            for (int k = 1; k < n; ++k) {
                wqu.union(k, k + 1);
            }
            int j = (n + 2)*(n + 1) + 1;
            for (int k = j; k < j + n - 1; ++k) {
                wqu.union(k, k + 1);
            }

            while (!perc.percolates()) {
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                if (perc.isOpen(x, y) || perc.isFull(x * (n + 2) + y)) {
                    continue;
                }
                else {
                    perc.open(x, y);

                }

                int id = x * (n + 2) + y;
                boolean flag = false;

                if (y - 1 > 0 && (perc.isOpen(x, y - 1) || perc.isFull(id - 1))) {
                    wqu.union(id, id - 1);
                    if (perc.isFull(id - 1)) {
                        flag = true;
                    }
                }
                if (y + 1 < n + 1 && (perc.isOpen(x, y + 1) || perc.isFull(id + 1))) {
                    wqu.union(id, id + 1);
                    if (perc.isFull(id + 1)) {
                        flag = true;
                    }
                }
                if (x - 1 >= 0 && (perc.isOpen(x - 1, y) || perc.isFull(id - n - 2))) {
                    wqu.union(id, id - n - 2);
                    if (perc.isFull(id - n - 2)) {
                        flag = true;
                    }
                }
                if (x + 1 <= n + 1 && (perc.isOpen(x + 1, y) || perc.isFull(id + n + 2))) {
                    wqu.union(id, id + n + 2);
                    if (perc.isFull(id + n + 2)) {
                        flag = true;
                    }
                }

                int root = wqu.find(id);

                if (flag) {
                    for (j = 1; j < (n + 2)*(n + 2) - 1; ++j) {
                        if (wqu.connected(j, root) && !perc.isFull(j)) {
                            perc.full(j);
                        }
                    }
                }
            }

            sum += (double)perc.numberOfOpenSites() / (n * n);
//            System.out.println(sum);
            dev += ((double)perc.numberOfOpenSites() / (n * n))
                 * ((double)perc.numberOfOpenSites() / (n * n));
        }
    }

    public double mean() {                         // sample mean of percolation threshold
        return sum / t;
    }
    public double stddev() {                       // sample standard deviation of percolation threshold
        return (dev - 2 * mean() * sum + mean() * mean() * t) / (t - 1);
    }
    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return mean() - 1.96 * Math.sqrt(stddev()) / Math.sqrt(t);
    }
    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return mean() + 1.96 * Math.sqrt(stddev()) / Math.sqrt(t);
    }

    public static void main(String[] args) {      // test client (described below)
        if (args.length != 2) throw new java.lang.IllegalArgumentException();

        PercolationStats stat = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + stat.mean());
        System.out.println("stddev                  = " + stat.stddev());
        System.out.println("95% confidence interval = " + '[' + stat.confidenceLo() + ','
                                                              + stat.confidenceHi() + ']');
    }
}
