import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;


public class ShortestCommonAncestor {
    //directed graph to calculate sca
    public DiGraph G;

    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null){
            throw new NullPointerException("G is null");
        }
        this.G = G;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        //calculate distance from vertice v and w to their common ancestor
        int distV = distFrom(v).get(ancestor(v, w));
        int distW = distFrom(w).get(ancestor(v, w));

        //sum
        return distV + distW;
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }

        // calculate distances from v and w to the other verticies
        SeparateChainingHashST<Integer, Integer> distV = distFrom(v);
        SeparateChainingHashST<Integer, Integer> distW = distFrom(w);

        //placeholders
        int minD = Integer.MAX_VALUE;
        int commonAncestor = -1;

        //iterate through all verticies from v
        for(int x : distV.keys()){
            //if also reachable from w then find distance
            if (distW.contains(x)){
                int sumD = distV.get(x) + distW.get(x);

                //update everything
                if(sumD < minD){
                    minD = sumD;
                    commonAncestor = x;
                }
            }
        }
        //return the shortest one
        return commonAncestor;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null || B == null) {
            throw new NullPointerException((A == null ? "A" : "B") + " is null");
        }

        // Check if A or B is empty
        if (!A.iterator().hasNext() || !B.iterator().hasNext()) {
            throw new IllegalArgumentException((!A.iterator().hasNext() ? "A" : "B") + " is empty");
        }
        int[] triad = triad(A, B);

        //calculate lengths from a and b to common ancestor
        int lenA = distFrom(triad[1]).get(triad[0]);
        int lenB = distFrom(triad[2]).get(triad[0]);

        //return sum
        return lenA + lenB;
    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null || B == null) {
            throw new NullPointerException((A == null ? "A" : "B") + " is null");
        }

        // Check if A or B is empty
        if (!A.iterator().hasNext() || !B.iterator().hasNext()) {
            throw new IllegalArgumentException((!A.iterator().hasNext() ? "A" : "B") + " is empty");
        }
        int[] triad = triad(A, B);

        //return sca
        return triad[0];
    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> dist = new SeparateChainingHashST<>();
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        boolean[] marked = new boolean[G.V()];
        int[] distTo = new int[G.V()];

        marked[v] = true;
        distTo[v] = 0;
        queue.enqueue(v);

        //BFS and return sybol table for distances
        while (!queue.isEmpty()){
            int currentV = queue.dequeue();
            int currentD = distTo[currentV];

            dist.put(currentV, currentD);

            for (int adjV: G.adj(currentV)) {
                if (!marked[adjV]) {
                    marked[adjV] = true;
                    distTo[adjV] = currentD + 1;
                    queue.enqueue(adjV);
                }
            }
        }
        return dist;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        int shortestAncestor = -1;
        int vertexV = -1;
        int vertexW = -1;
        int minLength = Integer.MAX_VALUE;

        //iterate through all combination of verticies and find the length of the path
        for (int v : A){
            for (int w : B){
                int pathLength = length(v, w);

                //if a shorter path found then update
                if (pathLength < minLength){
                    minLength = pathLength;
                    vertexV = v;
                    vertexW = w;
                    shortestAncestor = ancestor(v,w);
                }
            }
        }

        //return as an array
        int[] array= new int[3];
        array[0] = shortestAncestor;
        array[1] = vertexV;
        array[2] = vertexW;

        return array;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
