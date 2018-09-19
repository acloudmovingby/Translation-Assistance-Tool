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
import java.util.Iterator;

/**
 *
 * @author Chris
 */
public class ParseThaiLaw {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileNameThai = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleThaiLaw1.txt";
        String fileNameEng = "/Users/Chris/Desktop/Docs/Documents/Personal/Coding/Non-website design/Thai Parser Project/CAT1/src/CAT1/SampleEnglishLaw1.txt";

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
            /*
            New plan: 
                every line break to a new text (not just whitespace), add to segment list
                do for both Thai and english
                iterate through lists and check to see if a section
                    if a section, then check to see if other is a section and numbers match
            
            Revised Plan:
                break up into sections as before (because it's easier to match)
                For English, delete separator between "section" and text
                Trim whitespace for each section
                iterate through both lists and see if # of linebreaks is different/same
                line counter: 
                    int lineCount = string.chars().filter(x -> x == '\n').count() + 1;
             */

            //Stores all THAI มาตรา sections in Arraylist
            ArrayList<String> thaiSegments = new ArrayList(40);
            String line;

            StringBuilder sb = new StringBuilder();
            while ((line = buffReaderThai.readLine()) != null) {
                if (line.trim().startsWith("มาตรา")) {
                    thaiSegments.add(sb.toString());
                    sb = new StringBuilder();
                    sb.append(line);
                } else {
                    sb.append(line);
                }
            }

            ArrayList<String> engSegments = new ArrayList(40);
            while ((line = buffReaderEng.readLine()) != null) {
                if (line.trim().startsWith("Section")) {
                    engSegments.add(sb.toString());
                    sb = new StringBuilder();
                    sb.append(line).append(" ").append(buffReaderEng.readLine()).append(buffReaderEng.readLine());
                    //System.out.println(sb.append("\n\n"));
                } else {
                    sb.append(line).append("");
                }
            }

            thaiSegments = thaiCleanUp(thaiSegments);

            engSegments = engCleanUp(engSegments);

            checkSectionMatching(thaiSegments, engSegments);

            Iterator thaiIter = thaiSegments.iterator();
            Iterator engIter = engSegments.iterator();
            int count = 0;
            while (thaiIter.hasNext() && engIter.hasNext()) {
                System.out.println(count + "\n");
                System.out.println(thaiIter.next());
                System.out.println(engIter.next());
                count++;
            }

            // Always close files.
            buffReaderThai.close();
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

    private static ArrayList<String> thaiCleanUp(ArrayList<String> thaiSegments) {
        ArrayList<String> ret;
        ret = removeWhiteSpace(thaiSegments);
        return ret;
    }

    private static ArrayList<String> engCleanUp(ArrayList<String> engSegments) {
        ArrayList<String> ret;
        ret = removeWhiteSpace(engSegments);

        /*
        ArrayList<String> ret2 = new ArrayList(ret.size());
        for (String str : ret) {
            String str2;
            if (str.startsWith("Section") && str.length()<=13) {
                
            }
        } */
        return ret;
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

    private static void checkSectionMatching(ArrayList<String> thaiSegments, ArrayList<String> engSegments) {
        Iterator thaiIter = thaiSegments.iterator();
        Iterator engIter = engSegments.iterator();
        int adjustment = 0;
        while (thaiIter.hasNext() && engIter.hasNext()) {
            String th = (String) thaiIter.next();
            String en = (String) engIter.next();

            int thInt = getSectionNumber(th);
            int enInt = getSectionNumber(en);
            boolean isMatching = thInt + adjustment == enInt ;
            System.out.println(thInt + ": \t" + isMatching);
            if (isMatching == false) {
                System.out.println("\tThai section # = " + thInt);
                System.out.println("\tEnglish section # = " + enInt);
                adjustment = enInt-thInt;
            }
        }
    }

    private static int getSectionNumber(String foo) {

        if (foo.startsWith("Section")) {
            foo = foo.substring(8, 17);
            for (int i = 0; i < foo.length(); i++) {
                if (foo.charAt(i) == ' ') {
                    return Integer.parseInt(foo.substring(0, i));
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
            return -1;
        }
        return -1;
    }

    private static int thaiNumeralConverter(String thaiNumeral) {
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
                default:
                    throw new IllegalArgumentException("Must be a valid Thai numeral with no whitespace. Input was:" + thaiNumeral);
            }
        }
        return Integer.parseInt(sb.toString());
    }

}
