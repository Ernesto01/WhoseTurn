package edu.unlv.cs.whoseturn.domain;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Used to keep track of badges that are awarded to users.
 */
//@PersistenceCapable
@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class BadgeAwarded implements IsSerializable  {

    /**
     * Default constructor. Needed for Serializable.
     */
    public BadgeAwarded() {
    }

    /**
     * Primary key for a badge that is awarded to a user.
     */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String keyString;

    /**
     * How many times the badge has been awarded to that user.
     */
    @Persistent
    private Integer count;

    /**
     * Id to reference the the badge id.
     */
    @Persistent
    private Integer badgeId;

    /**
     * Soft delete state. True for deleted.
     */
    @Persistent
    private Boolean deleted;

    // Getters and Setters.
    /**
     * Gets the key string.
     * 
     * @return the key string.
     */
    public final String getKeyString() {
        return keyString;
    }

    /**
     * Sets the key string.
     * 
     * @param keyString
     *            the key string.
     */
    public final void setKeyString(final String keyString) {
        this.keyString = keyString;
    }

    /**
     * Gets the deleted state.
     * 
     * @return true for deleted.
     */
    public final Boolean getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted state.
     * 
     * @param deleted
     *            True for deleted.
     */
    public final void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the count of times awarded.
     * 
     * @return The count.
     */
    public final Integer getCount() {
        return count;
    }

    /**
     * Sets teh count of times awarded.
     * 
     * @param count
     *            The count to set.
     */
    public final void setCount(final Integer count) {
        this.count = count;
    }

    /**
     * Increments the badge count by one.
     */
    public final void increaseBadgeCount() {
        this.count++;
    }

    /**
     * Gets the badge id.
     * 
     * @return The id of the badge.
     */
    public final Integer getBadgeId() {
        return badgeId;
    }

    /**
     * Sets the badge id.
     * 
     * @param badgeId
     *            The id of the badge.
     */
    public final void setBadgeId(final Integer badgeId) {
        this.badgeId = badgeId;
    }

}
