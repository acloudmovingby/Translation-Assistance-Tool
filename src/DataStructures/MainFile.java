/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chris
 */
public class MainFile extends BasicFile {
    
    public MainFile(BasicFile file) {
        super(file);
    }
    
    /**
     * Splits a segment into two pieces. The second segment begins with the character at
     * the specified index. So if this method was given "unhappy" and 2, the two
     * new TUs would be "un" and "happy".
     *
     * @param seg
     * @param splitIndex
     * @return The newly created segments. Returns null if the split fails (index out of bounds or segment doesn't actually exist in this file). 
     */
    public List<Segment> splitSeg(Segment seg, int splitIndex) {
        // if the seg is null or is not in the list of active segs, return
        // if the split index is out of bounds, return
        if (seg == null 
                || splitIndex <= 0 
                || splitIndex >= seg.getThai().length()) {
            return null;
        }
        
        // This makes sure that the selected seg is actually in the active segs list.
        // we use == to make sure it's the exact same object (the .equals method for Segments compares the value of the fields, not identity of the object)
        // if two segs have same field values (including id) but are not the same object, this method (splitSeg) will not work correctly
        boolean isInActiveSegs = false;
        for (Segment s : getActiveSegs()) {
            if (s==seg) {
                isInActiveSegs = true;
            }
        }
        if (!isInActiveSegs) {
            return null;
        }
            
        // splits the Thai text into two parts, splitting at the splitIndex
        String firstThai = seg.getThai().substring(0, splitIndex);
        String secondThai = seg.getThai().substring(splitIndex);

        // retrieves index of the segment to be split (the old segment)
        int index = getActiveSegs().indexOf(seg);

        // creates first new seg and inserts
        SegmentBuilder sb = new SegmentBuilder(this);
        sb.setThai(firstThai);
        sb.setEnglish(seg.getEnglish());
        Segment newSeg1 = sb.createSegment();
        getActiveSegs().add(index, newSeg1);

        // creates second new seg and inserts
        sb.setThai(secondThai);
        sb.setEnglish("");
        Segment newSeg2 = sb.createSegmentNewID();
        getActiveSegs().add(index + 1, newSeg2);
        
        //hides old seg
        hideSeg(seg);
        
        List<Segment> retList = new ArrayList();
        retList.add(newSeg1);
        retList.add(newSeg2);
                
        return retList;
    }

    /**
     * Replaces the specified segment with a new one (with new id) where its English text field has been changed as specified. This then returns the newly created segment.
     * Note that the old seg will now be placed in the "removedSegs" list of this main file. IMPORTANT: if the seg does not exist in the file, then this returns null).
     * @param seg
     * @param newEnglishText
     * @return The new segment or null if seg does not exist in the activeSegs list of this file.
     */
    public Segment editEnglish(Segment seg, String newEnglishText) {
        
        // checks if seg exists
        if (!getActiveSegs().contains(seg)) {
            return null;
        } 
        
        // gets index of where seg is located
        int index = getActiveSegs().indexOf(seg);
        SegmentBuilder sb = new SegmentBuilder(seg);
        sb.setEnglish(newEnglishText);
        Segment newSeg = sb.createSegmentNewID();
        
        // replaces old seg with new seg
        getActiveSegs().set(index, newSeg);
        // adds old seg to the "removed" list
        getHiddenSegs().add(seg);
        
        return newSeg;
    }
    
    
    
}
