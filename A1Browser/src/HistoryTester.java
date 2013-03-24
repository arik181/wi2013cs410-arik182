import junit.framework.Assert;
import junit.framework.TestCase;

public class HistoryTester extends TestCase {

	public void testHistory() {
		BrowserHistory H = new BrowserHistory();
		String[] pagelist = {
				"ichi",
				"dos",
				"san",
				"drie",
				"wu",
				"six",
		};
		
		for(String s : pagelist) {
			H.go(s);			
		}
		
		Assert.assertTrue(H.noFuture());
		Assert.assertEquals("six", H.getBack());
		
		H.back("six");
		
		Assert.assertEquals("wu", H.getBack());
		Assert.assertEquals("six", H.getForward());
		
		for(String s : pagelist) {
			H.back(s);			
		}
		
		Assert.assertTrue(H.noHistory());
		
	}
}
