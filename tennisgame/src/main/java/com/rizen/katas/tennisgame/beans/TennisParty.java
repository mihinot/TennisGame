package com.rizen.katas.tennisgame.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.rizen.katas.tennisgame.utils.TennisPartyUtils;

/**
 * The party of Tennis game
 * 
 * @author Mihinot
 *
 */
public class TennisParty {

	private int playersNumber;
	private Map<Integer, List<Integer>> playersList;
	private List<Integer> playersListSetPoint;
	private String playersIds = "";
	private static boolean tieBreakAnnounced = false;

	public TennisParty() {
	}

	public TennisParty(int playersNumber) {
		super();
		this.playersNumber = playersNumber;
		this.playersList = new HashMap<>();
		this.playersListSetPoint = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < playersNumber; i++) {
			playersList.put(i, new ArrayList<>());
			sb.append((i + 1) + "|");
			playersListSetPoint.add(0);
		}
		playersIds = sb.toString().substring(0, sb.length() - 1);
	}

	/**
	 * @return the playersNumber
	 */
	public int getPlayersNumber() {
		return playersNumber;
	}

	/**
	 * @param playersNumber
	 *            the playersNumber to set
	 */
	public void setPlayersNumber(int playersNumber) {
		this.playersNumber = playersNumber;
	}

	public Map<Integer, List<Integer>> getPlayersList() {
		return playersList;
	}

	public void setPlayersList(Map<Integer, List<Integer>> playersList) {
		this.playersList = playersList;
	}

	@Override
	public String toString() {
		return playersList.toString();
	}

	/**
	 * @return the playersIds
	 */
	public String getPlayersIds() {
		return "[" + playersIds.trim() + "]";
	}

	/**
	 * @param playersIds
	 *            the playersIds to set
	 */
	public void setPlayersIds(String playersIds) {
		this.playersIds = playersIds;
	}

	/**
	 * @return the playersListSetPoint
	 */
	public List<Integer> getPlayersListSetPoint() {
		return playersListSetPoint;
	}

	/**
	 * @param playersListSetPoint
	 *            the playersListSetPoint to set
	 */
	public void setPlayersListSetPoint(List<Integer> playersListSetPoint) {
		this.playersListSetPoint = playersListSetPoint;
	}

	/**
	 * add a new set point for the scorer
	 */
	public boolean addSetPoint(int scorerId) {

		boolean result = false;

		try {
			int nextPlayerId = (playersListSetPoint.size() - scorerId) == 0 ? 0 : 1;
			//
			this.playersListSetPoint.set(scorerId,
					Integer.valueOf(this.playersListSetPoint.get(scorerId).intValue() + 1));

			int scorerCount = this.playersListSetPoint.get(scorerId);

			if (scorerCount >= 6) {
				// retrieve the second player points count for comparison
				int secondPlayerCount = this.playersListSetPoint.get(nextPlayerId);

				if (scorerCount == TennisPartyUtils.TIE_BREAK && secondPlayerCount == TennisPartyUtils.TIE_BREAK
						&& !tieBreakAnnounced) {
					tieBreakAnnounced = true;
					System.out.println("\n\t>> TIE BREAK !\n");
				}

				// check if the scorer win the set
				if (scorerCount > secondPlayerCount) {
					result = (scorerCount - secondPlayerCount) >= TennisPartyUtils.MIN_SET_WINNING_POINTS;
				}
			}
		} catch (IndexOutOfBoundsException | InputMismatchException exception) {
			System.err.println("Exception during processing scores! Check your input!");
		}

		return result;
	}

	/**
	 * reinitialize game scores
	 */
	public void reinitGameScores() {
		//
		this.playersList = new HashMap<>();
		for (int i = 0; i < playersNumber; i++) {
			playersList.put(i, new ArrayList<>());
		}
	}

	/**
	 * show set points for all users
	 */
	public void showSetPoints() {
		if (CollectionUtils.isNotEmpty(playersListSetPoint)) {
			int winner = 0;
			//
			StringBuilder sb = new StringBuilder();
			sb.append("\nSet scores :\n");
			for (int i = 0; i < playersListSetPoint.size(); i++) {
				int playerscore = playersListSetPoint.get(i);
				if (playerscore > winner)
					winner = i;
				sb.append("Player " + (i + 1) + " > " + playersListSetPoint.get(i) + "\n");
			}
			//
			sb.append("\nPlayer " + winner + " wins the set and match !");
			System.out.println(sb.toString());
		} else {
			System.out.println("No points to show!");
		}
	}
}
