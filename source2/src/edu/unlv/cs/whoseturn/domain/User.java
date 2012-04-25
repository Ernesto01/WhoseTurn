package edu.unlv.cs.whoseturn.domain;

import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * The user domain object. 
 */
//@PersistenceCapable
@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class User implements IsSerializable  {

    /**
     * Default constructor. Needed for Serializable.
     */
    public User() {
    }
    
    /**
     * Primary key for the user.
     */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String keyString;

    /**
     * User name, a way to identify a user in the database.
     */
    @Persistent
    private String username;

    /**
     * Email address.
     */
    @Persistent
    private String email;

    /**
     * Admin status.
     */
    @Persistent
    private Boolean admin;

    /**
     * Deleted state.
     */
    @Persistent
    private Boolean deleted;

    /**
     * Possible way to display a blob for a user.
     */
    @Persistent
    private byte[] avatarBlob;

    /**
     * How many times a user has lied.
     */
    @Persistent
    private Integer penaltyCount;

    /**
     * Turn items.
     */
    @Persistent
    private Set<String> turnItems;

    /**
     * List of badges.
     */
    @Persistent
    private Set<String> badges;

    /**
     * Get key string.
     * 
     * @return the key string.
     */
    public final String getKeyString() {
        return keyString;
    }

    // Getters and Setters.
    /**
     * Sets the key string.
     * 
     * @param keyString The key string.
     */
    public final void setKeyString(final String keyString) {
        this.keyString = keyString;
    }

    /**
     * Get the user name.
     * 
     * @return The user name.
     */
    public final String getUsername() {
        return username;
    }

    /**
     * Set the user name.
     * 
     * @param username The user name.
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Get the email address.
     * 
     * @return The email address.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     * 
     * @param email The email address.
     */
    public final void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Get the admin state.
     * 
     * @return The admin state.
     */
    public final Boolean getAdmin() {
        return admin;
    }

    /**
     * Sets the admin state.
     * 
     * @param admin The admin state.
     */
    public final void setAdmin(final Boolean admin) {
        this.admin = admin;
    }

    /**
     * Get the deleted state.
     * 
     * @return The deleted state.
     */
    public final Boolean getDeleted() {
        return deleted;
    }

    /**
     * Set the deleted state.
     * 
     * @param deleted The deleted state.
     */
    public final void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Get the avatar blob.
     * 
     * @return The avatar blob.
     */
    public final byte[] getAvatarBlob() {
        return avatarBlob;
    }

    /**
     * Set the avatar blob.
     * 
     * @param avatarBlob The avatar blob.
     */
    public final void setAvatarBlob(final byte[] avatarBlob) {
        this.avatarBlob = avatarBlob;
    }

    public final Set<String> getTurnItems() {
        return turnItems;
    }

    /**
     * Get the badges.
     * 
     * @return The badges.
     */
    public final Set<String> getBadges() {
        return badges;
    }

    /**
     * Set the badges.
     * 
     * @param badges The badges.
     */
    public final void setBadges(final Set<String> badges) {
        this.badges = badges;
    }

    /**
     * Add a badge.
     * 
     * @param badge The badge to add.
     */
    public final void addBadge(final BadgeAwarded badge) {
        badges.add(badge.getKeyString());
    }

    /**
     * Add a turn item.
     * 
     * @param turnItem The turn item to add.
     */
    public final void addTurnItem(final TurnItem turnItem) {
        turnItems.add(turnItem.getKeyString());
    }

    /**
     * Set the turn items.
     * 
     * @param turnItems The turn items.
     */
    public final void setTurnItems(final Set<String> turnItems) {
        this.turnItems = turnItems;
    }

    /**
     * Get the penalty count.
     * 
     * @return The penalty count.
     */
    public final Integer getPenaltyCount() {
        return penaltyCount;
    }

    /**
     * Set the penalty count.
     * 
     * @param penaltyCount The penalty count.
     */
    public final void setPenaltyCount(final Integer penaltyCount) {
        this.penaltyCount = penaltyCount;
    }

    /**
     * Increase the penalty count.
     */
    public final void increasePenaltyCount() {
        this.penaltyCount++;

    }

}
