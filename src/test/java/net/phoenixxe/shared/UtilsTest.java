package net.phoenixxe.shared;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.phoenixxe.shared.Utils;
import net.phoenixxe.shared.model.HasIdAndTitle;
import net.phoenixxe.shared.persistence.PersistedEntity;
import net.phoenixxe.shared.persistence.Realm;

import org.junit.Test;

public class UtilsTest {

	/**
	 * Tests {@link Utils#isEmpty(Object)} method. <br/>
	 * True if object is null 
	 */
	@Test
	public void isEmptyObjectTest(){
		assertTrue("ERROR: It's NULL value!", Utils.isEmpty((Object)null));
		assertFalse("new Object()", Utils.isEmpty(new Object()));
		assertFalse("is String", Utils.isEmpty((Object)"is string"));
		
	}
	

	/**
	 * Tests {@link Utils#isStringEmpty(String)} method. <br/>
	 * True if string is null or length=0
	 */
	@Test
	public void isEmptyStringTest(){
		assertTrue("ERROR: It's NULL value!",Utils.isStringEmpty((String)null));
		assertTrue("String is empty", Utils.isStringEmpty(""));
		assertFalse("full string", Utils.isStringEmpty("full string"));
	}

	/**
	 * Tests {@link Utils#isLongEmpty(Long)} method. <br/>
	 * True if value is null or equal 0l
	 */
	@Test
	public void isEmptyLongTest(){
		assertTrue("ERROR: It's NULL value!",Utils.isLongEmpty((Long)null));
		assertTrue("is zero", Utils.isLongEmpty((Long)0l));
		assertFalse("is six", Utils.isLongEmpty((Long)6l));
		assertFalse("is negative", Utils.isLongEmpty(-6l));
	} 

	/**
	 * Tests {@link Utils#isListEmpty(List)} method. <br/>
	 * True if List null or empty
	 */
	@Test
	public void isEmptyListTest(){
		List <String> list = new ArrayList<String>();
		List <String> list2 = new ArrayList<String>();
		list2.add("sd");
		assertTrue("ERROR: It's NULL value!", Utils.isListEmpty(list));
		assertTrue("ERROR: List is empty!", Utils.isListEmpty(new ArrayList<String>()));
		assertFalse("List.length=1", Utils.isListEmpty(list2));
		
	}

	/**
	 * Tests {@link Utils#isItemEmpty(HasIdAndTitle)} method. <br/>
	 * True in cases: entity is null, Id=0l or Title is empty.
	 */
	@Test
	public void isItemEmptyTest(){
		// PersistedEntity Realm UserInfo implements HasIdAndTitle interface
		// if setId(null) then NullPointerException. can setId negative value
		// I testing for PersistedEntity
		// 1. null 
		assertTrue(Utils.isItemEmpty(null));
		// 2. 0l+string 
		HasIdAndTitle pe;
		pe = new PersistedEntity();
		pe.setId(0l);
		pe.setTitle("ss");
    	assertTrue("0l + string", Utils.isItemEmpty(pe));
		// 3. 1l+null 
		pe.setId(1l);
		pe.setTitle(null);
    	assertTrue("1l + NULL", Utils.isItemEmpty(pe));
    	// 4. 1l+"" 
    	pe.setId(1l);
		pe.setTitle("");
    	assertTrue("1l + empty", Utils.isItemEmpty(pe));
		// 5. 0l+null
    	pe.setId(0l);
		pe.setTitle(null);
    	assertTrue("0l + NULL", Utils.isItemEmpty(pe));
		// 6. 1l+string 
    	pe.setId(1l);
		pe.setTitle("string");
    	assertFalse("1l + string", Utils.isItemEmpty(pe));
    	// 7. -3l+string
    	pe.setId(-3l);
    	pe.setTitle("string");
//    	assertFalse("-3l + string", Utils.isItemEmpty(pe));
    	
    	//File h = new File("123", "2", 6l);
    	//h.setId(-3l);
    	//assertTrue("-3l + string", Utils.isItemEmpty(h));
	}

	/**
	 * Tests {@link Utils#normaliseString(String)} method. <br/>
	 * Trim string and convert to low register 
	 */
	@Test
	public void normaliseStringTest() {
		String[] tests = {null, "", "Glav Riba", 
				" Бинжюы", "	test ", 
				" Q W E R T			Y ", "11252457124" , 
				"                               	 "
				};
		for (String string : tests) {
			String temp = null;
			try{
				temp = Utils.normaliseString(string);
				if(string != null){
					if(string.trim().toLowerCase().compareTo(temp) != 0)
						fail("\""+string + "\"" + " not normalised!");
				}
				else {
					if(!temp.isEmpty())
						fail("Fail with conversion NULL to empty string!");
				}
			} catch(NullPointerException npe){
				fail("The method can not work with null values.");
			} catch(Exception e){
				fail("Exception thrown on normalize string" + string +  " - " + e.getMessage());
			}	
		}
	}

	/**
	 * Tests {@link Utils#isIds(Object)}
	 */
	@Test
	public void isIdsTest(){
		ArrayList<Long> emptyArray = new ArrayList<Long>();
		assertFalse("emptyArray", Utils.isIds(emptyArray));
		ArrayList<Long> nullArray = null;
		assertFalse("nullArray", Utils.isIds(nullArray));
		ArrayList<String> stringArray = new ArrayList<String>();
		stringArray.add("any string");
		assertFalse("stringArray", Utils.isIds(stringArray));
		ArrayList<Long> longArray = new ArrayList<Long>();
		longArray.add(0l);
		assertTrue("longArray", Utils.isIds(longArray));
		

	}

	@Test
	public void testConvertArray() {

	}

	@Test
	public void testFormatArray() {

	}

	@Test
	public void testFormatEntitiesArray() {

	}

	@Test
	public void isSaasTest()
	{
		try{
			Realm r = null;
			assertTrue(Utils.isSaas(new Realm("Saas",true)));
			assertFalse("ERROR Realm is NULL!", Utils.isSaas(r));
			assertFalse(Utils.isSaas(new Realm("Anithing", true)));
		} catch(IllegalArgumentException iae){
			fail("Illegal Argument Exception - " + iae.getMessage());
		} catch(Exception e){
			fail("Exception thrown on validating - "+ e.getMessage());
		}
			
	}

	@Test
	public void testDecodeException() {

	}

	@Test
	public void isNonHostedDomainTest(){
		assertFalse("null",Utils.isNonHostedDomain(null));
		assertFalse("www",Utils.isNonHostedDomain("www"));
		assertFalse("gmail",Utils.isNonHostedDomain("gmail"));
		assertTrue("gmail.com",Utils.isNonHostedDomain("gmail.com"));
		assertTrue("google.com",Utils.isNonHostedDomain("google.com	"));
	}

	@Test
	public void testAreCollectionsEqual() {

	}

}
