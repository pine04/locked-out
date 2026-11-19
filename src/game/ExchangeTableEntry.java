package game;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExchangeTableEntry {
    private Map<String, ItemConsumption> inputItems;
    private Supplier<List<Item>> reward;
    private Consumer<Game> sideEffect;
    private int timesRewarded;
    private int maxTimesRewarded;

    public ExchangeTableEntry() {

    }

    public ExchangeResult tryExchange(List<Item> items) {
        return null;
    }
}
