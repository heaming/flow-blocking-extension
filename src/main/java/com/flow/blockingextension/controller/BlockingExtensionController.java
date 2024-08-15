package com.flow.blockingextension.controller;

import com.flow.blockingextension.dto.UserCustomExtensionAddRequest;
import com.flow.blockingextension.dto.UserExtensionDeleteRequest;
import com.flow.blockingextension.dto.UserExtensionUpdateRequest;
import com.flow.blockingextension.dto.UserFixedExtensionAddRequest;
import com.flow.blockingextension.model.Extension;
import com.flow.blockingextension.model.ExtensionType;
import com.flow.blockingextension.service.BlockingExtensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class BlockingExtensionController {

    private final BlockingExtensionService extensionService;

    @GetMapping("/blocking-extensions/{id}")
    public ResponseEntity<Extension> getExtensionById(@PathVariable Long id) {
        return ResponseEntity.ok(extensionService.getExtensionById(id));
    }

    @GetMapping("/blocking-extensions")
    public ResponseEntity<List<Extension>> getExtensionsByType(@RequestParam ExtensionType type) {
        List<Extension> list = extensionService.getExtensionsByType(type);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/blocking-extensions/user")
    public ResponseEntity<List<Extension>> getUserExtensions(@RequestParam String token) {
        List<Extension> list = extensionService.getUserExtensions(token);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/blocking-extensions/user/multiple")
    public ResponseEntity<List<Extension>> updateUserFixedExtensions(@RequestBody UserExtensionUpdateRequest request) {
        List<Extension> list = extensionService.updateUserFixedExtensions(request.token(), request.extensionIds());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/blocking-extensions/user/custom")
    public ResponseEntity<List<Extension>> updateUserCustomExtensions(@RequestBody UserCustomExtensionAddRequest request) {
        List<Extension> list = extensionService.insertUserCustomExtension(request.token(), request.extensionTitle());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/blocking-extensions/user/fixed")
    public ResponseEntity<List<Extension>> updateUserFixedExtension(@RequestBody UserFixedExtensionAddRequest request) {
        List<Extension> list = extensionService.updateUserFixedExtension(request.token(), request.extensionId());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/blocking-extensions/user")
    public ResponseEntity<List<Extension>> deleteUserExtension(@RequestBody UserExtensionDeleteRequest request) {
        List<Extension> list = extensionService.deleteUserExtension(request.token(), request.extensionId());
        return ResponseEntity.ok(list);
    }

}
