package com.oss.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static final String AES = "AES";

    @Value("${encryption.key}")
    private String secret;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        if (secret == null) {
            System.err.println("AttributeEncryptor: Secret key is null!");
            throw new IllegalStateException("Encryption key not configured");
        }
        try {
            Key key = new SecretKeySpec(secret.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            System.err.println("AttributeEncryptor: Error encrypting data - " + e.getMessage());
            e.printStackTrace();
            return null; // Fail safe
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        if (secret == null) {
            System.err.println("AttributeEncryptor: Secret key is null!");
            return null;
        }
        try {
            Key key = new SecretKeySpec(secret.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            System.err.println("AttributeEncryptor: Error decrypting data - " + e.getMessage());
            e.printStackTrace();
            return null; // Fail safe
        }
    }
}
