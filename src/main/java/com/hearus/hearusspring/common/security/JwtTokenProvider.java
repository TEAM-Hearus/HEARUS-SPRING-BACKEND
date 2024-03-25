package com.hearus.hearusspring.common.security;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key accessSecret;
    private final Key refreshSecret;


}
