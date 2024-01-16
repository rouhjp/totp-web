package jp.rouh.totpweb.service;

import jp.rouh.totpweb.util.ImageUtils;
import jp.rouh.totpweb.util.TotpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TotpService {

    private static final int QR_CODE_SIZE = 200;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String getEncodedQRCode(byte[] secret){
        var appName = "totp-web";
        var userName = "hello@example.com";
        var secretString = TotpUtils.encodeBase32(secret);
        var url = TotpUtils.otpAuthUrlOf(appName, userName, secretString);
        var qrCodeImage = ImageUtils.qrCodeImageOf(url, QR_CODE_SIZE);
        var qrCodeImageByteArray = ImageUtils.toBytes(qrCodeImage);
        return TotpUtils.encodeBase64(qrCodeImageByteArray);
    }

    public String getOneTimePassword(byte[] secret){
        long timeStep = TotpUtils.getTimeStep();
        var digest = TotpUtils.digestOf(secret, TotpUtils.toByte(timeStep, 8));
        int oneTimePassword = dynamicTruncation(digest) % 1000_000;
        var oneTimePasswordString = String.format("%06d", oneTimePassword);
        logger.info("timeStep="+ Arrays.toString(TotpUtils.toByte(timeStep, 8))
                +" digest="+Arrays.toString(digest)
                +" dynamicTranc="+dynamicTruncation(digest)
                +" oneTimePassword="+oneTimePasswordString);
        return oneTimePasswordString;
    }

    public int dynamicTruncation(byte[] hmacResult){
        assert hmacResult.length==20;
        int offset = hmacResult[19] & 0xf; //00001111
        return (hmacResult[offset] & 0x7f) << 24 //01111111
                | (hmacResult[offset + 1] & 0xff) << 16
                | (hmacResult[offset + 2] & 0xff) <<  8
                | (hmacResult[offset + 3] & 0xff);
    }



}
