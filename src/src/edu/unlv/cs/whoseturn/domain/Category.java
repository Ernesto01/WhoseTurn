package edu.unlv.cs.whoseturn.domain;

import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Category {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String keyString;

	@Persistent
    private String name;

	@Persistent
    private String strategyKeyString;

	@Persistent
    private int timeBoundryInHours;

	@Persistent
    private Boolean deleted;
    
	@Persistent
    private Set<String> turns;
	
	public Category() {}
	
	public Category(String categoryName) {
		this.name = categoryName;
	}
	

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStrategyKeyString() {
		return strategyKeyString;
	}

	public void setStrategyKeyString(String strategyKeyString) {
		this.strategyKeyString = strategyKeyString;
	}

	public int getTimeBoundryInHours() {
		return timeBoundryInHours;
	}

	public void setTimeBoundryInHours(int timeBoundryInHours) {
		this.timeBoundryInHours = timeBoundryInHours;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Set<String> getTurns() {
		return turns;
	}

	public void setTurns(Set<String> turns) {
		this.turns = turns;
	}
	
	public void addTurn(Turn turn)
	{
		turns.add(turn.getKeyString());
	}
}
