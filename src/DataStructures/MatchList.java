package DataStructures;

import java.util.Iterator;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents all the matches in the corpus for a given Segment. 
 * 
 * It stores these matches as a list of MatchSegments, which store additional information in addition to Segment, such as the number of characters matching.
 * @author Chris
 */
public class MatchList {
    
    private final ObservableList<MatchSegment> matchSegList;
    private String fileName;
    
    public MatchList() {
        matchSegList = FXCollections.observableArrayList();
        fileName = "default";
    }
    
    
    /**
     * Retrieves the MatchSegments stored in this MatchList.
     * @return 
     */
    public ObservableList<MatchSegment> getMatchSegments() {
        return matchSegList;
    }
    
    public void addEntry(MatchSegment t) {
       matchSegList.add(t);
    }
   
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public String toString() {
        sort();
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(fileName);
        sb.append("\n\t");
        
        for (MatchSegment ms : matchSegList) {
            sb.append(ms.toString());
            sb.append("\n\t");
        }
         return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof MatchList)) {
            return false;
        }

        MatchList m = (MatchList) o;
        
         // tests equality of all TMs within files
        boolean areTMsEqual = true;
        if (m.getMatchSegments().size() != this.getMatchSegments().size()) {
            return false;
        } else {
            sort();
            m.sort();
            Iterator i1 = this.getMatchSegments().iterator();
            Iterator i2 = m.getMatchSegments().iterator();
            while (i1.hasNext()) {
                if (!i1.next().equals(i2.next())) {
                    areTMsEqual = false;
                }
            }
        }
        
        return 
                this.getFileName().equals(m.getFileName()) &&
                areTMsEqual;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.matchSegList);
        hash = 67 * hash + Objects.hashCode(this.fileName);
        return hash;
    }

    private void sort() {
        matchSegList.sort(null);
    }

    public void removeEntry(MatchSegment toRemove) {
        matchSegList.remove(toRemove);
    }

    

    
    
   
}
