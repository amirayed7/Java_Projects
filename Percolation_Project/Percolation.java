// The percolation API.
public interface Percolation {
    // Opens site (i, j) if it is not already open.
    public void open(int i, int j);

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j);

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j);

    // Returns the number of open sites.
    public int numberOfOpenSites();

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates();
}