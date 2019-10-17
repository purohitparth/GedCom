package com.gedcom.application;

import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.printer.GedcomPrinter;
import com.gedcom.processor.GedcomProcessor;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.util.*;

/**
 * Created by Meghana on 9/12/2019.
 */
public class Application {
    public static void main(String[] args) throws ParseException, java.text.ParseException {
        GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
//        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/AGILE_SPRINT_1_SSHERKAR.ged");
        HashSet<String> tagSet = new HashSet<String>();
        List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

        for (String tagName: tagNames ) {
            tagSet.add(tagName);
        }
        GedcomProcessor gdp = new GedcomProcessor();
        GedcomResponse response= gdp.parser(gedcomLines,tagSet);
        IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();

        gedcomPrinter.printIndividuals(indiFamilyResponse.getIndividualList());
        gedcomPrinter.printFamily(indiFamilyResponse.getFamilyList());

        System.out.println("---- GEDCOM ERRORS -----");
        gedcomPrinter.printIndividualsWithBirthBeforeCurrentData(indiFamilyResponse.getFamilyList(),indiFamilyResponse.getIndividualList()); //US01
        gedcomPrinter.printBirthBeforeMarriageError(indiFamilyResponse);//US02
        gedcomPrinter.printBirthBeforeDeathError(indiFamilyResponse);//US03
        gedcomPrinter.printMarriageBeforeDivorceError(indiFamilyResponse);//US04
        gedcomPrinter.printMarriageBeforeDeathError(indiFamilyResponse);//US05
        gedcomPrinter.printIndividualsWithDivorceBeforeDeath(indiFamilyResponse);//US06
        gedcomPrinter.printIndividualsWithAgeMoreThan150(indiFamilyResponse.getIndividualList());//US07
        gedcomPrinter.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());//US08
        gedcomPrinter.printListOfIndividualsBornAfterParentsDeath(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());//US09
        gedcomPrinter.printMarriageBefore14Error(indiFamilyResponse);//US10

        //Call Priting Functions and make sure that user story numbers are are sorted like above and remove this comment at the end

        gedcomPrinter.printAmbiguosMaleLastNames(indiFamilyResponse);//US16
        gedcomPrinter.printAmbiguosSiblingMarriageList(indiFamilyResponse);//US18
        

    }

}
