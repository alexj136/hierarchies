package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import net.alexjeffery.hierarchies.syntax.Option;
import net.alexjeffery.hierarchies.util.ThrowingConsumer;
import net.alexjeffery.hierarchies.visitor.DeclarationVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    @NotNull
    public List<String> genDeclaration(@NotNull Declaration declaration) throws IOException {
        return declaration.accept(GenDeclarationVisitor.INSTANCE, null);
    }

    private static class GenDeclarationVisitor extends DeclarationVisitor<Void, List<String>, IOException> {

        private GenDeclarationVisitor() { }

        public static final GenDeclarationVisitor INSTANCE = new GenDeclarationVisitor();

        @NotNull
        public List<String> visit(@NotNull Declaration.Fixed fixed, Void input) throws IOException {
            List<String> output= new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            ClassGenerator.generate(builder, fixed.fields, fixed.name);
            output.add(builder.toString());
            return output;
        }

        @NotNull
        public List<String> visit(@NotNull Declaration.Options options, Void input) throws IOException {
            List<String> output = new ArrayList<>();

            StringBuilder builder = new StringBuilder();
            generateAbstractClass(builder, options.name);
            output.add(builder.toString());

            builder = new StringBuilder();
            generateVisitorClass(builder, options);
            output.add(builder.toString());

            for (Option option : options.options) {
                builder = new StringBuilder();
                ClassGenerator.generate(builder, option.fields, option.name, options.name);
                output.add(builder.toString());
            }

            return output;
        }
    }

    @NotNull
    private static void generateAbstractClass(
            @NotNull Appendable buffer,
            @NotNull String superName
    ) throws IOException {
        buffer.append("public abstract class ")
                .append(superName)
                .append(" {\n    public <I, O, X extends Throwable> O accept(@NotNull ")
                .append(superName)
                .append("Visitor<I, O, X> visitor, I input) throws X {\n        return null;\n    }\n}")
                .toString();
    }

    @NotNull
    private static void generateVisitorClass(
            @NotNull Appendable buffer,
            @NotNull Declaration.Options options
    ) throws IOException{
        buffer.append("public abstract class ")
                .append(options.name)
                .append("Visitor<I, O, X extends Throwable> {\n    ");
        ThrowingConsumer.consumeListWithIntermediateAction(
                option -> appendVisitMethod(buffer, option.name),
                options.options,
                __ -> buffer.append("\n\n    "));
        buffer.append("\n}").toString();
    }

    private static void appendVisitMethod(
            @NotNull Appendable buffer,
            @NotNull String subclassName
    ) throws IOException {
        buffer.append("public O visit(@NotNull ")
                .append(subclassName)
                .append(" ");
        appendSubclassNameUncapitalised(buffer, subclassName);
        buffer.append(", I input) throws X {\n        return null;\n    }");
    }

    private static void appendSubclassNameUncapitalised(
            @NotNull Appendable buffer,
            @NotNull String subclassName
    ) throws IOException {
        buffer.append(Character.toLowerCase(subclassName.charAt(0)));
        buffer.append(subclassName.substring(1));
    }
}
