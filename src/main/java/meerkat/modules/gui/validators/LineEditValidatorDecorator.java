package meerkat.modules.gui.validators;

import meerkat.modules.gui.ILineEditValidator;

/*
 * abstrakcyjna klasa dekorator - implementuje interfejs ILineEditValidator
 * 
 */
public abstract class LineEditValidatorDecorator implements ILineEditValidator {
	protected ILineEditValidator fieldValidator; // dekorowany validator

	public LineEditValidatorDecorator(ILineEditValidator fieldValidator) {
		this.fieldValidator = fieldValidator;
	}
}
