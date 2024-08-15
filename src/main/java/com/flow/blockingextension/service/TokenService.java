package com.flow.blockingextension.service;

import com.flow.blockingextension.model.Token;
import com.flow.blockingextension.respository.TokenJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenJpaRepository tokenJpaRepository;

    public void addToken(String token) {
        try{
            tokenJpaRepository.save(Token.builder().token(token).build());
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

}
