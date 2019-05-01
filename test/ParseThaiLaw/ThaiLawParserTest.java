/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ParseThaiLaw;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class ThaiLawParserTest {

    public ThaiLawParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of thaiNumeralConverter method, of class ThaiLawParser.
     */
    @Test
    public void testThaiNumeralConverter() {
        System.out.println("thaiNumeralConverter");
        String thaiNumeral = "";
        String expResult = "";
        String result = ThaiLawParser.thaiNumeralConverter(thaiNumeral);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSectionNumber method, of class ThaiLawParser.
     */
    @Test
    public void testGetSectionNumberReturnNull() {
        System.out.println("getSectionNumber");
        String foo = "";
        String expResult = null;
        String result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section";
        expResult = null;
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section ";
        expResult = null;
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section ๖45";
        expResult = null;
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา 45";
        expResult = null;
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

    }

    /**
     * Test of getSectionNumber method, of class ThaiLawParser.
     */
    @Test
    public void testGetSectionNumberEnglish() {
        System.out.println("getSectionNumber");

        String foo = "Section 4";
        String expResult = "4";
        String result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section 45";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section 45 ";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section 45[";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section 45[8";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section 45A";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section   45";
        expResult = "45";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section  1293 ";
        expResult = "1293";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "Section  1293/37";
        expResult = "37";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);
    }

    /**
     * Test of getSectionNumber method, of class ThaiLawParser.
     */
    @Test
    public void testGetSectionNumberThai() {
        System.out.println("getSectionNumber");

        String foo = "มาตรา ๑";
        String expResult = "1";
        String result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓ ";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓[";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓[8";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓A";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา   ๑๓";
        expResult = "13";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓๖๗๗";
        expResult = "13677";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๑๓๖๕/๓๖";
        expResult = "36";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);

        foo = "มาตรา ๖๔๘ อันการยืมใช้คงรูป ย่อมระงับ";
        expResult = "648";
        result = ThaiLawParser.getSectionNumber(foo);
        assertEquals(expResult, result);
    }

    /**
     * Test of skippedSectionCorrecter method, of class ThaiLawParser.
     */
    @Test
    public void testSkippedSectionCorrecterNothingChanges() {
        System.out.println("skippedSectionCorrecter");

        // empty strings
        ArrayList<String> input = new ArrayList();
        input.add("");
        input.add("");
        input.add("");
        ArrayList<String> expResult = new ArrayList();
        expResult.add("");
        expResult.add("");
        expResult.add("");
        ArrayList<String> result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // single section
        input = new ArrayList();
        input.add("Section 452 The law...");
        expResult = new ArrayList();
        expResult.add("Section 452 The law...");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // single section with blank section
        input = new ArrayList();
        input.add("Section 452 The law...");
        input.add("");
        expResult = new ArrayList();
        expResult.add("Section 452 The law...");
        expResult.add("");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        input = new ArrayList();
        input.add("");
        input.add("Section 452 The law...");
        expResult = new ArrayList();
        expResult.add("");
        expResult.add("Section 452 The law...");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // single section with non-section
        input = new ArrayList();
        input.add("The");
        input.add("Section 452 The law...");
        expResult = new ArrayList();
        expResult.add("The");
        expResult.add("Section 452 The law...");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        input = new ArrayList();
        input.add("Section 452 The law...");
        input.add("The");
        expResult = new ArrayList();
        expResult.add("Section 452 The law...");
        expResult.add("The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        //more than 1 skip
        input = new ArrayList();
        input.add("Section 452 The law...Section 457");
        input.add("Section 457 The");
        expResult = new ArrayList();
        expResult.add("Section 452 The law...Section 457");
        expResult.add("Section 457 The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        input = new ArrayList();
        input.add("ffjal;er");
        input.add("Section 452 The law...Section 457");
        input.add("Section 457 The");
        expResult = new ArrayList();
        expResult.add("ffjal;er");
        expResult.add("Section 452 The law...Section 457");
        expResult.add("Section 457 The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // Section mispelled
        input = new ArrayList();
        input.add("Section 450 ffjal;er Section 450 gaera");
        input.add("Section 45T The law...Section 457");
        input.add("Section 452 The");
        expResult = new ArrayList();
        expResult.add("Section 450 ffjal;er Section 450 gaera");
        expResult.add("Section 45T The law...Section 457");
        expResult.add("Section 452 The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // Section mispelled (THAI)
        input = new ArrayList();
        input.add("มาตรา ๔๕๐450 ffjal;er Section 450 gaera");
        input.add("มาตรา ๔๕) The law...Section 457");
        input.add("มาตรา ๔๕๒ The");
        expResult = new ArrayList();
        expResult.add("มาตรา ๔๕๐450 ffjal;er Section 450 gaera");
        expResult.add("มาตรา ๔๕) The law...Section 457");
        expResult.add("มาตรา ๔๕๒ The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // Subsection normal (THAI)
        input = new ArrayList();
        input.add("มาตรา ๔๕๐/๒450 ffjal;er Section 450 gaera");
        input.add("มาตรา ๔๕๐/๓ The law...Section 457");
        input.add("มาตรา ๔๕๐/๔ The");
        expResult = new ArrayList();
        expResult.add("มาตรา ๔๕๐/๒450 ffjal;er Section 450 gaera");
        expResult.add("มาตรา ๔๕๐/๓ The law...Section 457");
        expResult.add("มาตรา ๔๕๐/๔ The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // Subsection skipped 1 but does not exist (THAI)
        input = new ArrayList();
        input.add("มาตรา ๔๕๐/๒ 450 ffjal;er มาตรา ๔๕๐/๓๒ gaera");
        input.add("มาตรา ๔๕๐/๔ The law...Section 457");
        input.add("มาตรา ๔๕๐/๕ The");
        expResult = new ArrayList();
        expResult.add("มาตรา ๔๕๐/๒ 450 ffjal;er มาตรา ๔๕๐/๓๒ gaera");
        expResult.add("มาตรา ๔๕๐/๔ The law...Section 457");
        expResult.add("มาตรา ๔๕๐/๕ The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);

        // Skipped 1 twice but does not exist (THAI)
        input = new ArrayList();
        input.add("มาตรา ๔๕๐ 450 ffjal;er มาตรา ๔๕๐ gaera");
        input.add("มาตรา ๔๕๒ The law...มาตรา 457");
        input.add("มาตรา ๔๕๔ อฟ่ฟำ มาตรา ๔๕๔");
        input.add("มาตรา ๔๕๖ The");
        expResult = new ArrayList();
        expResult.add("มาตรา ๔๕๐ 450 ffjal;er มาตรา ๔๕๐ gaera");
        expResult.add("มาตรา ๔๕๒ The law...มาตรา 457");
        expResult.add("มาตรา ๔๕๔ อฟ่ฟำ มาตรา ๔๕๔");
        expResult.add("มาตรา ๔๕๖ The");
        result = ThaiLawParser.skippedSectionCorrecter(input);
        assertEquals(expResult, result);
    }

}
