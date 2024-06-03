package com.alfredcode.socialWebsite.Tools;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.alfredcode.socialWebsite.tools.Auth;

import jakarta.servlet.http.HttpServletResponse;

public class AuthTest {

    @Test
    public void testSetSession() {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Auth.setSession("usernam", response);


    }
}
