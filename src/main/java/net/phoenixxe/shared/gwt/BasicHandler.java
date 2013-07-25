package net.phoenixxe.shared.gwt;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Original source: <i>com.google.gwt.sample.contacts.client.event.ContactUpdatedEventHandler</i><br>
 * <b>Contacts</b> sample (standart GWT MVP). 
 * @author Arseniy Kaleshnyk, 12.03.2011, ver. 1.0
 */
public interface BasicHandler extends EventHandler{
	void onEvent();
	
	void onEvent(GwtEvent<?> event);
}
