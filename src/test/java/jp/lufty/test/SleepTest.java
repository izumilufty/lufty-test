package jp.lufty.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class SleepTest {

	@Test
	void test_millis() {

		long startTime = System.nanoTime();

		Sleep.byMillis(100);

		assertThat(System.nanoTime() - startTime, greaterThanOrEqualTo(TimeUnit.MILLISECONDS.toNanos(100)));
	}

	@Test
	void test_seconds() {

		long startTime = System.nanoTime();

		Sleep.bySeconds(1);

		assertThat(System.nanoTime() - startTime, greaterThanOrEqualTo(TimeUnit.SECONDS.toNanos(1)));
	}

	@Test
	void test_nanos() {

		long startTime = System.nanoTime();

		Sleep.byNanos(1000);

		assertThat(System.nanoTime() - startTime, greaterThanOrEqualTo(1000l));
	}
}
