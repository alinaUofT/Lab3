package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
// TODO CheckStyle: Wrong lexicographical order for 'java.util.HashMap' import (remove this comment once resolved)
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {
    private int numCountries;
    private Map<String, String> alpha2ToCountry;
    private Map<String, String> alpha3ToCountry;
    private Map<String, String> countryToAlpha;

    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        alpha2ToCountry = new HashMap<>();
        alpha3ToCountry = new HashMap<>();
        countryToAlpha = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            for (int i= 1; i<lines.size(); i++) {
                String line = lines.get(i);
                numCountries += 1;
                String[] split = line.split("\t");
                String[] split3countrycode = split[2].split(" ");
                alpha2ToCountry.put(split[1], split[0]);
                alpha3ToCountry.put(split3countrycode[0], split[0]);
                countryToAlpha.put(split[0], split3countrycode[0]);

            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        if (alpha3ToCountry.containsKey(code)) {
            return alpha3ToCountry.get(code);
        }
        else {
            return "Invalid code";
        }
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        if (countryToAlpha.containsKey(country)) {
            return countryToAlpha.get(country);
        }
        else {
            return "Invalid country";
        }
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return numCountries;
    }
}