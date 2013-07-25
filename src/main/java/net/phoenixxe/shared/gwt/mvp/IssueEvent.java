package net.phoenixxe.shared.gwt.mvp;

import net.phoenixxe.shared.gwt.BasicHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Original source: <i>com.google.gwt.sample.contacts.client.event.ContactUpdatedEvent</i><br>
 * <b>Contacts</b> sample (standart GWT MVP). 
 * @author Arseniy Kaleshnyk, 05.10.2010, ver. 1.0
 */
public class IssueEvent extends GwtEvent<BasicHandler> {
	public static Type<BasicHandler> TYPE = 
			new Type<BasicHandler>();
	private final Throwable cause;
	
	public IssueEvent(Throwable cause) {
		this.cause = cause;
	}
	
	@Override
	public Type<BasicHandler> getAssociatedType() {
		return TYPE;
	}
	
	public Throwable getCause() {
		return cause;
	}

	@Override
	protected void dispatch(BasicHandler handler) {
		handler.onEvent(this);
	}
	
	@Override
	public String toString() {
		return "IssuesEvent"; 
	}
}