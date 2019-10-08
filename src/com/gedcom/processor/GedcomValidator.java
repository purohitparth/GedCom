package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.Individual;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by Meghana on 9/28/2019.
 */
public class GedcomValidator {

    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            // case insensitive to parse JAN and FEB
            .parseCaseInsensitive()
            // add pattern
            .appendPattern(" d MMM yyyy")
            // create formatter (use English Locale to parse month names)
            .toFormatter(Locale.ENGLISH);

    public List<Family> marriageBeforeDeath(List<Individual> individualList, List<Family> familyList) {
        List<Family> ambiguosFamilyMarrDeathList = new ArrayList<>();

        for (Family family : familyList) {
            String husbandId = family.getHusbandId();
            String wifeId = family.getWifeId();

            Optional<Individual> husbandOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(husbandId);
            }).findFirst();

            Optional<Individual> wifeOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(wifeId);
            }).findFirst();
            String marriageDate = family.getMarried();
            if (husbandOpt.isPresent() && wifeOpt.isPresent() && marriageDate != null && !marriageDate.equals("") ) {
                Individual husband = husbandOpt.get();
                Individual wife = wifeOpt.get();

                String husbandDeathDate = husband.getDeath();
                String wifeDeathDate = wife.getDeath();


                LocalDate marrDate = LocalDate.parse(marriageDate, formatter);

                if (husbandDeathDate != null && !husbandDeathDate.isEmpty()) {
                    LocalDate husbandDeath = LocalDate.parse(husbandDeathDate, formatter);
                    if (husbandDeath.isBefore(marrDate)) {
                        ambiguosFamilyMarrDeathList.add(family);

                    }
                } else if (wifeDeathDate != null && !wifeDeathDate.isEmpty()) {
                    LocalDate wifeDeath = LocalDate.parse(wifeDeathDate, formatter);
                    if (wifeDeath.isBefore(marrDate)) {
                        ambiguosFamilyMarrDeathList.add(family);
                    }

                }
            }
        }

        return ambiguosFamilyMarrDeathList;

    }


    public List<Family> marriageBeforeDivorce(List<Family> familyList) {

        List<Family> ambiguosFamilyMarrDivList = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" d MMM yyyy");

        for (Family family : familyList) {
            String marriageDate = family.getMarried();
            String divorceDate = family.getDivorced();
            if (divorceDate != null && !divorceDate.isEmpty()) {
                if (marriageDate != null && !marriageDate.isEmpty()) {
                    LocalDate marrDate = LocalDate.parse(marriageDate, formatter);
                    LocalDate divDate = LocalDate.parse(divorceDate, formatter);
                    if (divDate.isBefore(marrDate)) {
                        ambiguosFamilyMarrDivList.add(family);
                    }
                }
            }
        }
        return ambiguosFamilyMarrDivList;
    }


}