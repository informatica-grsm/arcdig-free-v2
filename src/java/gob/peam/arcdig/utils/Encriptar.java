/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.peam.arcdig.utils;

import java.security.MessageDigest;

/**
 *
 * @author jpgprog84
 */
public class Encriptar {
    public static String md5(String clear) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(clear.getBytes());

        int size = b.length;
        StringBuilder h = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int u = b[i] & 255;
            if (u < 16) {
                h.append("0").append(Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }

    public String encriptar(String texto, String clave) {
        int tamtext = texto.length();
        int tamclav = clave.length();
        int temp, p = 0;
        String encriptado = "";
        /* Se crea un array de enteros que contendran los numeros que
         corresponde a los caracteres en Ascii de los String Texto y la Clave */
        int textoAscii[] = new int[tamtext];
        int claveAscii[] = new int[tamclav];
        /* Se guardan los caracteres de cada String en
         numeros correspondientes al Ascii           */
        for (int i = 0; i < tamtext; i++) {
            textoAscii[i] = texto.charAt(i);
        }
        for (int i = 0; i < tamclav; i++) {
            claveAscii[i] = clave.charAt(i);
        }
        //Se procede al ENCRIPTADO
        for (int i = 0; i < tamtext; i++) {
            p++;
            if (p >= tamclav) {
                p = 0;
            }
            temp = textoAscii[i] + claveAscii[p];
            if (temp > 255) {
                temp = temp - 255;
            }
            encriptado = encriptado + (char) temp;
        }
        return encriptado;
    }

    public String desencriptar(String texto, String clave) {
        int tamtext = texto.length();
        int tamclav = clave.length();
        int temp, p = 0;
        String desencriptado = "";
        /* Se crea un array de enteros que contendran los numeros que
         corresponde a los caracteres en Ascii de los String Texto y la Clave */
        int textoAscii[] = new int[tamtext];
        int claveAscii[] = new int[tamclav];
        /* Se guardan los caracteres de cada String en
         numeros correspondientes al Ascii           */
        for (int i = 0; i < tamtext; i++) {
            textoAscii[i] = texto.charAt(i);
        }
        for (int i = 0; i < tamclav; i++) {
            claveAscii[i] = clave.charAt(i);
        }
        //Se procede al DESENCRIPTADO
        for (int i = 0; i < tamtext; i++) {
            p++;
            if (p >= tamclav) {
                p = 0;
            }
            temp = textoAscii[i] - claveAscii[p];
            if (temp < 0) {
                temp = temp + 256;
            }
            desencriptado = desencriptado + (char) temp;
        }
        return desencriptado;
    }
}
