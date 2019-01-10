/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_1;

import DataStructures.MatchSegment;
import DataStructures.Segment;
import java.text.Format;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author Chris
 */
public class EditableEnglishCellFactory
        implements Callback<TableColumn<Segment, String>, TableCell<Segment, String>> {
    
    private TextAlignment alignment;
    private Format format;
    private Font font;
    
    public EditableEnglishCellFactory() {
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
    public TableCell<Segment, String> call(TableColumn<Segment, String> p) {
        TextFieldTableCell<Segment, String> cell = new TextFieldTableCell<Segment, String>(new StringConverter() {

            // String converter...don't know what it does but is needed for TextFieldTableCell
            @Override
            public String toString(Object t) {
                if (t != null) {
                    return t.toString();
                } else {
                    return null;
                }
            }
            
            @Override
            public Object fromString(String string) {
                return string;
            }
        }) {
            // actual body of cell
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
                    if (this.isEditing()) {
                        System.out.println("It's editing.");
                    } 
                    text = new Text(item);
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
