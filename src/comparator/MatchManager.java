package comparator;

import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Finds matches, caches them, and returns MatchSegments for display in the UIState.
 * 
 * Because lists of matches are retrieved whenever the user
 * selects a new Segment in the main file, and because finding matches is quite expensive, they
 * should be cached (for example, in JavaFX, when you remove a row from a table that was selected, the selection will flutter around to different rows before resolving, resulting in many unnecessary calls to this; hence just putting in a cache, rather than trying to figure out what's happening under the hood in JavaFX). 
 * 
 * If segments are removed/added from the pool of searchable segments
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
     * segments to the cached lists of matches.
     *
     */
    private final HashMap<Integer, HashMap<Segment, List<MatchSegment>>> basicMatchCache;

    public MatchManager(HashSet<Segment> segsToSearchForMatches) {
        plm = new PostingsListManager(segsToSearchForMatches);
        basicMatchCache = new HashMap();
    }

    /**
     * Returns a list of matching Segments for the specified segment.
     *
     * @param seg
     * @param minMatchLength
     * @return
     */
    public List<MatchSegment> basicMatch(Segment seg, int minMatchLength) {

        // Here, if the minMatchLength is less than 8, then the postings list with that exact size ngram is retrieved. Otherwise the 8-character ngram postings list is retrieved. 
        // for ngrams of 8 character length, the number of Segments to check dramatically decreases, so I didn't waste more memory making postingslists for ngrams longer than 8.
        PostingsList pl = plm.getPostingsList(
                (minMatchLength <= 8 ? minMatchLength : 8));
        
        return MatchFindingAlgorithms.basicMatch(seg, minMatchLength, pl);
    }

    /**
     * Notifies MatchManager that a committed segment should now be included in
     * match searches. (because it was just committed by the user, restored to
     * the MainFile after undo, etc.) If the segment is not committed, nothing
     * is done.
     *
     * 
     * @param newSeg
     */
    public boolean includeSegmentInMatches(Segment newSeg) {
        if (newSeg.isCommitted()) {
            plm.addSegment(newSeg);

            // for each minimum length, look through the cache and if source segs now match with newSeg, then add the match to the MatchList
            basicMatchCache.forEach((length, cache) -> {
                cache.forEach((sourceSeg, matchList) -> {
                    MatchFindingAlgorithms.singleSegBasicMatch(sourceSeg, newSeg, length)
                            .ifPresent((matchSeg) -> matchList.add(matchSeg));
                });
            });
            return true;
        }
        return false;
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
                for (MatchSegment m : matchList) {
                    if (m.getSegment().equals(seg)) {
                        toRemove = m;
                    }
                }
                matchList.remove(toRemove);
            });
        });
    }

    public PostingsListManager getPostingsListManager() {
        return plm;
    }

}
