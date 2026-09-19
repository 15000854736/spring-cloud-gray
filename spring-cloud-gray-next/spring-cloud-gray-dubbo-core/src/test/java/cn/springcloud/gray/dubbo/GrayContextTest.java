package cn.springcloud.gray.dubbo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrayContextTest {
    @Test
    void scopesTagWithoutLeakingIt() {
        GrayContext.runWithTag(" beta ", () -> assertEquals("beta", GrayContext.currentTag().orElseThrow()));
        assertTrue(GrayContext.currentTag().isEmpty());
    }
}
