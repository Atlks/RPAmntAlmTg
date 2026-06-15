package com.mntalm;

import org.sikuli.script.*;

public class TestOCR {
    public static void main(String[] args) throws Exception {
        Screen screen = new Screen();
        String text = String.valueOf(screen.findText("Hello"));
        System.out.println(text);
    }
}
