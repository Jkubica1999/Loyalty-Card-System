package MyCode;

/*
 * Program by Jedrzej Kubica
 * Bronze is a subclass of LoyaltyCard representing the Bronze tier.
 * Bonus points: 10
 * Conversion rate: 0.20
 * */

public class Bronze extends LoyaltyCard {

	//CONSTRUCTORS----------------------------------------------

	//Constructor generating a new userId
	public Bronze(String userName, double moneySpent, boolean cardActive) {
		super(userName, moneySpent, cardActive);
	}//end constructor

	//Constructor using userId as input (used when loading from CSV)
	public Bronze(String userId, String userName, double moneySpent, boolean cardActive) {
		super(userId, userName, moneySpent, cardActive);
	}//end constructor

	@Override
	public double getBonusPoints() {
		return 10;
	}//end getBonusPoints

	@Override
	public double getConversionRate() {
		return 0.2;
	}//end getConversionRate

}//end class
