package com.oss.auth.controller;

import com.oss.auth.entity.Address;
import com.oss.auth.entity.User;
import com.oss.auth.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(addressService.getUserAddresses(user));
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@AuthenticationPrincipal User user, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.addAddress(user, address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal User user, @PathVariable Long id) {
        addressService.deleteAddress(user, id);
        return ResponseEntity.ok().build();
    }
}
