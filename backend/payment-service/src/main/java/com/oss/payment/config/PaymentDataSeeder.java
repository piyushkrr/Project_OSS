package com.oss.payment.config;

import com.oss.payment.entity.SavedPaymentMethod;
import com.oss.payment.repository.SavedPaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentDataSeeder implements CommandLineRunner {

    private final SavedPaymentMethodRepository repository;

    @Override
    public void run(String... args) throws Exception {
        seedPayments();
    }

    private void seedPayments() {
        // user_id = 2 is "user@gmail.com"
        Long userId = 2L;

        if (repository.findByUserId(userId).isEmpty()) {
            // UPI Payment
            SavedPaymentMethod upi = new SavedPaymentMethod();
            upi.setUserId(userId);
            upi.setType("UPI");
            upi.setProvider("UPI");
            upi.setMaskedNumber("pk@upi");
            repository.save(upi);

            // Card Payment
            SavedPaymentMethod card = new SavedPaymentMethod();
            card.setUserId(userId);
            card.setType("CARD");
            card.setProvider("Card Network");
            card.setMaskedNumber("**** 1234");
            repository.save(card);

            System.out.println("Payment methods seeded for user_id: " + userId);
        }
    }
}
