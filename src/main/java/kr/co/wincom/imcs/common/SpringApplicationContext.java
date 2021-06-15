package kr.co.wincom.imcs.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * http://devyongsik.tistory.com/589 참조
 *
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware
{
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		context = applicationContext;
	}
	
	public static Object getBean(String beanName)
	{
		return context.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> requiredType)
	{
		return context.getBean(beanName, requiredType);
	}

}