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
import java.util.HashMap;
import java.util.Optional;

/**
 * Caches matches found. Because MatchLists are retrieved whenever the user selects a new cell and because finding matches is quite expensive, they should be cached (also because JavaFX can change selection quite unexpectedly at times). When the minimum match length is change, however, the cache is cleared. 
 * @author Chris
 */
public class MatchManager {
    
    private final MatchCache cache;
    
    private final PostingsListManager plm;
    
    /**
     * This contains all the MatchCache for each minimum match length. 
     * 
     * It is created lazily (i.e. a new MatchCache for a given length is only created when the user chooses that value for the first time).
     */
    private final HashMap<Integer, MatchCache> basicMatchCaches;
    
    public MatchManager(State state) {
        cache = new MatchCache();
        plm = new PostingsListManager(state.getCorpus());
        basicMatchCaches = new HashMap();
    }
    
    /**
     * Returns a MatchList (a list of matching segments) for the specified segment. 
     * @param seg
     * @param state
     * @return 
     */
    public MatchList basicMatch(Segment seg, int minMatchLength) {
        
        MatchCache cache = getBasicMatchCache(minMatchLength);
        Optional<MatchList> o = cache.getMatchList(seg);
        return o.orElseGet(() -> {
            PostingsList pl = plm.getPostingsList(
                (minMatchLength<=8 ? minMatchLength : 8));
            MatchList m = MatchFinderCoreAlgorithm.basicMatch(seg, minMatchLength, pl);
            cache.addMatchList(seg, m);
            return m;
        }); 
    }
    
    /**
     * This ensures that when a segment is added to the state that, if it is committed, it will show up in future match searches. 
     */
    public void includeSegmentInMatches(Segment seg) {
        plm.addSegment(seg);
    }
    
    /**
     * Ensures that when a segment is removed from the state that, if it was committed, will no longer show up in match searches.
     */
    public void removeSegmentFromMatches(Segment seg) {
        plm.removeSegment(seg);       
    }

    public PostingsListManager getPLM() {
        return plm;
    }

    private MatchCache getBasicMatchCache(int minMatchLength) {
        MatchCache m = basicMatchCaches.get(minMatchLength);
        if (m == null) {
            m = new MatchCache();
            basicMatchCaches.put(minMatchLength, m);
        }
        return m;
    }
    
}
