package com.flow.blockingextension.dto;

import com.flow.blockingextension.model.Extension;

import java.util.List;

public record UserExtensionUpdateRequest(String token, List<Long> extensionIds) {
}
