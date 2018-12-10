/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import Files.TUCompareEntry;
import Files.TUEntryBasic;
import java.text.Format;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 *
 * @author Chris
 */
public class BasicCellFactory     
        implements Callback<TableColumn<TUCompareEntry, String>, TableCell<TUCompareEntry, String>> {
    
    private TextAlignment alignment;
    private Format format;
    private Font font;
 
    public BasicCellFactory() {
        this.setAlignment(TextAlignment.LEFT);
    }
    
    public TextAlignment getAlignment() {
        return alignment;
    }
 
    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }
 
    public Format getFormat() {
        return format;
    }
 
    public void setFormat(Format format) {
        this.format = format;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public TableCell<TUCompareEntry, String> call(TableColumn<TUCompareEntry, String> p) {
        TableCell<TUCompareEntry, String> cell = new TableCell<TUCompareEntry, String>() {
            private Text text;
            @Override
            public void updateItem(String item, boolean empty) {
                if (item == getItem()) {
                    return;
                }
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else { 
                    text = new Text(item.toString());
                    text.setFont(font);
                    text.wrappingWidthProperty().bind(p.widthProperty().subtract(5));
                    setGraphic(text);
                }
            }
        };
        cell.setTextAlignment(alignment);
        switch (alignment) {
            case CENTER:
                cell.setAlignment(Pos.CENTER);
                break;
            case RIGHT:
                cell.setAlignment(Pos.CENTER_RIGHT);
                break;
            default:
                cell.setAlignment(Pos.CENTER_LEFT);
                break;
        }
        return cell;
    }
    
}


