package net.alexjeffery.hierarchies.syntax;

import org.antlr.v4.runtime.misc.NotNull;

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
        this(name, type, isList, false);
    }

    public Field(@NotNull String name, @NotNull String type) {
        this(name, type, false, false);
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
