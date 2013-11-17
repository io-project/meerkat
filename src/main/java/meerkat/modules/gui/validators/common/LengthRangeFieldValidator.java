package meerkat.modules.gui.validators.common;

import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.gui.validators.LineEditValidatorDecorator;

/*
 * Validator sprawdzajacy czy dlugosc wprowadzonego napisu jest w przedziale [min, max)
 */
public class LengthRangeFieldValidator extends LineEditValidatorDecorator {
	private int maxLength = Integer.MAX_VALUE;
	private int minLength = 0;

	public LengthRangeFieldValidator(ILineEditValidator fieldValidator,
			int minLength, int maxLength) {
		super(fieldValidator);
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

	public boolean validate(String label, String value) {
		boolean b = true;
		if (fieldValidator != null)
			b = fieldValidator.validate(label, value);

		return b && minLength <= value.length() && value.length() < maxLength;
	}
}
