package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.IOException;
import java.util.List;

public class SystemOutCodeGenerator {

    public static void printDeclarations(@NotNull List<Declaration> declarations) throws IOException {
        for (Declaration declaration : declarations)
            CodeGenerator.genDeclaration(() -> System.out, declaration);
    }
}
