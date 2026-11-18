package game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerUseOnTest {
    private final List<Item> items = new ArrayList<>();
    private final List<String> itemIds = new ArrayList<>();
    private final Item reward = new Item("reward", "");
    @Mock
    private Inventory inventory;
    @Mock
    private Location location;
    @Mock
    private Fixture fixture;
    private Player player;

    @Before
    public void setUp() {
        player = new Player(location, inventory);

        when(location.getFixture("oven")).thenReturn(Optional.of(fixture));
        when(fixture.getName()).thenReturn("oven");

        items.add(new Item("pizza base", ""));
        items.add(new Item("baking sheet", ""));
        items.add(new Item("flour", ""));
        items.add(new Item("trash", ""));

        itemIds.add("pizza base");
        itemIds.add("baking sheet");
        itemIds.add("flour");
        itemIds.add("trash");

        when(inventory.getMany(itemIds)).thenReturn(items);
        when(inventory.add(any())).thenReturn(true);
    }

    @Test
    public void givenNonexistentFixture_whenUseOn_shouldReturnStringSayingNoFixture() {
        when(location.getFixture("oven")).thenReturn(Optional.empty());

        String result = player.useOn("oven", itemIds);

        assertEquals("There is no oven in this location.", result);
    }

    @Test
    public void givenSomeNonexistentItems_whenUseOn_shouldReturnStringSayingNoItems() {
        when(inventory.getMany(itemIds)).thenReturn(items.subList(0, 2));

        String result = player.useOn("oven", itemIds);

        assertEquals("You don't have these items in your inventory: flour and trash.", result);
    }

    @Test
    public void givenRejectedItems_whenUseOn_shouldReturnStringSayingRejectedItems() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(items).build();
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        assertEquals("You can't use pizza base, baking sheet, flour, and trash on oven.", result);
    }

    @Test
    public void givenTradeWithConsumedItems_whenUseOn_shouldRewardItemsAndRemovedConsumedItems() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(items).rewardedItems(List.of(reward)).build();
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        itemIds.forEach(id -> verify(inventory).remove(id));
        verify(inventory).add(reward);
        assertEquals("You receive reward from oven as a reward.", result);
    }

    @Test
    public void givenTradeWithReturnedItems_whenUseOn_shouldRewardItemsAndKeepReturnedItems() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().returnedItems(items).rewardedItems(List.of(reward)).build();
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        verify(inventory).add(reward);
        assertEquals(
                "You can still keep your pizza base, baking sheet, flour, and trash after using them on oven.\n\nYou receive reward from oven as a reward.",
                result);
    }

    @Test
    public void givenTradeWithWithheldItems_whenUseOn_shouldRemoveWithheldItemsFromInventory() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().withheldItems(items).build();
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        itemIds.forEach(id -> verify(inventory).remove(id));
        assertEquals(
                "You use pizza base, baking sheet, flour, and trash on oven, but perhaps something else is also needed.",
                result);
    }

    @Test
    public void givenTradeWithMixedItems_whenUseOn_shouldHandleItemsAndFormatMessageCorrectly() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(List.of(items.getLast()))
                .consumedItems(List.of(items.getFirst()))
                .returnedItems(List.of(items.get(1)))
                .rewardedItems(List.of(reward))
                .withheldItems(List.of(items.get(2)))
                .build();
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        verify(inventory).remove("pizza base");
        verify(inventory).remove("flour");
        verify(inventory).add(reward);
        assertEquals("""
                             You can't use trash on oven.
                             
                             You can still keep your baking sheet after using them on oven.
                             
                             You receive reward from oven as a reward.
                             
                             You use flour on oven, but perhaps something else is also needed.""", result);
    }

    @Test
    public void givenInventoryFull_whenUseOn_shouldLeaveRewardsAtLocation() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(items).rewardedItems(List.of(reward)).build();
        when(inventory.add(reward)).thenReturn(false);
        when(fixture.interact(items)).thenReturn(exchangeResult);

        String result = player.useOn("oven", itemIds);

        verify(location).leaveItem(reward);
        assertEquals("""
                             You receive reward from oven as a reward.
                             
                             However, as your inventory is full, you had to leave reward at your location. You need to take some items off your inventory to pick them up.""",
                     result);
    }
}