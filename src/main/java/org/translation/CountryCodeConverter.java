package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
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
        final int three = 3;
        final int four = 4;
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                numCountries += 1;
                String[] parts = line.trim().split("\\s+");
                String alpha2Code = parts[parts.length - three];
                String alpha3Code = parts[parts.length - 2];
                String numericCode = parts[parts.length - 1];
                StringBuilder countryNameBuilder = new StringBuilder();
                for (int j = 0; j < parts.length - three; j++) {
                    countryNameBuilder.append(parts[j]);
                    if (j < parts.length - four) {
                        countryNameBuilder.append(" ");
                    }
                }
                String countryName = countryNameBuilder.toString();
                alpha2ToCountry.put(alpha2Code, countryName);
                alpha3ToCountry.put(alpha3Code, countryName);
                countryToAlpha.put(countryName, alpha3Code);

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
        if (alpha3ToCountry.containsKey(code.toUpperCase())) {
            return alpha3ToCountry.get(code.toUpperCase());
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
