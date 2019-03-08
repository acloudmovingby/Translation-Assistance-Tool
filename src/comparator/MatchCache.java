/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Caches MatchLists so they don't have to be recomputed.
 *
 * Changes to the main file during program runtime may affect the matches in the
 * cached MatchLists (which are expensive to recompute). These changes might
 * include removing segments or committing/uncommitting them. Thus there are
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
     * Retrieves the cached MatchList for the specified Segment or null if none
     * is cached.
     *
     * @param seg
     * @return
     */
    protected MatchList getCachedMatchList(Segment seg) {
        return matchCache.get(seg);
    }

    protected void cacheMatchList(Segment seg, MatchList m) {
        matchCache.put(seg, m);
    }

    /**
     * If the given segment matches any segments in the main file (whose matchlists have been cached), it adds this segment as well to those matchlists.
     * 
     * If the segment is not committed, no changes are made. 
     * 
     * @param seg 
     */
    protected void addSegment(Segment seg) {
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
                //MatchFinderCoreAlgorithm.getSingleSegmentMatch(seg, seg, 0)
            }
        } 
    }

}
