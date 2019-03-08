/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
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
    public MatchList basicMatch(Segment seg, State state) {
        // If this matchlist has been cached, it returns the cached value. Otherwise, it computes the matchlist and adds to the cache.
        MatchList m = cache.getCachedMatchList(seg);
        if (m == null) {
            m = MatchFinderCoreAlgorithm.basicMatch(seg, state.getMinMatchLength(), state);
            cache.cacheMatchList(seg, m);
        }
        return m;
    }
    
    /**
     * Clears the match cache (because the minimum match length has changed).
     */
    public void minMatchLengthChanged(int minMatchLength) {
        cache.minMatchLengthChanged(minMatchLength);
        
    }
    
    /**
     * This ensures that when a segment is added to the state that, if it is committed, it will show up in future match searches. 
     */
    public void includeSegmentInMatches(Segment seg) {
        cache.addSegment(seg);
        plm.addSegment(seg);
    }
    
    /**
     * Ensures that when a segment is removed from the state that, if it was committed, will no longer show up in match searches.
     */
    public void removeSegmentFromMatches(Segment seg) {
        cache.removeSegment(seg);        
        plm.removeSegment(seg);
    }
    
}
