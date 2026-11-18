package game;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExchangeResultTest {

    @Test
    public void givenExchangeResult_whenMerge_shouldAddItems() {
        Item i1 = new Item("i1", "");
        Item i2 = new Item("i2", "");
        Item i3 = new Item("i3", "");
        Item i4 = new Item("i4", "");
        Item i5 = new Item("i5", "");
        ExchangeResult r1 = new ExchangeResult.Builder().rejectedItems(List.of(i1))
                .returnedItems(List.of(i3))
                .withheldItems(List.of(i5))
                .build();
        ExchangeResult r2 = new ExchangeResult.Builder().consumedItems(List.of(i2)).rewardedItems(List.of(i4)).build();

        r1.merge(r2);

        assertEquals(List.of(i1), r1.getRejectedItems());
        assertEquals(List.of(i2), r1.getConsumedItems());
        assertEquals(List.of(i3), r1.getReturnedItems());
        assertEquals(List.of(i4), r1.getRewardedItems());
        assertEquals(List.of(i5), r1.getWithheldItems());
    }
}