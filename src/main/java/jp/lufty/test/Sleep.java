package jp.lufty.test;

import java.util.concurrent.TimeUnit;

public class Sleep {

	public static void byMillis(long millis) {
		byNanos(TimeUnit.MILLISECONDS.toNanos(millis));
	}

	public static void bySeconds(long secs) {
		byNanos(TimeUnit.SECONDS.toNanos(secs));
	}

	public static void byMinutes(long mins) {
		byNanos(TimeUnit.MINUTES.toNanos(mins));
	}
	
	public static void byNanos(long nanos) {
		try {
			TimeUnit.NANOSECONDS.sleep(nanos);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
