/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParseThaiLaw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Chris
 */
public class ThaiLawParser {


    int sectionNumsWrong;
    int sectionLengthsWrong;
    int thaiSecsSkipped;
    int engSecsSkipped;

    /*METHODS:
        -section num extractor, with defined cases for behavior (string->string)
            Section 187 -> 187
            Section 178/8 -> 8 (special case)
            Section 178[8] -> 178 (anything not an English numeral terminates)
            มาตรา ๔๕๖ -> ๔๕๖
            มาตรา ๔๕๖/๗ -> ๗ (special case)
            มาตรา ๔๕๖7[๗] -> ๔๕๖ (anything not an English numeral terminates)
        - thai numeral converter (string->string)
            ๔๕๖ -> 456
            anthing besides thai numerals -> returns -1
        - string to int converter (string->int)
            literally just Integer.parseint(string)
     */
    ThaiLawParser(String fileNameThai, String fileNameEng) {
        try {

            // FileReader reads text files in the default encoding.
            FileReader fileReaderThai
                    = new FileReader(fileNameThai);

            BufferedReader buffReaderThai
                    = new BufferedReader(fileReaderThai);

            FileReader fileReaderEng
                    = new FileReader(fileNameEng);

            BufferedReader buffReaderEng
                    = new BufferedReader(fileReaderEng);

            // parses files by section (มาตรา)
            ArrayList<String> thaiSegments = firstParseThaiFile(buffReaderThai);
            ArrayList<String> engSegments = firstParseEngFile(buffReaderEng);
            buffReaderThai.close();
            buffReaderEng.close();
            
            System.out.println("//////////////SECTION NUMBER MIS-MATCHES////////////");
            sectionNumsWrong = checkSectionMatching(thaiSegments, engSegments);
            
            
            System.out.println("//////////////SECTION LENGTH MIS-MATCHES////////////");
            ArrayList<ArrayList<String>> thaiSectionsParsed = parseWithinSections(thaiSegments);
            ArrayList<ArrayList<String>> engSectionsParsed = parseWithinSections(engSegments);
            sectionLengthsWrong = checkSectionParsing(thaiSectionsParsed, engSectionsParsed);
            
            System.out.println("//////////////SKIPPED SECTIONS////////////");
            
            thaiSecsSkipped = skippedSectionCounter(thaiSegments);
            engSecsSkipped = skippedSectionCounter(engSegments);
            
            System.out.println("\t/////////////ANALYSIS////////////////");
            System.out.println("Thai sections skipped by 1: \t\t" + thaiSecsSkipped);
            System.out.println("English sections skipped by 1: \t\t" + engSecsSkipped);
            System.out.println("Section # mis-matches: \t\t\t" + sectionNumsWrong);
            System.out.println("Section length mis-matches: \t\t" + sectionLengthsWrong);
            

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '"
                    + fileNameThai + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                    + fileNameThai + "'");
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }

    /**
     * Converts all the Thai numerals in a string to Arabic numerals (0-9) ® is
     * interpreted as 1 and ส is interpreted as 9 (common OCR mistake)
     *
     * @param thaiNumeral A string which may contain Thai numeral characters
     * @return The same string with all the Thai numerals converted to 0-9
     */
    public static String thaiNumeralConverter(String thaiNumeral) {
        thaiNumeral = thaiNumeral.trim();
        StringBuilder sb = new StringBuilder(thaiNumeral.length());
        for (int i = 0; i < thaiNumeral.length(); i++) {
            switch (thaiNumeral.charAt(i)) {
                case '๐':
                    sb.append('0');
                    break;
                case '๑':
                    sb.append('1');
                    break;
                case '๒':
                    sb.append('2');
                    break;
                case '๓':
                    sb.append('3');
                    break;
                case '๔':
                    sb.append('4');
                    break;
                case '๕':
                    sb.append('5');
                    break;
                case '๖':
                    sb.append('6');
                    break;
                case '๗':
                    sb.append('7');
                    break;
                case '๘':
                    sb.append('8');
                    break;
                case '๙':
                    sb.append('9');
                    break;
                // common OCR ERRORS
                case 'ส':
                    sb.append(9);
                    break;
                case '®':
                    sb.append(1);
                    break;
                default:
                    throw new IllegalArgumentException("This character is not a Thai numeral: " + thaiNumeral.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param list An ArrayList of Strings
     * @return An ArrayList with empty strings removed and each string's leading
     * and trailing whitespace removed-->string.trim()
     */
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

    private static int skippedSectionCounter(ArrayList<String> segments) {

        int priorNum = -37;
        Iterator<String> iter = segments.iterator();
        int numSecSkipped = 0;

        while (iter.hasNext()) {
            String s = iter.next();
            if (getSectionNumber(s) != null) {
                int curNum = Integer.parseInt(getSectionNumber(s));
                //System.out.println("curNum: " + curNum + "   priorNum: " + priorNum);

                if (curNum - priorNum == 2) {
                    numSecSkipped++;
                    if (s.startsWith("มาตรา")) {
                        System.out.println("มาตรา " + (curNum - 1) + " possibly skipped");
                    } else if (s.startsWith("Section")) {
                        System.out.println("Section " + (curNum - 1) + " possibly skipped");
                    }

                }
                priorNum = curNum;
            }
        }

        return numSecSkipped;
    }
    
    
    static ArrayList<String> skippedSectionCorrecter(ArrayList<String> input) {
        ArrayList<String> ret = new ArrayList(input.size());
        
        int priorNum = -37;
        String priorSec = "";
        Iterator<String> iter = input.iterator();

        while (iter.hasNext()) {
            String s = iter.next();
            if (getSectionNumber(s) != null) {
                int curNum = Integer.parseInt(getSectionNumber(s));
                //System.out.println("curNum: " + curNum + "   priorNum: " + priorNum);

                if (curNum - priorNum == 2) {
                    int index;
                    if (s.startsWith("มาตรา")) {
                        // find prior instance of มาตรา, split
                        index = s.lastIndexOf("มาตรา");
                    } else if (s.startsWith("Section")) {
                        index = s.lastIndexOf("Section");
                    }
                } else {
                    ret.add(s);
                }
                priorNum = curNum;
            }
        }

        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Checks to see if the section numbers match.
     *
     * @param thaiSegments
     * @param engSegments
     * @return The number of Sections that don't have matching numbers
     */
    static int checkSectionMatching(ArrayList<String> thaiSegments, ArrayList<String> engSegments) {

        int numWrong = 0;
        Iterator thaiIter = thaiSegments.iterator();
        Iterator engIter = engSegments.iterator();
        int adjustment = 0;

        while (thaiIter.hasNext() && engIter.hasNext()) {
            String th = (String) thaiIter.next();
            String en = (String) engIter.next();

            int thInt;
            int enInt;

            if (getSectionNumber(th) != null) {
                thInt = Integer.parseInt(getSectionNumber(th));
            } else {
                thInt = -1;
            }

            if (getSectionNumber(en) != null) {
                enInt = Integer.parseInt(getSectionNumber(en));
            } else {
                enInt = -1;
            }

            boolean isMatching = (thInt + adjustment == enInt);
            System.out.println(thInt + ": \t" + isMatching);
            if (isMatching == false) {
                System.out.println("\tThai section # = " + "\"" + thInt + "\"");
                System.out.println("\tEnglish section # = " + "\"" + enInt + "\"");
                adjustment = enInt - thInt;
                numWrong++;
            }
        }
        return numWrong;
    }

    /**
     * <h2>Gets Section Number</h2>
     * Returns section number from a line of Thai law if that line begins with the words
     * "Section" or "มาตรา". Thai numerals are converted to western numerals
     * (0-9). <br><br>
     * Returns null in the following cases: 
     * <ul>
     * <li>The line does not begin with "Section" or "มาตรา" </li>
     * <li>There is no number after the words "Section"/"มาตรา" in the language expected.</li>
     *</ul> <br>
     * The number returned will be the digits after the words "Section"/"มาตรา"
     * until a whitespace or non-digit character is found. 
     * <br><br><b>Special cases:</b> 
     * <ul>
     * <li>If a "/" is found then the digits after / are returned: </li>
     * <ul><li>"Section 193/3"returns "3" </li></ul>
     * <li>If the line begins in one language, only the digits in that language will be returned.</li>
     * <ul>
     * <li>"Section ๑๑" returns null </li>
     * <li>"มาตรา 18" returns null </li>
     * <li>"มาตรา ๑๒8" returns "12"</li>
     * </ul>
     * <li>ส and @ are understood to be ๙ (9) and ๑ (1) respectively as these are common OCR mistakes </li>
     * <ul><li>"มาตรา ๑๒ส" returns "129"</li></ul>
     * </ul>
     *
     * <b>Other Examples:</b> 
     * <ul>
     * <li>"Section 193 ..." returns "193" </li>
     * <li>"มาตรา ๑๖๖ ..." returns "166" </li>
     * <li>"มาตรา ๑๖๖3๑ ..." returns "166" </li>
     * <li>"Section 178[8] ..." returns "178"</li>
     * </ul>
     *
     * @param foo A line of a Thai law
     * @return The substring of the line representing the law section number.
     */
    public static String getSectionNumber(String foo) {
        int end = Math.min(foo.length(), 16);
        String regex = "";
        String foo2;

        if (foo.startsWith("Section")) {
            foo2 = foo.substring(7, end);
            regex = "[0-9]";
        } else if (foo.startsWith("มาตรา")) {
            foo2 = foo.substring(5, end);
            // includes all Thai numerals 0-9, 
            // also ignores ส and ® symbols which are common OCR mistakes for 9 (๙) and 1 (๑) respectively. 
            regex = "[๑๒๓๔๕๖๗๘๙๐ส®]";
        } else {
            return null;
        }

        foo2 = foo2.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < foo2.length(); i++) {
            String c = String.valueOf(foo2.charAt(i));
            if (c.matches("/")) {
                sb = new StringBuilder();
            } else {
                if (c.matches(regex)) {
                    sb.append(c);
                } else {
                    break;
                }
            }

        }

        if (sb.length() == 0) {
            return null;
        } else {
            if (foo.startsWith("มาตรา")) {
                return thaiNumeralConverter(sb.toString());
            } else {
                return sb.toString();
            }

        }

        /*
        if (foo.startsWith("Section")) {
            end = Math.min(foo.length(), 16);
            foo = foo.substring(8, end);
            for (int i = 1; i < foo.length(); i++) {
                if (foo.charAt(i) == ' ') {
                    return foo.substring(0, i);
                }
            }
        } else if (foo.startsWith("มาตรา")) {
            foo = foo.substring(6, end).trim();
            for (int i = 0; i < foo.length(); i++) {
                if (foo.charAt(i) == ' ') {
                    if (i==0) {
                        return null;
                    }
                    return thaiNumeralConverter(foo.substring(0, i));
                }
            }

        } else {
            return null;
        }
        return null; */
    }

    /**
     * Converts an English section number to an int. If the section number is
     * formatted like 193/3, then it returns 3.
     *
     * @param sectionNumber The section number in English
     * @return The section number converted to int.
     */
    /*
    private static int sectionNumToInt(String sectionNumber) {

        if (sectionNumber == null) {
            return -1;
        }

        String num = sectionNumber.trim();

        String regex;
        String firstChar = String.valueOf(num.charAt(0));
        if (firstChar.matches("[0-9]")) {
            regex = "[0-9]";
        } else if (firstChar.matches("[๑๒๓๔๕๖๗๘๙๐]")) {
            regex = "[๑๒๓๔๕๖๗๘๙๐]";
        } else {
            return -1;
        }

        StringBuilder sb = new StringBuilder(num.length());
        for (int i = 0; i < num.length(); i++) {
            //char c = num.charAt(i);
            String c = String.valueOf(num.charAt(i));
            if (c.matches(regex)) {
                sb.append(c);
            } else {
                int hey = -13;
                break;
            }
        }
        return Integer.parseInt(sb.toString());
    }
     */
    private static ArrayList<String> firstParseThaiFile(BufferedReader buffReaderThai) throws IOException {
        ArrayList<String> thSegs = new ArrayList(40);
        String line;

        StringBuilder sb = new StringBuilder();
        // Checks to see if a line begins with a new law "Section" (in Thai: มาตรา)
        while ((line = buffReaderThai.readLine()) != null) {

            String trimmed = line.trim();
            boolean startsSection = trimmed.startsWith("มาตรา");
            boolean isPageNum = trimmed.matches("[0-9]+");
            boolean isFooterEtc = (trimmed.equalsIgnoreCase("www.ThaiLaws.com")
                    || trimmed.equalsIgnoreCase("WWW.Thai Laws, com"));

            boolean isPageBreak = line.startsWith("\f");
            String regex = ("(มาตรา|บรรพ|ส่วนที่|หมวด|ลักษณะ|\\(.*\\)).*");
            boolean isNewPart = (trimmed.matches(regex));

            // if it starts a new section, it starts a new String segment in thSegs
            if (startsSection) {
                thSegs.add(sb.toString());
                sb = new StringBuilder();
                sb.append(line);
            } // does not add line if it's a page number or is a footer from website
            else if (!(isPageNum || isFooterEtc)) {
                // if it could be a page break (there was an empty line detected), then this checks to see if it begins with a special word (book, title, section, etc.). If not, it appends this line to the prior one.

                if (!isNewPart && isPageBreak) {
                    sb.append(line);

                } else if (!isNewPart && line.length() < 20) {
                    sb.append(line);
                } else {
                    sb.append("\n").append(line);
                }
            }
        }
        thSegs.add(sb.toString());
        thSegs = removeWhiteSpace(thSegs);
        return thSegs;
    }

    private static ArrayList<String> firstParseEngFile(BufferedReader buffReaderEng) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        ArrayList<String> enSegs = new ArrayList(40);
        while ((line = buffReaderEng.readLine()) != null) {

            String trimmed = line.trim();
            boolean startsSection = trimmed.startsWith("Section");
            boolean isPageNum = (trimmed.length() <= 3 && trimmed.length() > 0);
            boolean isFooterEtc = (trimmed.startsWith("www.")
                    || trimmed.startsWith("WWW.")
                    || trimmed.startsWith("Thailand Civil and Commercial Code"));

            boolean isPageBreak = line.startsWith("\f");
            String regex = "(Section|BOOK|PART|CHAPTER|TITLE|\\(.*\\)).*";
            boolean isNewPart = (trimmed.matches(regex));

            // if it starts a new section, it creates a new segment in enSegs
            if (startsSection) {
                enSegs.add(sb.toString());
                sb = new StringBuilder();

                /* for first file I made do it this way:
                sb.append(line).append(" ").append(buffReaderEng.readLine()).append(buffReaderEng.readLine());
                 */
                // for every other file use this:
                sb.append(line).append(" ").append(buffReaderEng.readLine());

            } // if it is not a page number of footer,etc. then it adds line to current segment
            else if (!isFooterEtc && !isPageNum) {
                // if it could be a page break (there was an empty line detected), then this checks to see if it begins with a special word (book, title, section, etc.). If not, it appends this line to the prior one.

                if (isPageBreak && !isNewPart) {
                    sb.append(line);
                } else {
                    sb.append("\n").append(line);
                }
            }
        }
        enSegs.add(sb.toString());
        enSegs = removeWhiteSpace(enSegs);
        return enSegs;
    }

    private static ArrayList<ArrayList<String>> parseWithinSections(ArrayList<String> segments) {

        ArrayList<ArrayList<String>> ret = new ArrayList(segments.size());

        for (String s : segments) {
            String[] sa = s.split("\n");
            ArrayList<String> newSection = new ArrayList(sa.length);
            newSection.addAll(Arrays.asList(sa));
            newSection = removeWhiteSpace(newSection);
            ret.add(newSection);
        }
        return ret;
    }

    private static int checkSectionParsing(ArrayList<ArrayList<String>> thaiSectionsParsed, ArrayList<ArrayList<String>> engSectionsParsed) {

        Iterator<ArrayList<String>> thIter = thaiSectionsParsed.iterator();
        Iterator<ArrayList<String>> enIter = engSectionsParsed.iterator();

        int numWrong = 0;

        while (thIter.hasNext() && enIter.hasNext()) {

            ArrayList<String> thSec = thIter.next();
            ArrayList<String> enSec = enIter.next();

            boolean isLengthEqual = (thSec.size() == enSec.size());

            if (!isLengthEqual) {
                System.out.println("************************************");
            }
            System.out.println(getSectionNumber(thSec.get(0)) + " : " + isLengthEqual);
            if (!isLengthEqual) {
                numWrong++;
                System.out.println(thSec.size() + " " + enSec.size());
                for (String s : thSec) {
                    System.out.println(s + "\n");
                }
                for (String s : enSec) {
                    System.out.println(s + "\n");
                }
            }
            if (!isLengthEqual) {
                System.out.println("************************************");
            }
        }

        if (thIter.hasNext()) {
            // if thai sections has remaining elements
        } else {
            // if eng sections has remaining elements
        }

        return numWrong;

    }

}
