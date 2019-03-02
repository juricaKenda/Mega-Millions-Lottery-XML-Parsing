package lottery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class LotteryStats {

	private String fileLoc = "...\\LotteryWinning Numbers.xml";
	private File dataFile = new File(fileLoc);
	
	private Log errorLogger = Log.getInstance();
	private ArrayList<LinkedList<DrawData>> dataStorage;
	private DrawData currentlyParsing = new DrawData();
	
	private static final int START_YEAR = 2002;
	private static final int ERROR_SLOT = 18; 
	
	
	public LotteryStats(){
		//Years stretch out from 2002 to 2019
		this.dataStorage = new ArrayList<LinkedList<DrawData>>();
		
		for(int each=0; each< ERROR_SLOT +1; each++) {
			this.dataStorage.add(each, new LinkedList<DrawData>());
		}
		parse();
		
	}
	
	public void parse() {
		
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = parserFactory.newSAXParser();
			parser.parse(this.dataFile,new Handler());
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}
	public void displayErrorLog() {
		if(this.errorLogger != null) {
			System.out.println(this.errorLogger.getLog());
		}
	}
	
	public void displayMostCommonNumbers() {
		System.out.println("----------------------");
		
		for(LinkedList<DrawData> eachList : this.dataStorage) {
			//Error slot should be empty and therefore not inspected
			if(eachList.equals(dataStorage.get(ERROR_SLOT))) {
				break;
			}
			/*
			 * New hash map for each linked list will store the
			 * numbers drawn and the amount of times they occurred that year
			 */
			HashMap<Integer,Integer> map = new HashMap<>();
			
			eachList.forEach(input -> {
				for(int eachNum : input.winningNumbersSeparated) {
					if(map.containsKey(eachNum)) {
						map.replace(eachNum, map.get(eachNum)+1);
					}else {
						map.put(eachNum, 1);
					}
				}
			});
			/*
			 * Since hash map is convenient for counting, and not for sorting,
			 * we move the data to an array list
			 */
			ArrayList<Entry<Integer,Integer>> sortedList = new ArrayList<>(map.entrySet());
			sortedList.sort(Entry.comparingByValue());
			Collections.reverse(sortedList);
			
			System.out.println("Year : "+eachList.get(0).getYear());
			System.out.println("Most common element: "
								+sortedList.get(0).getKey()
								+" appeared " + sortedList.get(0).getValue()
								+" times");
			
		}
	}
	public void displayMostConsecutiveDraw(){
		System.out.println("----------------------");
		System.out.println("DRAW WITH MOST CONSECUTIVE ELEMENTS:");
		
		DrawData mostConsecutive = this.dataStorage.get(0).get(0);
		int mostNumberOfConsecutive = 0,currentNumberOfConsecutive;
		
		for(LinkedList<DrawData> eachYear : this.dataStorage) {
			for(DrawData eachDraw : eachYear) {
				//Reset the consecutive counter and sort the draw numbers
				currentNumberOfConsecutive=0;
				int[] workingNumbers = eachDraw.winningNumbersSeparated;
				Arrays.sort(workingNumbers);

				//Count the number of consecutive numbers in this array
				for(int counter=0; counter < workingNumbers.length-1; counter++) {
					if(workingNumbers[counter+1] - workingNumbers[counter] == 1) {
						currentNumberOfConsecutive++;
					}else {
						currentNumberOfConsecutive=0;
					}
				}
				//Replace the 'top' draw with this one, if better
				if(currentNumberOfConsecutive >= mostNumberOfConsecutive) {
					mostConsecutive = eachDraw;
					mostNumberOfConsecutive = currentNumberOfConsecutive;
				}
				
			}
		}
		
		System.out.println(mostConsecutive.drawDate + " had "
				+(mostNumberOfConsecutive+1) +" consecutive numbers");
		
		System.out.println("All draw numbers: " + mostConsecutive.winningNumbersBundle);
		
	}
	/**
	 * Displays the number of draws that happened each year in the data set
	 */
	public void displayDrawsPerYear() {
		System.out.println("----------------------");
		System.out.println("DRAWS PER YEAR STATS:");
		
		for(LinkedList<DrawData> each : this.dataStorage) {
			
			if(each.equals(dataStorage.get(ERROR_SLOT))) {
				break;
			}
			System.out.println("Year : " + each.get(0).drawYear
					+" Total draws : " + each.size());
		}
	}
	
	/**
	 * 
	 * @return storage of the organized data, for testing purposes
	 */
	public ArrayList<LinkedList<DrawData>> getStorage(){
		return this.dataStorage;
	}
	
	/**
	 * 
	 * @return index of the error slot, for testing purposes
	 */
	public static int getErrorSlotNum() {
		return LotteryStats.ERROR_SLOT;
	}
	
	/**
	 * Handler class for SAX Parsing
	 *
	 */
	class Handler extends DefaultHandler{
		
		private boolean bDrawDate;
		private boolean bWinningNumbers;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes){
			if(qName.equalsIgnoreCase("draw_date")) {
				bDrawDate = true;
			}
			else if(qName.equalsIgnoreCase("winning_numbers")) {
				bWinningNumbers = true;
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) {
			String read = new String(ch,start,length);
			DrawData current = LotteryStats.this.currentlyParsing;
			
			if(bDrawDate) {
				bDrawDate = false;
				current.setDrawDate(read);
				
			}else if(bWinningNumbers) {
				bWinningNumbers = false; 
				current.setWinningNumbers(read);
				current.extract();
				
				if(currentlyParsing.isUsable()) {
					LinkedList<DrawData> list = LotteryStats.this.dataStorage.get(current.calculateSlot());
					list.add(current);
				}
				
				LotteryStats.this.currentlyParsing = new DrawData();
			
			}
		}
	}
	
	/**
	 * Represents relevant data about each draw
	 *
	 */
	class DrawData {
		
		private String drawDate,winningNumbersBundle;
		private int drawYear;
		private int[] winningNumbersSeparated;
		private boolean isUsable = true;
		
		public int getYear() {
			return this.drawYear;
		}
		public void setDrawDate(String date) {
			this.drawDate = date;
		}
		
		public void setWinningNumbers(String numbers) {
			this.winningNumbersBundle = numbers;
		}
		
		/**
		 * Extract the raw data to a format easier to work with
		 */
		private void extract() {
			try {
				this.drawYear = Integer.parseInt(this.drawDate.split("-")[0]);
				
				String[] numbers = this.winningNumbersBundle.split(" ");
				this.winningNumbersSeparated = new int[numbers.length];
				for(int each=0; each<numbers.length; each++) {
					this.winningNumbersSeparated[each] = Integer.parseInt(numbers[each]);
				}
				
			}catch(NumberFormatException e) {
				errorLogger.log("Parsing exception : " + this.drawDate);
				this.isUsable = false;
			}
		}


		public boolean isUsable() {
			return this.isUsable;
		}
		
		public int calculateSlot() {
			
			if(this.drawYear >= LotteryStats.START_YEAR) {
				return (this.drawYear - LotteryStats.START_YEAR);
			}
			return LotteryStats.ERROR_SLOT;
		}
	
		
		@Override
		public String toString() {
			return "Draw year:" + this.drawYear 
					+"/Numbers :" + this.winningNumbersBundle;
		}
	}

	
	
	public static void main(String[] args) {
		LotteryStats statistics = new LotteryStats();
		statistics.displayMostCommonNumbers();
		statistics.displayDrawsPerYear();
		statistics.displayMostConsecutiveDraw();
	}
}
