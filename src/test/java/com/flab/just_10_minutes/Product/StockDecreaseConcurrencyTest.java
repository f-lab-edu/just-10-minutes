package com.flab.just_10_minutes.Product;

import com.flab.just_10_minutes.Product.domain.Product;
import com.flab.just_10_minutes.Product.infrastructure.ProductDao;
import com.flab.just_10_minutes.Product.infrastructure.ProductMapper;
import com.flab.just_10_minutes.Product.service.StockService;
import com.flab.just_10_minutes.User.domain.Customer;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.flab.just_10_minutes.User.fixture.UserTestFixture.createSeller;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StockDecreaseConcurrencyTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    private ProductDao productDao;

    private StockService target;

    private Product product;

    @BeforeEach
    public void setUp() {
        productDao = new ProductDao(productMapper, userMapper);
        target = new StockService(productDao);
        product = saveProductWithSeller();
    }

    @AfterEach
    public void clean() {
        userMapper.delete(product.getSeller().getLoginId());
        productDao.delete(product.getId());
    }

    public Product saveProductWithSeller() {
        User user = createSeller();
        userMapper.save(user);
        Product product = createProduct(Customer.from(user));
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

    @Test
    public void 재고_차감_성공() throws Exception {
        // given
        int numberOfThreads = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(20);
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

        latch.await();

        Product fetchProduct = productDao.fetch(product.getId());

        //then
        assertThat(fetchProduct.getPurchasedStock()).isEqualTo(fetchProduct.getTotalStock());
    }
}
