package GFXTransformer;

public class Random {
	private final static long multiplier = 0x5DEECE66DL + 0x12345678;
	private long seed;

	public Random(long seed) {
		setSeed(seed);
	}

	public synchronized void setSeed(long seed) {
        this.seed = (seed ^ multiplier) & ((1L << 48) - 1);
    }

	protected synchronized int next(int bits) {
        seed = (seed * multiplier + 0xbL) & ((1L << 48) - 1);
        return (int) (seed >>> (48 - bits));
    }
	
	public int nextInt() {
        return next(32);
    }
}