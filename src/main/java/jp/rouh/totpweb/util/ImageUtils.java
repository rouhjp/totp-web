package jp.rouh.totpweb.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ImageUtils {

    private ImageUtils(){
    }

    /**
     * QRコード画像を生成します。
     *
     * @param string QRコードの内容
     * @param size QRコードのサイズ
     * @return QRコード画像
     */
    public static BufferedImage qrCodeImageOf(String string, int size){
        try {
            var writer = new QRCodeWriter();
            var bitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, size, size, null);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new InternalError(e);
        }
    }

    /**
     * 画像をバイト配列に変換します。
     * @param image 画像
     * @return バイト配列
     */
    public static byte[] toBytes(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos)){
            image.flush();
            ImageIO.write(image, "png", bos);
            return baos.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
