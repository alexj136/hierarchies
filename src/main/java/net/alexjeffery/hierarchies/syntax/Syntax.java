package net.alexjeffery.hierarchies.syntax;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public abstract class Syntax {

    public abstract class Declaration extends Syntax {

        @NotNull
        public final String name;

        public Declaration(@NotNull String name) {
            this.name = name;
        }
    }

    public class OptionDeclaration extends Declaration {

        @NotNull
        public final List<Option> options;

        public OptionDeclaration(@NotNull String name, @NotNull List<Option> options) {
            super(name);
            this.options = options;
        }
    }

    public class FixedDeclaration extends Declaration {

        @NotNull
        public final List<Field> fields;

        public FixedDeclaration(@NotNull String name, @NotNull List<Field> fields) {
            super(name);
            this.fields = fields;
        }
    }

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

    public class Field extends Syntax {

        @NotNull
        public final String name;
        @NotNull
        public final String type;
        public final boolean isList;
        public final boolean isOptional;

        public Field(@NotNull String name, @NotNull String type, boolean isList, boolean isOptional) {
            this.name = name;
            this.type = type;
            this.isList = isList;
            this.isOptional = isOptional;
        }

        public Field(@NotNull String name, @NotNull String type, boolean isList) {
            this(Character.toLowerCase(type.charAt(0)) + type.substring(1), type, isList, false);
        }

        public Field(@NotNull String name, @NotNull String type) {
            this(Character.toLowerCase(type.charAt(0)) + type.substring(1), type, false, false);
        }

        public Field(@NotNull String type, boolean isList, boolean isOptional) {
            this(Character.toLowerCase(type.charAt(0)) + type.substring(1), type, isList, isOptional);
        }

        public Field(@NotNull String type, boolean isList) {
            this(Character.toLowerCase(type.charAt(0)) + type.substring(1), type, isList, false);
        }

        public Field(@NotNull String type) {
            this(Character.toLowerCase(type.charAt(0)) + type.substring(1), type, false, false);
        }
    }
}
