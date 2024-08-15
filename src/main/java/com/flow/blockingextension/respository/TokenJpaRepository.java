package com.flow.blockingextension.respository;

import com.flow.blockingextension.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {
}
