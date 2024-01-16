package jp.rouh.totpweb.controller;

import jp.rouh.totpweb.service.TotpService;
import jp.rouh.totpweb.util.TotpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TotpController {

    @Autowired
    private TotpService service;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String get(Model model){
        var secret = TotpUtils.generateSecret(20);
        model.addAttribute("secret", TotpUtils.encodeBase32(secret));
        model.addAttribute("qrCode","data:image/png;base64,"+service.getEncodedQRCode(secret));
        return "index";
    }

    @PostMapping("/otp")
    @ResponseBody
    public String getOtp(@RequestParam  String secret) {
        logger.info("otpRequest: "+secret);
        return service.getOneTimePassword(TotpUtils.decodeBase32(secret));
    }

}
