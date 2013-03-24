import junit.framework.Assert;
import junit.framework.TestCase;

public class BrowserTester extends TestCase {

	public void testSetBrowserURL() {
		final String[] okurls = { 
				"http://www.facebook.com",
				"http://www.w3schools.com",
				"http://www.w3.org"};
		final String[] badurls = { 
				"facebook.com",
				"http://ww",
				"www.w3.org"};
		
		BrowserFrame B = new BrowserFrame("Test Browser");
		boolean r = false;
		
		// Positive tests
		for (String s: okurls)  { 
			r = B.setBrowserPage(s); 
			Assert.assertTrue(r); 
		}
		
		// Negative tests
		for (String s: badurls) { 
			r = B.setBrowserPage(s); 
			Assert.assertFalse(r); 
		}		
	}
}
