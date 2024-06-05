import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {
    private Term[] terms;

    // Constructs an autocomplete data structure from an array of terms.
    public Autocomplete(Term[] terms) {

        //corner case
        if (terms == null){
            throw new NullPointerException("terms is null");
        }

        // copy and then sort the terms in array
        this.terms = new Term[terms.length];

        for(int i = 0; i < terms.length; i++){
            this.terms[i] = terms[i];
        }

        Arrays.sort(this.terms);

    }

    // Returns all terms that start with prefix, in descending order of their weights.
    public Term[] allMatches(String prefix) {

        //corner case
        if (prefix == null){
            throw new NullPointerException("prefix is null");
        }

        //if no terms then return an empty array
        if(terms.length == 0){
            return new Term[0];
        }

        // Finding the first and last indexes of terms that match the prefixe;
        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix), Term.byPrefixOrder(prefix.length()));
        int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix), Term.byPrefixOrder(prefix.length()));

        //the amount of terms that match
        int n = lastIndex - firstIndex + 1;

        //create array to store the matching terms
        Term[] matches = new Term[n];

        //copuy terms into the array
        for( int i = firstIndex; i <= lastIndex; i++){
            matches[i - firstIndex] = terms[i];
        }

        //sort terms by their weights going down
        Arrays.sort(matches, Term.byReverseWeightOrder());

        return matches;
    }

    // Returns the number of terms that start with prefix.
    public int numberOfMatches(String prefix) {

        //corner case
        if (prefix == null){
            throw new NullPointerException("prefix is null");
        }

        // If no terms available, return 0
        if(terms.length == 0){
            return 0;
        }

        // Finding the first and last indexes of terms that match the prefix;
        int i = BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix), Term.byPrefixOrder(prefix.length()));
        int j = BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix), Term.byPrefixOrder(prefix.length()));

        return j - i + 1;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}
