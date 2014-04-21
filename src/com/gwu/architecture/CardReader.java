package com.gwu.architecture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	String logString;
	String cardString;
	String[] cardStrings;
	int flag;
	
	public CardReader() {
		fileStream = this.getClass().getResourceAsStream("Card1");
		fileStreamReader = new InputStreamReader(fileStream);
		logString = new String();
		
		
	}
	
	public void powerup() {
		
		logString = "";
		flag=1;
		init();
	}

	public void powerdown() {
		
	}

	public void destroy() {
		
	}

	public void init() {
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
		
		command = 6;
		
		cardStrings = cardString.split("\n");
		fileLength = cardStrings.length;
		for(int i=0; i<fileLength; i++) {
			cardStrings[i]=cardStrings[i].replaceAll("[^0-9]", "");;
		}
		curLine=0;
	}
	
	/***************************************operation for other class
	 * @throws IOException *******************************************/
	public void loadCard(String string) throws IOException {
		String[] tStrings;
		String tString;
		InputStream inputStream = new FileInputStream(string);
		char[] tchar;
		
		//read assembly file to memory
		InputStreamReader reader = new InputStreamReader(inputStream);
		tchar = new char[inputStream.available()];
		reader.read(tchar);
		tString = new String(tchar);
		
		//delete comments in assembly language
		tString = tString.replaceAll("(\r\n)+", "\n");
		tString= tString.replaceAll("\r", "\n");
		tString = tString.replaceAll("[ \t]*/.*", "");
		tString = tString.replaceAll("\n+", "\n");
		tStrings = tString.split("\n");
		
		fileLength = tStrings.length;
		cardStrings = new String[fileLength+1];
		reader.close();
		Map<String, String> tmap = new HashMap<String, String>();
		Vector<String> tVector = new Vector<String>();
		tVector.clear();
		tmap.clear();
		
		for(int i=0; i<fileLength; i++) {		//store the actual address with its label, using map class
			if(tStrings[i].matches("^.+:.+") == true) {
				String key = tStrings[i].split(":")[0];
				int value=i;
				value = i%255;					//compute the true address, address cannot be larger than 255
				assert tmap.containsKey(key) == false;
					
				tmap.put(key, Integer.toString(value));
				tVector.add(key);
			}
		}
		
		int vlen = tVector.size();
		
		for(int i=0; i<vlen; i++)
			System.out.println(tVector.get(i) + " " + tmap.get(tVector.get(i)));
		
		for(int i=0; i<fileLength; i++) {
			tStrings[i] = tStrings[i].replaceAll("^.+:[ \t]*", "");		//delete label with ':'
			//System.out.println(tStrings[i]);
			for(int j=0; j<vlen; j++) {
				//replace label with its actual address when it does not need to compute the additional compute
				tStrings[i]=tStrings[i].replaceAll("[ \t]+" + tVector.get(j) + "[ \t]+" , " "  + tmap.get(tVector.get(j)) + " " );
				tStrings[i]=tStrings[i].replaceAll("^" + tVector.get(j) + "[ \t]+" , tmap.get(tVector.get(j)) + " " );
				tStrings[i]=tStrings[i].replaceAll("[ \t]+" + tVector.get(j) + "$", " "  + tmap.get(tVector.get(j)));
				tStrings[i]=tStrings[i].replaceAll("^" + tVector.get(j) + "$", "0"+tmap.get(tVector.get(j))); 
				
				//replace label with its actual address when it needs to compute the additional compute
				tStrings[i]=tStrings[i].replaceAll("[ \t]+" + tVector.get(j) + "\\+" , " "  + tmap.get(tVector.get(j)) + "+" );
				tStrings[i]=tStrings[i].replaceAll("\\+" + tVector.get(j) + "[ \t]" , "+"  + tmap.get(tVector.get(j)) + " " );
				tStrings[i]=tStrings[i].replaceAll("^" + tVector.get(j) + "\\+" , tmap.get(tVector.get(j)) + "+" );
				tStrings[i]=tStrings[i].replaceAll("\\+" + tVector.get(j) + "$" , "+" + tmap.get(tVector.get(j)) );
			}
			Pattern p = Pattern.compile("[0-9]+\\+[0-9]+");
			Matcher m = p.matcher(tStrings[i]);
			while(m.find()) {				//compute the additional compute on address identified by '+' in assembly language
				String ttStrings[] = m.group().split("\\+");
				int a = Integer.parseInt(ttStrings[0]), b = Integer.parseInt(ttStrings[1]);
				tStrings[i] = tStrings[i].replaceAll("^" + ttStrings[0] + "\\+" + ttStrings[1], "0"+Integer.toString(a+b));
				tStrings[i] = tStrings[i].replaceAll(ttStrings[0] + "\\+" + ttStrings[1], Integer.toString(a+b));
			}
			System.out.println(tStrings[i]);
			cardStrings[i] = new String(Compiler.AssemblyToInteger(tStrings[i]));
			//System.out.println(cardStrings[i]);
		}
	}
	
	public void loadCard1(String string) throws IOException {
		String[] tStrings;
		String tString;
		InputStream inputStream = new FileInputStream(string);
		char[] tchar;
		
		//read assembly file to memory
		InputStreamReader reader = new InputStreamReader(inputStream);
		tchar = new char[inputStream.available()];
		reader.read(tchar);
		tString = new String(tchar);
		tStrings = tString.split("\n");
		fileLength = tStrings.length;
		cardStrings = new String[fileLength+1];
		reader.close();
		for(int i=0; i<fileLength; i++) {
			tStrings[i].replaceAll("\r", "");
			cardStrings[i] = tStrings[i];
		}
	}
	
	/***************************************operation for in/out/chk command*************************************/
	
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
	
	public static void main(String[] args) throws IOException {
		CardReader cardReader = new CardReader();
		cardReader.powerup();
		cardReader.loadCard("D:/wzhmmq/workspace/architecture/card");
		System.out.println(cardReader.cardStrings[0]);
	}
	
}
