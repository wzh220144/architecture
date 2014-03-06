package com.gwu.architecture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CardReader {
	
	/*
	 * 0:read next line
	 * 1:write next line		//currently, not support
	 * 2:change current line
	 * 3:check current line
	 * 4:check the length of visual card
	 * 5:check last operation
	 * 6: change command
	 */
	
	int command;
	int curLine;
	int fileLength;
	File cardFile;
	InputStreamReader fileStreamReader;
	InputStream fileStream;
	char[] cardChar;
	Scanner scanner;
	String logString;
	String cardString;
	String[] cardStrings;
	int flag;
	
	public CardReader() {
		fileStream = this.getClass().getResourceAsStream("Card1");
		fileStreamReader = new InputStreamReader(fileStream);
		logString = new String();
		try {
			cardChar = new char[fileStream.available()];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logString+=e;
		}
		try {
			fileStreamReader.read(cardChar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logString+=e;
		}
		cardString = new String(cardChar);
	}
	
	public void powerup() {
		command = 6;
		
		logString = "";
		cardStrings = cardString.split("\n");
		fileLength = cardStrings.length;
		for(int i=0; i<fileLength; i++) {
			cardStrings[i]=cardStrings[i].replaceAll("[^0-9]", "");;
		}
		curLine=0;
		flag=1;
	}

	public void powerdown() {
		
	}

	public void destroy() {
		
	}

	public void init() {
		
	}
	
	public int in() {
		int res=0;
		if(command==0) {
			if(curLine>=fileLength) {
				flag=0;
				res=0;
				logString+="(command="+command+")in: cannot read " + curLine + " line!!!\n";
				System.out.println("(command="+command+")in: cannot read " + curLine + " line!!!");
			}
			else {
				flag=1;
				res = Integer.parseInt(cardStrings[curLine++]);
			}
		}
		else {
			flag=0;
			logString+="(command="+command+")in: command is not valid!!!\n";
			System.out.println("(command="+command+")in: command is not valid!!!");
			res=0;
		}
		return res;
	}
	
	public void out(int c) {
		if(command==6) {
			command=c;
		}
		else if(command==1){
			logString+="(command="+command+")out " + c + ": Currently, not support!!!\n";
			System.out.println("(command="+command+")out " + c + ": Currently, not support!!!");
			flag=0;
		}
		else if(command==2) {
			curLine=c;
			flag=1;
		}
		else {
			logString+="(command="+command+")out " + c +": command is not valid!!!\n";
			System.out.println("(command="+command+")out " + c +": command is not valid!!!");
			flag=0;
		}
	}
	
	public int chk() {
		int res=-1;
		if(command==3) {
			res = curLine;
			flag=1;
		}
		else if(command==4) {
			res=fileLength;
			flag=1;
		}
		else if(command==5) {
			res=flag;
			flag=1;
		}
		else {
			logString+="(command="+command+")chk: command is not valid!!!\n";
			System.out.println("(command="+command+")chk: command is not valid!!!");
			flag=0;
		}
		command=6;
		return res;
	}
	
	public static void main(String[] args) {
		CardReader cardReader = new CardReader();
		cardReader.powerup();
		
		System.out.println("in: " + cardReader.in());
		cardReader.out(0);
		System.out.println("in: " + cardReader.in());
	}
	
}
