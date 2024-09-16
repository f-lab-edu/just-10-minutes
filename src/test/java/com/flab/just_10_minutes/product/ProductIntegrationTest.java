package com.flab.just_10_minutes.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.product.domain.Product;
import com.flab.just_10_minutes.product.dto.ProductDto;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductDao;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.user.infrastructure.repository.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
    
import static com.flab.just_10_minutes.user.fixture.UserTestFixture.createSellerEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductDao productDao;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String ENROLL_URL = "/products/enroll";

    public void saveSeller() {
        UserEntity user = createSellerEntity();
        userMapper.save(user);
    }

    public ProductDto createTestProductDto(String title, String description, String sellerId, Long originalPrice, Long totalStock) {
        return ProductDto.builder()
                .title(title)
                .description(description)
                .sellerId(sellerId)
                .originalPrice(originalPrice)
                .totalStock(totalStock)
                .build();
    }

    public static final String TITLE = "testProduct";
    public static final String DESCRIPTION = "test";
    public static final String SELLER_ID = "sellerId";
    public static final Long ORIGINAL_PRICE = 500000L;
    public static final Long TOTAL_STOCK = 100L;

    @Test
    public void enroll_실패_seller_가_존재하지_않음() throws Exception {
        //given
        final String url = ENROLL_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestProductDto(TITLE, DESCRIPTION, SELLER_ID, ORIGINAL_PRICE, TOTAL_STOCK)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isNotFound());
    }

    @Test
    public void enroll_성공() throws Exception {
        //setUp
        saveSeller();

        //given
        final String url = ENROLL_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestProductDto(TITLE, DESCRIPTION, SELLER_ID, ORIGINAL_PRICE, TOTAL_STOCK)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isOk());
        String content = resultAction.andReturn().getResponse().getContentAsString();
        Product product = mapper.readValue(content, Product.class);
        assertThat(productDao.fetch(product.getId())).isNotNull();

    }
}
