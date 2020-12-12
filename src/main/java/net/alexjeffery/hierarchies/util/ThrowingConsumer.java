package net.alexjeffery.hierarchies.util;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@FunctionalInterface
public interface ThrowingConsumer<T, X extends Throwable> {

    public void accept(T t) throws X;

    public static <T, X extends Throwable> void consumeListWithIntermediateAction(
            @NotNull ThrowingConsumer<T, X> elementAction,
            @NotNull List<T> elements,
            @NotNull ThrowingConsumer<Void, X> intermediateAction
    ) throws X {
        if (elements.isEmpty())
            return;
        elementAction.accept(elements.get(0));
        for (int i = 1; i < elements.size(); i++) {
            intermediateAction.accept(null);
            elementAction.accept(elements.get(i));
        }
    }
}
