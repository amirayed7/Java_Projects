// Accepts mode ("array" or "UF") and n (int) as command-line argument; constructs an n-by-n
// percolation system; and allows you to interactively open sites in the system by clicking on
// them and visually inspect if the system percolates or not. The mode determines which
// implementation (array-based or UF-based) of the Percolation API to use.

import stdlib.StdDraw;
import stdlib.StdOut;

public class InteractivePercolationVisualizer {
    // Delay in milliseconds (controls animation speed).
    private static final int DELAY = 20;

    // Entry point.
    public static void main(String[] args) {
        String mode = args[0];
        if (!mode.equals("array") && !mode.equals("UF")) {
            throw new IllegalArgumentException("Illegal command-line argument");
        }
        int n = Integer.parseInt(args[1]);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        StdDraw.pause(0);
        StdOut.println(n);
        Percolation perc = mode.equals("array") ? new ArrayPercolation(n) : new UFPercolation(n);
        PercolationVisualizer.draw(perc, n);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        StdDraw.pause(DELAY);
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                int i = (int) (n - Math.floor(y) - 1);
                int j = (int) (Math.floor(x));
                if (i >= 0 && i < n && j >= 0 && j < n) {
                    if (!perc.isOpen(i, j)) {
                        StdOut.println(i + " " + j);
                    }
                    perc.open(i, j);
                }
                PercolationVisualizer.draw(perc, n);
            }
            StdDraw.enableDoubleBuffering();
            StdDraw.show();
            StdDraw.pause(DELAY);
        }
    }
}
