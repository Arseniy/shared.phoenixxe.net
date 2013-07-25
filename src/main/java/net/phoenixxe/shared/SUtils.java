package net.phoenixxe.shared;

import java.text.DateFormat;
import java.util.Date;

import net.phoenixxe.shared.Utils;

/**
 * Basic utils for server-side.
 * @author Arseniy Kaleshnyk, ver. 1.0
 */
public class SUtils extends Utils {
	public static long timeLong() {
		return System.currentTimeMillis();
	}
	
	public static String shortTime() {
		return DateFormat.
				getTimeInstance(DateFormat.SHORT).
						format(new Date());
	}
	
	public static String decodeException(final Exception e) {
		if (e == null) return NULL_EXCEPTION_STRING;
		else return DECODE_EXCEPTION_STRING.
				replaceFirst("CAUSE", e.getClass().getSimpleName()).
				replaceFirst("MESSAGE", e.getMessage());
	}
}
