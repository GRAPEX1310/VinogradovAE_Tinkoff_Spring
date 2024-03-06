package edu.java.bot.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkValidator {

    private LinkValidator() {
    }

    public static boolean isLinkValid(String link) {
        Pattern pattern = Pattern.compile("https?:\\/\\/(github|stackoverflow)\\.\\S+");
        Matcher matcher = pattern.matcher(link);
        return matcher.find();
    }
}
