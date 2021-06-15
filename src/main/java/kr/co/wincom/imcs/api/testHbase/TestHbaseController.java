package kr.co.wincom.imcs.api.testHbase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestHbaseController {

	@ResponseBody
	@RequestMapping(value = "/servlets/testHbase", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	public ModelAndView doGet(
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		return new ModelAndView("testHbase/testHbase");
	}
	
	@ResponseBody
	@RequestMapping(value = "/servlets/testHbase/test", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
	public ModelAndView doGet2(
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception
	{
		return new ModelAndView("testHbase/testHbase");
	}
	
}
