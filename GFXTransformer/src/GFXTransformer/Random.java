package GFXTransformer;

import java.util.concurrent.atomic.AtomicLong;

public class Random {
	private AtomicLong seed;

	private final static long multiplier = 0x5DEECE66DL + 0x12345678;
	private final static long addend = 0xBL;
	private final static long mask = (1L << 48) - 1;

	public Random(long seed) {
		this.seed = new AtomicLong(0L);
		setSeed(seed);
	}

	synchronized public void setSeed(long seed) {
		seed = (seed ^ multiplier) & mask;
		this.seed.set(seed);
	}

	protected int next(int bits) {
		long oldseed, nextseed;
		AtomicLong seed = this.seed;
		do {
			oldseed = seed.get();
			nextseed = (oldseed * multiplier + addend) & mask;
		} while (!seed.compareAndSet(oldseed, nextseed));
		return (int) (nextseed >>> (48 - bits));
	}

	public int nextInt() {
		return next(32);
	}

	public int nextInt(int n) {
		if ((n & -n) == n) // i.e., n is a power of 2
			return (int) ((n * (long) next(31)) >> 31);

		int bits, val;
		do {
			bits = next(31);
			val = bits % n;
		} while (bits - val + (n - 1) < 0);
		return val;
	}

}