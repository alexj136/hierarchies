package net.alexjeffery.hierarchies.syntax;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public abstract class Declaration extends Syntax {

    @NotNull
    public final String name;

    public Declaration(@NotNull String name) {
        this.name = name;
    }

    public static class Options extends Declaration {

        @NotNull
        public final List<Option> options;

        public Options(@NotNull String name, @NotNull List<Option> options) {
            super(name);
            this.options = options;
        }
    }

    public static class Fixed extends Declaration {

        @NotNull
        public final List<Field> fields;

        public Fixed(@NotNull String name, @NotNull List<Field> fields) {
            super(name);
            this.fields = fields;
        }
    }
}
