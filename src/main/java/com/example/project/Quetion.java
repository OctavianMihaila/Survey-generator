package com.example.project;

import java.util.Map;

public class Quetion {

    private int id;
    private String text;
    private String type;
    private Map<String, Boolean> answers;

    public Quetion(int id, String text, String type, Map<String, Boolean> answers) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.answers = answers;
    }


}
