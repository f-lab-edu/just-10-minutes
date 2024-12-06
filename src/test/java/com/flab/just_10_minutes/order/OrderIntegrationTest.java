package com.flab.just_10_minutes.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.order.dto.OrderDto;
import com.flab.just_10_minutes.order.dto.OrderReceiptDto;
import com.flab.just_10_minutes.payment.dto.BillingRequest;
import com.flab.just_10_minutes.product.infrastructure.entity.ProductEntity;
import com.flab.just_10_minutes.product.infrastructure.repository.ProductMapper;
import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.user.infrastructure.repository.UserMapper;
import com.flab.just_10_minutes.common.iamport.IamportConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.payment.dto.BillingRequest.PG.NICE;
import static com.flab.just_10_minutes.product.StockDecreaseConcurrencyTest.createProductEntity;
import static com.flab.just_10_minutes.user.fixture.UserTestFixture.createUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String ORDER_URL = "/orders";
    private final String SELLER = "seller";
    private final String BUYER = "buyer";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IamportConfig iamportConfig;

    private UserDao target;

    @BeforeEach
    public void setUp() {
        target = new UserDao(userMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId, 100L);
        target.save(user);
    }

    public Long saveProduct() {
        ProductEntity entity = createProductEntity(SELLER);
        productMapper.save(entity);
        return entity.getId();
    }

    public void productSetUp() {
        saveProduct();
    }

    public void userSetUp() {
        saveUser(SELLER);
        saveUser(BUYER);
    }

    public OrderDto createOrderDto(Long productId) {
        return OrderDto.builder()
                .sellerLoginId(SELLER)
                .buyerLoginId(BUYER)
                .productId(productId)
                .requestDecreasedStock(2L)
                .requestUsedPoint(-100L)
                .billingRequest(BillingRequest.builder()
                        .pg(NICE)
                        .cardNumber(iamportConfig.getCardNumber())
                        .expiry(iamportConfig.getCardExpiry())
                        .birth(iamportConfig.getCardBirth())
                        .pwd2Digit(iamportConfig.getCardPwd2Digit())
                        .build())
                .build();
    }

    @Test
    public void order_성공() throws Exception {
        //setUp
        userSetUp();
        Long productId = saveProduct();

        //given
        final String url = ORDER_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createOrderDto(productId)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isOk());

        String content = resultAction.andReturn().getResponse().getContentAsString();
        OrderReceiptDto historyResponse = mapper.readValue(content, OrderReceiptDto.class);
        UserEntity buyerEntity = userMapper.findByLoginId(BUYER);
        ProductEntity productEntity = productMapper.findById(productId);

        log.info(buyerEntity.toString());
        log.info(productEntity.toString());
        log.info(historyResponse.toString());
    }


}
