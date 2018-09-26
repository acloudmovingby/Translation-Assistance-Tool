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
            ArrayList<String> thaiSegments = segmentThaiFile(buffReaderThai);
            ArrayList<String> engSegments = segmentEngFile(buffReaderEng);
            checkSectionMatching(thaiSegments, engSegments);
            /*
            engSegments.forEach((s) -> {
                System.out.println(s+"\n");
            });*/
            
            ArrayList<ArrayList<String>> thaiSectionsParsed = parseSections(thaiSegments);
            ArrayList<ArrayList<String>> engSectionsParsed = parseSections(engSegments);
            
            checkSectionParsing(thaiSectionsParsed, engSectionsParsed);
            
            
            buffReaderThai.close();
            buffReaderEng.close();
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

    public static String thaiNumeralConverter(String thaiNumeral) {
        thaiNumeral = thaiNumeral.trim();
        StringBuilder sb = new StringBuilder(thaiNumeral.length());
        for (int i = 0; i < thaiNumeral.length(); i++) {
            //System.out.println("\"" + thaiNumeral.charAt(i) + "\"");
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
                    sb.append(thaiNumeral.charAt(i));
            }
        }
        return sb.toString();
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

    /**
     * Checks to see if the section numbers match.
     *
     * @param thaiSegments
     * @param engSegments
     */
    public static void checkSectionMatching(ArrayList<String> thaiSegments, ArrayList<String> engSegments) {

        Iterator thaiIter = thaiSegments.iterator();
        Iterator engIter = engSegments.iterator();
        int adjustment = 0;

        while (thaiIter.hasNext() && engIter.hasNext()) {
            String th = (String) thaiIter.next();
            String en = (String) engIter.next();

            if (th == null || en == null) {
                System.out.println("wtf?");
            }

            int thInt = sectionNumToInt(getSectionNumber(th));
            int enInt = sectionNumToInt(getSectionNumber(en));

            boolean isMatching = (thInt + adjustment == enInt);
            System.out.println(thInt + ": \t" + isMatching);
            if (isMatching == false) {
                System.out.println("\tThai section # = " + "\"" + thInt + "\"");
                System.out.println("\tEnglish section # = " + "\"" + enInt + "\"");
                adjustment = enInt - thInt;
            }
        }
    }

    /**
     * Extract the Section number from a line (Thai or English) and returns as a
     * String.
     *
     * @param foo
     * @return
     */
    private static String getSectionNumber(String foo) {

        if (foo.startsWith("Section")) {
            foo = foo.substring(8, 17);
            for (int i = 0; i < foo.length(); i++) {
                if (foo.charAt(i) == ' ') {
                    return foo.substring(0, i);
                }
            }
        } else if (foo.startsWith("มาตรา")) {
            foo = foo.substring(6, 15).trim();
            for (int i = 0; i < foo.length(); i++) {
                if (foo.charAt(i) == ' ') {
                    return thaiNumeralConverter(foo.substring(0, i));
                }
            }

        } else {
            return null;
        }
        return null;
    }

    /**
     * Converts an English section number to an int. If the section number is
     * formatted like 193/3, then it returns 3.
     *
     * @param sectionNumber The section number in English
     * @return The section number converted to int.
     */
    private static int sectionNumToInt(String sectionNumber) {

        if (sectionNumber == null) {
            return -1;
        }

        StringBuilder sb = new StringBuilder(sectionNumber.length());
        for (int i = 0; i < sectionNumber.length(); i++) {
            char c = sectionNumber.charAt(i);
            if (c != '/') {
                sb.append(c);
            } else {
                sb = new StringBuilder();
            }
        }
        return Integer.parseInt(sb.toString());
    }

    private static ArrayList<String> segmentThaiFile(BufferedReader buffReaderThai) throws IOException {
        ArrayList<String> thSegs = new ArrayList(40);
        String line;

        StringBuilder sb = new StringBuilder();
        while ((line = buffReaderThai.readLine()) != null) {
            if (line.trim().startsWith("มาตรา")) {
                thSegs.add(sb.toString());
                sb = new StringBuilder();
                sb.append(line);
            } 
            // deals with the headers and footers from website that provided pdf
            else if (!line.trim().startsWith("www.ThaiLaws.com")
                    && !line.trim().startsWith("WWW.Thai Laws, com")) {
                sb.append(line);
            }
        }
        thSegs.add(sb.toString());
        thSegs = removeWhiteSpace(thSegs);
        return thSegs;
    }

    private static ArrayList<String> segmentEngFile(BufferedReader buffReaderEng) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        ArrayList<String> enSegs = new ArrayList(40);
        while ((line = buffReaderEng.readLine()) != null) {
            
            String trimmed = line.trim();
            if (trimmed.startsWith("Section")) {
                enSegs.add(sb.toString());
                sb = new StringBuilder();
                sb.append(line).append(" ").append(buffReaderEng.readLine()).append(buffReaderEng.readLine());
                //System.out.println(sb.append("\n\n"));
            } 
            // deals with page numbers (could cause problems for very short lines)
            // deals with the headers and footers from website that provided pdf
            // if it isn't one of these unwanted lines, then it appends the line to sb
            else if (!trimmed.startsWith("www.ThaiLaws.com")
                    && !trimmed.startsWith("WWW.Thai Laws, com")
                    && !trimmed.startsWith("Thailand Civil and Commercial Code")
                    && !(trimmed.length() <=3 && trimmed.length()>0)) {
                
                sb.append(line).append("\n");
            } 
        }
        enSegs.add(sb.toString());
        enSegs = removeWhiteSpace(enSegs);
        return enSegs;
    }

    private static ArrayList<String> engCleanUp(ArrayList<String> engSegments) {

        /*
        takes care of case where the OCR didn't add a linebreak before a section
        If a section number jumps up by 2, it backtracks to see if there is an intermediate section in between that didn't have a linebreak. If not, it just leaves the segmentation as is.
        
        ArrayList<String> newList = new ArrayList(ret.size());
        Iterator iter = ret.iterator();
        int prior = -20;
        String priorSeg = "";
        while (iter.hasNext()) {
            
            String curSeg = (String) iter.next();
            int cur = getSectionNumber(curSeg);
            
            // if the section # went up by 2, it tries to find it.
            if (cur - prior == 2) {
                int whereIsSection = priorSeg.lastIndexOf("Section " + (cur-1));
                if (whereIsSection != -1) {
                    newList.remove(newList.size()-1);
                    newList.add(priorSeg.substring(0, whereIsSection));
                    newList.add(priorSeg.substring(whereIsSection));
                } 
            } 
            newList.add(curSeg);
            prior = cur;
            priorSeg = curSeg;
        }*/

 /*
        ArrayList<String> ret2 = new ArrayList(ret.size());
        for (String str : ret) {
            String str2;
            if (str.startsWith("Section") && str.length()<=13) {
                
            }
        } */
        throw new UnsupportedOperationException();
    }

    private static ArrayList<ArrayList<String>> parseSections(ArrayList<String> segments) {
        
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
    
    private static void checkSectionParsing(ArrayList<ArrayList<String>> thaiSectionsParsed, ArrayList<ArrayList<String>> engSectionsParsed) {
        
        Iterator<ArrayList<String>> thIter = thaiSectionsParsed.iterator();
        Iterator<ArrayList<String>> enIter = engSectionsParsed.iterator();

        while (thIter.hasNext() && enIter.hasNext()) {
           
            ArrayList<String> thSec = thIter.next();
            ArrayList<String> enSec = enIter.next();
            
            boolean isLengthEqual = (thSec.size() == enSec.size());
            
            System.out.println(getSectionNumber(thSec.get(0)) + " : " + isLengthEqual);
            if (!isLengthEqual) {
                for (String s : thSec) {
                    System.out.println("\t"+s);
                }
                for (String s : enSec) {
                    System.out.println("\t"+s);
                }
            }
        }
        
        if (thIter.hasNext()) {
            // if thai sections has remaining elements
        } else {
            // if eng sections has remaining elements
        }
        
       
        
    }
}
