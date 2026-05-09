package fr.framelab.utils.emoji;

public final class EmojiUtils {

    private EmojiUtils() {
    }

    public static boolean isEmoji(int codePoint) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);

        return block == Character.UnicodeBlock.EMOTICONS
                || block == Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS
                || block == Character.UnicodeBlock.SUPPLEMENTAL_SYMBOLS_AND_PICTOGRAPHS
                || block == Character.UnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS
                || block == Character.UnicodeBlock.SYMBOLS_AND_PICTOGRAPHS_EXTENDED_A;
    }
}