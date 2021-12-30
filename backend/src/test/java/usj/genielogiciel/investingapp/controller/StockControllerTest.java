package usj.genielogiciel.investingapp.controller;

import lombok.val;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest                 // loads complete application context and injects all the beans which can be slow.
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class StockControllerTest
{
    // TODO: add documentation to the appropriate unit tests, links:
    // https://docs.spring.io/spring-restdocs/docs/current/reference/html5/#getting-started-documentation-snippets-setup-junit-5
    // https://dimitr.im/spring-rest-docs

    @Autowired
    private MockMvc mockMvc;
    private final String url = "/api/v1/stocks/";
    private String accessToken;

    @BeforeEach
    public void setUp() throws Exception
    {
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
        resultActions.andExpect(status().isForbidden()).andDo(document("getAllStocksForbidden"));
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
        resultActions.andExpect(status().isOk()).andDo(document("getAllStocks"));
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
    void getInvalidStock() throws Exception
    {
        ResultActions resultActions = mockMvc.perform(get(url+999).header("Authorization", accessToken));
        resultActions.andExpect(status().isNotFound());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("No Stock with this id: " + 999, json.getString("message"));
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

    @Test
    void addStockEmptyName() throws Exception
    {
        val stock = createStock("", "addStockEmptyName", "30");

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Name cannot be empty\n", json.getString("message"));
    }

    @Test
    void addStockEmptyTicker() throws Exception
    {
        val stock = createStock("addStockEmptyTicker", "", "30");

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Ticker cannot be empty\n", json.getString("message"));
    }

    @Test
    void addStockDuplicateTicker() throws Exception
    {
        val stock = createStock("addStock", "addStockDuplicateTicker", "30");

        ResultActions resultActions2 = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions2.andExpect(status().isCreated());

        ResultActions resultActions = mockMvc.perform(post(url)
                .header("Authorization", accessToken)
                .content(stock)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions.andExpect(status().isBadRequest());

        final MvcResult mvcResult = resultActions.andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        assertEquals("Ticker needs to be unique", json.getString("message"));
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
