package utils;

import java.util.List;

public class MathUtils {
	public static double sum(List<Double> values) {
		double sum = 0.0;
		for (double value: values) {
			sum += value;
		}
		return sum;
	}
}
