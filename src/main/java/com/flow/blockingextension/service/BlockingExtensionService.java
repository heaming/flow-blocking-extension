package com.flow.blockingextension.service;

import com.flow.blockingextension.exception.ApplicationException;
import com.flow.blockingextension.model.Extension;
import com.flow.blockingextension.model.ExtensionType;
import com.flow.blockingextension.model.Subscribe;
import com.flow.blockingextension.respository.ExtensionJpaRepository;
import com.flow.blockingextension.respository.SubscribeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.flow.blockingextension.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class BlockingExtensionService {

    private final ExtensionJpaRepository extensionJpaRepository;
    private final SubscribeJpaRepository subscribeJpaRepository;

    public Extension getExtensionById(Long id) {
        return extensionJpaRepository.findExtensionById(id).orElseThrow(INVALID_EXTENSION::build);
    }

    public List<Extension> getExtensionsByType(ExtensionType type) {
        return extensionJpaRepository.findByExtensionTypeOrderById(type)
                .orElse(new ArrayList<>());
    }

    public List<Extension> getUserExtensions(String token) {
        List<Subscribe> subscribes = getSubscribesByToken(token);

        if(!subscribes.isEmpty()) {
            subscribes.sort(Comparator.comparing(Subscribe::getDateCreated));
        }

        List<Extension> extensions = new ArrayList<>();
        for(Subscribe subscribe : subscribes)  {
            extensions.add(
                    extensionJpaRepository.findById(subscribe.getExtensionId())
                            .orElseThrow(INVALID_EXTENSION::build));
        }

        return extensions;
    }

    public List<Extension> insertUserCustomExtension(String token, String extensionTitle) {

        try {
            Extension newExtension = extensionJpaRepository.findExtensionByTitle(extensionTitle).orElse(null);

            if(newExtension == null) {
                newExtension = extensionJpaRepository.save(Extension.builder()
                        .title(extensionTitle)
                        .extensionType(ExtensionType.CUSTOM)
                        .createdBy(token)
                        .build()
                );
            }

            // duplicated check
            Subscribe subscribe = subscribeJpaRepository.findSubscribeByTokenAndExtensionId(token, newExtension.getId()).orElse(null);

            if(subscribe != null) {
                throw FAIL_SAVING.build();
            }

            subscribeJpaRepository.save(Subscribe.builder()
                    .token(token)
                    .extensionId(newExtension.getId())
                    .build());

            return getUserExtensions(token);

        } catch (ApplicationException ex) {
            throw FAIL_SAVING.build();
        }
    }

    public List<Extension> deleteUserExtension(String token, Long extensionId) {

        try {
            subscribeJpaRepository.deleteSubscribeByTokenAndExtensionId(token, extensionId)
                    .orElseThrow(INVALID_EXTENSION::build);

            return getUserExtensions(token);

        } catch (ApplicationException ex) {
            throw FAIL_SAVING.build();
        }

    }

    private List<Subscribe> getSubscribesByToken(String token) {
        List<Subscribe> subscribe = subscribeJpaRepository.findSubscribesByToken(token).orElse(null);
        if(subscribe == null) {
            subscribe = new ArrayList<>();
        }

        return subscribe;
    }

    public List<Extension> insertUserFixedExtension(String token, Long extensionId) {

        Extension extension = extensionJpaRepository.findExtensionById(extensionId).orElse(null);

        if(extension == null) {
            throw INVALID_EXTENSION.build();
        }

        // duplicated check
        Subscribe subscribe = subscribeJpaRepository.findSubscribeByTokenAndExtensionId(token, extension.getId()).orElse(null);
        if(subscribe != null) {
            throw FAIL_SAVING.build();
        }

        subscribeJpaRepository.save(Subscribe.builder()
                .token(token)
                .extensionId(extension.getId())
                .build());

        return getUserExtensions(token);
    }

    public List<Extension> updateUserFixedExtension(String token, Long extensionId) {

        Extension extension = extensionJpaRepository.findExtensionById(extensionId).orElse(null);

        if(extension == null) {
            throw INVALID_EXTENSION.build();
        }

        Subscribe subscribe = subscribeJpaRepository.findSubscribeByTokenAndExtensionId(token, extension.getId()).orElse(null);

        try {
            if(subscribe != null) {
                subscribeJpaRepository.deleteSubscribeByTokenAndExtensionId(token, extension.getId());
            } else {
                subscribeJpaRepository.save(Subscribe.builder()
                        .token(token)
                        .extensionId(extension.getId())
                        .build());
            }
        } catch (ApplicationException e) {
            throw FAIL_SAVING.build();
        }

        return getUserExtensions(token);
    }

    public List<Extension> updateUserFixedExtensions(String token, List<Long> extensionIds) {

        List<Extension> userExtension = extensionJpaRepository
                .findExtensionByTokenAndExtensionType(token, ExtensionType.FIXED)
                .orElse(new ArrayList<>());

        List<Long> userExtensionIds = new ArrayList<>();
        if(!userExtension.isEmpty()) {
            userExtensionIds = userExtension.stream().map(Extension::getId).toList();
        }

        for(Long extensionId : extensionIds) {
            Extension extension = extensionJpaRepository.findExtensionById(extensionId).orElse(null);

            if(extension == null) {
                throw INVALID_EXTENSION.build();
            }

            try {
                if(!userExtensionIds.contains(extensionId)) { // 저장된 것 없다
                    subscribeJpaRepository.save(Subscribe.builder()
                            .token(token)
                            .extensionId(extension.getId())
                            .build());
                }
            } catch (ApplicationException e) {
                throw FAIL_SAVING.build();
            }
        }

        for(Long userExtensionId : userExtensionIds) {
            try {
                if(!extensionIds.contains(userExtensionId)) { // 저장된 것 없다
                    subscribeJpaRepository.deleteSubscribeByTokenAndExtensionId(token, userExtensionId);
                }
            } catch (ApplicationException e) {
                throw FAIL_SAVING.build();
            }
        }

        return getUserExtensions(token);
    }

}
