package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import net.alexjeffery.hierarchies.syntax.Option;
import net.alexjeffery.hierarchies.util.Utils;
import net.alexjeffery.hierarchies.visitor.DeclarationVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    @NotNull
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
            List<String> output = new ArrayList<>();
            output.add(generateAbstractClass(options.name));
            output.add(generateVisitorClass(options));
            for (Option option : options.options) {
                output.add(ClassGenerator.generate(option.fields, option.name, options.name));
            }
            return output;
        }
    }

    @NotNull
    private static String generateAbstractClass(@NotNull String superName) {
        return new StringBuilder().append("public abstract class ")
                .append(superName)
                .append(" {\n    public <I, O, X extends Throwable> O accept(@NotNull ")
                .append(superName)
                .append("Visitor<I, O, X> visitor, I input) throws X {\n        return null;\n    }\n}")
                .toString();
    }

    @NotNull
    private static String generateVisitorClass(@NotNull Declaration.Options options) {
        StringBuilder builder = new StringBuilder();
        builder.append("public abstract class ")
                .append(options.name)
                .append("Visitor<I, O, X extends Throwable> {\n    ");
        Utils.consumeListWithIntermediateAction(
                option -> appendVisitMethod(builder, option.name),
                options.options,
                __ -> builder.append("\n\n    "));
        return builder.append("\n}").toString();
    }

    private static void appendVisitMethod(@NotNull StringBuilder builder, @NotNull String subclassName) {
        builder.append("public O visit(@NotNull ")
                .append(subclassName)
                .append(" ");
        appendSubclassNameUncapitalised(builder, subclassName);
        builder.append(", I input) throws X {\n        return null;\n    }");
    }

    private static void appendSubclassNameUncapitalised(@NotNull StringBuilder builder, @NotNull String subclassName) {
        builder.append(Character.toLowerCase(subclassName.charAt(0)));
        builder.append(subclassName.substring(1));
    }
}
