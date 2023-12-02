// Create a test class for the AuthenticationController class, testing the login and register methods.

package com.ecoufpel.ecoufpelapp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class AuthenticationControllerUnitTest {
    @Autowired
    AuthenticationController authenticationController;

    @Test
    public void cpfSanitizer_unitTest(){
        assert authenticationController.sanitizeCpf("000.000.000-00").get().equals("00000000000");
        assert authenticationController.sanitizeCpf("000192").isEmpty();
        assert authenticationController.sanitizeCpf("ajsbfjs").isEmpty();
        assert authenticationController.sanitizeCpf("12.234.456-12").isEmpty();
    }
}