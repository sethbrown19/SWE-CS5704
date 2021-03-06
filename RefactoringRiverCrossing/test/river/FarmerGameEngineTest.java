package river;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class FarmerGameEngineTest {
    private FarmerGameEngine engine;

    @Before
    public void setUp() throws Exception {
        engine = new FarmerGameEngine();
    }

    @Test
    public void testObjectsCallThrough() {
        Assert.assertEquals("Farmer", engine.getItemLabel(Item.ITEM_3));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_3));
        Assert.assertEquals(Color.MAGENTA, engine.getItemColor(Item.ITEM_3));

        Assert.assertEquals("Wolf", engine.getItemLabel(Item.ITEM_2));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_2));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_2));

        Assert.assertEquals("Goose", engine.getItemLabel(Item.ITEM_1));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_1));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_1));

        Assert.assertEquals("Beans", engine.getItemLabel(Item.ITEM_0));
        Assert.assertEquals(Location.START, engine.getItemLocation(Item.ITEM_0));
        Assert.assertEquals(Color.CYAN, engine.getItemColor(Item.ITEM_0));
    }

    private void transport(Item id) {
        engine.loadBoat(id);
        engine.rowBoat();
        engine.unloadBoat(id);
    }

    @Test
    public void testWinningGame() {
        // transport the goose
        engine.loadBoat(Item.ITEM_3);
        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport bottom (beans)
        transport(Item.ITEM_0);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back with Goose
        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // transport top (wolf)
        transport(Item.ITEM_2);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // go back alone
        engine.rowBoat();
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // travel to finish with goose and unload
        transport(Item.ITEM_1);
        engine.unloadBoat(Item.ITEM_3);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertTrue(engine.gameIsWon());
    }

    @Test
    public void testLosingGame() {
        engine.loadBoat(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        // transport the goose
        engine.loadBoat(Item.ITEM_3);
        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // row back alone and pick up wolf (top)
        engine.rowBoat();
        transport(Item.ITEM_2);
        engine.rowBoat();
        Assert.assertTrue(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());
    }

    @Test
    public void testError() {
        // transport the goose
        engine.loadBoat(Item.ITEM_3);
        transport(Item.ITEM_1);
        Assert.assertFalse(engine.gameIsLost());
        Assert.assertFalse(engine.gameIsWon());

        // save the state
        Location topLoc = engine.getItemLocation(Item.ITEM_2);
        Location midLoc = engine.getItemLocation(Item.ITEM_1);
        Location bottomLoc = engine.getItemLocation(Item.ITEM_0);
        Location playerLoc = engine.getItemLocation(Item.ITEM_3);

        // This action should do nothing since the wolf is not on the same shore as the
        // boat
        engine.loadBoat(Item.ITEM_2);

        // check that the state has not changed
        Assert.assertEquals(topLoc, engine.getItemLocation(Item.ITEM_2));
        Assert.assertEquals(midLoc, engine.getItemLocation(Item.ITEM_1));
        Assert.assertEquals(bottomLoc, engine.getItemLocation(Item.ITEM_0));
        Assert.assertEquals(playerLoc, engine.getItemLocation(Item.ITEM_3));
    }

    @Test
    public void testRestGame() {
        engine.loadBoat(Item.ITEM_2);
        Assert.assertTrue(engine.getItemLocation(Item.ITEM_2) == Location.BOAT);
        engine.resetGame();
        Assert.assertTrue(engine.getItemLocation(Item.ITEM_2) == Location.START);
    }

    @Test
    public void testLeftOver() {
        Assert.assertTrue(engine.getBoatLocation() == Location.START);
        Assert.assertTrue(engine.numberOfItems() == 4);
        engine.setItemLocation(Item.ITEM_2, Location.BOAT);
        Assert.assertTrue(engine.getItemLocation(Item.ITEM_2) == Location.BOAT);
        engine.setItemLocation(Item.ITEM_0, Location.FINISH);
        engine.setItemLocation(Item.ITEM_1, Location.FINISH);
        Assert.assertTrue(engine.gameIsLost());


    }
}
