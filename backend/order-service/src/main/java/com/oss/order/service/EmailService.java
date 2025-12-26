package com.oss.order.service;

import com.oss.order.client.ProductClient;

import com.oss.order.dto.ProductDTO;
import com.oss.order.dto.UserDTO;
import com.oss.order.entity.Order;
import com.oss.order.entity.OrderItem;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    // OrderRepository is no longer needed since we pass the Order object
    private final ProductClient productClient;

    @Value("${spring.mail.username:no-reply@oss.com}")
    private String sender;

    @Async
    @Transactional(readOnly = true)
    public void sendOrderConfirmation(Order order) {
        try {
            UserDTO user = order.getUser();
            if (user == null) {
                log.warn("User details not found in order object. Skipping email.");
                return;
            }
            String to = user.getEmail();
            String subject = "Order Confirmation - Order #" + order.getOrderTrackingId();

            StringBuilder body = new StringBuilder();
            body.append("<h1>Thank you for your order!</h1>");
            body.append("<p>Hi ").append(user.getFirstName()).append(",</p>");
            body.append("<p>Your order has been placed successfully.</p>");
            body.append("<p><strong>Tracking ID: </strong>").append(order.getOrderTrackingId()).append("</p>");
            body.append("<h3>Order Details:</h3>");
            body.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            body.append("<tr><th>Product</th><th>Qty</th><th>Price</th></tr>");

            for (OrderItem item : order.getOrderItems()) {
                ProductDTO product = productClient.getProductById(item.getProductId());
                body.append("<tr>");
                body.append("<td>").append(product.getName()).append("</td>");
                body.append("<td>").append(item.getQuantity()).append("</td>");
                body.append("<td>₹").append(item.getPrice()).append("</td>");
                body.append("</tr>");
            }
            body.append("</table>");
            body.append("<h3>Total: ₹").append(order.getTotalAmount()).append("</h3>");
            body.append("<p>We will notify you when your order ships.</p>");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body.toString(), true);

            javaMailSender.send(message);
            log.info("Order confirmation email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to process or send order confirmation email for Order ID: " + order.getId(), e);
        }
    }
}
