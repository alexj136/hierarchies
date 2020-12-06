package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import net.alexjeffery.hierarchies.syntax.Option;
import net.alexjeffery.hierarchies.visitor.DeclarationVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    public List<String> genDeclaration(@NotNull Declaration declaration) {
        return declaration.accept(GenDeclarationVisitor.INSTANCE, null);
    }

    private static class GenDeclarationVisitor extends DeclarationVisitor<Void, List<String>, RuntimeException> {

        private GenDeclarationVisitor() { }

        public static final GenDeclarationVisitor INSTANCE = new GenDeclarationVisitor();

        @NotNull
        public List<String> visit(@NotNull Declaration.Fixed fixed, Void input) throws RuntimeException {
            List<String> output= new ArrayList<>();
            output.add(ClassGenerator.generate(fixed.fields, fixed.name));
            return output;
        }

        @NotNull
        public List<String> visit(@NotNull Declaration.Options options, Void input) throws RuntimeException {
            List<String> output= new ArrayList<>();
            // TODO: output superclass
            for (Option option : options.options) {
                // TODO: make append superclass to header
                // TODO: make append visitor method
                output.add(ClassGenerator.generate(option.fields, option.name));
            }
            return output;
        }
    }
}
