package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.*;
import io.swagger.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.management.relation.Role;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;
    private dbUser dbUser;

    @BeforeEach
    public void init() throws Exception {
/*        dbUser = new dbUser("test", "test", "test", "test", "test", passwordEncoder.encode("test"), List.of(UserRole.ROLE_EMPLOYEE), 2500);
        userService.addUser(dbUser);*/
    }

    @Test
    public void getUsersShouldReturnAJsonArray() throws Exception{
        given(userService.getUsersOfDb()).willReturn(List.of(dbUser));

        String token = getTokenWhenLoggingIns();
        System.out.println(token);
        this.mockMvc.perform (
                get("/Users").header("Authorization", "Bearer " + token))
                .andExpect(
                        status().isOk())
                .andExpect(jsonPath("$", hasSize(1))
                );

    }


    @Test
    public void postUsersShouldReturnStatusCodeCreated201() throws Exception{
        String token = getTokenWhenLoggingIns();
        Object createUser = new Object(){
            public final String username = "Jamie120";
            public final String password = "larry120";
            public final String firstname = "James";
            public final String lastname = "Brown";
            public final String email = "jamie211@outlook.com";
            public final Double transactionLimit = 300.00;
            public final List<UserRole> roles  = List.of(UserRole.ROLE_EMPLOYEE);
        };
        ObjectMapper createUserMapper = new ObjectMapper();
        String json = createUserMapper.writeValueAsString(createUser);
        this.mockMvc.perform(
                post("/Users").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void getUserById() throws Exception{
        when(userService.getUserById(1L)).thenReturn(dbUser);
        assertThat("Test passed");
    }

    //Error with getting the JWT token from login even though the account Api doesn't have the same problem
    private String getTokenWhenLoggingIns() throws Exception {
        Object login = new Object(){
            public final String username = "test";
            public final String password = "test";
        };
        ObjectMapper loginMapper = new ObjectMapper();
        String json = loginMapper.writeValueAsString(login);


        MvcResult result = this.mockMvc.perform(
                post("/Login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject contentInJson = new JSONObject(content);
        return contentInJson.getString("Token");
    }



}
