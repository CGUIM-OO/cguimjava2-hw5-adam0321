import java.util.ArrayList;

public class Table {
	public final int MAXPLAYER = 4;
	private Deck deck;
	private Player[] tPlayer;
	private Dealer tdealer = new Dealer();
	private int[] pos_betArray = new int[MAXPLAYER];

	public Table(int nDeck) {
		deck = new Deck(nDeck);
		tPlayer = new Player[MAXPLAYER];
	}

	public void set_player(int pos, Player p) {
		tPlayer[pos] = p;
	}

	public Player[] get_player() {
		return tPlayer;
	}

	public void set_dealer(Dealer d) {
		tdealer = d;
	}

	public Card get_face_up_card_of_dealer() {
		ArrayList<Card> oneRoundCard;
		oneRoundCard = tdealer.getOneRoundCard();
		return oneRoundCard.get(1);
	}

	private void ask_each_player_about_bets() {
		for (int i = 0; i < MAXPLAYER; i++) {
			tPlayer[i].sayHello();
			pos_betArray[i] = tPlayer[i].makeBet();
		}
	}

	private void distribute_cards_to_dealer_and_players() {
		for (int i = 0; i < MAXPLAYER; i++) {
			ArrayList<Card> playerCard = new ArrayList<Card>();
			playerCard.add(deck.getOneCard(true));
			playerCard.add(deck.getOneCard(true));
			tPlayer[i].setOneRoundCard(playerCard);
		}
		ArrayList<Card> dealerCard = new ArrayList<Card>();
		dealerCard.add(deck.getOneCard(true));
		dealerCard.add(deck.getOneCard(true));
		tdealer.setOneRoundCard(dealerCard);
		System.out.print("Dealer's face up card is ");
		Card c = get_face_up_card_of_dealer();
		c.printCard();
	}

	private void ask_each_player_about_hits() {
		for (int i = 0; i < MAXPLAYER; i++) {
			boolean hit = false;
			do {
				hit = tPlayer[i].hit_me(this);
				if (hit) {
					ArrayList<Card> playerCard = new ArrayList<Card>();
					playerCard = tPlayer[i].getOneRoundCard();
					playerCard.add(deck.getOneCard(true));
					tPlayer[i].setOneRoundCard(playerCard);
					System.out.println("Hit");
					System.out.print(tPlayer[i].getName() + "'s Cards now:");
					for (Card c : tPlayer[i].getOneRoundCard()) {
						c.printCard();
					}
				} else {
					System.out.println(tPlayer[i].getName() + ",Pass hit!");
					System.out.println(tPlayer[i].getName() + "'s hit is over!");
					System.out.println(tPlayer[i].getName() + ",Cards now:");
					for (Card c : tPlayer[i].getOneRoundCard()) {
						c.printCard();
					}
				}
			} while (hit);
		}
	}

	private void ask_dealer_about_hits() {
		boolean hit = false;
		do {
			hit = tdealer.hit_me(this);
			if (hit) {
				ArrayList<Card> DealerCard = new ArrayList<Card>();
				DealerCard = tdealer.getOneRoundCard();
				DealerCard.add(deck.getOneCard(true));
				tdealer.setOneRoundCard(DealerCard);
				System.out.println("Hit");
				System.out.println("Dealer's Cards now:");
				for (Card c : tdealer.getOneRoundCard()) {
					c.printCard();
				}
			} else {
				System.out.println("Dealer's hit is over!");
				System.out.println("Dealer's Cards now:");
				for (Card c : tdealer.getOneRoundCard()) {
					c.printCard();
				}
			}
		} while (hit);
	}

	private void calculate_chips() {
		int Chip;
		Chip = tdealer.getTotalValue();
		System.out.println("Dealer's card value is " + Chip + ",Cards:");
		tdealer.printAllCard();
		for (int i = 0; i < MAXPLAYER; i++) {
			tPlayer[i].getTotalValue();
			;
			System.out.println(tPlayer[i].getName() + "'s Card:");
			tPlayer[i].printAllCard();
			System.out.print(tPlayer[i].getName() + "'s card value is " + tPlayer[i].getTotalValue());
			if (tPlayer[i].getTotalValue() <= 21 && tdealer.getTotalValue() > 21) {
				tPlayer[i].increaseChips(pos_betArray[i]);
				System.out.print(",Get " + pos_betArray[i] + " Chips, the Chips now is: ");
			} else if (tPlayer[i].getTotalValue() > 21 && tdealer.getTotalValue() <= 21) {
				tPlayer[i].increaseChips(-pos_betArray[i]);
				System.out.print(",Loss " + pos_betArray[i] + " Chips, the Chips now is: ");
			} else if (tPlayer[i].getTotalValue() > tdealer.getTotalValue() && tPlayer[i].getTotalValue() <= 21) {
				tPlayer[i].increaseChips(pos_betArray[i]);
				System.out.print(",Get " + pos_betArray[i] + " Chips, the Chips now is: ");
			} else if (tPlayer[i].getTotalValue() < tdealer.getTotalValue() && tdealer.getTotalValue() <= 21) {
				tPlayer[i].increaseChips(-pos_betArray[i]);
				System.out.print(",Loss " + pos_betArray[i] + " Chips, the Chips now is: ");
			} else {
				System.out.print(",chip has no change" + pos_betArray[i] + " Chips,the Chips now is: ");
			}
			System.out.println(tPlayer[i].getCurrentChips());
		}
	}

	public int[] get_players_bet() {
		return pos_betArray;
	}

	public void play() {
		ask_each_player_about_bets();
		distribute_cards_to_dealer_and_players();
		ask_each_player_about_hits();
		ask_dealer_about_hits();
		calculate_chips();
	}
}
