package com.kryeit;

public class Main {
    public static final String MOD_ID = "missions";

    public static void init() {
        
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
