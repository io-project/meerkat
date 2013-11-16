package meerkat.modules.gui.validators.common;

import meerkat.modules.gui.ILineEditValidator;

/**
 * Validator sprawdzajacy czy dane pole nie jest puste
 */
public class NotEmptyFieldValidator implements ILineEditValidator {

	@Override
	public boolean validate(String label, String value) {
		return value != null && !value.trim().isEmpty();
	}
}
