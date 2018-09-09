public class Percolation {
    private int[] grid;
    private int countOpen = 0;
    private int n;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        grid = new int[(n + 2) * (n + 2)];
        for (int k = 1; k < n + 1; ++k) {
            grid[k] = 2;
        }
        int j = (n + 2)*(n + 1) + 1;
        for (int k = j; k < j + n; ++k) {
            grid[k] = 1;
        }
    }

    public void open(int row, int col) {       // open site (row, col) if it is not open already
        int i = row * (n + 2) + col;
        grid[i] = 1;
        ++countOpen;
    }

    public void full(int id) {
        grid[id] = 2;
    }
    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        int i = row * (n + 2) + col;
        return grid[i] == 1;
    }

    public boolean isFull(int id) {  // is site (row, col) full?
        return grid[id] == 2;
    }

    public int numberOfOpenSites() {           // number of open sites
        return countOpen;
    }
    public boolean percolates() {               // does the system percolate?
        return isFull((n + 2) * (n + 2) - 2);
   }
}