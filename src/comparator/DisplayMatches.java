/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

/**
 *
 * @author Chris
 */
public class DisplayMatches {

    NGramWrapper t1;
    NGramWrapper t2;
    Matches matches;

    public DisplayMatches(NGramWrapper t1, NGramWrapper t2, Matches matches) {
        this.t1 = t1;
        this.t2 = t2;
        this.matches = matches;
    }

    public DisplayMatches(Comparator c) {
        this.t1 = c.text;
        this.t2 = c.corpus;
        this.matches = c.getMatches();
    }

    /**
     *
     * @return t1 with every substrig representing a match in upper case and
     * everything else put in lower case
     */
    public String t1UpperCase() {

        String t1Formatted = t1.text;
        String t2Formatted = t2.text;

        t1Formatted = t1Formatted.toLowerCase();
        t2Formatted = t2Formatted.toLowerCase();

        for (Object o : matches) {
            MatchEntry3 m = (MatchEntry3) o;

            int length = m.match.length();
            int index = m.index;

            String str1 = t1Formatted.substring(0, index);
            String str2 = t1Formatted.substring(index, index + length);
            String str3 = t1Formatted.substring(index + length);

            str2 = str2.toUpperCase();
            t1Formatted = (str1.concat(str2)).concat(str3);
        }

        if (!t1.text.equalsIgnoreCase(t1Formatted)) {
            System.out.println("PROBLEM");
        }

        return t1Formatted;
    }

    public String t2UpperCase() {
        String formattedText = t2.text;

        formattedText = formattedText.toLowerCase();

        for (Object o : matches) {
            MatchEntry3 m = (MatchEntry3) o;

            int length = m.match.length();

            for (Integer i : m.indices) {
                
                /*StringBuilder sb = new StringBuilder(formattedText.substring(0, i));
                sb = sb.append(formattedText.substring(i, i + length).toUpperCase());
                sb = sb.append(formattedText.substring(i + length));
                */
                
                String str1 = formattedText.substring(0, i);
                String str2 = formattedText.substring(i, i + length);
                String str3 = formattedText.substring(i + length);

                str2 = str2.toUpperCase();
                formattedText = (str1.concat(str2)).concat(str3);
                //formattedText = sb.toString();
            }

        }

        if (!t2.text.equalsIgnoreCase(formattedText)) {
            System.out.println("PROBLEM2");
        }

        return formattedText;
    }
    
    /**
     * 
     * @return The two texts with matches in upper case and all else in lower case.
     */
    public String upperCase() {
        return t1UpperCase() + "\n\n" + t2UpperCase();
    }
    
}
