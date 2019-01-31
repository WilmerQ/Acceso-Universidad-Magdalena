/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.logica;

import co.leafcompany.accesounimagdalena.clases.respuestaSoapUnimagdalena;
import co.leafcompany.accesounimagdalena.modelo.Usuario;
import co.leafcompany.accesounimagdalena.soap.WCFValidaciones;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.EncodingUtils;

/**
 *
 * @author wilme
 */
@Stateless
@LocalBean
public class LogicaUnimagdalena implements Serializable {

    @PersistenceContext(unitName = "AccesoUnimagdalenaV1PU")
    private EntityManager em;

    public Usuario ValidarUsuario(Long idTarjeta) {
        try {
            respuestaSoapUnimagdalena su = ValidarEstudiante(idTarjeta.toString());
            if (su.getSuccess()) {
                Usuario u = new Usuario();
                u.setIdTarjeta(idTarjeta);
                u.setNombreCompleto(su.getNombreCompleto());
                u.setCargo(su.getCargo());
                u.setDependencia(su.getDependencia());
                return u;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("ERROR: LogicaUnimagdalena - ValidarUsuario " + e.getMessage());
            return null;
        }
    }

    public respuestaSoapUnimagdalena ValidarEstudiante(String idtarjeta) {
        try {
            String key = Token(idtarjeta + "ACCESO");
            System.out.println("key " + key);
            WCFValidaciones cFValidaciones = new WCFValidaciones();
            String res = cFValidaciones.getBasicHttpBindingIWCFValidaciones().getValidarusuarioAcceso(idtarjeta, key);
            Gson gson = new Gson();
            String aux = Desencriptar(FixSPChart(res), key);
            return gson.fromJson(aux, respuestaSoapUnimagdalena.class);
        } catch (JsonSyntaxException e) {
            System.out.println("Error: LogicaUnimagdalena-ValidarEstudiante: " + e.getMessage());
            return null;
        }
    }

    private String Desencriptar(String res, String key) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(res);
            System.out.println("Res codificado " + new String(decodedBytes, StandardCharsets.UTF_8));

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(key.getBytes(StandardCharsets.UTF_8));
            String md5 = EncodingUtils.getString(digestOfPassword, "UTF-8");
            System.out.println("keyBytes codificado " + md5);
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            for (int j = 0, k = 16; j < 8;) {
                keyBytes[k++] = keyBytes[j++];
            }
            String textDesencriptado = decrypt(keyBytes, decodedBytes);
            System.out.println(textDesencriptado);
            return textDesencriptado;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error: LogicaUnimagdalena-Desencriptar: " + ex.getLocalizedMessage());
            return null;
        }
    }

    private String decrypt(byte[] keyArray, byte[] encrypted) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            SecretKeySpec key = new SecretKeySpec(keyArray, 0, 24, "DESede");
            Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText2 = cipher.doFinal(encrypted);
            return new String(plainText2, StandardCharsets.UTF_8);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            System.out.println("Error: LogicaUnimagdalena-decrypt: " + ex.getLocalizedMessage());
            return null;
        }
    }

    private String FixSPChart(String sTheInput) {
        sTheInput = sTheInput.replace('-', '+');
        sTheInput = sTheInput.replace('*', '/');
        sTheInput = sTheInput.replace('!', '=');
        return sTheInput;
    }

    private String Token(String codigo) {
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String DateToStr = dateFormat.format(d);
        String temp = codigo + "*" + DateToStr + "+.+";
        System.out.println(temp);
        String token = getMD5Hash(temp);
        return token.toUpperCase();
    }

    private String getMD5Hash(String data) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02X", b));
            }
            result = sb.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.out.println("Error: LogicaUnimagdalena-getMD5Hash: " + e.getLocalizedMessage());
        }
        return result;
    }
}
