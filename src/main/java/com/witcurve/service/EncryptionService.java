package com.witcurve.service;

public interface EncryptionService {

    String encrypt(String data, String cryptoKey);

    String decrypt(String data, String cryptoKey);

    String encrypt(String data);

    String decrypt(String data);

    String generateAESKey();
}
