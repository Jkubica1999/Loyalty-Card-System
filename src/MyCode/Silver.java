package MyCode;

/*
 * Program by Jedrzej Kubica
 * Silver is a subclass of LoyaltyCard representing the Silver tier.
 * Bonus points: 50
 * Conversion rate: 0.30
 * */

public class Silver extends LoyaltyCard {

	//CONSTRUCTORS----------------------------------------------

	//Constructor generating a new userId
	public Silver(String userName, double moneySpent, boolean cardActive) {
		super(userName, moneySpent, cardActive);
	}//end constructor

	//Constructor using userId as input (used when loading from CSV)
	public Silver(String userId, String userName, double moneySpent, boolean cardActive) {
		super(userId, userName, moneySpent, cardActive);
	}//end constructor

	@Override
	public double getBonusPoints() {
		return 50;
	}//end getBonusPoints

	@Override
	public double getConversionRate() {
		return 0.3;
	}//end getConversionRate

}//end class
