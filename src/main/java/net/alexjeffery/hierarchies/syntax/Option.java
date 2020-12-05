package net.alexjeffery.hierarchies.syntax;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public class Option extends Syntax {

    @NotNull
    public final String name;
    @NotNull
    public final List<Field> fields;

    public Option(@NotNull String name, @NotNull List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }
}
