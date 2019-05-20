/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import javafx.scene.control.Label;

/**
 *
 * @author Chris
 */
public class CustomTabButton extends Label {
    
    /*
    SHOULD HAVE FOLLOWING IMAGES:
        - SELECTED
        - NORMAL
        - HOVER
        - DEPRESSED
    Constructor takes 4 images (or can)
    Depressed / hover are listners within this class (they check to make sure it's not selected)
    outside of this class, onSelectionChange triggers "change selection" here
    Tab has essentially two states: selected (no depressed/hover effects) and unselected (has depressed/hover effects)
    */
    
}
