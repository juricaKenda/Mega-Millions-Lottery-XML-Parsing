package lottery;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.stream;

import org.junit.jupiter.api.Test;

class StorageTests {

	LotteryStats testObject = new LotteryStats();
	
	
	@Test
	void errorSlotEmpty() {
		int errorSlotLen = testObject.getStorage()
									 .get(LotteryStats.getErrorSlotNum()).size();
		assertEquals(errorSlotLen,0);
	}
	
	@Test
	void noDistinctYears() {
		long totalDistinct = 0;
		
		for(int eachSlot=0; eachSlot<testObject.getStorage().size()-1; eachSlot++) {
			
			totalDistinct += 1 - testObject.getStorage().get(eachSlot)
											   		   .stream()
											   		   .mapToInt(dataBundle -> dataBundle.getYear())
											   		   .distinct()
											   		   .count();
		}
		assertEquals(totalDistinct,0);
	}

}
