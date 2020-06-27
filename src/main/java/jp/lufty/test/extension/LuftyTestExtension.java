package jp.lufty.test.extension;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LuftyTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

	private long nanotime = 0;

	@Override
	public void beforeTestExecution(ExtensionContext context) throws Exception {
		System.out.println("#[TEST_START] " + context.getDisplayName());
		this.nanotime = System.nanoTime();
	}

	@Override
	public void afterTestExecution(ExtensionContext context) throws Exception {
		long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.nanotime);
		System.out.println("#[TEST_END]   " + context.getDisplayName() + " time=" + millis + "ms");
	}
}
