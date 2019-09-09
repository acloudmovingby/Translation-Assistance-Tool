/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchSegment;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javafx.util.Pair;

/**
 * Provides ways of ranking matches once they've been found.
 *
 * @author Chris
 */
public class RankMatches {

    /**
     * One of the primary issues for this program is that some words may be very
     * common so if a given source segment matches with 1000 other segments, 900
     * of those might just be a match with some highly common word. This ranking
     * algorithm groups together MatchSegments by if they are "redundant", and
     * then alternates between these different groups so the user quickly sees
     * all possible word locations in the source segment where matches have been
     * found. "Redundant" here means that the two target segments (as found by
     * the match finding algorithm) have a common substring with the source
     * segment in the same place.
     *
     * Basically what we do is take all MatchSegments (which represent matching
     * Segments in the corpus) and for each common substring they have with the
     * source Segment, to give it an interval showing where that substring is in
     * the source text. Then we take all these intervals and organize them into
     * a DAG where each node's children represent intervals that are within that
     * node's bounds (the node is a superset of all of its children). By
     * organizing them this way and then taking the topological sort of the
     * graph, it very nicely skips between clusters, still prioritizing the
     * longest match intervals but jumping between different parts of the source
     * string so the user sees a variety of match types.
     *
     * @param matchSegs
     * @return
     */
    public static List<MatchSegment> rankByAlternatingClusters(List<MatchSegment> matchSegs) {

        IntervalGraph<MatchSegment> graph = new IntervalGraph();

        // for every match, find every interval that it matches to in the source text, then add that match/interval pair as a node to the graph
        matchSegs.forEach((matchSeg) -> {
            matchSeg.getSourceMatchIntervals().forEach((interval) -> {
                graph.add(matchSeg, interval.getKey(), interval.getValue());
            });
        });

        return graph.getTopologicalSort().stream()
                .map(Pair::getKey) // gets the MatchSegment
                .distinct() // each MatchSegment may have had multiple intevals, so just use the first one in the list
                .collect(Collectors.toList());
    }

    /**
     * Sorts the matches according to the longest contiguous match (the longest
     * common substring). So if MatchSegment A shows common substrings in the
     * source text at intervals [0,10] and [22,30], and MatchSegment B has
     * [0,11], then B will appear first because it's longest interval match is
     * 11 characters.
     *
     * @param matchSegs
     * @return
     */
    public static List<MatchSegment> rankByMaximumMatchLength(List<MatchSegment> matchSegs) {
        return matchSegs.stream()
                .sorted((MatchSegment a, MatchSegment b) -> {
                    OptionalInt maxIntervalInA = a.getTargetMatchIntervals().stream()
                            .mapToInt(intervalA -> intervalA.getValue() - intervalA.getKey())
                            .max();
                    OptionalInt maxIntervalInB = b.getTargetMatchIntervals().stream()
                            .mapToInt(intervalB -> intervalB.getValue() - intervalB.getKey())
                            .max();
                    if (maxIntervalInA.isPresent() && maxIntervalInB.isPresent()) {
                        return maxIntervalInA.getAsInt() - maxIntervalInB.getAsInt();
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Sorts the matches according to the total common substring length. So if
     * MatchSegment A shows common substrings in the source text at intervals
     * [0,10] and [22,30], and MatchSegment B has [0,11], then A will appear
     * first because in total it has 18 characters in common.
     *
     * @param matchSegs
     * @return
     */
    public static List<MatchSegment> rankByTotalMatchLength(List<MatchSegment> matchSegs) {
        return matchSegs.stream()
                .sorted((MatchSegment a, MatchSegment b) -> {
                    int aTotalMatchLength = a.getTargetMatchIntervals().stream()
                            .mapToInt(interval -> interval.getValue() - interval.getKey()).sum();
                    int bTotalMatchLength = b.getTargetMatchIntervals().stream()
                            .mapToInt(interval -> interval.getValue() - interval.getKey()).sum();
                    return aTotalMatchLength - bTotalMatchLength;
                })
                .collect(Collectors.toList());

    }

}
