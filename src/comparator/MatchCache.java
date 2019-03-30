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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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

    protected MatchCache() {
        matchCache = new HashMap();
    }

    /**
     * Removes a segment if it exists in any cached matchLists.
     * @param seg 
     */
    protected void removeSegment(Segment seg) {

        // this conditional just ensures that not all MatchLists are needlessly searched. A non-committed Segment should not appear in any MatchLists.
        // if the MatchLists were formed incorrectly (i.e. with non-committed Segments), then this would obviously fail as well. 
        // Note: Segments are immutable, so assuming MatchLists were formed correctly, this should always be correct.
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
     * Retrieves the MatchList for a given source segment from the main file or an empty Optional if no MatchLists are cached as such.
     * 
     * @param seg
     * @return Optional containing a MatchList or an empty Optional if no list has been cached.
     */
    protected Optional<MatchList> getMatchList(Segment seg) {
        MatchList m = matchCache.get(seg);
        return Optional.ofNullable(m);
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
    
    /**
     * Adds the specified MatchSegment to the MatchList of the specified Segment.
     * 
     * If no MatchList is cached, then this creates a new one with matchSeg as its only member. If the MatchSegment already exists in the MatchList, then this does not change the MatchList.
     * @param seg The source Segment.
     * @param matchSeg 
     */
    protected void addMatch(Segment seg, MatchSegment matchSeg) {
        MatchList matchList = matchCache.get(seg);
        
        if (matchList == null) {
            matchList = new MatchList();
            matchList.addEntry(matchSeg);
            matchCache.put(seg, matchList);
        }
        
        if (!matchList.getMatchSegments().contains(matchSeg)) {
            matchList.addEntry(matchSeg);
        }
    }

    /**
     * Applies the provided matching algorithm against all source Segments stored in the cache and updates their MatchLists if a match is found.
     * 
     * More precisely: The supplied function should return an Optional wrapping a MatchSegment. If for a given source segment the match algorithm supplies a non-empty Optional, then it adds the MatchSegment contained in the Optional into the MatchList for that source segment.
     * 
     * @param target
     * @param matchAlgorithm 
     */
    protected void updateMatchLists(Function<Segment, Optional<MatchSegment>> matchAlgorithm) {
        matchCache.forEach((sourceSeg, matchList) -> {
            // if returned Optional contains a MatchSegment (i.e. a match was found), it adds it to the matchList
            matchAlgorithm.apply(sourceSeg).
                    ifPresent(matchSeg -> matchList.addEntry(matchSeg));
        });
    }

}
