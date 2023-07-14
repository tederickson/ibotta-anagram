package com.ibotta.anagram;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnagramApplicationTests {

    @Test
    public void applicationContextLoads() {
        String[] args = {};
        AnagramApplication.main(args);
    }

}
