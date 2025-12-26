package com.oss.service;

import com.oss.entity.Order;
import com.oss.entity.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final com.oss.repository.OrderRepository orderRepository;

    @Value("${spring.mail.username:no-reply@oss.com}")
    private String sender;

    @Async
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public void sendOrderConfirmation(Long orderId) {
        try {
            Order order = orderRepository.findWithItemsById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found for email"));

            String to = order.getUser().getEmail();
            String subject = "Order Confirmation - Order #" + order.getId();

            StringBuilder body = new StringBuilder();
            body.append("<h1>Thank you for your order!</h1>");
            body.append("<p>Hi ").append(order.getUser().getFirstName()).append(",</p>");
            body.append("<p>Your order has been placed successfully.</p>");
            body.append("<h3>Order Details:</h3>");
            body.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            body.append("<tr><th>Product</th><th>Qty</th><th>Price</th></tr>");

            for (OrderItem item : order.getOrderItems()) {
                body.append("<tr>");
                body.append("<td>").append(item.getProduct().getName()).append("</td>");
                body.append("<td>").append(item.getQuantity()).append("</td>");
                body.append("<td>₹").append(item.getPrice()).append("</td>");
                body.append("</tr>");
            }
            body.append("</table>");
            body.append("<h3>Total: ₹").append(order.getTotalAmount()).append("</h3>");
            body.append("<p>We will notify you when your order ships.</p>");

            // Check if JavaMailSender is configured (basic check, though bean always
            // exists)
            // Ideally we'd wrap this in a try-catch for actual sending
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body.toString(), true);

            javaMailSender.send(message);
            log.info("Order confirmation email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to process or send order confirmation email.", e);
            log.info("Order ID: {}", orderId);
            // Fallback to console log if needed, though we don't have the full body here
            // easily if it failed early.
        }
    }
}
