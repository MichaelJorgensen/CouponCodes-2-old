package net.lala.CouponCodes.misc;

import java.util.Random;

public class Misc {

	private static Random random = new Random();
	private static final String seed = "QWERTYUIPADFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm12346789";
	
	public static String generateName() {
		return generateName(5, "");
	}

	public static String generateName(int len, String prefix) {
		random.setSeed(random.nextLong() + len);
		String ref = prefix;
		while (ref.length() < len)
			ref = ref + seed.charAt(random.nextInt(seed.length()));
		return ref;
	}
}
