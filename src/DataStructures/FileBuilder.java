package DataStructures;

import ParseThaiLaw.TMXResourceBundle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Utility methods to properly construct BasicFile objects.
 *
 * @author Chris
 */
public class FileBuilder {

    /**
     * Takes two strings (thai and english) and splits into segments based on
     * line breaks. Both texts must have equal number of line breaks or it
     * throws an IllegalArgumentException.
     *
     * @param thai
     * @param english
     * @return BasicFile with each Segment representing a line break in original
     * text.
     */
    public static BasicFile buildBasicParse(String thai, String english) {
        String[] th = thai.split("\r?\n");
        String[] en = english.split("\r?\n");

        /*
        if (th.length != en.length) {
            throw new IllegalArgumentException("Unequal number of line breaks in the two texts.");
        }*/
        BasicFile bf = new BasicFile();

        for (int i = 0; i < th.length; i++) {
            SegmentBuilder sb = new SegmentBuilder(bf);
            sb.setThai(th[i]);
            if (i < en.length) {
                sb.setEnglish(en[i]);
            } else {
                sb.setEnglish("");
            }
            bf.addSeg(sb.createSegment());
        }

        return bf;
    }

    public static BasicFile justThaiFilePath(String filePath) {
        BasicFile ret = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReaderThai
                    = new FileReader(filePath);

            BufferedReader buffReaderThai
                    = new BufferedReader(fileReaderThai);

            String line;

            StringBuilder sb = new StringBuilder();
            int counter = 0;
            while ((line = buffReaderThai.readLine()) != null) {
                if (line.trim().length() > 0) {
                    sb.append(line + "\n");
                    counter++;
                }
            }

            StringBuilder sb2 = new StringBuilder(counter);

            /*
            //makes "english" just be an index number, for testing purposes only
            
            for (int i=0; i<counter; i++) {
                sb2.append(i + "\n");
            }*/
            ret = buildBasicParse(sb.toString(), sb2.toString());

            buffReaderThai.close();
            ret.setFileName(makeFileNameFromPath(filePath));
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + filePath + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + filePath + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }

        return ret;
    }

    /**
     * Creates a BasicFile from two lists of Strings. The file created
     * represents a translated file, so all Segments are committed.
     *
     * The two lists are assumed to correspond (of course). For example, the 3rd
     * English string should be the translation of the 3rd Thai string. This
     * method of course has no way of verifying this, of course. If one list is
     * longer than the other, the returned file will only contain Segments up to
     * the shorter of the lists and the remaining Strings in the longer list are
     * discarded.
     *
     * @param thaiSegments
     * @param englishSegments
     * @return
     */
    public static BasicFile fromArrayLists(ArrayList<String> thaiSegments, ArrayList<String> englishSegments) {

        BasicFile bf = new BasicFile();

        // creates iterators for each list of segments
        Iterator<String> iterThai = thaiSegments.iterator();
        Iterator<String> iterEnglish = englishSegments.iterator();

        // iterates down each list, adding them together as TUs
        while (iterThai.hasNext() && iterEnglish.hasNext()) {
            SegmentBuilder sb = new SegmentBuilder(bf);
            sb.setThai(iterThai.next());
            sb.setEnglish(iterEnglish.next());
            sb.setCommitted(true);
            bf.addSeg(sb.createSegment());
        }

        return bf;
    }

    private static ArrayList<String> removeWhiteSpace(ArrayList<String> list) {
        ArrayList<String> ret = new ArrayList(list.size());
        for (String str : list) {
            if (str.trim().length() > 0) {
                str = str.trim();
                ret.add(str);
            }
        }
        return ret;
    }

    public static String makeFileNameFromPath(String filePath) {
        String fileName = "default";
        if (filePath != null) {
            String[] filePathSplit = filePath.split("/");
            fileName = filePathSplit[filePathSplit.length - 1];
        }
        return fileName;
    }

    public static BasicFile parseTMX(String filePath) {
        BasicFile bf = new BasicFile();
        bf.setFileName(makeFileNameFromPath(filePath));

        TMXResourceBundle thaiBundle = new TMXResourceBundle(
                filePath,
                "th-TH");

        TMXResourceBundle engBundle = new TMXResourceBundle(
                filePath,
                "en-US");

        for (String s : thaiBundle.keySet()) {
            if (engBundle.containsKey(s)) {
                SegmentBuilder sb = new SegmentBuilder(bf);
                sb.setThai(thaiBundle.getString(s));
                sb.setEnglish(engBundle.getString(s));
                sb.setCommitted(true);
                bf.addSeg(sb.createSegment());

            }
        }

        return bf;
    }

}
