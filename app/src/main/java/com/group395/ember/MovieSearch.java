package com.group395.ember;
import java.util.ArrayList;

public class MovieSearch {
    public static ArrayList<String> keyPhraseParser(String searchInput){
        ArrayList<String> keyPhrases = new ArrayList<String>();
        String current = "";
        for(int i=0; i<searchInput.length(); i++){
            if(searchInput.charAt(i) == '"' && current.length()==0){
                i++;
                while(searchInput.charAt(i) != '"'){
                    current += searchInput.charAt(i);
                    i++;
                }
                keyPhrases.add(current.toLowerCase());
                current = "";
            }
            else{
                if(searchInput.charAt(i) == ' '){
                    if(current.length()>0){
                        keyPhrases.add(current.toLowerCase());
                        current = "";
                    }
                }
                else{
                    if(searchInput.charAt(i) != '"')
                        current += searchInput.charAt(i);
                }
            }
        }
        if(current.length()>0){
            keyPhrases.add(current.toLowerCase());
            current = "";
        }
        return keyPhrases;
    }

    public static int keyPhrasesContained(ArrayList<String> keyPhrases, String name){
        int phrasesContained = 0;
        name = name.toLowerCase();
        for(int i=0; i<keyPhrases.size(); i++){
            if(name.contains(keyPhrases.get(i)))
                phrasesContained++;
        }
        return phrasesContained;
    }
}
