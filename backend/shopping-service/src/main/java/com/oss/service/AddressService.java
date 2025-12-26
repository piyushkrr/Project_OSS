package com.oss.service;

import com.oss.entity.Address;
import com.oss.entity.User;
import com.oss.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    @Transactional
    public Address addAddress(User user, Address address) {
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(User user, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this address");
        }

        addressRepository.delete(address);
    }
}
