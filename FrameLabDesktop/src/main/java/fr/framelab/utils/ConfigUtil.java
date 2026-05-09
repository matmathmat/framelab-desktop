package fr.framelab.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigUtil {

    private ConfigUtil() {}

    public static Properties loadServiceConfig() {
        try (InputStream is = ConfigUtil.class.getResourceAsStream("/fr/framelab/service.txt")) {

            if (is == null) {
                throw new IllegalStateException("Fichier service.txt introuvable dans les resources");
            }

            Properties props = new Properties();
            props.load(new BufferedReader(new InputStreamReader(is)));

            if (props.getProperty("apiUrl") == null || props.getProperty("frontUrl") == null) {
                throw new IllegalStateException("service.txt : apiUrl ou frontUrl manquant");
            }

            return props;

        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire service.txt : " + e.getMessage(), e);
        }
    }
}