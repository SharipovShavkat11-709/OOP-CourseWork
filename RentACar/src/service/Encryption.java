package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс шифрования информации
 * @author User
 */
public class Encryption {
    
    /**
     * Метод шифрования информации
     * @param info информация подлежащая шифрованию
     * @param algoritm алгоритм шифрования данных
     * @return Хеш-представление информации
     */
    public static String md5HashCoding(String info, String algoritm){
        //Создание ссылки на класс шифрования
        MessageDigest md = null;
        try {
            //Инициализация объекта шифрования с конкретным алгоритмом
            md = MessageDigest.getInstance(algoritm);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encryption.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERROR: md5HashCoding()");
        }
        //Получение массива байт шифрования
        byte[] bytes = md.digest(info.getBytes());
        StringBuilder builder = new StringBuilder();
        for(byte b : bytes) {
            //Приведение в 16тиричное значение массива байт
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
    
}
