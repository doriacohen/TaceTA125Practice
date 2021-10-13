package seleniumProject;

public class Constants {
	
	public static class XPath {
		static String TAUrl = "https://www.tase.co.il/en";
		static String TALinkButton = "//*[@id=\"trades_panel1\"]//div//table//tr[3]/td/a";
		static String TAMoreAboutButton = "//*[@id=\"mainContent\"]/index-lobby/section[1]/div/div/section[2]/button";
		static String TAIndexComponents = "//*[@id=\"more_madad_nav\"]/ul/li[1]/ul/li[4]/a";
		static String NextPage = "//*[@id=\"pageS\"]//li[8]/a";
		static String NameElement = "//tbody/tr/td[1]";
		static String URLElement = "//table/tbody/tr/td/a";
		static String SymbolElement = "//tbody/tr/td[2]"; 
	}
	
	public static class Disk {
		static String FileNameCompanies = "companies";
		static String FileNameTableListCompanies = "tableListCompanies.txt";
	}
}
