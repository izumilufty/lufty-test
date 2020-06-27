package jp.lufty.test.matcher;

import org.hamcrest.Matcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LuftyMatchers {

	public static Matcher<ILoggingEvent> isLog(Level level, Matcher<? super String> strMatcher) {
		return LogMatcher.isLog(level, strMatcher);
	}

	public static Matcher<ILoggingEvent> isLog(Level level, String string) {
		return LogMatcher.isLog(level, string);
	}
	
	public static Matcher<ILoggingEvent> isLog(Matcher<? super String> strMatcher) {
		return LogMatcher.isLog(strMatcher);
	}

	public static Matcher<ILoggingEvent> isLog(String string) {
		return LogMatcher.isLog(string);
	}
	
//	public static Matcher<java.lang.Iterable<? extends ILoggingEvent>>  isNoError(ILoggingEvent ... events ) {
//		
//	}
}
