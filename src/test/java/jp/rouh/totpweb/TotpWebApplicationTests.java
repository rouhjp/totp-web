package jp.rouh.totpweb;

import jp.rouh.totpweb.service.TotpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TotpWebApplicationTests {

	@Autowired
	private TotpService service;

	@Test
	void contextLoads() {
	}

}
