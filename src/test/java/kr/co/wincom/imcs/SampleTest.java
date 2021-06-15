package kr.co.wincom.imcs;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={"test-servlet-context.xml", "test-root-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 메소드명으로 실행 순서 지정
public class SampleTest
{
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;
	
	@Before
    public void init() throws Exception
    {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
  
   @Test
   public void sampleTest_2() throws Exception
   {	   
	   MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/sample");
	   request.param("testValue", "한글임다.2");
	   request.characterEncoding("UTF-8");
	   request.accept(MediaType.TEXT_PLAIN);
	   
       ResultActions result = this.mockMvc.perform(request);
       result.andDo(MockMvcResultHandlers.print());
       result.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
       result.andExpect(MockMvcResultMatchers.status().isOk());
    }
   
   @Test
   public void sampleTest_1() throws Exception
   {	   
	   MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/sample?testValue=한글임다.1");
	   request.characterEncoding("UTF-8");
	   request.accept(MediaType.TEXT_PLAIN);
	   
       ResultActions result = this.mockMvc.perform(request);
       result.andDo(MockMvcResultHandlers.print());
       result.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"));
       result.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
