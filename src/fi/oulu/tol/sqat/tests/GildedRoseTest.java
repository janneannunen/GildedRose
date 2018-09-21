package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80)); hotword
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)); hotword
//   Item("Conjured Mana Cake", 3, 6));

	
	// tests needed:
	//OK  standard agening for normal product. One sample is enough
	//OK -1 every day
	//OK Check border values 0 and 
	//OK 50 (normal) ajetaan 40 päivää, +
	//OK special (80)
	//OK Back stage pass same price till 10, 10-6 +2, 5-0 +3 after 0 menee nollaksi
	
	
	
	
	@Test
	// Syntax Item("Name",SellIn,Quality)
	public void testUpdateEndOfDay_AgedBrie_Quality_10_11() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals(11, itemBrie.getQuality());
	}
	
	
	
	//@Test
	//public void testUpdateEndOfDay() {
	//	fail("Test not implemented");
	// }

	// Syntax Item("Name",SellIn,Quality)
	//   Test normal item -1/day result 0 for both
		@Test
		public void testUpdateEndOfDay_SellIn_Quality_11_11() {
			// Arrange
			GildedRose store = new GildedRose();
			store.addItem(new Item("Normal Item", 11, 11) ); // nimi, myynnissä olevat päivät, laautarvo
			
			int days=0;
			// Act
			do {
				store.updateEndOfDay();
				days++;
			}
			while( days < 11); // tästä kopio ja päivä lisää, tulos on sama jolloin ylitys on tarkistettu
			
			// Assert
			List<Item> items = store.getItems();
			Item itemNormal = items.get(0);
			assertEquals(11, days); //looppi että pyörähtänyt 11 kertaa
			assertEquals(0, itemNormal.getQuality()); // osannut vähentää jokaiselle päivälle -1
			assertEquals(0, itemNormal.getSellIn());  //myyntiarvo vähentynyt yhdellä 11 päivän jälkeen
		}
		
	// -2x
		@Test
		public void testUpdateEndOfDay_SellIn_Quality_1_12() {
			// Arrange
			GildedRose store = new GildedRose();
			store.addItem(new Item("Normal Item", 1, 12) );
			int days=0;
			// Act
			do {
				store.updateEndOfDay();
				days++;
			}
			while( days < 5); // kun päiviä on jäljellä alle 5 eli menee 4 yli myynnissä olevan päivän
			// eli jäännösarvoksi laadulle jää getQuality 3
			
			// Assert
			List<Item> items = store.getItems();
			Item itemNormal = items.get(0);
			assertEquals(5, days); // lähtöarvo 1, 4 yli eli arvolla x2 = 8
			assertEquals(3, itemNormal.getQuality()); //lähtöarvo 12 -1 -8 = 3
			assertEquals(0, itemNormal.getSellIn());
		}
		
		//   Test normal item -1/day result 0 for both even if overtime
		@Test
		public void testUpdateEndOfDay_SellIn_Quality_11_11_overdue12() {
			// Arrange
			GildedRose store = new GildedRose();
			store.addItem(new Item("Normal Item", 11, 11) ); // nimi, myynnissä olevat päivät, laautarvo
			
			int days=0;
			// Act
			do {
				store.updateEndOfDay();
				days++;
			}
			while( days < 12); // tästä kopio ja päivä lisää, tulos on sama jolloin ylitys on tarkistettu
			
			// Assert
			List<Item> items = store.getItems();
			Item itemNormal = items.get(0);
			assertEquals(12, days); //looppi että pyörähtänyt 12 kertaa
			assertEquals(0, itemNormal.getQuality()); // osannut vähentää jokaiselle päivälle -1 mutta arvo 0 alitu
			assertEquals(0, itemNormal.getSellIn());  //myyntiarvo vähentynyt yhdellä 11 päivän jälkeen
		}
		
	//  Item("Backstage passes to a TAFKAL80ETC concert", 15, 20)); Quality +2 SellIn<10 and +3 SellIn<5
		@Test
		public void testUpdateEndOfDayBackstage_passes() {
			// Arrange
			GildedRose store = new GildedRose();
			store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 15, 40)); //max saa olla 50
			int days=0;
			// Act
			do {
				store.updateEndOfDay();
				days++;
				}
			while( days < 15);
			// Assert
			List<Item> items = store.getItems();
			Item itemRagnaros = items.get(0);
			assertEquals(15, days);
			assertEquals(50, itemRagnaros.getQuality()); // saa olla max 50
			assertEquals(0, itemRagnaros.getSellIn());
		}
	 //  Item("Sulfuras, Hand of Ragnaros", 0, 80)); legend
			@Test
			public void testUpdateEndOfDayRagnaros() {
				// Arrange
				GildedRose store = new GildedRose();
				store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80)); //erikoistuote, ei saa muuttua
				int days=0;
				// Act
				do {
					store.updateEndOfDay();
					days++;
					}
				while( days < 5);
				// Assert
				List<Item> items = store.getItems();
				Item itemRagnaros = items.get(0);
				assertEquals(5, days); // päivämäärällä ei saa olla merkitystä
				assertEquals(80, itemRagnaros.getQuality()); //pitää säilyä samana
				assertEquals(0, itemRagnaros.getSellIn()); // putoaa päiväyksen jälkeen nollaksi ei saaa mennä miinukselle
			}
}
