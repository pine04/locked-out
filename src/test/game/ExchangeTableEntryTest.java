package game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeTableEntryTest {
    private final Item pizza = new Item("pizza", "");
    private final Item cheese = new Item("cheese", "");
    private final Item rewardItem = new Item("reward", "");
    @Mock
    private Supplier<List<Item>> reward;
    @Mock
    private SideEffect sideEffect;

    @Test
    public void givenNegativeMaxTimesRewarded_whenCallAcceptsNewExchanges_shouldReturnTrue() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().maxTimesRewarded(-1).build();

        assertTrue(entry.acceptsNewExchanges());
    }

    @Test
    public void givenZeroMaxTimesRewarded_whenCallAcceptsNewExchanges_shouldReturnFalse() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().maxTimesRewarded(0).build();

        assertFalse(entry.acceptsNewExchanges());
    }

    @Test
    public void givenPositiveMaxTimesRewarded_whenCallAcceptsNewExchanges_shouldReturnTrue() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().maxTimesRewarded(1).build();

        assertTrue(entry.acceptsNewExchanges());
    }

    @Test
    public void givenRequiredItemId_whenCallRequires_shouldReturnTrue() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED).build();

        assertTrue(entry.requires("pizza"));
    }

    @Test
    public void givenNonrequiredItemId_whenCallRequires_shouldReturnFalse() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED).build();

        assertFalse(entry.requires("cheese"));
    }

    @Test
    public void givenEntryNotAcceptingExchanges_whenCallTryExchange_shouldReturnEmptyResult() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED)
                .reward(reward)
                .sideEffect(sideEffect)
                .maxTimesRewarded(0)
                .build();

        ExchangeResult result = entry.tryExchange(List.of(new Item("pizza", "")));

        assertTrue(isEmptyExchangeResult(result));
        verify(reward, never()).get();
        verify(sideEffect, never()).execute();
    }

    @Test
    public void givenIncompleteItems_whenCallTryExchange_shouldReturnEmptyResult() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED)
                .addItem("cheese", ItemConsumption.CONSUMED)
                .reward(reward)
                .sideEffect(sideEffect)
                .maxTimesRewarded(1)
                .build();

        ExchangeResult result = entry.tryExchange(List.of(pizza));

        assertTrue(isEmptyExchangeResult(result));
        verify(reward, never()).get();
        verify(sideEffect, never()).execute();
    }

    @Test
    public void givenCompleteItems_whenCallTryExchange_shouldCallRewardAndSideEffectAndReturnNonEmptyResult() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED)
                .reward(reward)
                .sideEffect(sideEffect)
                .maxTimesRewarded(-1)
                .build();
        when(reward.get()).thenReturn(List.of(rewardItem));

        ExchangeResult result = entry.tryExchange(List.of(pizza));

        verify(reward).get();
        verify(sideEffect).execute();
        assertEquals(List.of(pizza), result.getConsumedItems());
        assertEquals(List.of(rewardItem), result.getRewardedItems());
    }

    @Test
    public void givenTradeThatReachedLimit_whenCallTryExchange_shouldReturnEmptyResult() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED)
                .reward(reward)
                .sideEffect(sideEffect)
                .maxTimesRewarded(1)
                .build();
        when(reward.get()).thenReturn(List.of(rewardItem));

        entry.tryExchange(List.of(pizza));
        ExchangeResult secondResult = entry.tryExchange(List.of(pizza));

        assertTrue(isEmptyExchangeResult(secondResult));
        verify(reward, times(1)).get();
        verify(sideEffect, times(1)).execute();
    }

    @Test
    public void givenExchangeWithReturnedItems_whenCallTryExchange_shouldReturnResultWithReturnedItems() {
        ExchangeTableEntry entry = new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED)
                .addItem("cheese", ItemConsumption.RETURNED)
                .maxTimesRewarded(1)
                .build();

        ExchangeResult result = entry.tryExchange(List.of(pizza, cheese));

        assertEquals(List.of(pizza), result.getConsumedItems());
        assertEquals(List.of(cheese), result.getReturnedItems());
    }

    @Test
    public void givenExchangeWithNoRewards_whenCallTryExchange_shouldReturnResultWithEmptyReward() {
        ExchangeTableEntry entry =
                new ExchangeTableEntry.Builder().addItem("pizza", ItemConsumption.CONSUMED).maxTimesRewarded(1).build();

        ExchangeResult result = entry.tryExchange(List.of(pizza));

        assertEquals(List.of(), result.getRewardedItems());
    }

    private boolean isEmptyExchangeResult(ExchangeResult result) {
        return result.getRejectedItems().isEmpty() && result.getConsumedItems().isEmpty() &&
                result.getReturnedItems().isEmpty() && result.getRewardedItems().isEmpty() &&
                result.getWithheldItems().isEmpty();
    }
}