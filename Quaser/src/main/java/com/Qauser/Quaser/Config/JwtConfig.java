package com.Qauser.Quaser.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

        @Value("${jwt.secret}")
        private String secret;

        public String getSecret() {
            return secret;
        }
    }
