package com.rizen.katas.tennisgame;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.rizen.katas.tennisgame.beans.TennisParty;
import com.rizen.katas.tennisgame.utils.TennisPartyUtils;

/**
 * Tennis party launcher.
 * 
 * @author Mihinot
 *
 */
public class PlayTennisParty {

	//
	private static TennisParty party = null;
	// for reading referee directives
	private static Scanner in = new Scanner(System.in);
	private static boolean stopGame = false;

	private static int scorer = 0, round = 0, currentSet = 1;
	private static boolean deuceActivated = false;

	public static void main(String[] args) {
		// run the party
		runSets();
		in.close();
	}

	public static void runGame() {
		try {
			initializeScores();

			System.out.println("Set, Go!");

			do {
				// setting scores
				System.out.print("Who score " + party.getPlayersIds() + " ? : ");
				scorer = in.nextInt() - 1;

				int adv = 0;
				try {
					adv = setScores(scorer, round);
				} catch (NullPointerException e) {
					//
					System.err.println("\n\nBad option. Game over!");
					stopGame = true;
				}

				//
				if (adv == TennisPartyUtils.ADV) {
					System.out.println("\t>> ADV Player " + (scorer + 1));

				} else if (adv == TennisPartyUtils.END) {
					// set scores updating
					stopGame = true;
					adv = 0;
				}

				// next turn
				round++;

			} while (!stopGame);

			// party end
			System.out.println("\nThe player " + (scorer + 1) + " win the set!");
			System.out.println("Set end!\n");

			showSetScores();

		} catch (IndexOutOfBoundsException | InputMismatchException exception) {
			System.err.println("Exception during processing scores! Check your input!");
		}
	}

	/**
	 * reinit all the settings param before starting a new game
	 */
	private static void reinitGameParams() {
		//
		scorer = 0;
		round = 0;
		stopGame = false;
		deuceActivated = false;
	}

	/**
	 * initialize zero scores for all player on play starting
	 */
	private static void initializeScores() {
		//
		// players game initialization
		if (party == null) {
			party = new TennisParty(TennisPartyUtils.NB_PLAYERS);
		}

		// initialisation des scores
		party.getPlayersList().forEach((x, y) -> y.add(0));
		//
		round++;
	}

	/**
	 * initialize scores for a set turn on adding last point for all players and
	 * changing the point for the scorer.
	 * 
	 * @param playerId
	 * @param round
	 * @return
	 * @throws NullPointerException
	 */
	public static int setScores(int playerId, int round) throws NullPointerException {

		// index where add the point
		int scoresLastIndex = round;

		// scorer point update
		int point = getPointToAdd(party.getPlayersList().get(playerId).get(round - 1));

		// initialisation des scores du tour
		party.getPlayersList().forEach((x, y) -> {
			int i = y.get(y.size() - 1);
			if (i != 0)
				y.add(i);
			else
				y.add(0);
		});

		// which point to add
		if (point == TennisPartyUtils.ADV) {
			point = TennisPartyUtils.ADV;
		} else if (point == TennisPartyUtils.ADV_DEUCE) {
			point = TennisPartyUtils.ADV;
			scoresLastIndex++;
		}

		// setting right point for the scorer
		party.getPlayersList().get(playerId).set(scoresLastIndex, point);

		// 0:Point | 1:ADV | END:WIN
		int result = 0;
		if (party.getPlayersList().get(playerId).get(round) == TennisPartyUtils.ADV) {
			result = TennisPartyUtils.END;
		} else if (point == TennisPartyUtils.MAX_POINT) {
			result = 1;
		}

		//
		if (!deuceActivated) {
			checkDeuce();
		}

		return result;
	}

	/**
	 * Change points to string datas and display scores
	 */
	public static void showSetScores() {
		System.out.println("[Set " + currentSet + " scores]");
		//
		setLastScores();

		//
		party.getPlayersList().forEach((x, y) -> {
			System.out.println(y.stream().map(z -> String.valueOf(z)).parallel().map(point -> {
				if (point.equalsIgnoreCase("1")) {
					return " ADV";
				} else if (point.equalsIgnoreCase("2")) {
					return "DEUCES";
				} else {
					return String.valueOf(point);
				}
			}).collect(Collectors.toList()).toString());
		});
	}

	/**
	 * Set last score at 40 for all users with deuce score (except the winner)
	 * before printing results.
	 */
	private static void setLastScores() {
		//
		// set all deuces to 40 if the game is finished
		if (deuceActivated) {
			party.getPlayersList().forEach((x, y) -> {
				int lastIndex = y.size() - 1;
				if (y.get(lastIndex) == TennisPartyUtils.ADV_DEUCE)
					y.set(lastIndex, TennisPartyUtils.GAME_POSSIBLE_POINTS[3]);
			});
		}
	}

	/**
	 * return point to add for the scorer
	 * 
	 * @param lastPoint
	 *            the scorer's last point
	 * @return
	 */
	public static int getPointToAdd(int lastPoint) {
		//
		int x = 0;

		switch (lastPoint) {
		case 0:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[1];
			break;
		case 15:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[2];
			break;
		case 30:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[3];
			break;
		case 40:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[4];
			break;
		case 1:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[4];
			break;
		case 2:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[4];
			break;
		default:
			x = TennisPartyUtils.GAME_POSSIBLE_POINTS[0];
			break;
		}

		return x;
	}

	/**
	 * check for deuce during party
	 */
	static boolean checkDeuce() {

		List<Integer> deuces = new ArrayList<>();
		party.getPlayersList().forEach((x, y) -> {
			if (y.stream().reduce((a, b) -> b).orElse(null).intValue() == 40) {
				deuces.add(x);
			}
		});

		// check if all deuces size is equals to the number of players
		deuceActivated = (deuces.size() == TennisPartyUtils.NB_PLAYERS) ? true : false;

		if (deuceActivated) {
			// alert the referee
			System.out.println("\t>> Deuce rule activated!");

			// set deuces point[2] on scores of all players
			party.getPlayersList().forEach((x, y) -> {
				y.add(TennisPartyUtils.GAME_POSSIBLE_POINTS[TennisPartyUtils.GAME_POSSIBLE_POINTS.length - 1]);
			});
			round++;
		}

		//
		return deuceActivated;
	}

	/**
	 * run each set
	 */
	private static void runSets() {
		// number of sets
		int setsCount = 1;
		boolean endGame = false;

		System.out.println("Tennis, Go!");

		while (!endGame) {
			System.out.println("\nSet " + setsCount);

			// run a new game
			runGame();

			// update points for the player who schore (scorer) and check if the
			// set is the last
			if (party.addSetPoint(scorer)) {
				endGame = true;
			}

			// reinit games params and update the set number
			setsCount++;
			reinitGameParams();
			party.reinitGameScores();
		}

		// show set points
		party.showSetPoints();
	}
}
