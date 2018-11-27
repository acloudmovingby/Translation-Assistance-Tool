  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParseThaiLaw;

import Files.BasicFile;
import Files.FileBuilder;
import static Files.FileBuilder.makeFileNameFromPath;
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

    String fileNameThai;
    
    private int sectionNumsWrong;
    private int sectionLengthsWrong;
    private int thaiSecsSkipped;
    private int engSecsSkipped;
    private static String sectionNumsAnalysis;
    private static String sectionLengthsAnalysis;
    private static String skippedSectionsAnalysis;
    private ArrayList<String> finalThaiSegs;
    private ArrayList<String> finalEngSegs;

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
    public ThaiLawParser(String fileNameThai, String fileNameEng) {
        this.fileNameThai = fileNameThai;
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
            //System.out.println(sectionNumsAnalysis);

            System.out.println("//////////////SECTION LENGTH MIS-MATCHES////////////");
            ArrayList<ArrayList<String>> thaiSectionsParsed = parseWithinSections(thaiSegments);
            ArrayList<ArrayList<String>> engSectionsParsed = parseWithinSections(engSegments);
            sectionLengthsWrong = checkSectionParsing(thaiSectionsParsed, engSectionsParsed);
            //System.out.println(sectionLengthsAnalysis);

            System.out.println("//////////////SKIPPED SECTIONS////////////");
            thaiSecsSkipped = checkSectionSkips(thaiSegments);
            engSecsSkipped = checkSectionSkips(engSegments);
            System.out.println(skippedSectionsAnalysis);

            ArrayList<String> thaiSegments2 = skippedSectionCorrecter(thaiSegments);
            ArrayList<String> engSegments2 = skippedSectionCorrecter(engSegments);
            int thaiSecsSkipped2 = checkSectionSkips(thaiSegments2);
            int engSecsSkipped2 = checkSectionSkips(engSegments2);
            
            ArrayList<ArrayList<String>> thaiSectionsParsed2 = parseWithinSections(thaiSegments2);
            //System.out.println(thaiSectionsParsed2);
            /*for (ArrayList<String> al : thaiSectionsParsed2) {
                String indent = "";
                for (String s : al) {
                    System.out.println(indent + s);
                    indent = "\t-";
                }
            }*/
            ArrayList<ArrayList<String>> engSectionsParsed2 = parseWithinSections(engSegments2);
            int sectionNumsWrong2 = checkSectionMatching(thaiSegments2, engSegments2);
            int sectionLengthsWrong2 = checkSectionMatching(thaiSegments2, engSegments2);
            System.out.println("\n//////////////SECTION NUMBER MIS-MATCHES 2////////////");
            System.out.println(sectionNumsAnalysis);
            System.out.println("//////////////SECTION LENGTH MIS-MATCHES 2////////////");
            //System.out.println(sectionLengthsAnalysis.substring(sectionLengthsAnalysis.length()/2));
            System.out.println("//////////////SKIPPED SECTIONS 2////////////");
            System.out.println(skippedSectionsAnalysis);

            System.out.println("///////////////ANALYSIS////////////////");

            System.out.println("Original Thai section count: " + thaiSegments.size());
            System.out.println("Original English section count: " + engSegments.size());

            System.out.println("\nORIGINAL PARSE");
            System.out.println("\tThai sections skipped by 1: \t\t" + thaiSecsSkipped);
            System.out.println("\tEnglish sections skipped by 1: \t\t" + engSecsSkipped);
            System.out.println("\tSection # mis-matches: \t\t\t" + sectionNumsWrong);
            System.out.println("\tSection length mis-matches: \t\t" + sectionLengthsWrong);

            System.out.println("\nAFTER CORRECTING FOR SKIPS");
            System.out.println("\tThai sections skipped by 1: \t\t" + thaiSecsSkipped2);
            System.out.println("\tEnglish sections skipped by 1: \t\t" + engSecsSkipped2);
            System.out.println("\tSection # mis-matches: \t\t\t" + sectionNumsWrong2);
            System.out.println("\tSection length mis-matches: \t\t" + sectionLengthsWrong2);
            
            finalThaiSegs = thaiSegments2;
            finalEngSegs = engSegments2;
           // finalThaiSegs = this.unpackSections(parseWithinSections(thaiSegments2));
           // finalEngSegs =  this.unpackSections(parseWithinSections(engSegments2));

            /*
            System.out.println(sectionNumsAnalysis);
            System.out.println(sectionLengthsAnalysis);
            System.out.println(skippedSectionsAnalysis);
             */
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

    /**
     * Checks to see if a section number was skipped (i.e. went from Section 24 to 26). Does not correct these errors, but sets variable skippedSectionAnalysis.
     * @param segments After first parse, before parse within sections. 
     * @return The number of skipped sections.
     */
    private static int checkSectionSkips(ArrayList<String> segments) {

        int priorNum = -37;
        Iterator<String> iter = segments.iterator();
        int numSecSkipped = 0;
        StringBuilder sb = new StringBuilder();

        while (iter.hasNext()) {
            String s = iter.next();
            if (getSectionNumber(s) != null) {
                int curNum = Integer.parseInt(getSectionNumber(s));

                if (curNum - priorNum == 2) {
                    numSecSkipped++;
                    if (s.startsWith("มาตรา")) {
                        sb.append("มาตรา ").append(curNum - 1).append(" possibly skipped\n");
                        // System.out.println("มาตรา " + (curNum - 1) + " possibly skipped");
                    } else if (s.startsWith("Section")) {
                        sb.append("Section ").append(curNum - 1).append(" possibly skipped\n");
                        //System.out.println("Section " + (curNum - 1) + " possibly skipped");
                    }

                }
                priorNum = curNum;
            }
        }
        skippedSectionsAnalysis = sb.toString();
        return numSecSkipped;
    }

    /**
     * Tries to make a new entry for a skipped section if it can be found. Specifically, if a section number skips (say from 24 to 26), it looks in the prior section to see if the skipped section is in there and the OCR didn't add a line break properly. (i.e. it looks for Section 25 near the end of what was previously the Section 24 segment).
     * @param input A list of sections 
     * @return The list but with as many skipped sections unpacked as could be found. 
     */
    static ArrayList<String> skippedSectionCorrecter(ArrayList<String> input) {
        ArrayList<String> ret = new ArrayList(input.size());

        int priorNum = -37;
        String sPrior = "";
        Iterator<String> iter = input.iterator();

        while (iter.hasNext()) {
            String s = iter.next();
            if (getSectionNumber(s) != null) {
                int curNum = Integer.parseInt(getSectionNumber(s));
                //System.out.println("curNum: " + curNum + "   priorNum: " + priorNum);

                if (curNum - priorNum == 2) {
                    int index = -1;
                    if (s.startsWith("มาตรา")) {
                        // find prior instance of มาตรา. If it doesn't exist, index=-1
                        index = sPrior.lastIndexOf("มาตรา");
                    } else if (s.startsWith("Section")) {
                        // find prior instance of Section. If it doesn't exist, index=-1
                        index = sPrior.lastIndexOf("Section");
                    }

                    // if there is a section hidden in sPrior, it extract number
                    int foundNum = -217;
                    if (index != -1) {
                        String sectionNum = getSectionNumber(sPrior.substring(index));
                        foundNum = Integer.parseInt(sectionNum);
                    }

                    // if that section is one away from current num, it then splits the prior section into two pieces and then also adds the current section
                    if (foundNum + 1 == curNum) {
                        ret.remove(ret.size() - 1);
                        ret.add(sPrior.substring(0, index));
                        ret.add(sPrior.substring(index));
                    }
                    ret.add(s);
                } else {
                    ret.add(s);
                }
                sPrior = s;
                priorNum = curNum;
            }
        }
        return ret;
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

        StringBuilder sb = new StringBuilder();

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
            sb.append(thInt).append(": \t").append(isMatching).append("\n");
            //System.out.println(thInt + ": \t" + isMatching);
            if (isMatching == false) {

                sb.append("\tThai section # = \"").append(thInt).append("\"\n");
                sb.append("\t\t").append(th).append("\n");
                //System.out.println("\tThai section # = " + "\"" + thInt + "\"");
                sb.append("\tEnglish section # = \"").append(enInt).append("\"\n");
                sb.append("\t\t").append(en).append("\n");
                //System.out.println("\tEnglish section # = " + "\"" + enInt + "\"");
                adjustment = enInt - thInt;
                numWrong++;
            }
        }
        sectionNumsAnalysis = sb.toString();
        return numWrong;
    }

    /**
     * <h2>Gets Section Number</h2>
     * Returns section number from a line of Thai law if that line begins with
     * the words "Section" or "มาตรา". Thai numerals are converted to western
     * numerals (0-9). <br><br>
     * Returns null in the following cases:
     * <ul>
     * <li>The line does not begin with "Section" or "มาตรา" </li>
     * <li>There is no number after the words "Section"/"มาตรา" in the language
     * expected.</li>
     * </ul> <br>
     * The number returned will be the digits after the words "Section"/"มาตรา"
     * until a whitespace or non-digit character is found.
     * <br><br><b>Special cases:</b>
     * <ul>
     * <li>If a "/" is found then the digits after / are returned: </li>
     * <ul><li>"Section 193/3"returns "3" </li></ul>
     * <li>If the line begins in one language, only the digits in that language
     * will be returned.</li>
     * <ul>
     * <li>"Section ๑๑" returns null </li>
     * <li>"มาตรา 18" returns null </li>
     * <li>"มาตรา ๑๒8" returns "12"</li>
     * </ul>
     * <li>ส and @ are understood to be ๙ (9) and ๑ (1) respectively as these
     * are common OCR mistakes </li>
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

            boolean isNotTranslatedSection;
            isNotTranslatedSection = trimmed.matches("Section \\d+ Section.*");

            // if it starts a new section, it creates a new segment in enSegs
            if (startsSection) {
                
                    enSegs.add(sb.toString());
                    sb = new StringBuilder();

                    /*if the Section has content (i.e. was translated), appends the line to 
                    a new StringBuilder along with the next line. Otherwise, it breaks up the line
                    into several segments to add to enSegs;
                    */
                    if (!isNotTranslatedSection) {
                        /* for first file I made do it this way:
                sb.append(line).append(" ").append(buffReaderEng.readLine()).append(buffReaderEng.readLine());
                     */
                    // for every other file use this:
                        
                        sb.append(line).append(" ").append(buffReaderEng.readLine());
                    } else {
                        /* splits into substrings wherever you have this: "Section ### Section"
                        deals with issue of OCR seeing blank sections and putting them
                        on one line. 
                        */
                        //sb.append(line).append(" ").append(buffReaderEng.readLine());
                       String[] sa = trimmed.split("(?=(Section \\d+ Section))");
                        enSegs.addAll(Arrays.asList(sa));
                        String lastLine = enSegs.remove(enSegs.size()-1);
                        enSegs.addAll(Arrays.asList(lastLine.split("(?=(Section))")));
                    }
                
            } // if it is not a page number of footer,etc. then it adds line to current segment
            else if (!isPageNum && !isFooterEtc) {
                // if there is a page break (\f), then this checks to see if it begins with a special word (book, title, section, etc.). If not, it appends this line to the prior one. Generally solves more problems than it causes.
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
        StringBuilder sb = new StringBuilder();

        int numWrong = 0;

        while (thIter.hasNext() && enIter.hasNext()) {

            ArrayList<String> thSec = thIter.next();
            ArrayList<String> enSec = enIter.next();

            boolean isLengthEqual = (thSec.size() == enSec.size());

            if (!isLengthEqual) {
                sb.append("************************************\n");
                //System.out.println("************************************");
            }
            sb.append(thSec.get(0)).append(" : ").append(isLengthEqual).append("\n");
            //System.out.println(getSectionNumber(thSec.get(0)) + " : " + isLengthEqual);
            if (!isLengthEqual) {
                numWrong++;
                sb.append(thSec.size()).append(" ").append(enSec.size()).append("\n");
                //System.out.println(thSec.size() + " " + enSec.size());
                for (String s : thSec) {
                    sb.append(s).append("\n\n");
                    //System.out.println(s + "\n");
                }
                for (String s : enSec) {
                    sb.append(s).append("\n\n");
                    //System.out.println(s + "\n");
                }
                sb.append("************************************\n");
                //System.out.println("************************************");
            }
        }

        if (thIter.hasNext()) {
            // if thai sections has remaining elements
        } else {
            // if eng sections has remaining elements
        }

        sectionLengthsAnalysis = sb.toString();
        return numWrong;

    }
    
    // Takes the nested arraylists and turns them into a single arrayList
    private ArrayList<String> unpackSections(ArrayList<ArrayList<String>> arrayList) {
        ArrayList<String> ret = new ArrayList(arrayList.size());
        for (ArrayList<String> al : arrayList) {
            for (String s : al) {
                ret.add(s);
            }
        }
        return ret;
    }
    
    public BasicFile makeFile() {
        BasicFile file = FileBuilder.fromArrayLists(finalThaiSegs, finalEngSegs);
        file.setFileName(makeFileNameFromPath(fileNameThai));
         return file;
    }

}
