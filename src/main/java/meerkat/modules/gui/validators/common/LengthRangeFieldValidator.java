package meerkat.modules.gui.validators.common;

import meerkat.modules.gui.ILineEditValidator;

/*
 * Validator sprawdzajacy czy dlugosc wprowadzonego napisu jest w przedziale [min, max)
 */
public class LengthRangeFieldValidator implements ILineEditValidator {
	private int maxLength = Integer.MAX_VALUE;
	private int minLength = 0;

	public LengthRangeFieldValidator() {
	}

	public LengthRangeFieldValidator(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public void setMax(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setMin(int minLength) {
		this.minLength = minLength;
	}

	public int getMax() {
		return maxLength;
	}

	public int getMin() {
		return minLength;
	}

	@Override
	public boolean validate(String label, String value) {
		return minLength <= value.length() && value.length() < maxLength;
	}
}
