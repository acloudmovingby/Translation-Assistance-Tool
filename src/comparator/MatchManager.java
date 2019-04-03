/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchList;
import DataStructures.MatchSegment;
import DataStructures.PostingsList;
import DataStructures.Segment;
import State.State;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

/**
 * Caches matches found. Because MatchLists are retrieved whenever the user
 * selects a new cell and because finding matches is quite expensive, they
 * should be cached (also because JavaFX can change selection quite unexpectedly
 * at times). When the minimum match length is change, however, the cache is
 * cleared.
 *
 * @author Chris
 */
public class MatchManager {

    private final PostingsListManager plm;

    /**
     * This contains all the MatchCache for each minimum match length.
     *
     * It is created lazily (i.e. a new MatchCache for a given length is only
     * created when the user chooses that value for the first time).
     */
    private final HashMap<Integer, MatchCache> basicMatchCaches;

    public MatchManager(State state) {
        plm = new PostingsListManager(state.getCorpus());
        basicMatchCaches = new HashMap();
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
        MatchCache cache = getBasicMatchCache(minMatchLength);
        Optional<MatchList> o = cache.getMatchList(seg);
        return o.orElseGet(() -> {
            PostingsList pl = plm.getPostingsList(
                    (minMatchLength <= 8 ? minMatchLength : 8));
            MatchList m = MatchFindingAlgorithms.basicMatch(seg, minMatchLength, pl);
            cache.addMatchList(seg, m);
            return m;
        });*/
    }

    /**
     * Notifies MatchManager that a segment should now be included in match searches. (e.g., because it was committed, restored to the MainFile after undo, etc.)
     */
    public void includeSegmentInMatches(Segment newSeg) {
        plm.addSegment(newSeg);
        
        basicMatchCaches.forEach((minLength, cache) -> {
            // This allows the cache to update its contents without needing to know the implementation details of the match algorithm
            Function<Segment, Optional<MatchSegment>> matchAlgorithm
                    = ((sourceSeg) -> {
                        return MatchFindingAlgorithms.singleSegBasicMatch(sourceSeg, newSeg, minLength);
                    });
            cache.updateMatchLists(matchAlgorithm);
        });
    }

    /**
     * Notifies MatchManager that a segment should no longer be included in match searches. (either because it was uncommitted or it was removed).
     *
     * Removes the segment from both the PostingsListManager and from all
     * currently cached MatchLists.
     *
     * @param seg The Segment which should no longer be a potential match.
     */
    public void removeSegmentFromMatches(Segment seg) {
        plm.removeSegment(seg);
        basicMatchCaches.values().forEach(cache -> cache.removeSegment(seg));
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
