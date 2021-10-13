package seleniumProject;
import java.io.Serializable;

public class CompanyData implements Serializable, Comparable<CompanyData> {

	String name;
	String companyUrl;
	String symbol;
	String isin;
	String lastRate;
	String change;
	double turnover;
	String lastTrade;
	String basePrice;
	String openingPrice;

	public CompanyData(String name, String companyUrl, String symbol, String isin, 
			String lastRate, String change, double turnover,
			String lastTrade, String basePrice, String openingPrice) {
		super();
		this.name = name;
		this.companyUrl = companyUrl;
		this.symbol = symbol;
		this.isin = isin;
		this.lastRate = lastRate;
		this.change = change;
		this.turnover = turnover;
		this.lastTrade = lastTrade;
		this.basePrice = basePrice;
		this.openingPrice = openingPrice;
	}

	// In order to sort by turover
	@Override
	public int compareTo(CompanyData anotherCompanyData) {
		 return (int) (anotherCompanyData.turnover - this.turnover);
	}
	// in order to print it in a next txt file
	public String toString(){
		return "Company name: " + this.name + ", Symbol: " + this.symbol + ", ISIN: " + this.isin + ", Last Rate: " + this.lastRate + ", Change: " + this.change + ", Turnover: " + this.turnover +  ", Last Trade: " + this.lastTrade + ", Base price: " + this.basePrice + ", Opening Price: " + this.openingPrice;
	}
}


