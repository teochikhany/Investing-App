package usj.genielogiciel.investingapp.controller;

import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest
{
    private final String url = "/api/v1";
    private final MockMvc mockMvc;
    private final String accessToken;

    @Autowired
    UserControllerTest(MockMvc mockMvc) throws Exception
    {
        this.mockMvc = mockMvc;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/v1/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "teo")
                .param("password", "12345");

        MvcResult result = mockMvc.perform(request).andReturn();
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        accessToken = "Bearer " + json.get("access_token");
    }

    @Test
    void addUser() throws Exception
    {
        val AppUser = createUser("addUser", "name", "password");

        ResultActions resultActions = mockMvc.perform(post(url+"/user/save")
                .content(AppUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void addUserEmptyName() throws Exception
    {
        val AppUser = createUser("addUserEmptyName", "", "password2");

        ResultActions resultActions = mockMvc.perform(post(url+"/user/save")
                .content(AppUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Name cannot be empty\n", json.getString("message"));
    }

    @Test
    void addUserEmptyUsername() throws Exception
    {
        val AppUser = createUser("", "addUserEmptyUsername", "password2");

        ResultActions resultActions = mockMvc.perform(post(url+"/user/save")
                .content(AppUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Username cannot be empty\n", json.getString("message"));
    }

    @Test
    void addUserEmptyPassword() throws Exception
    {
        val AppUser = createUser("username", "addUserEmptyPassword", "");

        ResultActions resultActions = mockMvc.perform(post(url+"/user/save")
                .content(AppUser)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Password cannot be empty\n", json.getString("message"));
    }

    @Test
    void getAuthUserForbidden() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url+"/user"));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void getAuthUser() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url+"/user").header("Authorization", accessToken));
        resultActions.andExpect(status().isOk());

        val response = resultActions.andReturn();
        assertEquals("teo", response.getResponse().getContentAsString());
    }

    private String createUser(String username, String name, String password) throws JSONException
    {
        JSONObject user = new JSONObject();
        user.put("id", "0");
        user.put("username", username);
        user.put("name", name);
        user.put("password", password);

        return user.toString();
    }
}
