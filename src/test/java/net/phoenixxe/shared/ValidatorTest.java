package net.phoenixxe.shared;

import static junit.framework.Assert.*;
import org.junit.Test;

//import net.recman.recordsmanager.shared.model.Realm;

import net.phoenixxe.shared.Validator;
import net.phoenixxe.shared.model.HasIdAndTitle;
import net.phoenixxe.shared.persistence.PersistedEntity;
import net.phoenixxe.shared.persistence.Realm;


public class ValidatorTest{
	public ValidatorTest(){}
	//  -------------------------- auxiliary methods --------------------------
	
	private void validateEmail(String email, boolean valid) {
		try {
			Validator.validateEmail(email);
			if (!valid) fail ("EMail: "+email+" is correct!");
		} catch (IllegalArgumentException iae) {
			if (valid) fail ("EMail: "+email+" is incorrect!");
		} catch (Exception e) {
			fail("Exception thrown on validating " + email + " - " + e.getMessage());
		}
	}
	
	private void validateEmailandDomain(String email, String domain, boolean valid) {
		try {
			Validator.validateEmail(email,domain);
			if (!valid) fail ("EMail: "+email+" belong to " + domain );
		} catch (IllegalArgumentException iae) {
			if (valid) fail ("EMail: "+email+" doesn't belong to " + domain );
		} catch (Exception e) {
			fail("Exception thrown on validating " + email + " that belong to " + domain +  " - " + e.getMessage());
			
		}
	}
	
	private void checkObject(Object b, boolean valid)
	{
		try {
			Validator.checkObject(b);
			if(!valid) fail ("value " + b + " is object!" );
		} catch (IllegalArgumentException iae) {
			if (valid) fail ("value " + b + " is not object!" );
		} catch(Exception e){
			fail("Any exception in method Validator.checkObjects!");
		}
		
	}
	
	private void checkId(Long l, boolean valid)
	{
		try {
			Validator.checkId(l);
			if(!valid) fail ("value " + l + " is ID!" );
		} catch (IllegalArgumentException iae) {
			if (valid) fail (iae.getMessage());
		} catch(Exception e){
			fail("Any exception in method Validator.checkId!");
		}
		
	}
	
	private void checkString(String s, boolean valid)
	{
		try {
			Validator.checkString(s, "string");
			if(!valid) fail ("value " + s + " is string!" );
		} catch (IllegalArgumentException iae) {
			if (valid) fail (iae.getMessage());
		} catch(Exception e){
			fail("Any exception in method Validator.checkString!");
		}
		
	}
	
	private void checkItem(HasIdAndTitle s, boolean valid)
	{
		try {
			Validator.checkItem(s);
			if(!valid) fail ("value " + s + " is Item!" );
		} catch (IllegalArgumentException iae) {
			if (valid) fail (iae.getMessage());
		} catch(Exception e){
			fail("Any exception in method Validator.checkItem!");
		}
		
	}
	private void checkPersistedEntity(PersistedEntity entity, String title, long realmId, boolean valid)
	{
		try {
			Validator.checkPersistedEntity(entity, title, realmId);
			if(!valid) fail ("value " + entity + " is PersistedEntity!" );
		} catch (IllegalArgumentException iae) {
			if (valid) fail (iae.getMessage());
		} catch(Exception e){
			fail("Any exception in method Validator.checkPersistedEntity!" + e.getMessage());
		}
	}
	// --------------------------- Tests ------------------------
	/**
	 * Tests {@link Validator#checkObject(Object)} <br> 
	 * and {@link Validator#checkObject(Object, String)} methods:<br>
	 * fail if object is null
	 */
	@Test
	public void checkObjectTest(){
		Object b = new Long("33");
		checkObject(b, true);
		checkObject(null, false);
		checkObject(new Object(), true);
		checkObject(5, true);
		checkObject("222", true);
		checkObject(new Realm("internal.domain.net",true), true);
	};
	
	/**
	 * Tests {@link Validator#checkId(Long)} <br>
	 * and {@link Validator#checkId(Long, String)} methods:<br>
	 * fail if value is null or not positive 
	 * 
	 */
	@Test
	public void checkIdTest(){
		checkId(null, false);
		checkId(0l, false);
		checkId(-5l, false);
		checkId(6l, true);
	};
	
	/**
	 * Tests {@link Validator#checkString(String, String)} method:<br>
	 */
	@Test
	public void checkStringTest(){
		checkString(null,false);
		checkString("",false);
		checkString(new String(),false); 
		checkString("1",true);
		checkString("5555",true);

	};
	
	/**
	 * Tests {@link Validator#checkItem(HasIdAndTitle)} <br>
	 * and {@link Validator#checkItem(HasIdAndTitle, String)}
	 */
	@Test
	public void checkItemTest(){
		checkItem(null, false);
		checkItem(new Realm(null,true), false);
		Realm r = new Realm("test",true);
		r.setId(-6l);
		checkItem(r,false);
		r.setId(0l);
		checkItem(r,false);
		r.setId(5l);
		checkItem(r,true);
	};
		
	/**
	 * Tests {@link Validator#checkPersistedEntity(net.recman.recordsmanager.shared.model.PersistedEntity, String, long)} 
	 */
	@Test
	public void checkPersistedEntityTest(){
		checkPersistedEntity(null, "persisted entity", 5l, false);
		
		PersistedEntity pe = new PersistedEntity("title");
		pe.setRealmId(5l);
		checkPersistedEntity(pe, "title", 52l, false);
		checkPersistedEntity(pe, "title2", 5l, false);
		pe.setId(12l);
		checkPersistedEntity(pe, "title", 5l, true);
	};
	
	/**
	 * 
	 * Tests for {@link Validator#validateEmail(String)} method:
	 * Use correct and incorrect email. 
	 * @throws IllegalArgumentException - 
	 */
	@Test
	public void validateEmailTest() throws IllegalArgumentException {
		String[] arrayCorrectEmail = {
				"admin@recman.net",
				"foxter@gmail.com",
				"admin@starup-circle.net",
			};
		for(int i=0; i<arrayCorrectEmail.length; i++)
			validateEmail(arrayCorrectEmail[i], true);
		String[] arrayIncorrectEmail = {
				null,
				"",
				"admin.recman.net",
				"foxter.@gmail.com",
				"a@a..a",
				"почта@ya.ru",
				"ya spbaka@archi.net"
			};
		for(int i=0; i<arrayIncorrectEmail.length; i++)
			validateEmail(arrayIncorrectEmail[i], false);
	}

	/**
	 * Tests {@link Validator#validateEmail(String, String)} method:
	 * @throws IllegalArgumentException
	 */
	@Test
	public void validateEmailBelonghDomainTest() throws IllegalArgumentException{
		String[][] arrayCorrectEmail = {
				{"admin@recman.net","recman.net"},
				{"admin@startup-circle.net","startup-circle.net"},
				{"admin@gmail.com","gmail.com"},
				{"admin@internal.domain.net","internal.domain.net"}
			};
		for(int i=0; i<arrayCorrectEmail.length; i++)
			validateEmailandDomain(arrayCorrectEmail[i][0],arrayCorrectEmail[i][1], true);
		
		String[][] arrayIncorrectEmail = {
				{null,"recman.net."},
				{null,null},				
				{"adminrecman.net",null},
				{"",""},
				{"admin@recman.net","recman.net."},
				{"admin@recman.net","gmail.com"},
				{"adminrecman.net",""},
				{"adminrecman.net",""},
				{"почта@ya.ru","ya.ru"}
			};
		for(int i=0; i<arrayCorrectEmail.length; i++)
			validateEmailandDomain(arrayIncorrectEmail[i][0],arrayIncorrectEmail[i][1], false);
	}
	
	
}