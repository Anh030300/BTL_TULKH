package util;

import java.util.Random;

public class RandomUtil {

	private static Random r = new Random();

	public static double nextDouble() {
		return r.nextDouble();
	}

	public static int nextInt(int bound) {
		return r.nextInt(bound);
	}

}
