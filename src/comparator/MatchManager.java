/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.PostingsList;
import DataStructures.Segment;
import State.State;

/**
 * Caches matches found. Because MatchLists are retrieved whenever the user selects a new cell and because finding matches is quite expensive, they should be cached (also because JavaFX can change selection quite unexpectedly at times). When the minimum match length is change, however, the cache is cleared. 
 * @author Chris
 */
public class MatchManager {
    
    private final MatchCache cache;
    
    private final PostingsListManager plm;
    
    public MatchManager(State state) {
        cache = new MatchCache();
        plm = new PostingsListManager(state.getCorpus());
    }
    
    /**
     * Returns a MatchList (a list of matching segments) for the specified segment. 
     * @param seg
     * @param state
     * @return 
     */
    public MatchList basicMatch(Segment seg, int minMatchLength) {
        MatchList m = cache.getMatchList(seg);
        if (m == null) {
            // if a min matchLength is greater than 8, it simply looks at ngrams of length 8
            PostingsList pl = plm.getPostingsList(
                (minMatchLength<=8 ? minMatchLength : 8));
            m = MatchFinderCoreAlgorithm.basicMatch(seg, minMatchLength, pl);
            cache.addMatchList(seg, m);
        }
        return m;
    }
    
    /**
     * Clears the match cache (because the minimum match length has changed).
     */
    public void minMatchLengthChanged(int newMinLength) {
        cache.minMatchLengthChanged(newMinLength);
    }
    
    /**
     * This ensures that when a segment is added to the state that, if it is committed, it will show up in future match searches. 
     */
    public void includeSegmentInMatches(Segment seg) {
        plm.addSegment(seg);
        cache.addSegment(seg);
    }
    
    /**
     * Ensures that when a segment is removed from the state that, if it was committed, will no longer show up in match searches.
     */
    public void removeSegmentFromMatches(Segment seg) {
        plm.removeSegment(seg);
        cache.removeSegment(seg);        
    }

    public PostingsListManager getPLM() {
        return plm;
    }
    
}
