/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import State.UIState;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

/**
 * Helps build TextFlow objects for cases where you want to highlight text.
 * @author Chris
 */
public final class HighlightText {
    
    /**
     * Returns a TextFlow object that shows the given String highlighted at the specified intervals
     * @param text
     * @param highlightIntervals
     * @return 
     */
    public static TextFlow highlightText(String text, List<Pair<Integer,Integer>> highlightIntervals) {
        Color highlightColor = Color.GREEN;
        Color baseColor = Color.BLACK;
        ArrayList<String> substrings = new ArrayList();
        TextFlow textFlow = new TextFlow();
        
        // if no area is to be highlighted, just return the text as the base color
        if (highlightIntervals.isEmpty()) {
            textFlow.getChildren().add(
                    getTextObjectWithColor(text, baseColor));
            return textFlow;
        }
        
        // if the highlight doesn't start at the first character, add the first part of the text as the base color
        if (highlightIntervals.get(0).getKey() > 0) {
            String firstChunkOfText = text.substring(0, highlightIntervals.get(0).getKey());
            textFlow.getChildren().add(
                    getTextObjectWithColor(firstChunkOfText, baseColor));
        } 
        
// sequentially add each highlighted interval as well as the non-highlighted part that follows to the TextFlow object
        for (int i=0; i<highlightIntervals.size(); i++) {
            Pair<Integer,Integer> interval = highlightIntervals.get(i);
            // first add the highlighted text
            String highlightedText = text.substring(interval.getKey(), interval.getValue());
            textFlow.getChildren().add(
                    getTextObjectWithColor(highlightedText, highlightColor));
            
            // second, check to see if end of the text has been reached; if not, then add non-highlighted text until the next interval or until the end of the text
            // if the current interval extends to less than the end, then we need to add the next non-highlighted chunk
            if (interval.getValue() < text.length()) {
                int endOfBaseColor; 
                // if there are no more intervals, make the end of this non-highlighted part just be the end of the string
                if (i+1 == highlightIntervals.size()) {
                    System.out.println("case 1");
                    endOfBaseColor = text.length();
                } else {
                    System.out.println("case 2");
                    endOfBaseColor = highlightIntervals.get(i+1).getKey();
                }
                System.out.println("interval# = " + (i+1) +"/" + highlightIntervals.size() + ", interval = (" + interval.getKey() + "," + interval.getValue() + "), endOfBaseColor = " + endOfBaseColor + ", text length = " +text.length());
                textFlow.getChildren().add(
                    getTextObjectWithColor(text.substring(interval.getValue(), endOfBaseColor), baseColor));
            }
            
        }
        return textFlow;
    }

    /**
     * Returns a JavaFX Text object with the color as specified and the text being the substring at the specified interval
     * @param str
     * @param color
     * @param interval
     * @return 
     */
    private static Text getTextObjectWithColor(String str, Color color) {
        Text ret = new Text(str);
        ret.setFill(color);
        ret.setFont(UIState.getThaiFont());
        return ret;
    }
    
    
}
