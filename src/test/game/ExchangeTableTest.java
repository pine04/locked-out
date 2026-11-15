package game;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

public class ExchangeTableTest {
    private final Item pizza = new Item("pizza", "");
    private final Item cheese = new Item("cheese", "");
    private final Item bread = new Item("bread", "");
    private final Item apple = new Item("apple", "");
    private final Item reward1 = new Item("reward1", "");
    private final Item reward2 = new Item("reward2", "");

    @Test
    public void givenNoEntriesAcceptingItems_whenExchange_shouldReturnResultWithRejectedItems() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        when(mockEntry.requires(any())).thenReturn(false);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result = table.exchange(List.of(pizza, cheese));

        assertEquals(List.of(pizza, cheese), result.getRejectedItems());
    }

    @Test
    public void givenNoEntriesAcceptingExchanges_whenExchange_shouldReturnResultWithAllItemsRejected() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        when(mockEntry.acceptsNewExchanges()).thenReturn(false);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result = table.exchange(List.of(pizza, cheese));

        assertEquals(List.of(pizza, cheese), result.getRejectedItems());
    }

    @Test
    public void givenProvidedItemsSatisfyAnEntry_whenExchange_shouldReturnResultWithReward() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(List.of(pizza)).rewardedItems(List.of(reward1)).build();
        when(mockEntry.tryExchange(any())).thenReturn(exchangeResult);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result = table.exchange(List.of(pizza));

        assertEquals(List.of(pizza), result.getConsumedItems());
        assertEquals(List.of(reward1), result.getRewardedItems());
    }

    @Test
    public void givenTradeWithMixedItems_whenExchange_shouldReturnCorrectResult() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        when(mockEntry.requires(apple.identifier())).thenReturn(false);
        ExchangeResult exchangeResult = new ExchangeResult.Builder().consumedItems(List.of(pizza))
                .returnedItems(List.of(bread))
                .rewardedItems(List.of(reward1))
                .build();
        when(mockEntry.tryExchange(any())).thenReturn(exchangeResult);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result = table.exchange(List.of(pizza, bread, apple));

        assertEquals(List.of(apple), result.getRejectedItems());
        assertEquals(List.of(pizza), result.getConsumedItems());
        assertEquals(List.of(bread), result.getReturnedItems());
        assertEquals(List.of(reward1), result.getRewardedItems());
    }

    @Test
    public void givenIncompleteItems_whenExchange_shouldReturnResultWithWithheldItems() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result = table.exchange(List.of(pizza));

        assertEquals(List.of(pizza), result.getWithheldItems());
        assertEquals(List.of(), result.getRewardedItems());
    }

    @Test
    public void givenAnotherExchangeToFinishEntry_whenExchange_shouldReturnResultWithReward() {
        ExchangeTableEntry mockEntry = createBaseMockEntry();
        ExchangeResult exchangeResult = new ExchangeResult.Builder().consumedItems(List.of(pizza, cheese))
                .rewardedItems(List.of(reward1))
                .build();
        when(mockEntry.tryExchange(argThat(args -> args.containsAll(List.of(pizza, cheese))))).thenReturn(
                exchangeResult);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry).build();

        ExchangeResult result1 = table.exchange(List.of(pizza));
        ExchangeResult result2 = table.exchange(List.of(cheese));

        assertEquals(List.of(pizza), result1.getWithheldItems());
        assertEquals(List.of(), result1.getRewardedItems());
        assertEquals(List.of(pizza, cheese), result2.getConsumedItems());
        assertEquals(List.of(reward1), result2.getRewardedItems());
    }

    @Test
    public void givenAnotherExchangeToFinishTwoEntries_whenExchange_shouldReturnResultWithBothRewards() {
        ExchangeTableEntry mockEntry1 = createBaseMockEntry();
        ExchangeResult exchangeResult1 = new ExchangeResult.Builder().consumedItems(List.of(pizza, cheese))
                .rewardedItems(List.of(reward1))
                .build();
        when(mockEntry1.tryExchange(argThat(args -> args.containsAll(List.of(pizza, cheese))))).thenReturn(
                exchangeResult1);
        ExchangeTableEntry mockEntry2 = createBaseMockEntry();
        ExchangeResult exchangeResult2 = new ExchangeResult.Builder().consumedItems(List.of(bread, apple))
                .rewardedItems(List.of(reward2))
                .build();
        when(mockEntry2.tryExchange(argThat(args -> args.containsAll(List.of(bread, apple))))).thenReturn(
                exchangeResult2);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry1).addEntry(mockEntry2).build();

        ExchangeResult result1 = table.exchange(List.of(pizza, bread));
        ExchangeResult result2 = table.exchange(List.of(cheese, apple));

        assertEquals(List.of(pizza, bread), result1.getWithheldItems());
        assertEquals(List.of(), result1.getRewardedItems());
        assertEquals(List.of(pizza, cheese, bread, apple), result2.getConsumedItems());
        assertEquals(List.of(reward1, reward2), result2.getRewardedItems());
    }

    @Test
    public void givenFirstEntryFailsAndSecondEntrySucceeds_whenExchange_shouldReturnResultWithSecondEntryRewards() {
        ExchangeTableEntry mockEntry1 = createBaseMockEntry();
        ExchangeTableEntry mockEntry2 = createBaseMockEntry();
        ExchangeResult exchangeResult =
                new ExchangeResult.Builder().consumedItems(List.of(pizza)).rewardedItems(List.of(reward2)).build();
        when(mockEntry2.tryExchange(any())).thenReturn(exchangeResult);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry1).addEntry(mockEntry2).build();

        ExchangeResult result = table.exchange(List.of(pizza));

        assertEquals(List.of(pizza), result.getConsumedItems());
        assertEquals(List.of(reward2), result.getRewardedItems());
    }

    @Test
    public void givenTwoEntriesWithSameConsumedItemsAndFirstSucceeds_whenExchange_shouldFailSecond() {
        ExchangeTableEntry mockEntry1 = createBaseMockEntry();
        ExchangeResult exchangeResult1 = new ExchangeResult.Builder().consumedItems(List.of(pizza, cheese))
                .rewardedItems(List.of(reward1))
                .build();
        when(mockEntry1.tryExchange(any())).thenReturn(exchangeResult1);
        ExchangeTableEntry mockEntry2 = createBaseMockEntry();
        ExchangeResult exchangeResult2 = new ExchangeResult.Builder().consumedItems(List.of(pizza, apple))
                .rewardedItems(List.of(reward2))
                .build();
        when(mockEntry2.tryExchange(argThat(args -> args.containsAll(List.of(pizza, apple))))).thenReturn(
                exchangeResult2);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry1).addEntry(mockEntry2).build();

        ExchangeResult result = table.exchange(List.of(pizza, cheese, apple));

        assertEquals(List.of(pizza, cheese), result.getConsumedItems());
        assertEquals(List.of(apple), result.getWithheldItems());
        assertEquals(List.of(reward1), result.getRewardedItems());
    }

    @Test
    public void givenTwoEntriesWithSameReturnedItemsAndFirstSucceeds_whenExchange_shouldFailSecond() {
        ExchangeTableEntry mockEntry1 = createBaseMockEntry();
        ExchangeResult exchangeResult1 = new ExchangeResult.Builder().returnedItems(List.of(pizza, cheese))
                .rewardedItems(List.of(reward1))
                .build();
        when(mockEntry1.tryExchange(any())).thenReturn(exchangeResult1);
        ExchangeTableEntry mockEntry2 = createBaseMockEntry();
        ExchangeResult exchangeResult2 = new ExchangeResult.Builder().returnedItems(List.of(pizza, apple))
                .rewardedItems(List.of(reward2))
                .build();
        when(mockEntry2.tryExchange(argThat(args -> args.containsAll(List.of(pizza, apple))))).thenReturn(
                exchangeResult2);
        ExchangeTable table = new ExchangeTable.Builder().addEntry(mockEntry1).addEntry(mockEntry2).build();

        ExchangeResult result = table.exchange(List.of(pizza, cheese, apple));

        assertEquals(List.of(pizza, cheese), result.getReturnedItems());
        assertEquals(List.of(apple), result.getWithheldItems());
        assertEquals(List.of(reward1), result.getRewardedItems());
    }

    private ExchangeTableEntry createBaseMockEntry() {
        ExchangeTableEntry mockEntry = Mockito.mock(ExchangeTableEntry.class);
        when(mockEntry.acceptsNewExchanges()).thenReturn(true);
        when(mockEntry.requires(any())).thenReturn(true);
        when(mockEntry.tryExchange(any())).thenReturn(ExchangeResult.empty());
        return mockEntry;
    }
}