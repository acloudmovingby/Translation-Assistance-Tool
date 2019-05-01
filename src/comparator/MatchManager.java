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
import java.util.HashSet;

/**
 * Caches matches found. Because MatchLists are retrieved whenever the user
 * selects a new cell and because finding matches is quite expensive, they
 * should be cached (also because JavaFX can change selection quite unexpectedly
 * at times). If segments are removed/added from the pool of searchable segments
 * (such as when a segment is committed), then MatchManager should be notified
 * via the methods includeSegmentInMatches and removeSegmentFromMatches.
 *
 * @author Chris
 */
public class MatchManager {

    private final PostingsListManager plm;

    /**
     * Cache for MatchLists for basic matches. Outer HashMap links a minimum
     * match length to the appropriate cache. The inner HashMap links source
     * segments to the cached match lists.
     *
     */
    private final HashMap<Integer, HashMap<Segment, MatchList>> basicMatchCache;

    public MatchManager(HashSet<Segment> segsToSearchForMatches) {
        plm = new PostingsListManager(segsToSearchForMatches);
        basicMatchCache = new HashMap();
    }

    /**
     * Returns a MatchList (a list of matching segments) for the specified
     * segment.
     *
     * @param seg
     * @param minMatchLength
     * @return
     */
    public MatchList basicMatch(Segment seg, int minMatchLength) {

        PostingsList pl = plm.getPostingsList(
                (minMatchLength <= 8 ? minMatchLength : 8));
        return MatchFindingAlgorithms.basicMatch(seg, minMatchLength, pl);
        /*
        
        HashMap<Segment, MatchList> cacheForThisLength = basicMatchCache.get(minMatchLength);
        
        // if that minMatchLength hasn't been queried, then make a new cache for that length
        if (cacheForThisLength == null) {
            cacheForThisLength = new HashMap();
            basicMatchCache.put(minMatchLength, cacheForThisLength);
        }
        
        MatchList m = basicMatchCache.get(minMatchLength).get(seg);
        
        // if cache doesn't currently have a cached matchlist, then generate one and add to cache.
        if (m == null) {
            PostingsList pl = plm.getPostingsList(
                    (minMatchLength <= 8 ? minMatchLength : 8));
            m = MatchFindingAlgorithms.basicMatch(seg, minMatchLength, pl);
            basicMatchCache.get(minMatchLength).put(seg, m);
        }
        
        return m;
         */
    }

    /**
     * Notifies MatchManager that a segment should now be included in match
     * searches. (because it was committed, restored to the MainFile after undo,
     * etc.)
     *
     * @param newSeg
     */
    public void includeSegmentInMatches(Segment newSeg) {
        plm.addSegment(newSeg);

        // for each minimum length, look through the cache and if source segs now match with newSeg, then add the match to the MatchList
        basicMatchCache.forEach((length, cache) -> {
            cache.forEach((sourceSeg, matchList) -> {
                MatchFindingAlgorithms.singleSegBasicMatch(sourceSeg, newSeg, length)
                        .ifPresent((matchSeg) -> matchList.addEntry(matchSeg));
            });
        });
    }

    /**
     * Notifies MatchManager that a segment should no longer be included in
     * match searches. (because it was uncommitted, was removed, disappeared
     * after an undo, etc.).
     *
     * Removes the segment from both the PostingsListManager and from all
     * currently cached MatchLists.
     *
     * @param seg The Segment which should no longer be a potential match.
     */
    public void removeSegmentFromMatches(Segment seg) {
        plm.removeSegment(seg);
        basicMatchCache.forEach((length, cache) -> {
            cache.forEach((sourceSeg, matchList) -> {
                // if the matchList contains a matchsegment pointing to seg, then remove that MatchSegment from the list
                MatchSegment toRemove = null;
                for (MatchSegment m : matchList.getMatchSegments()) {
                    if (m.getSegment().equals(seg)) {
                        toRemove = m;
                    }
                }
                matchList.removeEntry(toRemove);
            });
        });
    }

    public PostingsListManager getPostingsListManager() {
        return plm;
    }

}
