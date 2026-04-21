package com.wordlearner.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DictionaryService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getEnglishMeaning(String word) {
        try {
            String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word.toLowerCase();
            String response = restTemplate.getForObject(url, String.class);
            return parseEnglishMeaning(response);
        } catch (HttpClientErrorException.NotFound e) {
            return "Word not found";
        } catch (Exception e) {
            return "Unable to fetch meaning";
        }
    }

    private String parseEnglishMeaning(String json) {
        StringBuilder sb = new StringBuilder();
        Pattern meaningsPattern = Pattern.compile("\"meanings\"\\s*:\\s*\\[([\\s\\S]*?)\\]");
        Matcher meaningsMatcher = meaningsPattern.matcher(json);

        if (meaningsMatcher.find()) {
            String meanings = meaningsMatcher.group(1);
            Pattern defPattern = Pattern.compile("\"definition\"\\s*:\\s*\"([^\"]+)\"");
            Matcher defMatcher = defPattern.matcher(meanings);

            int count = 0;
            while (defMatcher.find() && count < 3) {
                if (count > 0) sb.append("\n");
                sb.append(++count).append(". ").append(defMatcher.group(1));
            }
        }
        return sb.length() > 0 ? sb.toString() : "No definition found";
    }

    public String getHindiMeaning(String word) {
        try {
            String encodedWord = java.net.URLEncoder.encode(word, "UTF-8");
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=hi&dt=t&dj=1&q=" + encodedWord;
            String response = restTemplate.getForObject(url, String.class);
            return parseGoogleTranslate(response);
        } catch (Exception e) {
            return "Unable to translate: " + e.getMessage();
        }
    }

    private String parseGoogleTranslate(String json) {
        Pattern pattern = Pattern.compile("\"trans\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Translation not found";
    }
}