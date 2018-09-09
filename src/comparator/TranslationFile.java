/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Chris
 */
public class TranslationFile {
    String thai;
    String english;
    
    ArrayList<TMSeg> tmSegments;

    private class TMSeg {
        String th;
        String eng;
        public TMSeg(String th, String eng) {
            this.th = th;
            this.eng = eng;
        }
        
        @Override
        public boolean equals(Object o) {
             if (o == this) {
                return true;
            }

            if (!(o instanceof TMSeg)) {
                return false;
            }

            TMSeg t = (TMSeg) o;

            return (t.eng.equals(this.eng) && t.th.equals(this.th));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.th);
            hash = 97 * hash + Objects.hashCode(this.eng);
            return hash;
        }
    }
    
    
            
       
}
