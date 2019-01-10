/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import Database.DatabaseOperations;
import java.util.ArrayList;

/**
 *
 * @author Chris
 */
public class BasicFileTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        BasicFile bf = new BasicFile();

            // makes two copies of 5 TUs
            Segment seg1 = bf.newSeg();
            seg1.setID(DatabaseOperations.makeTUID());
            seg1.setThai("t1");
            seg1.setEnglish("e1");

            Segment seg2 = bf.newSeg();
            seg2.setID(DatabaseOperations.makeTUID());
            seg2.setThai("t2");
            seg2.setEnglish("e2");

            Segment seg3 = bf.newSeg();
            seg3.setID(DatabaseOperations.makeTUID());
            seg3.setThai("t3");
            seg3.setEnglish("e3");

            Segment seg4 = bf.newSeg();
            seg4.setID(DatabaseOperations.makeTUID());
            seg4.setThai("t4");
            seg4.setEnglish("e4");

            Segment seg5 = bf.newSeg();
            seg5.setID(DatabaseOperations.makeTUID());
            seg5.setThai("t5");
            seg5.setEnglish("e5");

            ArrayList<Segment> selectedTUs = new ArrayList();
        
            // merges seg1 and seg2
                    selectedTUs.add(seg1);
                    selectedTUs.add(seg2);
                    bf.mergeTUs(selectedTUs);

                    //merges seg3 and seg4
                    selectedTUs = new ArrayList();
                    selectedTUs.add(seg3);
                    selectedTUs.add(seg4);
                    bf.mergeTUs(selectedTUs);

                    // at this point the file should have three segments in activeSegs
                    // the first two segs are the result of the prior merges
                    // the last seg is seg5
                    // now we merge the second merged seg with seg5
                    selectedTUs = new ArrayList();
                    selectedTUs.add(bf.getActiveSegs().get(1));
                    selectedTUs.add(seg5);
                    bf.mergeTUs(selectedTUs);
    }
    
}
