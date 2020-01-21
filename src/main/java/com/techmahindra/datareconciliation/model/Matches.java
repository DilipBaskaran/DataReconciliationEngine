package com.techmahindra.datareconciliation.model;

import java.util.List;

public class Matches {

	private List<String> exactMatch;
	private List<String> weakMatch;
	private List<String> xBreak;
	private List<String> yBreak;
	
	public Matches(List<String> exactMatch, List<String> weakMatch, List<String> xBreak, List<String> yBreak) {
		super();
		this.exactMatch = exactMatch;
		this.weakMatch = weakMatch;
		this.xBreak = xBreak;
		this.yBreak = yBreak;
	}
	public List<String> getExactMatch() {
		return exactMatch;
	}
	public void setExactMatch(List<String> exactMatch) {
		this.exactMatch = exactMatch;
	}
	public List<String> getWeakMatch() {
		return weakMatch;
	}
	public void setWeakMatch(List<String> weakMatch) {
		this.weakMatch = weakMatch;
	}
	public List<String> getxBreak() {
		return xBreak;
	}
	public void setxBreak(List<String> xBreak) {
		this.xBreak = xBreak;
	}
	public List<String> getyBreak() {
		return yBreak;
	}
	public void setyBreak(List<String> yBreak) {
		this.yBreak = yBreak;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exactMatch == null) ? 0 : exactMatch.hashCode());
		result = prime * result + ((weakMatch == null) ? 0 : weakMatch.hashCode());
		result = prime * result + ((xBreak == null) ? 0 : xBreak.hashCode());
		result = prime * result + ((yBreak == null) ? 0 : yBreak.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matches other = (Matches) obj;
		if (exactMatch == null) {
			if (other.exactMatch != null)
				return false;
		} else if (!exactMatch.equals(other.exactMatch))
			return false;
		if (weakMatch == null) {
			if (other.weakMatch != null)
				return false;
		} else if (!weakMatch.equals(other.weakMatch))
			return false;
		if (xBreak == null) {
			if (other.xBreak != null)
				return false;
		} else if (!xBreak.equals(other.xBreak))
			return false;
		if (yBreak == null) {
			if (other.yBreak != null)
				return false;
		} else if (!yBreak.equals(other.yBreak))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Matches [exactMatch=" + exactMatch + ", weakMatch=" + weakMatch + ", xBreak=" + xBreak + ", yBreak="
				+ yBreak + "]";
	}
	
	
	
	
}
