package com.company;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class DsaParams {

    public int L = 1024;
    public int N = 160;

    private Random random = new Random();
    MessageDigest digest = MessageDigest.getInstance("SHA-1");

    BigInteger q = BigInteger.probablePrime(N, random);
    BigInteger p = computeP();
    BigInteger g = computeG();

    public DsaParams() throws NoSuchAlgorithmException { }

    private BigInteger computeG() {
        BigInteger probableG;

        var h = BigInteger.ONE;
        do {
            probableG = (h.add(BigInteger.ONE)).modPow((p.subtract(BigInteger.ONE)).divide(q), p);
        } while (probableG.compareTo(BigInteger.ONE) == 0);

        return probableG;
    }

    private BigInteger computeP() {
        BigInteger max = BigInteger.valueOf(2).pow(L);
        BigInteger min = BigInteger.valueOf(2);

//      (min.pow(L - 1) / q + 1) * q + 1
        var probableP = (((min.pow(L - 1).divide(q)).add(BigInteger.ONE)).multiply(q)).add(BigInteger.ONE);

        // 100 is the default value is for probablePrime
        while (!probableP.isProbablePrime(100) && probableP.compareTo(max) == -1) {
            probableP = probableP.add(q);
        }

        assert(probableP.compareTo(max) == -1);

        return probableP;
    }

}
