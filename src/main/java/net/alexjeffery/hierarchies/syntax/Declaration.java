package net.alexjeffery.hierarchies.syntax;

import net.alexjeffery.hierarchies.visitor.DeclarationVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public abstract class Declaration extends Syntax {

    @NotNull
    public final String name;

    public Declaration(@NotNull String name) {
        this.name = name;
    }

    public abstract <I, O, X extends Throwable> O accept(@NotNull DeclarationVisitor<I, O, X> visitor, I input) throws X;

    public static class Options extends Declaration {

        @Override
        public <I, O, X extends Throwable> O accept(@NotNull DeclarationVisitor<I, O, X> visitor, I input) throws X {
            return visitor.visit(this, input);
        }

        @NotNull
        public final List<Option> options;

        public Options(@NotNull String name, @NotNull List<Option> options) {
            super(name);
            this.options = options;
        }
    }

    public static class Fixed extends Declaration {

        @Override
        public <I, O, X extends Throwable> O accept(@NotNull DeclarationVisitor<I, O, X> visitor, I input) throws X {
            return visitor.visit(this, input);
        }

        @NotNull
        public final List<Field> fields;

        public Fixed(@NotNull String name, @NotNull List<Field> fields) {
            super(name);
            this.fields = fields;
        }
    }
}
