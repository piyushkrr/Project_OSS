package com.oss.payment.controller;

import com.oss.payment.dto.UserPrincipal;
import com.oss.payment.entity.SavedPaymentMethod;
import com.oss.payment.repository.SavedPaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments/saved")
@RequiredArgsConstructor
public class SavedPaymentMethodController {

    private final SavedPaymentMethodRepository repository;

    @GetMapping
    public ResponseEntity<List<SavedPaymentMethod>> getSavedMethods(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(repository.findByUserId(principal.getId()));
    }

    @PostMapping
    public ResponseEntity<SavedPaymentMethod> addSavedMethod(@AuthenticationPrincipal UserPrincipal principal,
            @RequestBody SavedPaymentMethod method) {
        method.setUserId(principal.getId());
        return ResponseEntity.ok(repository.save(method));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavedPaymentMethod> updateSavedMethod(@AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @RequestBody SavedPaymentMethod updatedDetails) {
        return repository.findById(id)
                .filter(method -> method.getUserId().equals(principal.getId()))
                .map(method -> {
                    if (updatedDetails.getCardHolderName() != null)
                        method.setCardHolderName(updatedDetails.getCardHolderName());
                    if (updatedDetails.getExpiryDate() != null)
                        method.setExpiryDate(updatedDetails.getExpiryDate());
                    return ResponseEntity.ok(repository.save(method));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedMethod(@AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        repository.findById(id).ifPresent(method -> {
            if (method.getUserId().equals(principal.getId())) {
                repository.delete(method);
            }
        });
        return ResponseEntity.ok().build();
    }
}
