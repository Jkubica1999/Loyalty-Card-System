package MyCode;

/*
 * Program by Jedrzej Kubica
 * Gold is a subclass of LoyaltyCard representing the Gold tier.
 * Bonus points: 100
 * Conversion rate: 0.40
 * */

public class Gold extends LoyaltyCard {

	//CONSTRUCTORS----------------------------------------------

	//Constructor generating a new userId
	public Gold(String userName, double moneySpent, boolean cardActive) {
		super(userName, moneySpent, cardActive);
	}//end constructor

	//Constructor using userId as input (used when loading from CSV)
	public Gold(String userId, String userName, double moneySpent, boolean cardActive) {
		super(userId, userName, moneySpent, cardActive);
	}//end constructor

	@Override
	public double getBonusPoints() {
		return 100;
	}//end getBonusPoints

	@Override
	public double getConversionRate() {
		return 0.4;
	}//end getConversionRate

}//end class
