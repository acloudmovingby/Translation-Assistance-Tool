/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import State.State;
import java.util.List;

/**
 *
 * @author Chris
 */
public class Merge implements Action {
    
    private final List<Segment> segsToMerge;

    public Merge(List<Segment> segsToMerge) {
       this.segsToMerge = segsToMerge;
    }

    @Override
    public void execute(State state) {
        
    }
    
}
