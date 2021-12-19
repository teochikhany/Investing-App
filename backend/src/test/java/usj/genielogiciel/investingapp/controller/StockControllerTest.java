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
        val stock = createStock("teo", "getStock", "20");

        MvcResult result = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int newStockID = Integer.parseInt(result.getResponse().getContentAsString());

        ResultActions resultActions = mockMvc.perform(get(url+newStockID).header("Authorization", accessToken));
        resultActions.andExpect(status().isOk());
    }

    @Test
    void addStock() throws Exception
    {
        val stock = createStock("add", "addStock", "30");

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isCreated());
    }

    private String createStock(String name, String ticker, String price) throws JSONException
    {
        JSONObject stock = new JSONObject();
        stock.put("id", "0");
        stock.put("name", name);
        stock.put("ticker", ticker);
        stock.put("price", price);

        return stock.toString();
    }
}
