package game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerGiveTest {
    private final Item pizza = new Item("pizza", "Pizza description.");
    private final Item coke = new Item("coke", "Coke description.");
    private final Item bread = new Item("bread", "Bread description.");
    private final Item cheese = new Item("cheese", "Cheese description.");
    private final Item reward = new Item("reward", "reward desc");
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
    }

    @Test
    public void givenNonexistentCharacter_whenGive_shouldReturnStringSayingNoCharacter() {
        when(location.getCharacter("Alex")).thenReturn(Optional.empty());

        String result = player.give("Alex", List.of("pizza", "coke"));

        assertEquals("There is no character named Alex in this location.", result);
    }

    @Test
    public void givenSomeNonexistentItems_whenGive_shouldReturnStringSayingNoItems() {
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(List.of("pizza", "coke", "bread"))).thenReturn(List.of(pizza));

        String result = player.give("Alex", List.of("pizza", "coke", "bread"));

        assertEquals("You don't have these items in your inventory: coke and bread.", result);
    }

    @Test
    public void givenRejectedItems_whenGive_shouldReturnStringSayingRejectedItems() {
        List<String> itemIds = List.of("pizza", "coke");
        List<Item> items = List.of(pizza, coke);
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(items).build();
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(itemIds)).thenReturn(items);
        when(character.trade(items)).thenReturn(exchangeResult);
        when(character.getName()).thenReturn("Alex");

        String result = player.give("Alex", itemIds);

        assertEquals("Alex doesn't seem interested in pizza and coke.", result);
    }

    @Test
    public void givenTradeWithConsumedItems_whenGive_shouldRewardItemsAndRemovedConsumedItems() {
        List<String> itemIds = List.of("pizza", "coke");
        List<Item> items = List.of(pizza, coke);
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(items).rewardedItems(List.of(reward)).build();
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(itemIds)).thenReturn(items);
        when(character.trade(items)).thenReturn(exchangeResult);
        when(character.getName()).thenReturn("Alex");

        String result = player.give("Alex", itemIds);

        verify(inventory).remove("pizza");
        verify(inventory).remove("coke");
        verify(inventory).add(reward);
        assertEquals("Alex gives you reward in return.", result);
    }

    @Test
    public void givenTradeWithReturnedItems_whenGive_shouldRewardItemsAndKeepReturnedItems() {
        List<String> itemIds = List.of("pizza", "coke");
        List<Item> items = List.of(pizza, coke);
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().returnedItems(items).rewardedItems(List.of(reward)).build();
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(itemIds)).thenReturn(items);
        when(character.trade(items)).thenReturn(exchangeResult);
        when(character.getName()).thenReturn("Alex");

        String result = player.give("Alex", itemIds);

        verify(inventory).add(reward);
        assertEquals(
                "Alex feels thankful for your pizza and coke and has given them back.\n\nAlex gives you reward in return.",
                result);
    }

    @Test
    public void givenTradeWithWithheldItems_whenGive_shouldRemoveWithheldItemsFromInventory() {
        List<String> itemIds = List.of("pizza", "coke");
        List<Item> items = List.of(pizza, coke);
        ExchangeResult exchangeResult = new ExchangeResult.Builder().withheldItems(items).build();
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(itemIds)).thenReturn(items);
        when(character.trade(items)).thenReturn(exchangeResult);
        when(character.getName()).thenReturn("Alex");

        String result = player.give("Alex", itemIds);

        verify(inventory).remove("pizza");
        verify(inventory).remove("coke");
        assertEquals("Alex accepts your pizza and coke but needs something else to go with them.", result);
    }

    @Test
    public void givenTradeWithMixedItems_whenGive_shouldHandleItemsAndFormatMessageCorrectly() {
        List<String> itemIds = List.of("pizza", "coke", "bread", "cheese");
        List<Item> items = List.of(pizza, coke, bread, cheese);
        ExchangeResult exchangeResult = new ExchangeResult.Builder().rejectedItems(List.of(items.getFirst()))
                .consumedItems(List.of(items.get(1)))
                .returnedItems(List.of(items.get(2)))
                .rewardedItems(List.of(reward))
                .withheldItems(List.of(items.get(3)))
                .build();
        when(location.getCharacter("Alex")).thenReturn(Optional.of(character));
        when(inventory.getMany(itemIds)).thenReturn(items);
        when(character.trade(items)).thenReturn(exchangeResult);
        when(character.getName()).thenReturn("Alex");

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
}