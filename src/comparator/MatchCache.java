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
 * Caches MatchLists so they don't have to be recomputed, as this involves searching the whole corpus.
 *
 * Finding the matches is expensive, hence why they are cached here. In addition, changes to the main file during program runtime may affect the matches in the
 * cached MatchLists. These changes might
 * include removing segments or committing/uncommitting them from the main file. Thus there are
 * methods provided to add or remove individual segments from the matchlists
 * cached here so the whole MatchList doesn't have to be recomputed.
 *
 * @author Chris
 */
public class MatchCache {

    /**
     * Represents pairings between source Segments in the MainFile and the corresponding matches in the corpus. 
     */
    private final HashMap<Segment, MatchList> matchCache;

    protected MatchCache() {
        matchCache = new HashMap();
    }
    
    /**
     * Here the cache can be pre-filled. This is useful for testing where a specific cache state needs to be tested.
     * @param cacheContents 
     */
    protected MatchCache(HashMap<Segment, MatchList> cacheContents) {
        matchCache = cacheContents;
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
     * Applies the provided matching algorithm against all source Segments stored in the cache and updates their MatchLists if matches are found.
     * 
     * More precisely: If f(x) is the function, then x is the source Segment and f(x) is an Optional wrapping a MatchSegment if a match exists. If for the given source segment the match algorithm returns a non-empty Optional, then it adds the MatchSegment contained in the Optional into the MatchList for that source segment.
     * 
     * If no match is found, then an empty Optional is returned. 
     * 
     * @param matchAlgorithm A function that takes as input a Segment and returns an Optional storing a match or, if no match exists, an empty Optional.
     */
    protected void updateMatchLists(Function<Segment, Optional<MatchSegment>> matchAlgorithm) {
        matchCache.forEach((sourceSeg, matchList) -> {
            // if returned Optional contains a MatchSegment (i.e. a match was found), it adds it to the matchList
            matchAlgorithm
                    .apply(sourceSeg)
                    .ifPresent(matchSeg -> {
                        if (!matchList.getMatchSegments().contains(matchSeg)) {
                            System.out.println("matchList is " + matchList);
                            matchList.addEntry(matchSeg);
                        }
                    });
        });
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MatchCache)) {
            return false;
        }

        MatchCache m = (MatchCache) o;
      
        return getHashMap().equals(m.getHashMap());
    }
    
    /**
     * Used for testing equality. Not meant to be called in program. 
     * @return 
     */
    public HashMap<Segment, MatchList> getHashMap() {
        return matchCache;
    }
    
}
