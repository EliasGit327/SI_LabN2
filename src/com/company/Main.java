package com.company;

import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        DSA dsa = new DSA(new DsaParams());

        String message = "Elias Smith Corporation";

        var cyberSign = dsa.createSign(message);
        var result = dsa.verifySign(cyberSign, message);

        System.out.println(cyberSign.first);
        System.out.println(cyberSign.second);
        System.out.println(result);
    }
}
