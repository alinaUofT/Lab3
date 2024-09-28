package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final Map<String, Map<String, String>> c2code2c = new HashMap<String, Map<String, String>>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                Iterator<String> codes = jsonArray.getJSONObject(i).keys();
                Map<String, String> code2country = new HashMap<String, String>();
                while (codes.hasNext()) {
                    String code = codes.next();
                    if (!("id".equals(code) || "alpha2".equals(code) || "alpha3".equals(code))) {
                        code2country.put(code, jsonArray.getJSONObject(i).getString(code));
                    }
                }
                this.c2code2c.put(jsonArray.getJSONObject(i).getString("alpha3"), code2country);

            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {

        return new ArrayList<>(this.c2code2c.get(country).keySet());
    }

    @Override
    public List<String> getCountries() {

        return new ArrayList<>(this.c2code2c.keySet());
    }

    @Override
    public String translate(String country, String language) {

        return this.c2code2c.get(country).get(language);
    }
}
