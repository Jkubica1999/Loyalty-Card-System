package MyCode;

/*
 * Program by Jedrzej Kubica
 * Platinum is a subclass of LoyaltyCard representing the Platinum tier.
 * Bonus points: 200
 * Conversion rate: 0.50
 * */

public class Platinum extends LoyaltyCard {

	//CONSTRUCTORS----------------------------------------------

	//Constructor generating a new userId
	public Platinum(String userName, double moneySpent, boolean cardActive) {
		super(userName, moneySpent, cardActive);
	}//end constructor

	//Constructor using userId as input (used when loading from CSV)
	public Platinum(String userId, String userName, double moneySpent, boolean cardActive) {
		super(userId, userName, moneySpent, cardActive);
	}//end constructor

	@Override
	public double getBonusPoints() {
		return 200;
	}//end getBonusPoints

	@Override
	public double getConversionRate() {
		return 0.5;
	}//end getConversionRate

}//end class
