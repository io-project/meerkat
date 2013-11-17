package meerkat.modules.gui.validators.common;

import meerkat.modules.gui.ILineEditValidator;
import meerkat.modules.gui.validators.LineEditValidatorDecorator;

/**
 * Validator sprawdzajacy czy dane pole nie jest puste
 */
public class NotEmptyFieldValidator extends LineEditValidatorDecorator {

	public NotEmptyFieldValidator(ILineEditValidator fieldValidator) {
		super(fieldValidator);
	}

	public boolean validate(String label, String value) {
		boolean b = true;
		if (fieldValidator != null)
			b = fieldValidator.validate(label, value);

		return b && value != null && !value.trim().isEmpty();
	}
}
