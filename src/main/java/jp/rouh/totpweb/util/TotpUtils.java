package jp.rouh.totpweb.util;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;

public final class TotpUtils {

    private TotpUtils(){
    }

    /**
     * long型の整数値をバイト配列に変換します。
     *
     * @param longValue long型の整数値
     * @param size 割り当てるバイト配列の長さ
     * @return バイト配列
     */
    public static byte[] toByte(long longValue, int size){
        var byteBuffer = ByteBuffer.allocate(size);
        byteBuffer.putLong(longValue);
        return byteBuffer.array();
    }

    /**
     * Base32文字列をバイト配列に変換します。
     *
     * @param string Base32文字列
     * @return バイト配列
     */
    public static byte[] decodeBase32(String string){
        var base32 = new Base32();
        return base32.decode(string);
    }

    /**
     * バイト配列をBase64文字列に変換します。
     *
     * @param byteArray バイト配列
     * @return Base64文字列
     */
    public static String encodeBase64(byte[] byteArray){
        var base64 = new Base64();
        return base64.encodeAsString(byteArray);
    }

    /**
     * バイト配列をBase32文字列に変換します。
     *
     * @param byteArray バイト配列
     * @return Base32文字列
     */
    public static String encodeBase32(byte[] byteArray){
        var base32 = new Base32();
        return base32.encodeAsString(byteArray);
    }

    /**
     * 最大32バイトまでのランダムなバイト配列を生成します。
     *
     * @param size シークレットの長さ(バイト)
     * @return バイト配列
     */
    public static byte[] generateSecret(int size){
        try {
            var generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            var keyByteArray = generator.generateKey().getEncoded();
            return Arrays.copyOf(keyByteArray, size);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError(e);
        }
    }

    /**
     * HMAC-SHA-1によるダイジェスト値を取得します。
     *
     * @param key キー
     * @param message メッセージ
     * @return ダイジェスト値
     */
    public static byte[] digestOf(byte[] key, byte[] message){
        try {
            var mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            mac.update(message);
            return mac.doFinal();
        }catch (InvalidKeyException | NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * OTPAuthのURLを取得します。
     *
     * @param appName アプリ名
     * @param userName ユーザ名
     * @param secret シークレット(Base64文字列)
     * @return URL
     */
    public static String otpAuthUrlOf(String appName, String userName, String secret){
        var algorithm = "SHA1";
        int digit = 6;
        int period = 30;
        return "otpauth://totp/"+appName+":"+userName
                + "?secret="+secret
                + "&issuer="+appName
                + "&algorithm="+algorithm
                + "&digit="+digit
                + "&period="+period;
    }

    /**
     * 現在のタイムステップを取得します。
     *
     * @return タイムステップ
     */
    public static long getTimeStep(){
        long current = Instant.now().toEpochMilli() / 1000;
        return current/30;
    }
}
