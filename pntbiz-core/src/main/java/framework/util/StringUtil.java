package framework.util;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by ucjung on 2017-07-31.
 */
public class StringUtil {
    static public String convertCamelToSeperatedString(String source) {
        return source.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    static public String convertCamelToSeperatedString(String source, String seperator) {
        return source.replaceAll("([a-z])([A-Z])", "$1" + seperator + "$2").toLowerCase();
    }

    static public String convertStringToCamel(String source) {
        return WordUtils.uncapitalize(WordUtils.capitalizeFully(source, '_').replaceAll("_", ""));
    }

    static public String convertStringToPascal(String source) {
        return WordUtils.capitalizeFully(source, '_').replaceAll("_", "");
    }

}
