package jp.lufty.test.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import lombok.Getter;

public class LogExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

	private static final Logger LOGGER;

	@Getter
	private static final List<ILoggingEvent> events = Collections.synchronizedList(new ArrayList<>());

	public static final ReentrantLock lock = new ReentrantLock();

	static {
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get("ch.qos.logback.classic.Logger");
			CtMethod m = cc.getDeclaredMethod("callAppenders");
			m.setModifiers(Modifier.SYNCHRONIZED);
			m.insertBefore(//
					"jp.lufty.test.extension.LogExtension.lock.lock();" + //
					// "try {" + //
							"jp.lufty.test.extension.LogExtension.addEvent(event);" //
			// "} finally {jp.lufty.test.extension.LogExtension.lock.unlock();}" //
			);
			m.insertAfter("jp.lufty.test.extension.LogExtension.lock.unlock();");
			cc.toClass(LogExtension.class.getClassLoader(), LogExtension.class.getProtectionDomain());

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		LOGGER = LoggerFactory.getLogger(LogExtension.class);
	}

	public synchronized static void addEvent(ILoggingEvent event) {
		events.add(event);
	}

	@Override
	public synchronized void beforeEach(ExtensionContext context) throws Exception {
		events.clear();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		LOGGER.debug("log events=" + events);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == LogResult.class;
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {

		return new LogResult();
	}

	synchronized List<String> getLogMessage(Level... levels) {
		assertValidLevel(levels);
		return events.stream().filter(event -> {
			return -1 < Arrays.binarySearch(levels, event.getLevel());
		}).map(event -> event.getFormattedMessage()).collect(Collectors.toList());
	}

	synchronized List<ILoggingEvent> getLoggingEvent(Level... levels) {
		assertValidLevel(levels);
		return events.stream().filter(event -> {
			return -1 < Arrays.binarySearch(levels, event.getLevel(), new LevelComparater());
		}).collect(Collectors.toList());
	}

	private static void assertValidLevel(Level... levels) {
		for (Level level : levels) {
			switch (level.toString()) {
			case "DEBUG":
			case "INFO":
			case "TRACE":
			case "ERROR":
			case "WARN":
				// 何もしない
				return;
			default:
				throw new IllegalArgumentException(level.toString());
			}
		}
	}

	private static class LevelComparater implements Comparator<Level> {
		@Override
		public int compare(Level o1, Level o2) {
			return o1.toString().equals(o2.toString()) ? 0 : -1;
		}
	}

	public class LogResult {
		/**
		 * ログレベルを指定してログを取得する。
		 * 
		 * @param levels ログレベル
		 * @return ログのリスト
		 */
		public List<String> getLogMessage(Level... levels) {
			return LogExtension.this.getLogMessage(levels);
		}

		/***
		 * ログレベルを指定してロギングイベントを取得する。
		 * 
		 * @param levels ログレベル
		 * @return ログのリスト
		 */
		public List<ILoggingEvent> getLoggingEvent(Level... levels) {
			return LogExtension.this.getLoggingEvent(levels);
		}
	}

}
