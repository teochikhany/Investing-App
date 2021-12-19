package usj.genielogiciel.investingapp.controller;

import org.apache.tomcat.util.http.FastHttpDateFormat;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import usj.genielogiciel.investingapp.model.Stock;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class StockControllerTest
{
    private final String url = "/api/v1/stocks/";
    private final MockMvc mockMvc;
    private final String accessToken;

    @Autowired
    StockControllerTest(MockMvc mockMvc) throws Exception
    {
        this.mockMvc = mockMvc;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/login");
        request.contentType(MediaType.APPLICATION_FORM_URLENCODED);

        request.param("username", "teo");
        request.param("password", "12345");

        MvcResult result = mockMvc.perform(request).andReturn();
        String responce = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(responce);
        accessToken = "Bearer " + json.get("access_token");
    }

    @Test
    void getAllStocksForbidden() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void getStockForbidden() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url+"1"));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void addStockForbidden() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(post(url));
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void getAllStocks() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url).header("Authorization", accessToken));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void getStock() throws Exception
    {
        JSONObject stock = new JSONObject();
        stock.put("id", "0");
        stock.put("name", "teo");
        stock.put("ticker", "getStock");
        stock.put("price", "20");

        MvcResult result = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int newStockID = Integer.parseInt(result.getResponse().getContentAsString());

        ResultActions resultActions = mockMvc.perform(get(url+newStockID).header("Authorization", accessToken));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void addStock() throws Exception
    {
        JSONObject stock = new JSONObject();
        stock.put("id", "0");
        stock.put("name", "teo");
        stock.put("ticker", "ticker");
        stock.put("price", "20");

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock.toString())
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isCreated());
    }
}
