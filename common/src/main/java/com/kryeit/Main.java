package com.kryeit;

public class Main {
    public static final String MOD_ID = "missions";
    // We can use this if we don't want to use DeferredRegister

    public static void init() {
        
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
