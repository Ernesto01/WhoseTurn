package edu.unlv.cs.whoseturn.domain;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The turn item domain object.
 */
//@PersistenceCapable
@PersistenceCapable(identityType=IdentityType.APPLICATION,detachable="true")
public class TurnItem implements IsSerializable  {

    /**
     * Default constructor. Needed for Serializable.
     */
    public TurnItem() {
    }
    
    /**
     * The key string.
     */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String keyString;

    /**
     * The turn key string.
     */
    @Persistent
    private String turnKeyString;

    /**
     * The owner's key string.
     */
    @Persistent
    private String ownerKeyString;

    /**
     * The category key string.
     */
    @Persistent
    private String categoryKeyString;

    /**
     * If the user associated with this record was selected.
     */
    @Persistent
    private Boolean selected;

    /**
     * The vote of this from the users, need it be valid or not.
     */
    @Persistent
    private Integer vote;

    /**
     * The deleted state of the record.
     */
    @Persistent
    private Boolean deleted;

    // Getters and Setters.
    /**
     * Gets the key string.
     * 
     * @return The key string.
     */
    public final String getKeyString() {
        return keyString;
    }

    /**
     * Sets the key string.
     * 
     * @param keyString The key string.
     */
    public final void setKeyString(final String keyString) {
        this.keyString = keyString;
    }

    /**
     * Gets the turn key string.
     * 
     * @return The turn key string.
     */
    public final String getTurnKeyString() {
        return turnKeyString;
    }

    /**
     * Sets the turn key string.
     * 
     * @param turnKeyString The turn key string.
     */
    public final void setTurnKeyString(final String turnKeyString) {
        this.turnKeyString = turnKeyString;
    }

    /**
     * Gets the owner key string.
     * 
     * @return The owner key string.
     */
    public final String getOwnerKeyString() {
        return ownerKeyString;
    }
    
    /**
     * Sets the owner key string.
     * 
     * @param ownerKeyString The owner key string.
     */
    public final void setOwnerKeyString(final String ownerKeyString) {
        this.ownerKeyString = ownerKeyString;
    }

    /**
     * Get if selected or not.
     * 
     * @return true if selected.
     */
    public final Boolean getSelected() {
        return selected;
    }

    /**
     * Set if selected.
     * 
     * @param selected True if selected.
     */
    public final void setSelected(final Boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets the vote of the user.
     * 
     * @return -1 for lie, 0 for not voted, 1 for valid.
     */
    public final Integer getVote() {
        return vote;
    }

    /**
     * Sets the vote of the user.
     * 
     * @param vote -1 for lie, 0 for not voted, 1 for valid.
     */
    public final void setVote(final int vote) {
        this.vote = vote;
    }

    /**
     * Gets the deleted state of the record.
     * 
     * @return true for deleted.
     */
    public final Boolean getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted state of the record.
     * 
     * @param deleted true for deleted.
     */
    public final void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the category key string.
     * 
     * @return The category key string.
     */
    public final String getCategoryKeyString() {
        return categoryKeyString;
    }

    /**
     * Sets the category key string.
     * 
     * @param categoryKeyString The category key string.
     */
    public final void setCategoryKeyString(final String categoryKeyString) {
        this.categoryKeyString = categoryKeyString;
    }
}
