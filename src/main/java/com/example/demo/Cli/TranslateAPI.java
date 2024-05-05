package com.example.demo.Cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslateAPI {

    public static String googleTranslate(String langFrom, String langTo, String text) throws IOException {
        String urlScript = "https://script.google.com/macros/s/AKfycbw1qSfs1Hvfnoi3FzGuoDWijwQW69eGcMM_iGDF7p5vu1oN_CaFqIDFmCGzBuuGCk_N/exec" +
                "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlScript);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    //https://translate.google.com.vn/translate_tts?ie=UTF-8&q=ANYTHING_TEXT&tl=en&client=tw-ob
    //above are the api for the audio

    public static void main(String[] args) throws IOException {
//        String url = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=ANYTHING_TEXT&tl=en&client=tw-ob";
//        //can change the accent of the english word by addign tl=en-us or tl=en-uk
//        Media sound = new Media(url);
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();
        System.out.println(googleTranslate("vi","en","xin chào tên tôi là duc"));

    }
}
