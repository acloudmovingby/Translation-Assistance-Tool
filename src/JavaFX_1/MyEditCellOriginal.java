package JavaFX_1;

import javafx.event.Event;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;


public class MyEditCellOriginal<S, T> extends TableCell<S, T> {

    // Text area for editing
    private final TextArea textArea = new TextArea();
    
    // Converter for converting the text in the text field to the user type, and vice-versa:
    private final StringConverter<T> converter;
    
    public MyEditCellOriginal(StringConverter<T> converter) {
        this.converter = converter ;
        
        itemProperty().addListener((obx, oldItem, newItem) -> {
            if (newItem == null) {
                setText(null);
            } else {
                setText(converter.toString(newItem));
            }
        });
        
        setGraphic(textArea);
        setContentDisplay(ContentDisplay.TEXT_ONLY);
        //setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setWrapText(true);
        textArea.setWrapText(true);
        // This makes it so that if the user clicks somewhere else outside of the cell, the text is committed
        textArea.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (! isNowFocused) {
                commitEdit(this.converter.fromString(textArea.getText()));
            }
        });
        
    }
    
    /**
     * Convenience converter that does nothing (converts Strings to themselves and vice-versa...).
     */
    public static final StringConverter<String> IDENTITY_CONVERTER = new StringConverter<String>() {

        @Override
        public String toString(String object) {
            return object;
        }

        @Override
        public String fromString(String string) {
            return string;
        }
        
    };
    
    /**
     * Convenience method for creating an EditCell for a String value.
     * @param <S>
     * @return
     */
    public static <S> MyEditCellOriginal<S, String> createStringEditCell() {
        return new MyEditCellOriginal<S, String>(IDENTITY_CONVERTER);
    }
    

    // set the text of the text field and display the graphic
    @Override
    public void startEdit() {
        super.startEdit();
        textArea.setText(converter.toString(getItem()));
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        textArea.requestFocus();   
    }

    // revert to text display
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    // commits the edit. Update property if possible and revert to text display
    @Override
    public void commitEdit(T item) {
        
        // This block is necessary to support commit on losing focus, because the baked-in mechanism
        // sets our editing state to false before we can intercept the loss of focus.
        // The default commitEdit(...) method simply bails if we are not editing...
        if (! isEditing() && ! item.equals(getItem())) {
            TableView<S> table = getTableView();
            if (table != null) {
                TableColumn<S, T> column = getTableColumn();
                CellEditEvent<S, T> event = new CellEditEvent<>(table, 
                        new TablePosition<S,T>(table, getIndex(), column), 
                        TableColumn.editCommitEvent(), item);
                Event.fireEvent(column, event);
            }
        }

        super.commitEdit(item);
        
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

}