/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import State.State;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Caches MatchLists so they don't have to be recomputed.
 *
 * Finding the matches is expensive, hence why they are cached here. In addition, changes to the main file during program runtime may affect the matches in the
 * cached MatchLists (which are expensive to recompute entirely). These changes might
 * include removing segments or committing/uncommitting them from the main file. Thus there are
 * methods provided to add or remove individual segments from the matchlists
 * cached here so the whole corpus does not have to be re-searched.
 *
 * @author Chris
 */
public class MatchCache {

    private final HashMap<Segment, MatchList> matchCache;
    private int currentMinMatchLength;

    protected MatchCache() {
        matchCache = new HashMap();
    }

    protected void minMatchLengthChanged(int minMatchLength) {
        this.currentMinMatchLength = minMatchLength;
        matchCache.clear();
    }

    /**
     * If the given segment matches any segments in the main file (whose matchlists have been cached), it then adds this segment to those matchlists.
     * 
     * If the segment is not committed, no changes are made. 
     * 
     * @param seg 
     */
    protected void addSegment(Segment seg) {
        
        // have some previously cached segment
        // add null
        // add a segment that is empty
        // add a segment that has no overlap with the cached seg (but same min length)
        // add a segment that has an overlap with it
        // double add segments that match (ensure matchlist contains, not necessarily in any specific order)
        
        /*
        Other to do:
            - verify how I do the minMatchLength is involved in this
            - 
        */
        if (seg.isCommitted()) {
            for (Entry<Segment, MatchList> e : matchCache.entrySet()) {
                Segment segInMainFile = e.getKey();
                MatchFinderCoreAlgorithm.getSingleSegmentMatch(segInMainFile, seg, currentMinMatchLength);
            }
        }
    }

    /**
     * Removes a segment if it exists in any cached matchLists.
     * @param seg 
     */
    protected void removeSegment(Segment seg) {

        if (seg.isCommitted()) {
            
            for (Entry<Segment, MatchList> e : matchCache.entrySet()) {
                MatchList currentMatchList = e.getValue();
                
                MatchSegment toRemove = null;
                for (MatchSegment m : currentMatchList.getMatchSegments()) {
                    if (seg.equals(m.getSegment())) {
                        toRemove = m;
                        break;
                    }
                }
                
                if (toRemove != null) {
                    currentMatchList.removeEntry(toRemove);
                }
            }
        } 
    }

    /**
     * Retrieves the MatchList for a given source segment from the main file or null if no MatchList was cached.
     * @param seg
     * @param state
     * @return MatchList or null if no cached list.
     */
    protected MatchList getMatchList(Segment seg, State state) {
        MatchList m = matchCache.get(seg);
        
        
        return m;
    }
    
    /**
     * Stores the MatchList for the given seg in the cache.
     * 
     * If the seg already is present in the cache, then the old MatchList is replaced by the new one (m).
     * 
     * @param seg A source segment (from the main file).
     * @param m The MatchList representing the matches associated with seg.
     */
    protected void addMatchList(Segment seg, MatchList m) {
        matchCache.put(seg, m);
    }

}
