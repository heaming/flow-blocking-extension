package com.flow.blockingextension.respository;

import com.flow.blockingextension.model.Extension;
import com.flow.blockingextension.model.ExtensionType;
import com.flow.blockingextension.model.Subscribe;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExtensionJpaRepository extends JpaRepository<Extension, Long> {

    Optional<Extension> findExtensionById(Long id);
    Optional<Extension> findExtensionByTitle(String title);
    Optional<Extension> findExtensionByIdOrTitle(@Nullable Long id, String title);
    Optional<List<Extension>> findByExtensionTypeOrderById(ExtensionType type);
    @Query(value = "select e from Extension e join Subscribe s on s.extensionId = e.id and e.extensionType = :type and s.token = :token")
    Optional<List<Extension>> findExtensionByTokenAndExtensionType(@Param("token") String token,
                                                                   @Param("type") ExtensionType type);

}
