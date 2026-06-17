package com.trinity.banco;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "jwt.secret=testSecretKeyForUnitTestingPurposesOnly12345678",
    "jwt.expiration=86400000"
})
class BancoApplicationTests {

	@Test
	void contextLoads() {
	}

}
