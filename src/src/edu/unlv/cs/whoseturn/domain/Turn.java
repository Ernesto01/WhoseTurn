package edu.unlv.cs.whoseturn.domain;

import java.util.Date;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Turn {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String keyString;
	
	@Persistent
    private Date turnDateTime;

	@Persistent
    private String categoryKeyString;
    
	@Persistent
    private Set<String> turnItems;

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public Date getTurnDateTime() {
		return turnDateTime;
	}

	public void setTurnDateTime(Date turnDateTime) {
		this.turnDateTime = turnDateTime;
	}

	public String getCategoryKeyString() {
		return categoryKeyString;
	}

	public void setCategoryKeyString(String categoryKeyString) {
		this.categoryKeyString = categoryKeyString;
	}

	public Set<String> getTurnItems() {
		return turnItems;
	}

	public void setTurnItems(Set<String> turnItems) {
		this.turnItems = turnItems;
	}
	
	public void addTurnItem(TurnItem turnItem)
	{
		turnItems.add(turnItem.getKeyString());
	}
}
