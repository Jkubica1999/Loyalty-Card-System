package MyCode;
import java.util.Random;

/*
 * Program by Jedrzej Kubica
 * This is an abstract LoyaltyCard class with private variables for:
 * 	-userId, userName, moneySpent and cardActive
 * Getters for:
 * 	-userId, userName, moneySpent, cardActive and tier
 * Setters for:
 * 	-userName, moneySpent and cardActive
 * No setter exists for:
 * 	-userId - Generated automatically on creation, never changes
 * Abstract methods (implemented by subclasses):
 * 	-getBonusPoints - Returns the bonus points for the card tier
 * 	-getConversionRate - Returns the conversion rate for the card tier
 * Methods:
 * 	-getTier - Returns the tier name as a string based on the bonus points value
 * 	-calculatePoints - Calculates points earned based on moneySpent, conversionRate and bonusPoints
 * 	-getTotalPointsEarned - Returns 0 if card is inactive, otherwise returns calculatePoints()
 * 	-getRandomId - Static helper that generates a random 6-character alphanumeric ID
 * 	-toString - Returns a formatted string with the card details
 * */

public abstract class LoyaltyCard {

	private String userId;
	private String userName;
	private double moneySpent;
	private boolean cardActive;

	//CONSTRUCTORS----------------------------------------------

	//Constructor generating a random userId
	public LoyaltyCard(String userName, double moneySpent, boolean cardActive) {
		this.userId = getRandomId();
		this.userName = userName;
		this.moneySpent = moneySpent;
		this.cardActive = cardActive;
	}//end constructor

	//Constructor accepting the userId as input (used when loading from CSV)
	public LoyaltyCard(String userId, String userName, double moneySpent, boolean cardActive) {
		this.userId = userId;
		this.userName = userName;
		this.moneySpent = moneySpent;
		this.cardActive = cardActive;
	}//end constructor

/* ==========================================================
 * ==                                                      ==
 * ==           ------------ GETTERS ------------          ==
 * ==                                                      ==
 * ==========================================================
 * */

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public double getMoneySpent() {
		return moneySpent;
	}

	public boolean isCardActive() {
		return cardActive;
	}

	//Returns the tier name as a string by matching the bonus points value unique to each subclass
	public String getTier() {
		switch ((int) this.getBonusPoints()) {
			case 10:  return "Bronze";
			case 50:  return "Silver";
			case 100: return "Gold";
			case 200: return "Platinum";
			default:  return "Unknown";
		}//end switch
	}//end getTier

/* ==========================================================
 * ==                                                      ==
 * ==           ------------ SETTERS ------------          ==
 * ==                                                      ==
 * ==========================================================
 * */

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setMoneySpent(double moneySpent) {
		this.moneySpent = moneySpent;
	}

	public void setCardActive(boolean cardActive) {
		this.cardActive = cardActive;
	}

/* ==========================================================
 * ==                                                      ==
 * ==        ------------ CLASS METHODS ------------       ==
 * ==                                                      ==
 * ==========================================================
 * */

	//Abstract methods to be implemented by each subclass
	public abstract double getBonusPoints();
	public abstract double getConversionRate();

	//Calculates points earned based on money spent, conversion rate and bonus points
	public double calculatePoints() {
		double tierBonus = this.getBonusPoints();
		double conversion = this.getConversionRate();
		double points = this.getMoneySpent() * conversion + tierBonus;
		return points;
	}//end calculatePoints

	//Returns 0 if the card is inactive, otherwise returns the calculated points
	public double getTotalPointsEarned() {
		if (!this.isCardActive()) {
			return 0;
		} else {
			return this.calculatePoints();
		}//end if
	}//end getTotalPointsEarned

	//Static helper method that generates a random 6-character alphanumeric ID
	public static String getRandomId() {
		Random rand = new Random();
		String id = "";
		//Convert chars values with ASCII table: 0-9 to numbers (48-57), 10-35 to capital letters (65-90)
		for (int i = 0; i < 6; i++) {
			int num = rand.nextInt(36);
			if (num > 9) {
				num += 55;//(10-35) + 55 = (65-90) = 'A'-'Z'
			} else {
				num += 48;//(0-9) + 48 = (48-57) = '0'-'9'
			}//end if
			char ch = (char) num;
			id = id + ch;//concatenate random character
		}//end for
		return id;
	}//end getRandomId

	//Returns a formatted string with the card details
	@Override
	public String toString() {
		String moneyFormatted = String.format("%.2f", this.moneySpent);
		String pointsFormatted = String.format("%.2f", this.getTotalPointsEarned());
		return "\n------------------------------" +
				"\nID:             " + this.userId +
				"\nName:           " + this.userName +
				"\nTier:           " + this.getTier() +
				"\nMoney Spent:    €" + moneyFormatted +
				"\nActive:         " + this.cardActive +
				"\nPoints Earned:  " + pointsFormatted +
				"\n------------------------------";
	}//end toString
	
	

}//end class
