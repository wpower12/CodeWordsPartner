/**
 * Created by wpower12 on 3/27/16.
 */
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.*;
import java.util.*;


public class CodeWordsPartner {

    static int NUM_TARGETS=3, NUM_VISIBLE=10;

    static class Clue {
        String c;
        int s;
        public Clue( String word, int size ){
            c = word; s = size;
        }

        @Override
        public String toString(){
            return c+", "+s;
        }
    }

    public static void main( String args[] ) throws Exception {

        // Open GoogleNews model
        File google_model = new File("src/main/resources/GoogleNews-vectors-negative300.bin.gz");
        Word2Vec vec = (Word2Vec) WordVectorSerializer.loadGoogleModel(google_model, true);

        // Open list of possible words
        File word_list = new File("src/main/resources/wordlist.txt");

        int NUM_TARGETS=3, NUM_VISIBLE=10;

        Scanner kb = new Scanner(System.in);

        ArrayList<String> sample;
        Collection<String> visible, target, assassin;
        Clue res;
        String in;

        // Loop until quit
        while( true ){
            sample = resSample(word_list, NUM_VISIBLE);

            visible = (Collection<String>) sample.clone();
            assassin = sample.subList(0,1);
            target = sample.subList(1 ,NUM_TARGETS+1);

            System.out.println("Visibile: \n"+visible);

            res = generateClue( vec, target, assassin );

            System.out.println("Clue: "+res);
            System.out.println("Enter guesses: ");
            in = kb.nextLine();
            System.out.println("Targets: \n"+target);
            System.out.println("Assassin: "+assassin);
        }

        // Close all the things
    }

    public static ArrayList<String> resSample(File words, int size ) throws IOException {
        int seen = 0, count = 0, rp, r;
        ArrayList<String> res = new ArrayList<String>();
        String line;

        BufferedReader br = new BufferedReader(new FileReader(words));
        Random rand = new Random();

        while( (line = br.readLine()) != null ){
            if( count < size){
                res.add(line);
                count++;
            } else {
                r = rand.nextInt( seen+1 );
                if( r < size ){
                    rp = rand.nextInt(size);
                    res.remove(rp);
                    res.add(rp, line);
                }
            }
            seen++;
        }
        return res;
    }

    private static Clue generateClue(Word2Vec vec, Collection<String> target, Collection<String> assassin) {
        // For a target size of N, we need to know the size_of_power_set(N).  That is the set of all possible groupings
        // of target words.  Specfically we need the size for the set if order doesnt matter.
        // this is N! ?
        // For N = 3 that means 6 possible groups.
        Set<Set<String>> power_set = getPowerSet( new HashSet(target) );
        Set<String> r = null;
        for( Set<String> s : power_set ) {  // Removing the empty set
            if(s.isEmpty()) r = s;
        }
        power_set.remove(r);

        float max = 0.0f, score;
        String clue = "";
        int clue_size = 0;

        Collection<String> res = null;
        for( Set<String> s : power_set ){
            // Get nearby vector results
            res = vec.wordsNearest( s, assassin, 5 );

            for( String p_clue : res ){
                if( isTrivial( s, p_clue ) ){
                    // Do nothing?
                } else {
                    score = getAverageSimilarity( vec, s, p_clue )*((float)s.size()/(float)NUM_TARGETS);
                    if( score > max ) max = score; clue = p_clue; clue_size = s.size();
                }
            }
        }

        return new Clue( clue, clue_size);
    }

    private static <T> Set<Set<T>> getPowerSet(Set<T> target) {
      Set<Set<T>> sets = new HashSet<Set<T>>();
      if (target.isEmpty()) {
      	sets.add(new HashSet<T>());
      	return sets;
      }
      List<T> list = new ArrayList<T>(target);
      T head = list.get(0);
      Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
      for (Set<T> set : getPowerSet(rest)) {
      	Set<T> newSet = new HashSet<T>();
      	newSet.add(head);
      	newSet.addAll(set);
      	sets.add(newSet);
      	sets.add(set);
      }
      return sets;
    }

    private static Boolean isTrivial( Collection<String> targets, String clue ){
        // If there is an underscore, return true
        if( clue.contains("_") ) return true;

        // If the clue is just any target with an s, return true
        for( String t : targets ){
            if( t.compareToIgnoreCase(clue) == 0 ) return true;
            if( (t+"s").compareToIgnoreCase(clue) == 0 ) return true;
        }
        return false;
    }

    private static float getAverageSimilarity( Word2Vec vec, Collection<String> targets, String clue ){
        int n = targets.size();
        float res = 0.0f;

        for( String t : targets ){
            res += vec.similarity( clue, t );
        }
        res = res / (float)n;

        return res;
    }
}
