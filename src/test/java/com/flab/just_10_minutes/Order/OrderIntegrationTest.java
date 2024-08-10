package com.flab.just_10_minutes.Order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.Order.dto.OrderDto;
import com.flab.just_10_minutes.Payment.domain.BillingData;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
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

import static com.flab.just_10_minutes.User.UserTestFixture.createUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private UserDao target;

    @BeforeEach
    public void setUp() {
        target = new UserDao(userMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId);
        target.save(user);
    }

    public void userSetUp() {
        saveUser(SELLER);
        saveUser(BUYER);
    }

    public OrderDto createOrderDto() {
        return OrderDto.builder()
                .sellerLoginId(SELLER)
                .buyerLoginId(BUYER)
                .productId(1L)
                .requestDecreasedStock(2L)
                .requestUsedPoint(0L)
                .billingData(BillingData.builder()
                                .pg("")
                                .cardNumber("")
                                .expiry("")
                                .birth("")
                                .pwd_2Digit("")
                                .build())
                .build();
    }

    @Test
    public void order_성공() throws Exception {
        //setUp
        userSetUp();
        //given
        final String url = ORDER_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createOrderDto()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isOk());
    }


}
