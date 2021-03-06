package com.pclewis.mcpatcher;

public abstract class WeightedIndex {
	final int size;

	public static WeightedIndex create(int size) {
		if (size <= 0) {
			return null;
		}

		return new WeightedIndex(size) {
			@Override
			public int choose(long key) {
				return mod(key, size);
			}

			@Override
			public String toString() {
				return "unweighted";
			}
		};
	}

	public static WeightedIndex create(int size, final String weightList) {
		if (size <= 0 || weightList == null) {
			return create(size);
		}

		final int[] weights = new int[size];
		int sum1 = 0;
		boolean useWeight = false;
		String[] list = weightList.trim().split("\\s+");
		for (int i = 0; i < size; i++) {
			if (i < list.length && list[i].matches("^\\d+$")) {
				weights[i] = Math.max(Integer.parseInt(list[i]), 0);
			} else {
				weights[i] = 1;
			}
			if (i > 0 && weights[i] != weights[0]) {
				useWeight = true;
			}
			sum1 += weights[i];
		}
		if (!useWeight || sum1 <= 0) {
			return create(size);
		}
		final int sum = sum1;

		return new WeightedIndex(size) {
			@Override
			public int choose(long key) {
				int index;
				int m = mod(key, sum);
				for (index = 0; index < size - 1 && m >= weights[index]; index++) {
					m -= weights[index];
				}
				return index;
			}

			@Override
			public String toString() {
				StringBuilder sb = new StringBuilder();
				sb.append("%(");
				for (int i = 0; i < weights.length; i++) {
					if (i > 0) {
						sb.append(", ");
					}
					sb.append(String.format("%.1f", 100.0 * weights[i] / sum));
				}
				sb.append(")");
				return sb.toString();
			}
		};
	}

	protected WeightedIndex(int size) {
		this.size = size;
	}

	protected final int mod(long n, int modulus) {
		return (int) (((n >> 32) ^ n) & 0x7fffffff) % modulus;
	}

	abstract public int choose(long key);
}