/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of extELLE
 * 
 * extELLE is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * extELLE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extELLE.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.elle.gui.wizard;


public class WizardException extends Exception {

	private boolean closeWizard = true, showMessage = true;

	public WizardException(Exception e) {
		super(e);
	}

	public WizardException(String message) {
		super(message);
	}

	public WizardException(String message, boolean closeWizard) {
		this(message);
		this.closeWizard = closeWizard;
	}

	public WizardException(String message, boolean closeWizard, boolean showMessage) {
		this(message, closeWizard);
		this.showMessage = showMessage;
	}

	public WizardException(String text, Exception e1) {
		super(text, e1);
	}

	public boolean closeWizard() {
		return closeWizard;
	}

	public boolean showMessage() {
		return showMessage;
	}

}
