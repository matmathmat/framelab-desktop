package fr.framelab.utils.emoji;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class EmojiList {

    public static final List<String> EMOJIS = loadEmojis();

    private EmojiList() {
    }

    private static List<String> loadEmojis() {
        List<String> emojis = new ArrayList<>();

        try {
            InputStream stream = EmojiList.class.getClassLoader()
                    .getResourceAsStream("fr/framelab/emojis.txt");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    emojis.add(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Erreur chargement emojis: " + e.getMessage());
        }

        return List.copyOf(emojis);
    }
}