package edu.unlv.cs.whoseturn.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.unlv.cs.whoseturn.client.BadgeService;
import edu.unlv.cs.whoseturn.domain.BadgeAwarded;
import edu.unlv.cs.whoseturn.domain.PMF;
import edu.unlv.cs.whoseturn.domain.Turn;
import edu.unlv.cs.whoseturn.domain.TurnItem;
import edu.unlv.cs.whoseturn.domain.User;

/**
 * The badge service, used to go through the badge checks and reward users.
 */
public class BadgeServiceImpl extends RemoteServiceServlet implements
		BadgeService {

	/**
	 * Allows the class to be serialized.
	 */
	private static final long serialVersionUID = 3341571143301810951L;

	/**
	 * Used to award the Jackass badge. Checks to see if the user submitted a
	 * turn with only himself. Awards the Jackass badge to the submitter.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void Jackass(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item and then get the
			// user
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Jackass badge check
			if (turn.getNumberOfUsers() == 1) {
				for (int i = 0; i < badgeSet.size(); i++) {
					// get key for the BadgeAwarded entity and retrieve the
					// object
					Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
							.next());
					BadgeAwarded badge = pm.getObjectById(BadgeAwarded.class,
							badgeKey);

					if (badge.getBadgeId().equals(1000)) {
						badge.increaseBadgeCount();
						break;
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the TeamCheater badge. Checks to see if everybody in the
	 * turn was selected. Awards the Team Cheater badge to everyone.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void TeamCheater(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();
		Integer user_count = turn.getNumberOfUsers();
		Integer selected_count = 0;

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);

			if (turn_item.getSelected()) {
				selected_count++;
			}
		}

		// TeamCheater badge check: see if everyone in the turn was selected
		if (selected_count == user_count) {
			// award the badge to everyone in the turn
			for (String turn_key : turn_items) {
				Key turnItemKey = KeyFactory.stringToKey(turn_key);
				TurnItem turn_item = pm.getObjectById(TurnItem.class,
						turnItemKey);

				// get the key of the user who owns this turn item and then get
				// the user
				Key ownerKey = KeyFactory.stringToKey(turn_item
						.getOwnerKeyString());
				User user = pm.getObjectById(User.class, ownerKey);
				Set<String> badgeSet = user.getBadges();

				for (int i = 0; i < badgeSet.size(); i++) {
					// get key for the BadgeAwarded entity and retrieve the
					// object
					Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
							.next());
					BadgeAwarded badge = pm.getObjectById(BadgeAwarded.class,
							badgeKey);

					if (badge.getBadgeId().equals(1019)) {
						badge.increaseBadgeCount();
						break;
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the CornerStone badge. Checks to see if the user was
	 * selected or not out of a group of 4. Awards the Corner Stone badge if
	 * selected. Awards the Don't Cross The Streams badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void CornerStone(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item and then get the
			// user
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() == 4) {
				// Corner Stone badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1001)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Don't Cross The Streams badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object.
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1002)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the HumanSacrifice badge. Checks to see if the user was
	 * selected or not out of a group of 5. Awards the Human Sacrifice badge if
	 * selected. Awards the Not The Thumb badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void HumanSacrifice(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);

			// Get the key of the user who owns this turn item and then get the
			// user.
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() == 5) {
				// Human Sacrifice badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1003)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Not The Thumb badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1004)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the SixMinuteAbs badge. Checks to see if the user was
	 * selected or not out of a group of 6. Awards the Six Minute Abs badge if
	 * selected. Awards the Pick Up Sticks badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void SixMinuteAbs(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item and then get the
			// user
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() == 6) {
				// Six Minute Abs badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object.
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1005)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Pick Up Sticks badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object.
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1006)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the CrappedOut badge. Checks to see if the user was
	 * selected or not out of a group of 7. Awards the Crapped Out badge if
	 * selected. Awards the Lucky No. 7 badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void CrappedOut(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item and then get the
			// user
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() == 7) {
				// Crapped Out badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object.
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1007)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Lucky No. 7 badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1008)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the SnowWhite badge. Checks to see if the user was selected
	 * or not out of a group of 8. Awards the Snow White badge if selected.
	 * Awards the Dwarf badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void SnowWhite(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item and then get the
			// user
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() == 8) {
				// Snow White badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1009)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Dwarf badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1010)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the FML badge. Checks to see if the user was selected or
	 * not out of a group of more than 8 people. Awards the FML badge if
	 * selected. Awards the Statistically Speaking badge if not selected.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void FML(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> turn_items = turn.getTurnItems();

		// get the keys of the turn items
		for (String turn_key : turn_items) {
			Key turnItemKey = KeyFactory.stringToKey(turn_key);
			TurnItem turn_item = pm.getObjectById(TurnItem.class, turnItemKey);
			// get the key of the user who owns this turn item
			Key ownerKey = KeyFactory
					.stringToKey(turn_item.getOwnerKeyString());
			User user = pm.getObjectById(User.class, ownerKey);
			Set<String> badgeSet = user.getBadges();

			// Badge Check
			if (turn.getNumberOfUsers() > 8) {
				// FML badge
				if (turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1011)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
				// Statistically Speaking badge
				if (!turn_item.getSelected()) {
					for (int i = 0; i < badgeSet.size(); i++) {
						// get key for the BadgeAwarded entity and retrieve the
						// object
						Key badgeKey = KeyFactory.stringToKey(badgeSet
								.iterator().next());
						BadgeAwarded badge = pm.getObjectById(
								BadgeAwarded.class, badgeKey);

						if (badge.getBadgeId().equals(1012)) {
							badge.increaseBadgeCount();
							break;
						}
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the Saint badge. Checks to see if the user has never lied
	 * for 50 turns.
	 * 
	 * @param user
	 *            The user to check to see if they get the Saint badge.
	 */
	public void Saint(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Integer countTurns = user.getTurnItems().size();
		Set<String> badgeSet = user.getBadges();
		BadgeAwarded userSaintBadge = null;
		boolean noLies = true;

		if (countTurns == 50) {
			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1013)) {
					userSaintBadge = pm.getObjectById(BadgeAwarded.class,
							badgeKey);
				}

				// check if user does not have any liar badges (Jackass or
				// TeamCheater)
				if (userBadge.getBadgeId().equals(1000)
						|| userBadge.getBadgeId().equals(1019)) {
					if (!userBadge.getCount().equals(0)) {
						noLies = false;
					}
				}
			}
		}

		if (noLies && userSaintBadge != null && userSaintBadge.getCount() == 0) {
			userSaintBadge.increaseBadgeCount();
		}

		pm.close();
	}

	/**
	 * Used to award the Socialite badge. Checks to see if the users have
	 * participated in a turn with more than 10 people.
	 * 
	 * @param turn
	 *            The turn to analyze to determine if someone gets the badge.
	 */
	public void Socialite(Turn turn) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Integer number_of_users = turn.getNumberOfUsers();

		if (number_of_users > 10) {
			for (String turnItemKeyString : turn.getTurnItems()) {
				// get key for the TurnItem and retrieve the object
				Key turnItemKey = KeyFactory.stringToKey(turnItemKeyString);
				TurnItem turnItem = pm.getObjectById(TurnItem.class,
						turnItemKey);
				// get key for user of the turn item, and then get the user
				String ownerKeyString = turnItem.getOwnerKeyString();
				Key ownerKey = KeyFactory.stringToKey(ownerKeyString);
				User user = pm.getObjectById(User.class, ownerKey);
				Set<String> badgeSet = user.getBadges();

				for (int i = 0; i < badgeSet.size(); i++) {
					// get key for the BadgeAwarded entity and retrieve the
					// object
					Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
							.next());
					BadgeAwarded badge = pm.getObjectById(BadgeAwarded.class,
							badgeKey);

					if (badge.getBadgeId().equals(1014)
							&& badge.getCount() == 0) {
						badge.increaseBadgeCount();
						break;
					}
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the Rookie badge. Checks to see if the user has
	 * participated in 10 turns.
	 * 
	 * @param user
	 *            The user to check to see if they get the Rookie.
	 */
	public void Rookie(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Integer number_of_turns = user.getTurnItems().size();

		if (number_of_turns == 10) {
			Set<String> badgeSet = user.getBadges();

			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1015)
						&& userBadge.getCount() == 0) {
					userBadge.increaseBadgeCount();
					break;
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the Veteran badge. Checks to see if the user has
	 * participated in 100 turns.
	 * 
	 * @param user
	 *            The user to check to see if they get the Veteran badge.
	 */
	public void Veteran(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Integer number_of_turns = user.getTurnItems().size();

		if (number_of_turns == 100) {
			Set<String> badgeSet = user.getBadges();

			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1016)
						&& userBadge.getCount() == 0) {
					userBadge.increaseBadgeCount();
					break;
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the Elite badge. Checks to see if the user has participated
	 * in 250 turns.
	 * 
	 * @param user
	 *            The user to check to see if they get the Elite badge.
	 */
	public void Elite(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Integer number_of_turns = user.getTurnItems().size();

		if (number_of_turns == 250) {
			Set<String> badgeSet = user.getBadges();

			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1017)
						&& userBadge.getCount() == 0) {
					userBadge.increaseBadgeCount();
					break;
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the WhoseTurnMaster badge. Checks to see if the user has
	 * received every badge (excludes the special StormShadow and MythBusters
	 * badges).
	 * 
	 * @param user
	 *            The user to check to see if they have every badge (excluding
	 *            StormShadow and MythBusters badges).
	 */
	public void WhoseTurnMaster(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> badgeSet = user.getBadges();
		BadgeAwarded masterBadge = null;
		boolean hasEveryBadge = true;

		for (int i = 0; i < badgeSet.size(); i++) {
			// get key for the BadgeAwarded entity and retrieve the object
			Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator().next());
			BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
					badgeKey);

			if (userBadge.getBadgeId().equals(1018)) {
				masterBadge = pm.getObjectById(BadgeAwarded.class, badgeKey);
			}

			if (userBadge.getCount() == 0
					&& !(userBadge.getBadgeId().equals(1018)
							|| userBadge.getBadgeId().equals(1020) || userBadge
							.getBadgeId().equals(1021))) {
				hasEveryBadge = false;
			}
		}

		if (hasEveryBadge && masterBadge != null && masterBadge.getCount() == 0) {
			masterBadge.increaseBadgeCount();
		}

		pm.close();
	}

	/**
	 * Used to award the StormShadow badge. Checks to see if the user is Chris
	 * Jones. Awards the StormShadow badge if true.
	 * 
	 * @param user
	 *            The user to check to see if they get the StormShadow badge.
	 */
	public void StormShadow(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> badgeSet = user.getBadges();

		if (user.getUsername().equals("Chris Jones")) {
			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object.
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1020)
						&& userBadge.getCount() == 0) {
					userBadge.increaseBadgeCount();
					break;
				}
			}
		}

		pm.close();
	}

	/**
	 * Used to award the MythBusters badge. Checks to see if the user is Matthew
	 * Sowders. Awards the MythBusters badge if true.
	 * 
	 * @param user
	 *            The user to check to see if they get the MythBusters badge.
	 */
	public void MythBusters(User user) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Set<String> badgeSet = user.getBadges();
		if (user.getUsername().equals("Matthew Sowders")) {
			for (int i = 0; i < badgeSet.size(); i++) {
				// get key for the BadgeAwarded entity and retrieve the object
				Key badgeKey = KeyFactory.stringToKey(badgeSet.iterator()
						.next());
				BadgeAwarded userBadge = pm.getObjectById(BadgeAwarded.class,
						badgeKey);

				if (userBadge.getBadgeId().equals(1021)
						&& userBadge.getCount() == 0) {
					userBadge.increaseBadgeCount();
					break;
				}
			}
		}

		pm.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void calculateBadges(String turnKeyString) {
		/**
		 * Persistence Manager, used for CRUD with the db.
		 */
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key turnKey = KeyFactory.stringToKey(turnKeyString);
		Turn turn = pm.getObjectById(Turn.class, turnKey);
		// Retrieve a list of all users in the database for badge calculation.
		Query query = pm.newQuery(edu.unlv.cs.whoseturn.domain.User.class);
		List<edu.unlv.cs.whoseturn.domain.User> userQueryList = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();
		List<edu.unlv.cs.whoseturn.domain.User> userList = new ArrayList<edu.unlv.cs.whoseturn.domain.User>();

		userQueryList = (List<edu.unlv.cs.whoseturn.domain.User>) query.execute();
		for (User user : userQueryList) {
			userList.add(user);
		}

		query.closeAll();
		pm.close();

		// Initiate badge calculation based on the turn submitted.
		Jackass(turn);
		TeamCheater(turn);
		CornerStone(turn);
		HumanSacrifice(turn);
		SixMinuteAbs(turn);
		CrappedOut(turn);
		SnowWhite(turn);
		FML(turn);
		Socialite(turn);

		// Initiate badge calculation for the users.
		for (User user : userList) {
			Saint(user);
			Rookie(user);
			Veteran(user);
			Elite(user);
			WhoseTurnMaster(user);
			StormShadow(user);
			MythBusters(user);
		}
	}

}