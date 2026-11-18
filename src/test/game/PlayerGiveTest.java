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
public class PlayerGiveTest {
    private final List<Item> items = new ArrayList<>();
    private final List<String> itemIds = new ArrayList<>();
    private final Item reward = new Item("reward", "");
    @Mock
    private Inventory inventory;
    @Mock
    private Location location;
    @Mock
    private Character character;
    private Player player;

    @Before
    public void setUp() {
        player = new Player(location, inventory);

        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(character.getName()).thenReturn("Alex");

        items.add(new Item("pizza", ""));
        items.add(new Item("coke", ""));
        items.add(new Item("bread", ""));
        items.add(new Item("cheese", ""));

        itemIds.add("pizza");
        itemIds.add("coke");
        itemIds.add("bread");
        itemIds.add("cheese");

        when(inventory.getMany(itemIds)).thenReturn(items);
        when(inventory.add(any())).thenReturn(true);
    }

    @Test
    public void givenNonexistentCharacter_whenGive_shouldReturnStringSayingNoCharacter() {
        when(location.getCharacter("Alex")).thenReturn(Optional.empty());

        String result = player.give("Alex", itemIds);

        assertEquals("There is no character named Alex in this location.", result);
    }

    @Test
    public void givenSomeNonexistentItems_whenGive_shouldReturnStringSayingNoItems() {
        when(inventory.getMany(itemIds)).thenReturn(items.subList(0, 2));

        String result = player.give("Alex", itemIds);

        assertEquals("You don't have these items in your inventory: bread and cheese.", result);
    }

    @Test
    public void givenRejectedItems_whenGive_shouldReturnStringSayingRejectedItems() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(items).build();
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        assertEquals("Alex doesn't seem interested in pizza, coke, bread, and cheese.", result);
    }

    @Test
    public void givenTradeWithConsumedItems_whenGive_shouldRewardItemsAndRemovedConsumedItems() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(items).rewardedItems(List.of(reward)).build();
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        itemIds.forEach(id -> verify(inventory).remove(id));
        verify(inventory).add(reward);
        assertEquals("Alex gives you reward in return.", result);
    }

    @Test
    public void givenTradeWithReturnedItems_whenGive_shouldRewardItemsAndKeepReturnedItems() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().returnedItems(items).rewardedItems(List.of(reward)).build();
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        verify(inventory).add(reward);
        assertEquals(
                "Alex feels thankful for your pizza, coke, bread, and cheese and has given them back.\n\nAlex gives you reward in return.",
                result);
    }

    @Test
    public void givenTradeWithWithheldItems_whenGive_shouldRemoveWithheldItemsFromInventory() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().withheldItems(items).build();
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        itemIds.forEach(id -> verify(inventory).remove(id));
        assertEquals("Alex accepts your pizza, coke, bread, and cheese but needs something else to go with them.",
                     result);
    }

    @Test
    public void givenTradeWithMixedItems_whenGive_shouldHandleItemsAndFormatMessageCorrectly() {
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(List.of(items.getFirst()))
                .consumedItems(List.of(items.get(1)))
                .returnedItems(List.of(items.get(2)))
                .rewardedItems(List.of(reward))
                .withheldItems(List.of(items.get(3)))
                .build();
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        verify(inventory).remove("coke");
        verify(inventory).remove("cheese");
        verify(inventory).add(reward);
        assertEquals("""
                             Alex doesn't seem interested in pizza.
                             
                             Alex feels thankful for your bread and has given them back.
                             
                             Alex gives you reward in return.
                             
                             Alex accepts your cheese but needs something else to go with them.""", result);
    }

    @Test
    public void givenInventoryFull_whenGive_shouldLeaveRewardsAtLocation() {
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(items).rewardedItems(List.of(reward)).build();
        when(inventory.add(reward)).thenReturn(false);
        when(character.trade(items)).thenReturn(exchangeResult);

        String result = player.give("Alex", itemIds);

        verify(location).leaveItem(reward);
        assertEquals("""
                             Alex gives you reward in return.
                             
                             However, as your inventory is full, you had to leave reward at your location. You need to take some items off your inventory to pick them up.""",
                     result);
    }
}