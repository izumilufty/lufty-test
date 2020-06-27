package jp.lufty.test.extension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

import lombok.Getter;
import lombok.Setter;

public class ExecutorExtension extends ExtensionBase<ExecutorService> {

	@Getter
	private ExecutorService executor;

	@Setter
	private int threads = 3;

	public ExecutorExtension(int threads) {
		this.threads = threads;
	}

	@Override
	public void beforeEachCore(ExtensionContext context) throws Exception {
		this.executor = Executors.newFixedThreadPool(this.threads, new ExtensionThreadFactory());
	}

	@Override
	public void afterEachCore(ExtensionContext context) throws Exception {
		if (this.executor != null) {
			this.executor.shutdownNow();
		}
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return this.executor;
	}

	@Override
	public Class<ExecutorService> getParamType() {
		return ExecutorService.class;
	}

	private static class ExtensionThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		ExtensionThreadFactory() {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "test-" + poolNumber.getAndIncrement() + "-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}

}
