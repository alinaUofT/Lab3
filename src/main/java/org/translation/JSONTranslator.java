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
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private Map<String, Map<String, String>> countriesToLang;
    private Map<String, List<String>> countriesToLangCode;
    private List<String> countries;
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
    @SuppressWarnings({"checkstyle:MagicNumber", "checkstyle:SuppressWarnings"})
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {
            countriesToLang = new HashMap<>();
            countriesToLangCode = new HashMap<>();
            countries = new ArrayList<>();

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject country = jsonArray.getJSONObject(i);
                String countryCode = country.getString("alpha3");
                countries.add(countryCode);

                List<String> keysList = new ArrayList<>();
                for (int j = 0; j < country.length(); j++) {
                    Iterator<String> keys = country.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        keysList.add(key);
                    }
                }
                countriesToLangCode.put(countryCode, keysList);

                Map<String, String> languages = new HashMap<>();
                for (int k = 3; k < country.length(); k++) {
                    String langKey = keysList.get(k);
                    languages.put(langKey, country.getString(langKey));
                }
                countriesToLang.put(countryCode, languages);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        return this.countriesToLangCode.get(country);
    }

    @Override
    public List<String> getCountries() {
        return this.countries;
    }

    @Override
    public String translate(String country, String language) {
        return this.countriesToLang.get(country).get(language);
    }
}
