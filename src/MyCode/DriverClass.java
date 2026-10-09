package MyCode;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * Program by Jedrzej Kubica
 * DriverClass is the main controller for the Loyalty Card System.
 * On startup it loads existing card data from a CSV file into an ArrayList of LoyaltyCard objects.
 * It then presents a menu that loops until the user chooses to exit, allowing them to:
 * 	-View all cards
 * 	-Search for a card by ID
 * 	-Register a new card
 * 	-Delete a card
 * 	-Update card details
 * 	-Display the highest-scoring active card per tier
 * 	-Save all cards to CSV
 * 	-Exit the program
 * Helper methods handle safe input validation for int, double and boolean values.
 * */

public class DriverClass {

	public static void main(String[] args) {
		ArrayList<LoyaltyCard> allCards = new ArrayList<>();
		Scanner input = new Scanner(System.in);
		String line;
		String csvPath = "src/MyCode/cards.csv";
		int csvColumnCount = 5;
		boolean activeMenu = true;

		//CSV Handling--------------------
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvPath), "UTF-8"))) {
			br.readLine();//skip header row
			int failCount = 0;//counts how many cards failed to load

			while ((line = br.readLine()) != null) {//while the line isn't empty
				String[] values = line.split(",");//split the line by commas
				if (values.length == csvColumnCount) {
					try {
						String userId = values[4].trim();
						String userName = values[1].trim();
						double moneySpent = Double.parseDouble(values[2].trim());
						boolean cardActive = Boolean.parseBoolean(values[3].trim().toLowerCase());
						String type = values[0].trim().toLowerCase();
						switch (type) {//add next LoyaltyCard to the ArrayList based on its tier
							case "bronze":
								allCards.add(new Bronze(userId, userName, moneySpent, cardActive));
								break;
							case "silver":
								allCards.add(new Silver(userId, userName, moneySpent, cardActive));
								break;
							case "gold":
								allCards.add(new Gold(userId, userName, moneySpent, cardActive));
								break;
							case "platinum":
								allCards.add(new Platinum(userId, userName, moneySpent, cardActive));
								break;
							default:
								failCount++;
								break;
						}//end switch
					} catch (Exception f) {
						failCount++;
						f.printStackTrace();
					}//end try catch
				} else {
					System.out.println("CSV row skipped: column count doesn't match.");
					failCount++;
				}//end if
			}//end while

			if (failCount > 0) {
				System.out.println(failCount + " card(s) failed to load from the CSV.");
			}//end if
		} catch (IOException e) {
			System.out.println("ERROR: Failed to load the CSV file.");
			e.printStackTrace();
		}//end try catch

		//Menu loop--------------------
		while (activeMenu) {
			printMenu();
			int menuChoice = readInt(input);//read menu choice safely

			switch (menuChoice) {
				case 1://View all cards
					viewAllCards(allCards);
					break;
				case 2://Search for a card by ID
					System.out.print("Enter card ID: ");
					String searchId = input.nextLine().trim().toUpperCase();
					LoyaltyCard found = findCardById(allCards, searchId);
					if (found != null) {
						System.out.println(found.toString());
					} else {
						System.out.println("No card found with ID: " + searchId);
					}//end if
					break;
				case 3://Register a new card
					registerCard(allCards, input);
					break;
				case 4://Delete a card
					System.out.print("Enter card ID to delete: ");
					String deleteId = input.nextLine().trim().toUpperCase();
					deleteCard(allCards, deleteId);
					break;
				case 5://Update card details
					System.out.print("Enter card ID to update: ");
					String updateId = input.nextLine().trim().toUpperCase();
					updateCard(allCards, updateId, input);
					break;
				case 6://Display the highest-scoring active card per tier
					displayTopPerTier(allCards);
					break;
				case 7://Save all cards to CSV
					saveToCSV(allCards, csvPath);
					break;
				case 8://Exit the program
					activeMenu = false;
					System.out.println("Session ended.");
					break;
				default:
					System.out.println("Invalid option. Please enter a number between 1 and 8.");
					break;
			}//end switch
		}//end while

		input.close();//close scanner
	}//end main

/* ==========================================================
 * ==                                                      ==
 * ==        ------------ CLASS METHODS ------------       ==
 * ==                                                      ==
 * ==========================================================
 * */

	//Prints the menu options to the console
	public static void printMenu() {
		System.out.println("\n=============================="
				+ "\n   Loyalty Card System Menu"
				+ "\n=============================="
				+ "\n1. View all cards"
				+ "\n2. Search for a card"
				+ "\n3. Register a new card"
				+ "\n4. Delete a card"
				+ "\n5. Update card details"
				+ "\n6. Display highest-scoring active card per tier"
				+ "\n7. Save all cards to CSV"
				+ "\n8. Exit"
				+ "\n------------------------------"
				+ "\nEnter choice: ");
	}//end printMenu

	//Prints all cards in the ArrayList using their toString method
	public static void viewAllCards(ArrayList<LoyaltyCard> cards) {
		if (cards.isEmpty()) {
			System.out.println("No cards registered.");
			return;
		}//end if
		System.out.println("\n====== All Loyalty Cards ======");
		for (LoyaltyCard card : cards) {
			System.out.println(card.toString());
		}//end for
	}//end viewAllCards

	//Searches the ArrayList for a card matching the given ID, returns null if not found
	public static LoyaltyCard findCardById(ArrayList<LoyaltyCard> cards, String id) {
		for (LoyaltyCard card : cards) {
			if (card.getUserId().equals(id)) {
				return card;
			}//end if
		}//end for
		return null;//return null if no card found
	}//end findCardById

	//Registers a new card by collecting data from the user and adding it to the ArrayList
	public static void registerCard(ArrayList<LoyaltyCard> cards, Scanner input) {
		System.out.print("Enter cardholder name: ");
		String name = input.nextLine().trim();

		System.out.print("Enter total money spent (€): ");
		double moneySpent = readDouble(input);
		//ensure money spent is not negative
		while (moneySpent < 0) {
			System.out.println("Money spent cannot be negative.");
			System.out.print("Enter total money spent (€): ");
			moneySpent = readDouble(input);
		}//end while

		System.out.print("Is the card active? (true/false): ");
		boolean cardActive = readBoolean(input);

		//ask for tier and validate against valid options
		String[] validTiers = {"bronze", "silver", "gold", "platinum"};
		String tier = "";
		boolean isValid = false;
		while (!isValid) {
			System.out.print("Enter card tier (Bronze / Silver / Gold / Platinum): ");
			tier = input.nextLine().trim().toLowerCase();
			for (int i = 0; i < validTiers.length; i++) {
				if (validTiers[i].equals(tier)) {
					isValid = true;
				}//end if
			}//end for
			if (!isValid) {
				System.out.println("Invalid tier.");
			}//end if
		}//end while

		//create the correct subclass object based on the tier
		switch (tier) {
			case "bronze":
				cards.add(new Bronze(name, moneySpent, cardActive));
				break;
			case "silver":
				cards.add(new Silver(name, moneySpent, cardActive));
				break;
			case "gold":
				cards.add(new Gold(name, moneySpent, cardActive));
				break;
			case "platinum":
				cards.add(new Platinum(name, moneySpent, cardActive));
				break;
		}//end switch
		System.out.println("Card registered successfully.");
	}//end registerCard

	//Deletes a card from the ArrayList by ID and confirms whether it was removed
	public static void deleteCard(ArrayList<LoyaltyCard> cards, String id) {
		LoyaltyCard toDelete = findCardById(cards, id);
		if (toDelete != null) {
			cards.remove(toDelete);
			System.out.println("Card " + id + " deleted successfully.");
		} else {
			System.out.println("No card found with ID: " + id);
		}//end if
	}//end deleteCard

	//Updates a card's name, money spent and active status by ID
	public static void updateCard(ArrayList<LoyaltyCard> cards, String id, Scanner input) {
		LoyaltyCard card = findCardById(cards, id);
		if (card == null) {
			System.out.println("No card found with ID: " + id);
			return;
		}//end if

		System.out.println("Updating card: " + id);

		System.out.print("Enter new name (current: " + card.getUserName() + "): ");
		String newName = input.nextLine().trim();
		if (!newName.equals("")) {
			card.setUserName(newName);
		}//end if

		System.out.print("Enter new money spent (current: €" + String.format("%.2f", card.getMoneySpent()) + "): ");
		double newMoneySpent = readDouble(input);
		//ensure money spent is not negative
		while (newMoneySpent < 0) {
			System.out.println("Money spent cannot be negative.");
			System.out.print("Enter new money spent: ");
			newMoneySpent = readDouble(input);
		}//end while
		card.setMoneySpent(newMoneySpent);

		System.out.print("Is the card active? (current: " + card.isCardActive() + ") (true/false): ");
		card.setCardActive(readBoolean(input));

		System.out.println("Card updated successfully.");
	}//end updateCard

	//Displays the highest-scoring active card in each tier
	//uses getTier() which identifies the tier by matching the bonus points value unique to each subclass
	public static void displayTopPerTier(ArrayList<LoyaltyCard> cards) {
		//variables to hold the top card in each tier
		LoyaltyCard topBronze = null;
		LoyaltyCard topSilver = null;
		LoyaltyCard topGold = null;
		LoyaltyCard topPlatinum = null;

		for (LoyaltyCard card : cards) {
			if (!card.isCardActive()) {
				continue;//skip inactive cards
			}//end if

			//check which tier the card belongs to using getTier() and compare points with current top
			switch (card.getTier()) {
				case "Bronze":
					if (topBronze == null || card.getTotalPointsEarned() > topBronze.getTotalPointsEarned()) {
						topBronze = card;
					}//end if
					break;
				case "Silver":
					if (topSilver == null || card.getTotalPointsEarned() > topSilver.getTotalPointsEarned()) {
						topSilver = card;
					}//end if
					break;
				case "Gold":
					if (topGold == null || card.getTotalPointsEarned() > topGold.getTotalPointsEarned()) {
						topGold = card;
					}//end if
					break;
				case "Platinum":
					if (topPlatinum == null || card.getTotalPointsEarned() > topPlatinum.getTotalPointsEarned()) {
						topPlatinum = card;
					}//end if
					break;
			}//end switch
		}//end for

		System.out.println("\n====== Highest-Scoring Active Card Per Tier ======");
		System.out.println("Bronze:   " + (topBronze   != null ? topBronze.getUserName()   + " (" + String.format("%.2f", topBronze.getTotalPointsEarned())   + " pts)" : "No active Bronze cards"));
		System.out.println("Silver:   " + (topSilver   != null ? topSilver.getUserName()   + " (" + String.format("%.2f", topSilver.getTotalPointsEarned())   + " pts)" : "No active Silver cards"));
		System.out.println("Gold:     " + (topGold     != null ? topGold.getUserName()     + " (" + String.format("%.2f", topGold.getTotalPointsEarned())     + " pts)" : "No active Gold cards"));
		System.out.println("Platinum: " + (topPlatinum != null ? topPlatinum.getUserName() + " (" + String.format("%.2f", topPlatinum.getTotalPointsEarned()) + " pts)" : "No active Platinum cards"));
	}//end displayTopPerTier

	//Saves all cards to the CSV file using FileWriter, overwrites the existing file
	public static void saveToCSV(ArrayList<LoyaltyCard> cards, String csvPath) {
		try (FileWriter fw = new FileWriter(csvPath)) {
			fw.write("Type,Name,MoneySpent,Active,ID\n");//write header row
			for (LoyaltyCard card : cards) {
				//getTier() is used to write the correct tier name to the CSV
				fw.write(card.getTier() + ","
						+ card.getUserName() + ","
						+ card.getMoneySpent() + ","
						+ card.isCardActive() + ","
						+ card.getUserId() + "\n");
			}//end for
			System.out.println("Cards saved to CSV successfully.");
		} catch (IOException e) {
			System.out.println("ERROR: Failed to save to CSV.");
			e.printStackTrace();
		}//end try catch
	}//end saveToCSV

/* ==========================================================
 * ==                                                      ==
 * ==     ------------ INPUT VALIDATION ------------       ==
 * ==                                                      ==
 * ==========================================================
 * */

	//Safely reads an integer from the scanner, re-prompts on invalid input
	public static int readInt(Scanner input) {
		while (true) {
			try {
				int value = input.nextInt();
				input.nextLine();//clear scanner
				return value;
			} catch (Exception e) {
				System.out.print("Invalid input. Enter a number: ");
				input.nextLine();//clear scanner
			}//end try catch
		}//end while
	}//end readInt

	//Safely reads a double from the scanner, re-prompts on invalid input
	public static double readDouble(Scanner input) {
		while (true) {
			try {
				double value = input.nextDouble();
				input.nextLine();//clear scanner
				return value;
			} catch (Exception e) {
				System.out.print("Invalid input. Enter a number: ");
				input.nextLine();//clear scanner
			}//end try catch
		}//end while
	}//end readDouble

	//Safely reads a boolean from the scanner, re-prompts on invalid input
	public static boolean readBoolean(Scanner input) {
		while (true) {
			try {
				boolean value = input.nextBoolean();
				input.nextLine();//clear scanner
				return value;
			} catch (Exception e) {
				System.out.print("Invalid input. Enter true or false: ");
				input.nextLine();//clear scanner
			}//end try catch
		}//end while
	}//end readBoolean

}//end class
