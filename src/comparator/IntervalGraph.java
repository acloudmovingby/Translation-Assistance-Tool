/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import DataStructures.MatchSegment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.util.Pair;

/**
 * A graph that stores objects according to an associated interval. Every node's
 * interval is always within the bounds of their parent(s)'. If you view these
 * intervals as sets, then every child is a subset of its parent. So, for
 * example, if a parent had an interval of (-1,5) then its children could
 * include (2,3), (3,4), or even (-1,5), but not (-2,1). For objects whose
 * intervals are identical there is no guarantee about their ordering in the
 * tree. To be more specific, if a node A1 exists in the tree and has children B
 * and C and then another node A2 is added such that A1 and A2 have the same
 * interval, both B and C will be a children of one of the two but it's not
 * deterministic which of the two will be the "parent" and which will be the
 * "grandparent".
 *
 * Nodes with identical objects and intervals are allowed.
 *
 * @author Chris
 * @param <U> The type of obj stored in the graph
 */
public class IntervalGraph<U> {

    private final Set<Node> allNodes;

    public IntervalGraph() {
        allNodes = new HashSet();
    }

    /**
     * The interval length must be AT LEAST 1. Zero or negative intervals will
     * throw an IllegalArgumentException. NO DUPLICATES (The same object can be stored in the graph in multiple nodes, but they must have different intervals; if you try to add an exact duplicate, this will return false and not change the graph).
     *
     * This is done inefficiently by brute force and it works okay and was simple so I left it this way for now.  Currently, when you add a node x, it just checks x' relationship with every other node in the graph to determine if it's a parent, child, or whatever. However due to the structure of the graph, much of this is redundant (if x is a superset of node y, then you don't need to really check all the children of y too (because those are subsets of y, and therefore, subsets of x)). 
     * 
     * @param obj
     * @param intervalBegin
     * @param intervalEnd
     * @return
     */
    public boolean add(U obj, int intervalBegin, int intervalEnd) {

        if (intervalEnd - intervalBegin < 1) {
            throw new IllegalArgumentException("Interval must be > 1. Interval bounds given were: (" + intervalBegin + ", " + intervalEnd + ")");
        }

        Node<U> newNode = new Node(obj, new Pair(intervalBegin, intervalEnd));
        
        if (allNodes.contains(newNode)) {
            return false;
        }

        for (Node node : allNodes) {
            switch (newNode.relationShipTo(node)) {
                case IDENTICAL:
                    // This is an arbitrary ordering
                    newNode.addChild(node);
                    node.addParent(newNode);
                    break;
                case SUPERSET:
                    newNode.addChild(node);
                    node.addParent(newNode);
                    break;
                case SUBSET:
                    node.addChild(newNode);
                    newNode.addParent(node);
                    break;
                default:
                    // where it's either disjoint or intersecting
                    break;
            }
        }
        allNodes.add(newNode);
        return true;
    }

    public class Node<U> {

        final U obj;
        public final Pair<Integer, Integer> interval;
        final Set<Node<U>> children;
        final Set<Node<U>> parents;

        public Node(U object, Pair<Integer, Integer> interval) {
            this.obj = object;
            this.interval = interval;
            this.children = new HashSet();
            this.parents = new HashSet();
        }

        public boolean addParent(Node<U> parent) {
            if (children.contains(parent)) {
                throw new IllegalArgumentException("No loops (child can't also be parent of a parent.");
            }
            return parents.add(parent);
        }

        public boolean addChild(Node<U> child) {
            if (parents.contains(child)) {
                throw new IllegalArgumentException("No loops (child can't also be parent of parent.");
            }
            return children.add(child);
        }

        public U getObject() {
            return obj;
        }

        @Override
        public String toString() {
            return "(" + interval.toString() + ", " + obj.toString() + ")";
        }

        /**
         * Checks equivalence of the intervals and then runs the
         *
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {

            if (o == this) {
                return true;
            }

            if (!(o instanceof Node)) {
                return false;
            }

            Node s = (Node) o;

            return interval.equals(s.interval) && obj.equals(s.obj);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.obj);
            hash = 53 * hash + Objects.hashCode(this.interval);
            return hash;
        }

        /**
         * Shows the relationship of this node towards the argument node using
         * the SetRelationship enum. Returns a SUPERSET if this node is a
         * superset of the argument node (its interval surrounds the argument
         * node's intervals). Intervals are assumed to be inclusive on the left
         * and exclusive on the right meaning that (0,1) and (1,3) will be
         * considered disjoint not intersecting.
         *
         * @param node
         * @return
         */
        private SetRelationship relationShipTo(Node node) {
            Pair<Integer, Integer> thisInter = this.interval;
            Pair<Integer, Integer> nodeInter = node.interval;

            int leftMinusLeft = thisInter.getKey() - nodeInter.getKey();
            int rightMinusRight = thisInter.getValue() - nodeInter.getValue();

            if (leftMinusLeft == 0) {
                if (rightMinusRight == 0) {
                    return SetRelationship.IDENTICAL;
                } else if (rightMinusRight > 0) {
                    return SetRelationship.SUPERSET;
                } else {
                    return SetRelationship.SUBSET;
                }
            } else if (leftMinusLeft > 0) {
                if (rightMinusRight <= 0) {
                    return SetRelationship.SUBSET;
                } else if (thisInter.getKey() - nodeInter.getValue() >= 0) {
                    // NOTE: for this tree, intervals are inclusive on left and exclusive on right, so the two intervals (0,4) and (4,6) would be considered disjoint
                    return SetRelationship.DISJOINT;
                } else {
                    return SetRelationship.INTERSECTING;
                }
            } else {
                if (rightMinusRight >= 0) {
                    return SetRelationship.SUPERSET;
                } else if (thisInter.getValue() - nodeInter.getKey() <= 0) {
                    // NOTE: for this tree, intervals are inclusive on left and exclusive on right, so the two intervals (0,4) and (4,6) would be considered disjoint// NOTE: for this tree, intervals are inclusive on left and exclusive on right, so the two intervals (0,4) and (4,6) would be considered disjoint
                    return SetRelationship.DISJOINT;
                } else {
                    return SetRelationship.INTERSECTING;
                }
            }

        }
    }

    private enum SetRelationship {
        SUPERSET, SUBSET, DISJOINT, INTERSECTING, IDENTICAL
    }

    /**
     * Finds topological sort of the DAG using Kahn's algorithm. Because a DAG
     * is a partial ordering, not all nodes are comparable. In this graph, these
     * non-comparable nodes are those who are disjoint or intersecting sets of
     * each other (where there is no clear parent/child relationship). In that
     * case they are sorted by the left bound of their intervals, increasing. So
     * (1,5) will come before (2,4) which will come before (10, 19)
     *
     * @return
     */
    public List<Pair<U, Pair<Integer, Integer>>> getTopologicalSort() {

        HashMap<Node, Integer> inCount = new HashMap();

        allNodes.forEach((node) -> {
            inCount.put(node, node.parents.size());
        });

        List<Node> topoSortAsList = KahnAlgorithm(allNodes, inCount);

        return topoSortAsList.stream()
                .map((Node a) -> {
                    Pair<Integer, Integer> interval = a.interval;
                    Pair<U, Pair<Integer, Integer>> item = new Pair(a.obj, interval);
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<Node> KahnAlgorithm(Set<Node> nodes, HashMap<Node, Integer> inCount) {

        if (nodes.isEmpty()) {
            return new ArrayList();
        }

        // find all nodes whose in-degree (their number of parents) is zero. 
        List<Node> zeroIn = inCount.entrySet().stream()
                .filter((Entry<Node, Integer> e) -> e.getValue() == 0)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        
        System.out.println("NEW LEVEL");
        zeroIn.stream().forEach(a -> System.out.println("Interval: " + a.interval + ", no. of children: " + a.children.size()/* + ", substring: " + ((MatchSegment) a.getObject()).getThai().substring((Integer) a.interval.getKey(), (Integer) a.interval.getValue())*/));
        
        // sort non-comparable nodes by the lower int in their interval. (Non-comparable means they are disjoint or intersecting sets so their is no clear parent/child relationship)
        // these could of course by sorted in any way (or not sorted), but sorting them in this way allows us to easily test the output because there is a defined ordering we can predict
        zeroIn.sort((Node a, Node b) -> {
            Pair<Integer, Integer> interA = a.interval;
            Pair<Integer, Integer> interB = b.interval;
            return interA.getKey() - interB.getKey();
        });

        // decrement every child's incount
        zeroIn.forEach(a -> {
            Set<Node> children = a.children;
            children.forEach(aChild -> {
                Integer count = inCount.get(aChild);
                inCount.replace(aChild, --count);
                count = inCount.get(aChild);
            });
        });

        // remove those that were in the nodes
        nodes.removeAll(zeroIn);
        zeroIn.stream().forEach(inCount::remove);
        ArrayList topSortList = new ArrayList(zeroIn);

        // recur
        topSortList.addAll(KahnAlgorithm(nodes, inCount));

        return topSortList;
    }

}
