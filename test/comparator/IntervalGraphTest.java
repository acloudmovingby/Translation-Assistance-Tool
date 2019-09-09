/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class IntervalGraphTest {
    
    public IntervalGraphTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class IntervalGraph.
     */
    @Test
    public void testEmptyTree() {
        System.out.println("empty");
        IntervalGraph graph = new IntervalGraph();
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        assertEquals(expResult, result);
    }

    /**
     * Test of breadthFirstTraversal method, of class IntervalGraph.
     */
    @Test
    public void testOneNode() {
        String a = "A";
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 2);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,2)));
        assertEquals(expResult, result);
    }
    
    /**
     *  Identical nodes are NOT allowed. 
     */
    @Test
    public void testTwoIdentical() {
        String a = "A";
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 2);
        graph.add(a, 0, 2);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,2)));
        assertEquals(expResult, result);
    }
    
    /**
     * Test of breadthFirstTraversal method, of class IntervalGraph.
     */
    @Test
    public void testASupersetB() {
        String a = "A";
        String b = "B";
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 5);
        graph.add(b, 1, 4);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,5)));
        expResult.add(new Pair("B", new Pair(1,4)));
        assertEquals(expResult, result);
    }
    
    /**
     * Test of breadthFirstTraversal method, of class IntervalGraph.
     */
    @Test
    public void testASubsetB() {
        String a = "A";
        String b = "B";
        
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 1, 4);
        graph.add(b, 0, 5);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("B", new Pair(0,5)));
        expResult.add(new Pair("A", new Pair(1,4)));
        assertEquals(expResult, result);
    }
    
    /**
     * A is a subset of both B and C (but B and C are incomparable). Note that for this list method, A will appear twice because the same interval can appear in multiple trees.
     */
    @Test
    public void testASubsetBC() {
        // graph is correct
        String a = "A";
        String b = "B";
        String c = "C";
        IntervalGraph graph = new IntervalGraph();
        graph.add(b, 0, 5);
        graph.add(c, 2, 7);
        graph.add(a, 2, 3);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("B", new Pair(0,5)));
        expResult.add(new Pair("C", new Pair(2,7)));
        expResult.add(new Pair("A", new Pair(2,3)));
        assertEquals(expResult, result);
    }
    
    /**
     * A is a superset of both B and C (but B and C are incomparable). 
     */
    @Test
    public void testASupersetBC() {
        // graph is correct
        String a = "A";
        String b = "B";
        String c = "C";
        
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 5);
        graph.add(b, 1, 3);
        graph.add(c, 2, 4);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,5)));
        expResult.add(new Pair("B", new Pair(1,3)));
        expResult.add(new Pair("C", new Pair(2,4)));
        
        assertEquals(expResult, result);
    }
    
    /**
     * A is a superset of B which is a superset of C. Tested all permutations of adding nodes to make sure it comes out the same.
     */
    @Test
    public void testASupersetBSupersetC() {
        String a = "A";
        String b = "B";
        String c = "C";
        
        // insertion order: a, b, c
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 5);
        graph.add(b, 1, 4);
        graph.add(c, 2, 3);
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,5)));
        expResult.add(new Pair("B", new Pair(1,4)));
        expResult.add(new Pair("C", new Pair(2,3)));
        assertEquals(expResult, result);
        
        // a, c, b
        graph = new IntervalGraph();
        graph.add(a, 0, 5);
        graph.add(c, 2, 3);
        graph.add(b, 1, 4);
        result = graph.getTopologicalSort();
        assertEquals(expResult, result);
        
        // b, a, c
        graph = new IntervalGraph();
        graph.add(b, 1, 4);
        graph.add(a, 0, 5);
        graph.add(c, 2, 3);
        result = graph.getTopologicalSort();
        assertEquals(expResult, result);
        
        // b, c, a
        graph = new IntervalGraph();
        graph.add(b, 1, 4);
        graph.add(c, 2, 3);
        graph.add(a, 0, 5);
        result = graph.getTopologicalSort();
        assertEquals(expResult, result);
       
        
        // c, a, b
        graph = new IntervalGraph();
        graph.add(c, 2, 3);
        graph.add(a, 0, 5);
        graph.add(b, 1, 4);
        result = graph.getTopologicalSort();
        assertEquals(expResult, result);
        
        
        // c, b, a
        graph = new IntervalGraph();
        graph.add(c, 2, 3);
        graph.add(b, 1, 4);
        graph.add(a, 0, 5);
        result = graph.getTopologicalSort();
        assertEquals(expResult, result);
        
    }
    
    /**
     * Tested a complex graph where there are multiple levels and heierarchies, identical nodes added, etc.
     */
    @Test
    public void testComplexGraph() {
        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";
        String e = "E";
        String f = "F";
        String g = "G";
        String h = "H";
        
        // insertion order: a, b, c
        IntervalGraph graph = new IntervalGraph();
        graph.add(a, 0, 20);
        graph.add(b, 18, 30);
        graph.add(c, 1, 9);
        graph.add(d, 4, 16);
        graph.add(e, 14, 20);
        graph.add(f, 2, 9);
        graph.add(g, 10, 15);
        graph.add(h, 3, 6);
        graph.add(a, 10, 13); // can have duplicate objects as long as interval is different
        List result = graph.getTopologicalSort();
        List expResult = new ArrayList();
        expResult.add(new Pair("A", new Pair(0,20)));
        expResult.add(new Pair("B", new Pair(18,30)));
        expResult.add(new Pair("C", new Pair(1,9)));
        expResult.add(new Pair("D", new Pair(4,16)));
        expResult.add(new Pair("E", new Pair(14,20)));
        expResult.add(new Pair("F", new Pair(2,9)));
        expResult.add(new Pair("G", new Pair(10,15)));
        expResult.add(new Pair("H", new Pair(3,6)));
        expResult.add(new Pair("A", new Pair(10,13)));
        assertEquals(expResult, result);
    }
    
    
    
}
