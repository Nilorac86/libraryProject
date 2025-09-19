package com.carolin.libraryproject.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class HtmlSanitizer {

    // Tar bort all HTML
    public static String cleanAll(String input) {
        return Jsoup.clean(input, Safelist.none());
    }

    // Tar bort farlig HTML men behåller vissa ofarliga taggar
    public static String cleanBasic(String input) {
        return Jsoup.clean(input, Safelist.basic());
    }
}
