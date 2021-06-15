package kr.co.wincom.curation.common.schedule;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;

/**
 * @author medialog
 * @date 2015. 6. 17.
 * kr.co.wincom.curation.common.scheduled.ScheduleService
 */
@Service
public class ScheduleService {
	
	/**
	 * <pre>
	 * 1. 로그파일이 생성되지 않았으면 빈파일이라도 생성하도록 
	 * 2. 00시 00분 01초 작동
	 * </pre>
	 * @author medialog
	 * @date 2015. 6. 17.
	 * @method autoCheckFile
	 * @return void
	 */
	@Scheduled(cron="1 0 0 * * *")
	public void autoCheckFile(){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		for (Logger logger : loggerContext.getLoggerList()) {
			Iterator<Appender<ILoggingEvent>> logAppend = logger.iteratorForAppenders();
			while (logAppend.hasNext()) {
				Appender<ILoggingEvent> logEv = logAppend.next();
				if (logEv instanceof RollingFileAppender) {
					RollingFileAppender rollingfileAppender = (RollingFileAppender) logEv;
					/**
					 * TLO로그는 요청이 없어도 빈파일이라도 남겨야한다.
					 * (TLO로그 NAS에 빈파일이라도 넘겨야 함.)
					 */
					if(!StringUtils.startsWith(rollingfileAppender.getName(),"TLO_")){ 
						continue;
					}
					TriggeringPolicy triggeringPolicy = rollingfileAppender.getTriggeringPolicy();
					if (triggeringPolicy instanceof TimeBasedRollingPolicy) {		
						 if (triggeringPolicy.isTriggeringEvent(null, null)) {
							 rollingfileAppender.rollover();
						 }
					}
				}
			}
		}
	}

}
