package com.flab.just_10_minutes.product;

import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.infrastructure.entity.ProductEntity;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductMapper;
import com.flab.just_10_minutes.product.service.StockService;
import com.flab.just_10_minutes.user.domain.Customer;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.user.infrastructure.repository.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.*;

import static com.flab.just_10_minutes.user.fixture.UserTestFixture.createSellerEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockDecreaseConcurrencyTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private StockService target;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = saveProductWithSeller();
    }

    @AfterEach
    public void clean() {
        userMapper.delete(product.getSeller().getLoginId());
        productDao.delete(product.getId());
    }

    public Product saveProductWithSeller() {
        UserEntity user = createSellerEntity();
        userMapper.save(user);
        Product product = createProduct(Customer.from(UserEntity.toDomain(user)));
        return productDao.save(product);
    }

    public static Product createProduct(Customer seller) {
        return Product.builder()
                .title("testProduct")
                .description("test")
                .seller(seller)
                .originalPrice(500000L)
                .totalStock(100L)
                .purchasedStock(0L)
                .build();
    }

    public static ProductEntity createProductEntity(String sellerId) {
        return ProductEntity.builder()
                .title("testProduct")
                .description("test")
                .sellerId(sellerId)
                .originalPrice(5000L)
                .totalStock(100L)
                .purchasedStock(0L)
                .build();
    }

    @Test
    public void 동시성_상황에서_재고_차감_성공() throws Exception {
        // given
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        //when
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    target.decreaseStock(product.getId(), 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(100L, TimeUnit.SECONDS);
        Product fetchProduct = productDao.fetch(product.getId());

        //then
        assertThat(fetchProduct.getPurchasedStock()).isEqualTo(fetchProduct.getTotalStock());
    }

}
