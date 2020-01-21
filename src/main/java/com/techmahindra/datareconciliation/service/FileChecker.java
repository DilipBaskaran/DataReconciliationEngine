package com.techmahindra.datareconciliation.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.techmahindra.datareconciliation.exceptions.ApplicationException;
import com.techmahindra.datareconciliation.model.Matches;
import com.techmahindra.datareconciliation.model.Transaction;

@Service
public class FileChecker {


	public Matches matchFile(Path fileX,Path fileY) throws ApplicationException {

		//For storing each line as Transaction List
		List<Transaction> transactionsX = new ArrayList<Transaction>();
		List<Transaction> transactionsY = new ArrayList<Transaction>();

		//For storing each of match Types
		List<String> exactMatch = new ArrayList<String>();
		List<String> xBreak = new ArrayList<String>();
		List<String> yBreak = new ArrayList<String>();
		List<String> weakMatch = new ArrayList<String>();

		try (Stream<String> streamX = Files.lines(fileX);
				Stream<String> streamY = Files.lines(fileY)) {//create stream of file lines for making each line as transaction object 
			
			splitStreamStringToTransaction(streamX,transactionsX);
			splitStreamStringToTransaction(streamY,transactionsY);
			//System.out.println(transactionsX.size());
			int i = 0,j=0; 
			for(;i < transactionsX.size()&&j<transactionsY.size();i++,j++) {
				Transaction transactionX =transactionsX.get(i);
				Transaction transactionY =transactionsY.get(j);
				//System.out.println(transactionX+"\n"+transactionY);
				if(transactionX.equals(transactionY)) {//If exactly equal, exact match
					exactMatch.add(transactionX.getTransactionID()+""+transactionY.getTransactionID());
				}else {
					if(!transactionX.getAccountID().equals(transactionY.getAccountID())) {// if accountID mismatches, X Break and y Break
						xBreak.add(transactionX.getTransactionID());
						yBreak.add(transactionY.getTransactionID());
					}else {
						BigDecimal amountX =transactionX.getAmount();
						BigDecimal amountY =transactionY.getAmount();
						if(amountX.compareTo(amountY)>=0) {//If amount of X is greater than amount Y 
							if(amountX.subtract(amountY).doubleValue() <= 0.01) {//if amount difference<= 0.01 check for date match 
								dateMatch(transactionX, transactionY, weakMatch,xBreak, yBreak); 
							}else {//else breaks
								xBreak.add(transactionX.getTransactionID());
								yBreak.add(transactionY.getTransactionID()); 
							}
						}else {//If amount of X is lesser than amount of Y 
							BigDecimal temp = amountX;
							amountX = amountY; 
							amountY = temp; 
							if(amountX.subtract(amountY).doubleValue()>0.01) {// if difference is more than 0.01, its break 
								xBreak.add(transactionX.getTransactionID());
								yBreak.add(transactionY.getTransactionID()); 
							}else { //else date Match
								dateMatch(transactionX,transactionY, weakMatch, xBreak, yBreak); 
							} 
						}
					}
				}
			}

			for(;i<transactionsX.size();i++)
				xBreak.add(transactionsX.get(i).getTransactionID());

			for(;j<transactionsX.size();j++)
				yBreak.add(transactionsY.get(j).getTransactionID());


		} catch (IOException e) {
			throw new ApplicationException("Files not able to open");
		} catch(RuntimeException e) {
			throw new ApplicationException("Files are not proper");
		}
		return  new Matches(exactMatch,weakMatch,xBreak,yBreak);
	}

	private void splitStreamStringToTransaction(Stream<String> stream, List<Transaction> transactions) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		stream.forEach(str->{
			String[] transaction = str.split(";");
			Date date =null;
			try {
				date = simpleDateFormat.parse(transaction[2].trim());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			transactions.add(new Transaction(transaction[0].trim(),transaction[1].trim(),date,new BigDecimal(transaction[3].trim())));
		});
		
	}

	private void dateMatch(Transaction transactionX, Transaction transactionY,List<String> weakMatch,List<String> xBreak,List<String> yBreak) {

		Date dateX = transactionX.getPostingDate();
		Date dateY = transactionY.getPostingDate();
		//System.out.println(dateX.compareTo(dateY));
		if(dateX.compareTo(dateY)==0) {//if date of X and y are equal
			weakMatch.add(transactionX.getTransactionID()+transactionY.getTransactionID());
		}else {
			//System.out.println("Compare="+dateX.compareTo(dateY));
			if(dateX.compareTo(dateY) <= 3 && dateX.compareTo(dateY) >=-3) {// if date difference is <3 and >-3
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateX);
				int dayOfWeekX = calendar.get(Calendar.DAY_OF_WEEK);
				calendar.setTime(dateY);
				int dayOfWeekY = calendar.get(Calendar.DAY_OF_WEEK);
				if((dayOfWeekX == 6 && (dayOfWeekY==7 ||dayOfWeekY==1 ||dayOfWeekY==2))
						||(dayOfWeekY == 6 && (dayOfWeekX==7 ||dayOfWeekX==1 ||dayOfWeekX==2))
						|| (dayOfWeekX-dayOfWeekY <= 1 && dayOfWeekX-dayOfWeekY >= -1)) {
					weakMatch.add(transactionX.getTransactionID()+transactionY.getTransactionID());
				}else {
					xBreak.add(transactionX.getTransactionID());
					yBreak.add(transactionY.getTransactionID());
				}
			}else {
				xBreak.add(transactionX.getTransactionID());
				yBreak.add(transactionY.getTransactionID());
			}
		}
	}


	/*
	 * public static void main(String...strings) throws ApplicationException {
	 * Calendar calendar = Calendar.getInstance(); calendar.setTime(new Date()); int
	 * dayOfWeekX = calendar.get(Calendar.DAY_OF_WEEK); System.out.println(new
	 * Date().getDate()+""+dayOfWeekX); new
	 * FileChecker().matchFile(Paths.get("X.txt"), Paths.get("Y.txt"));
	 * 
	 * 
	 * 
	 * }
	 */
}
