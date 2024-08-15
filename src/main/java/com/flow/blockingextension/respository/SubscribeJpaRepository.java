package com.flow.blockingextension.respository;

import com.flow.blockingextension.model.Subscribe;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeJpaRepository extends JpaRepository<Subscribe, Long> {
    Optional<Subscribe> findSubscribeById(Long id);
    Optional<List<Subscribe>> findSubscribesByToken(String token);
    @Transactional(rollbackFor = Exception.class)
    Optional<Void> deleteSubscribeByTokenAndExtensionId(String token, Long extensionId);
    Optional<Subscribe> findSubscribeByTokenAndExtensionId(String token, Long extensionId);

}
