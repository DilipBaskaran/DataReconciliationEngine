package com.techmahindra.datareconciliation.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction implements Comparable<Transaction>{

	private String transactionID;
	private String accountID;
	private Date postingDate;
	private BigDecimal amount;
	//private Double amount;
	
	
	public Transaction(String transactionID, String accountID, Date postingDate, BigDecimal amount) {
		super();
		this.transactionID = transactionID;
		this.accountID = accountID;
		this.postingDate = postingDate;
		this.amount = amount.setScale(2);
	}
	
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getAccountID() {
		return accountID;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public Date getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
	@Override
	public String toString() {
		return "Transaction [transactionID=" + transactionID + ", accountID=" + accountID + ", postingDate="
				+ postingDate + ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((postingDate == null) ? 0 : postingDate.hashCode());
		result = prime * result + ((transactionID == null) ? 0 : transactionID.hashCode());
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
		Transaction other = (Transaction) obj;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (postingDate == null) {
			if (other.postingDate != null)
				return false;
		} else if (!postingDate.equals(other.postingDate))
			return false;
		
		
		return true;
	}

	@Override
	public int compareTo(Transaction trans) {
		
		if(this.accountID.equals(trans.accountID)) {
			if(this.amount.equals(trans.amount)) {
				if(this.postingDate.equals(trans.postingDate))
					return 0;
				else
					return this.postingDate.compareTo(trans.postingDate);
			}else
				return this.amount.compareTo(trans.amount);
		}else
			return this.accountID.compareTo(trans.accountID);
		
	}

	
	
	
}
