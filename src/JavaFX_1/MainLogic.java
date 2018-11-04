/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Files.BasicFile;
import Files.FileList;

/**
 *
 * @author Chris
 */
public class MainLogic {
    /**
     * The string that was used to set the current compare table.
     */
    private String currentCompareString;

    /**
     * The minimum length for matching substrings shown in compare table viewer.
     */
    private int minMatchLength;
    /**
     * The file currently being translated.
     */
    BasicFile file1;
    /**
     * The corpus where matches are found.
     */
    FileList corpus;
    
    public MainLogic() {
        // Default minimum length for matches
        minMatchLength = 5;
        
        
    }
}
