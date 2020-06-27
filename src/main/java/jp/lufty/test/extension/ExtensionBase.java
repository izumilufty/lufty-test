package jp.lufty.test.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public abstract class ExtensionBase<T> implements AfterEachCallback, BeforeEachCallback, ParameterResolver {

	public abstract void beforeEachCore(ExtensionContext context) throws Exception;

	public abstract void afterEachCore(ExtensionContext context) throws Exception;

	public abstract Class<T> getParamType();

	@Override
	public final void beforeEach(ExtensionContext context) throws Exception {
		beforeEachCore(context);
		System.out.println("#[Extension] beforeEach=" + this.getClass().getSimpleName());
	}

	@Override
	public final void afterEach(ExtensionContext context) throws Exception {
		afterEachCore(context);
		System.out.println("#[Extension] afterEeach=" + this.getClass().getSimpleName());
	}

	@Override
	public final boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return (parameterContext.getParameter().getType() == getParamType());
	}

}
