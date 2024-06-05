import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;


public class WordNet {
    //symbol table for nouns and Ids
    private SeparateChainingHashST<String, Set<Integer>> st;
    //reverse symbol table rst
    private SeparateChainingHashST<Integer, String> rst;
    //shortest common ancestor
    private ShortestCommonAncestor sca;


    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }

        //initialize symbol tables and build directed graph
        st = new SeparateChainingHashST<>();
        rst = new SeparateChainingHashST<>();

        // Populate symbol tables st and rst from synsets file
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String line = synsetsIn.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String[] nouns = parts[1].split("\\s");

            // Populate rst
            rst.put(id, parts[1]);

            // Populate st
            for (String noun : nouns) {
                if (!st.contains(noun)) {
                    Set<Integer> ids = new Set<>();
                    ids.add(id);
                    st.put(noun, ids);
                } else {
                    st.get(noun).add(id);
                }
            }
        }

        // Construct DiGraph G from hypernyms file
        DiGraph G = new DiGraph(rst.size());
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String line = hypernymsIn.readLine();
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                G.addEdge(synsetId, Integer.parseInt(parts[i]));
            }
        }

        // Initialize ShortestCommonAncestor
        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        //return nouns
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        if(word == null){
            throw new NullPointerException("word is null");
        }
        //check if word is noun
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if(noun1 == null){
            throw new NullPointerException("noun1 is null");
        }
        if(noun2 == null){
            throw new NullPointerException("noun2 is null");
        }
        if(!isNoun(noun1)){
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if(!isNoun(noun2)){
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        //return shortest common ancestor for the two nounds
        Set<Integer> NounOne = st.get(noun1);
        Set<Integer> NounTwo = st.get(noun2);

        int shortestId = sca.ancestor(NounOne, NounTwo);

        return rst.get(shortestId);
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if(noun1 == null){
            throw new NullPointerException("noun1 is null");
        }
        if(noun2 == null){
            throw new NullPointerException("noun2 is null");
        }
        if(!isNoun(noun1)){
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if(!isNoun(noun2)){
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        //return shortest distance between noun1 and 2 in graph
        Set<Integer> NounOne = st.get(noun1);
        Set<Integer> NounTwo = st.get(noun2);

        return sca.length(NounOne, NounTwo);
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}
