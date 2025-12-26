package com.oss.product.config;

import com.oss.product.entity.Product;
import com.oss.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductDataSeeder implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final RestTemplate restTemplate = new RestTemplate();

        @Override
        public void run(String... args) throws Exception {
                seedProducts();
        }

        private void seedProducts() {
                if (productRepository.count() >= 60) {
                        return;
                }
                System.out.println("⚠️ Found fewer than 60 products. Clearing and re-seeding...");
                productRepository.deleteAll();

                List<Product> products = new ArrayList<>();

                // ELECTRONICS (15 products)
                products.add(createProduct("iPhone 15 Pro Max",
                                "Latest flagship with titanium design, A17 Pro chip, and advanced camera system",
                                "1199.00",
                                "Electronics", 45, "https://picsum.photos/seed/iphone15/600/400"));
                products.add(createProduct("MacBook Pro 16\"",
                                "M3 Max chip, 36GB RAM, stunning Liquid Retina XDR display",
                                "2499.00", "Electronics", 25, "https://picsum.photos/seed/macbook/600/400"));
                products.add(createProduct("Samsung Galaxy S24 Ultra",
                                "200MP camera, S Pen included, AI-powered features",
                                "1299.00", "Electronics", 50, "https://picsum.photos/seed/galaxy/600/400"));
                products.add(createProduct("Sony WH-1000XM5", "Industry-leading noise cancellation headphones",
                                "399.00",
                                "Electronics", 60, "https://picsum.photos/seed/sony/600/400"));
                products.add(createProduct("iPad Air M2", "10.9-inch Liquid Retina display, Apple M2 chip", "599.00",
                                "Electronics", 40, "https://picsum.photos/seed/ipad/600/400"));
                products.add(createProduct("AirPods Pro 2nd Gen", "Active noise cancellation, spatial audio", "249.00",
                                "Electronics", 100, "https://picsum.photos/seed/airpods/600/400"));
                products.add(createProduct("Dell XPS 15", "Intel Core i7, 32GB RAM, 4K OLED display", "1899.00",
                                "Electronics",
                                20, "https://picsum.photos/seed/dell/600/400"));
                products.add(createProduct("Nintendo Switch OLED", "7-inch OLED screen, enhanced audio, 64GB storage",
                                "349.99",
                                "Electronics", 75, "https://picsum.photos/seed/nintendo/600/400"));
                products.add(createProduct("LG 65\" OLED TV", "4K OLED evo, α9 AI processor, Dolby Vision", "1799.00",
                                "Electronics", 15, "https://picsum.photos/seed/lgtv/600/400"));
                products.add(createProduct("Canon EOS R6", "20MP full-frame sensor, 4K 60fps video", "2499.00",
                                "Electronics",
                                12, "https://picsum.photos/seed/canon/600/400"));
                products.add(createProduct("Bose QuietComfort Earbuds",
                                "Premium wireless earbuds with noise cancellation",
                                "299.00", "Electronics", 55, "https://picsum.photos/seed/bose/600/400"));
                products.add(createProduct("Apple Watch Series 9", "Advanced health sensors, always-on Retina display",
                                "429.00", "Electronics", 80, "https://picsum.photos/seed/watch/600/400"));
                products.add(createProduct("Kindle Paperwhite", "6.8\" display, adjustable warm light, waterproof",
                                "139.99",
                                "Electronics", 120, "https://picsum.photos/seed/kindle/600/400"));
                products.add(createProduct("GoPro Hero 12", "5.3K60 video, HyperSmooth 6.0 stabilization", "399.00",
                                "Electronics", 35, "https://picsum.photos/seed/gopro/600/400"));
                products.add(createProduct("Sony PlayStation 5", "Ultra-high speed SSD, ray tracing, 4K gaming",
                                "499.99",
                                "Electronics", 30, "https://picsum.photos/seed/ps5/600/400"));

                // FASHION (15 products)
                products.add(createProduct("Premium Cotton T-Shirt",
                                "100% organic Supima cotton, crew neck, breathable",
                                "29.99", "Fashion", 200, "https://picsum.photos/seed/tshirt/600/400"));
                products.add(createProduct("Levi's 501 Original Jeans",
                                "Classic straight fit, button fly, iconic denim",
                                "89.99", "Fashion", 150, "https://picsum.photos/seed/jeans/600/400"));
                products.add(createProduct("Nike Air Max 270", "Max Air unit, breathable mesh upper, stylish design",
                                "150.00",
                                "Fashion", 100, "https://picsum.photos/seed/nike/600/400"));
                products.add(createProduct("Leather Chelsea Boots",
                                "Genuine leather, elastic side panels, Goodyear welt",
                                "189.00", "Fashion", 45, "https://picsum.photos/seed/boots/600/400"));
                products.add(createProduct("Cashmere Sweater", "100% pure cashmere, ribbed cuffs, ultra-soft", "149.00",
                                "Fashion", 60, "https://picsum.photos/seed/sweater/600/400"));
                products.add(createProduct("Wool Trench Coat", "Water-resistant, belted waist, timeless style",
                                "249.00",
                                "Fashion", 35, "https://picsum.photos/seed/trench/600/400"));
                products.add(createProduct("Silk Evening Dress", "Elegant floor-length, adjustable straps, lined",
                                "299.00",
                                "Fashion", 25, "https://picsum.photos/seed/dress/600/400"));
                products.add(createProduct("Designer Sunglasses", "UV400 protection, polarized lenses, metal frame",
                                "189.00",
                                "Fashion", 80, "https://picsum.photos/seed/sunglasses/600/400"));
                products.add(createProduct("Leather Handbag",
                                "Genuine leather, multiple compartments, adjustable strap",
                                "329.00", "Fashion", 40, "https://picsum.photos/seed/handbag/600/400"));
                products.add(createProduct("Wool Blazer", "Tailored fit, notch lapel, two-button closure", "279.00",
                                "Fashion",
                                50, "https://picsum.photos/seed/blazer/600/400"));
                products.add(createProduct("Running Shoes", "Responsive cushioning, breathable upper, durable outsole",
                                "129.00", "Fashion", 120, "https://picsum.photos/seed/running/600/400"));
                products.add(createProduct("Denim Jacket", "Classic trucker style, cotton denim, chest pockets",
                                "89.00",
                                "Fashion", 90, "https://picsum.photos/seed/denim/600/400"));
                products.add(createProduct("Silk Scarf", "100% Mulberry silk, vibrant patterns, square shape", "69.00",
                                "Fashion", 70, "https://picsum.photos/seed/scarf/600/400"));
                products.add(createProduct("Formal Oxford Shoes", "Premium leather, blake stitched, polished finish",
                                "199.00",
                                "Fashion", 55, "https://picsum.photos/seed/oxford/600/400"));
                products.add(createProduct("Athletic Shorts", "Moisture-wicking, elastic waistband, side pockets",
                                "39.99",
                                "Fashion", 150, "https://picsum.photos/seed/shorts/600/400"));

                // HOME & KITCHEN (10 products)
                products.add(createProduct("Espresso Machine", "Professional-grade, 15-bar pump, milk frother",
                                "699.00",
                                "Home & Kitchen", 25, "https://picsum.photos/seed/espresso/600/400"));
                products.add(createProduct("Ergonomic Office Chair",
                                "Lumbar support, breathable mesh, adjustable height",
                                "299.00", "Home & Kitchen", 40, "https://picsum.photos/seed/chair/600/400"));
                products.add(createProduct("Air Purifier HEPA",
                                "True HEPA filter, removes 99.9% particles, quiet operation",
                                "249.00", "Home & Kitchen", 50, "https://picsum.photos/seed/purifier/600/400"));
                products.add(createProduct("Stand Mixer", "10-speed, 5-quart bowl, multiple attachments", "349.00",
                                "Home & Kitchen", 30, "https://picsum.photos/seed/mixer/600/400"));
                products.add(createProduct("Ceramic Dinnerware Set", "16-piece set, microwave safe, elegant design",
                                "129.00",
                                "Home & Kitchen", 60, "https://picsum.photos/seed/dinnerware/600/400"));
                products.add(createProduct("Memory Foam Mattress", "Queen size, gel-infused, pressure relief", "799.00",
                                "Home & Kitchen", 20, "https://picsum.photos/seed/mattress/600/400"));
                products.add(createProduct("Bamboo Cutting Board Set",
                                "3-piece set, knife-friendly, naturally antibacterial",
                                "49.99", "Home & Kitchen", 100, "https://picsum.photos/seed/cutting/600/400"));
                products.add(createProduct("Stainless Steel Cookware Set",
                                "10-piece set, induction compatible, dishwasher safe", "399.00", "Home & Kitchen", 35,
                                "https://picsum.photos/seed/cookware/600/400"));
                products.add(createProduct("Smart Thermostat", "Wi-Fi enabled, energy-saving, voice control", "199.00",
                                "Home & Kitchen", 70, "https://picsum.photos/seed/thermostat/600/400"));
                products.add(createProduct("Ceramic Vase Set", "Hand-thrown, matte finish, modern minimalist", "59.00",
                                "Home & Kitchen", 80, "https://picsum.photos/seed/vase/600/400"));

                // FITNESS & SPORTS (8 products)
                products.add(createProduct("Yoga Mat Pro", "6mm thick, eco-friendly, non-slip surface", "79.00",
                                "Fitness", 120,
                                "https://picsum.photos/seed/yoga/600/400"));
                products.add(createProduct("Adjustable Dumbbells", "5-50 lbs per hand, quick-change system", "449.00",
                                "Fitness", 25, "https://picsum.photos/seed/dumbbell/600/400"));
                products.add(createProduct("Fitness Tracker", "Heart rate monitor, sleep tracking, GPS", "149.00",
                                "Fitness",
                                90, "https://picsum.photos/seed/tracker/600/400"));
                products.add(createProduct("Resistance Bands Set", "5-band set, different resistance levels, portable",
                                "29.99",
                                "Fitness", 150, "https://picsum.photos/seed/bands/600/400"));
                products.add(createProduct("Treadmill Electric", "3.0 HP motor, 12 programs, foldable design", "899.00",
                                "Fitness", 15, "https://picsum.photos/seed/treadmill/600/400"));
                products.add(createProduct("Exercise Bike", "Magnetic resistance, LCD monitor, adjustable seat",
                                "399.00",
                                "Fitness", 30, "https://picsum.photos/seed/bike/600/400"));
                products.add(createProduct("Foam Roller", "High-density foam, muscle recovery, trigger points", "34.99",
                                "Fitness", 140, "https://picsum.photos/seed/roller/600/400"));
                products.add(createProduct("Sports Water Bottle", "64oz, insulated, leak-proof, BPA-free", "39.99",
                                "Fitness",
                                200, "https://picsum.photos/seed/bottle/600/400"));

                // BEAUTY & PERSONAL CARE (7 products)
                products.add(createProduct("Skincare Gift Set", "Cleanser, serum, moisturizer, natural ingredients",
                                "129.00",
                                "Beauty", 60, "https://picsum.photos/seed/skincare/600/400"));
                products.add(createProduct("Hair Dryer Professional", "Ionic technology, multiple heat settings, quiet",
                                "149.00", "Beauty", 45, "https://picsum.photos/seed/dryer/600/400"));
                products.add(createProduct("Electric Toothbrush", "Sonic cleaning, 5 modes, 30-day battery", "99.99",
                                "Beauty",
                                85, "https://picsum.photos/seed/toothbrush/600/400"));
                products.add(createProduct("Makeup Brush Set", "12-piece professional set, synthetic bristles", "79.00",
                                "Beauty", 70, "https://picsum.photos/seed/makeup/600/400"));
                products.add(createProduct("Facial Cleansing Brush", "Sonic vibration, waterproof, 3 speed settings",
                                "89.00",
                                "Beauty", 55, "https://picsum.photos/seed/facial/600/400"));
                products.add(createProduct("Aromatherapy Diffuser", "Ultrasonic, LED lights, 400ml capacity", "49.99",
                                "Beauty",
                                100, "https://picsum.photos/seed/diffuser/600/400"));
                products.add(createProduct("Hair Straightener", "Ceramic plates, adjustable temperature, fast heating",
                                "79.99",
                                "Beauty", 65, "https://picsum.photos/seed/straightener/600/400"));

                // BOOKS & LEISURE (10 products)
                products.add(createProduct("Coffee Table Art Book", "Contemporary photography, hardcover, 300 pages",
                                "59.00",
                                "Books", 50, "https://picsum.photos/seed/artbook/600/400"));
                products.add(createProduct("Cookbook Collection", "500+ recipes, full-color photos, step-by-step",
                                "45.00",
                                "Books", 80, "https://picsum.photos/seed/cookbook/600/400"));
                products.add(createProduct("Instant Camera", "Auto-focus, built-in flash, instant prints", "129.00",
                                "Leisure",
                                60, "https://picsum.photos/seed/camera/600/400"));
                products.add(createProduct("Board Game Collection", "Strategy game, 2-4 players, 60-minute playtime",
                                "49.99",
                                "Leisure", 90, "https://picsum.photos/seed/boardgame/600/400"));
                products.add(createProduct("Acoustic Guitar", "Spruce top, mahogany back, steel strings", "299.00",
                                "Leisure",
                                25, "https://picsum.photos/seed/guitar/600/400"));
                products.add(createProduct("Puzzle 1000 Pieces", "Premium quality, vibrant artwork, thick pieces",
                                "24.99",
                                "Leisure", 120, "https://picsum.photos/seed/puzzle/600/400"));
                products.add(createProduct("Digital Drawing Tablet",
                                "8192 pressure levels, battery-free pen, tilt support",
                                "299.00", "Leisure", 40, "https://picsum.photos/seed/tablet/600/400"));
                products.add(createProduct("Binoculars HD", "10x42 magnification, waterproof, bird watching", "149.00",
                                "Leisure", 55, "https://picsum.photos/seed/binoculars/600/400"));
                products.add(createProduct("Telescope Beginner", "70mm aperture, tripod included, moon filter",
                                "199.00",
                                "Leisure", 30, "https://picsum.photos/seed/telescope/600/400"));
                products.add(createProduct("Art Supply Set", "Professional quality, 100+ pieces, carrying case",
                                "89.00",
                                "Leisure", 70, "https://picsum.photos/seed/artsupply/600/400"));

                productRepository.saveAll(products);
                System.out.println("🎉 Successfully seeded " + products.size()
                                + " products with REAL photographic images!");
        }

        private Product createProduct(String name, String description, String price, String category, int stock,
                        String imageUrl) {
                byte[] imageBytes = null;
                String contentType = "image/jpeg";
                try {
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                                imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
                                System.out.println("Downloaded image for: " + name);
                        }
                } catch (Exception e) {
                        System.err.println("Failed to download image from " + imageUrl + ": " + e.getMessage());
                }

                Product p = Product.builder()
                                .name(name)
                                .description(description)
                                .price(new BigDecimal(price))
                                .category(category)
                                .stockQuantity(stock)
                                .mainImageUrl(imageUrl)
                                .mainImage(imageBytes)
                                .imageType(contentType)
                                .build();

                p.setRating(3.5 + (Math.random() * 1.5)); // 3.5 to 5.0
                p.setIsPopular(Math.random() > 0.5); // 50% are popular
                p.setCreatedAt(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
                return p;
        }
}