import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    public WordNet wordnet;


    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Returns the outcast noun from nouns.
    public String outcast(String[] nouns) {
        int maxDist = Integer.MIN_VALUE;
        String largestNoun = null;

        //iterate over each noun and calculate diustance from other nouns
        for(String noun : nouns){
            int distance = 0;
            for (String otherNoun : nouns) {
                if (!noun.equals(otherNoun)) {
                   distance += wordnet.distance(noun, otherNoun);
                }
            }
            //if greater then update
            if (distance > maxDist){
               maxDist = distance;
               largestNoun = noun;
            }

        }
        return largestNoun;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}
