/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserActions;

import DataStructures.Segment;
import State.State;

/**
 *
 * @author Chris
 */
public class EditThai implements Action {
    
    Segment seg;
    String newEnglish;
    
    EditThai(Segment seg, String newEnglish) {
        this.seg = seg;
        this.newEnglish = newEnglish;
    }

    @Override
    public void execute(State state) {
        //state.changeThai(seg, newEnglish);
    }
    
}
