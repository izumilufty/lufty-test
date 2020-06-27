package jp.lufty.test.matcher;

import static org.hamcrest.Matchers.is;

import java.util.Objects;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogMatcher extends BaseMatcher<ILoggingEvent> {

	private final Level level;
	private final Matcher<? super String> strMatcher;

	public static Matcher<ILoggingEvent> isLog(Level level, Matcher<? super String> strMatcher) {
		return new LogMatcher(level, strMatcher);
	}

	public static Matcher<ILoggingEvent> isLog(Level level, String  string) {
		return new LogMatcher(level, is(string));
	}
	
	public static Matcher<ILoggingEvent> isLog(Matcher<? super String> strMatcher) {
		return new LogMatcher(null, strMatcher);
	}

	public static Matcher<ILoggingEvent> isLog(String string) {
		return new LogMatcher(null, is(string));
	}
	
	@Override
	public boolean matches(Object item) {
		ILoggingEvent actual = (ILoggingEvent) item;
		if ( this.level == null || Objects.equals(actual.getLevel(), this.level)) {
			return this.strMatcher.matches(actual.getFormattedMessage());
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		if (this.level != null) {
			description.appendText("log level is [" + this.level + "], ");
		}
		description.appendText("message " + this.strMatcher);
	}

}
