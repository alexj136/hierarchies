package net.alexjeffery.hierarchies.util;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class Utils {

    public static <T> void consumeListWithIntermediateAction(
            @NotNull Consumer<T> elementAction,
            @NotNull List<T> elements,
            @NotNull Consumer<Void> intermediateAction) {
        if (elements.isEmpty())
            return;
        elementAction.accept(elements.get(0));
        for (int i = 1; i < elements.size(); i++) {
            intermediateAction.accept(null);
            elementAction.accept(elements.get(i));
        }
    }
}
