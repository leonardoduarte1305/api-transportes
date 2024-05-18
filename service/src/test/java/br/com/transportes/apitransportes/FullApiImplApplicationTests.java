package br.com.transportes.apitransportes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FullApiImplApplication.class)
class FullApiImplApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertEquals("UPPER_CASE", "upper_case".toUpperCase());
    }

}
