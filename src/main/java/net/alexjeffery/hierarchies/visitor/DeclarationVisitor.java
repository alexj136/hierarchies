package net.alexjeffery.hierarchies.visitor;

import net.alexjeffery.hierarchies.syntax.Declaration;
import org.antlr.v4.runtime.misc.NotNull;

public abstract class DeclarationVisitor<I, O, X extends Throwable> {

    public O visit(@NotNull Declaration.Fixed fixed, I input) throws X {
        return null;
    }

    public O visit(@NotNull Declaration.Options options, I input) throws X {
        return null;
    }
}
