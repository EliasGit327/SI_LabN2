package com.company;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DSA {

    DSA(DsaParams dsaParams) throws NoSuchAlgorithmException {
        this.dsaParams = dsaParams;
        this.x = new BigInteger(dsaParams.N, random).mod(dsaParams.q);
        this.y = dsaParams.g.modPow(this.x, dsaParams.p);
    }

    private DsaParams dsaParams;


    private Random random = new Random();
    private MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

    private BigInteger x;
    private BigInteger y;


    Pair createSign(String message) {
        var hash = new BigInteger(messageDigest.digest(message.getBytes()));

        BigInteger k;
        BigInteger r;
        BigInteger s;

        do {
            do {
                do {
                    k = new BigInteger(dsaParams.N - 1, random);
                } while (k == BigInteger.ZERO);

                r = dsaParams.g.modPow(k, dsaParams.p).mod(dsaParams.q);
            } while (r == BigInteger.ZERO);

            // ab mod p = (a mod p)(b mod p) mod p
            s = (((hash.add(x.multiply(r))).mod(dsaParams.q)).multiply(k.modInverse(dsaParams.q))).mod(dsaParams.q);
        } while (s == BigInteger.ZERO);

        return new Pair(r, s);
    }

    Boolean verifySign(Pair sign, String message) {

        assert (sign.first.compareTo(BigInteger.ZERO) == 1 && sign.first.compareTo(dsaParams.q) == -1);
        assert (sign.second.compareTo(BigInteger.ZERO) == 1 && sign.second.compareTo(dsaParams.q) == -1);

        var hash = new BigInteger(messageDigest.digest(message.getBytes()));

        var w = sign.second.modInverse(dsaParams.q);

        var u1 = hash.multiply(w).mod(dsaParams.q);
        var u2 = sign.first.multiply(w).mod(dsaParams.q);

        // ab mod p = (a mod p)(b mod p) mod p
        var v = (((dsaParams.g.modPow(u1, dsaParams.p).mod(dsaParams.p))).multiply(y.modPow(u2, dsaParams.p).mod(dsaParams.p)))
                .mod(dsaParams.p).mod(dsaParams.q);

        return v.equals(sign.first);
    }
}


class Pair {
    BigInteger first = BigInteger.ZERO;
    BigInteger second = BigInteger.ZERO;

    Pair(BigInteger first, BigInteger second) {
        this.first = first;
        this.second = second;
    }

}