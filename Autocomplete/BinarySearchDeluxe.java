import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdOut;

public class BinarySearchDeluxe {
    // Returns the index of the first key in a that equals the search key, or -1, according to
    // the order induced by the comparator c.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> c) {

        // check for null for corner case
        if (a == null || key == null || c == null ){
            throw new NullPointerException("a, key, or c is null");
            }

        //if array empty, return -1
        if (a.length == 0){
            return -1;
        }

        int low = 0;
        int high = a.length - 1;
        int index = -1;

        //Binary search
        while (low <= high){
            int mid = low + (high - low) / 2;
            int compare = c.compare(key, a[mid]);

            //if key is found, update and search left
            if (compare == 0){
                index = mid;
                high = mid - 1;
            }
            else if (compare < 0){
                //search left
                high = mid - 1;
            }
            else{
                //search right
                low = mid + 1;
            }
        }

        return index;
    }

    // Returns the index of the first key in a that equals the search key, or -1, according to
    // the order induced by the comparator c.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> c) {
        if (a == null || key == null || c == null ){
            throw new NullPointerException("a, key, or c is null");
        }
        if (a.length == 0){
            return -1;
        }

        int low = 0;
        int high = a.length - 1;
        int index = -1;

        while (low <= high){
            int mid = low + (high - low) / 2;
            int compare = c.compare(key, a[mid]);

            if (compare == 0){
                // If found, update index and search right
                index = mid;
                low = mid + 1;
            }
            else if (compare < 0){
                //search left
                high = mid - 1;
            }
            else{
                //search right
                low = mid + 1;
            }
        }

        return index;
    }

    // Unit tests the library. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        String prefix = args[1];
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Arrays.sort(terms);
        Term term = new Term(prefix);
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        int i = BinarySearchDeluxe.firstIndexOf(terms, term, prefixOrder);
        int j = BinarySearchDeluxe.lastIndexOf(terms, term, prefixOrder);
        int count = i == -1 && j == -1 ? 0 : j - i + 1;
        StdOut.println("firstIndexOf(" + prefix + ") = " + i);
        StdOut.println("lastIndexOf(" + prefix + ")  = " + j);
        StdOut.println("frequency(" + prefix + ")    = " + count);
    }
}
