package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import net.alexjeffery.hierarchies.syntax.Field;
import net.alexjeffery.hierarchies.syntax.Option;
import net.alexjeffery.hierarchies.util.ThrowingConsumer;
import net.alexjeffery.hierarchies.visitor.DeclarationVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

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
            ConcreteClassGenerator.generate(builder, fixed.fields, fixed.name);
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
                ConcreteClassGenerator.generate(builder, option.fields, option.name, options.name);
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

    private static class ConcreteClassGenerator {

        public static void generate(
                @NotNull Appendable buffer,
                @NotNull List<Field> fields,
                @NotNull String name
        ) throws IOException {
            ConcreteClassGenerator.generate(buffer, fields, name, null);
        }

        public static void generate(
                @NotNull Appendable buffer,
                @NotNull List<Field> fields,
                @NotNull String name,
                @Nullable String superName
        ) throws IOException {
            ConcreteClassGenerator generator = new ConcreteClassGenerator(buffer);
            generator.appendClass(fields, name, superName);
        }

        @NotNull
        private Appendable buffer;

        private ConcreteClassGenerator(@NotNull Appendable buffer) {
            this.buffer = buffer;
        }

        @NotNull
        private void appendClass(
                @NotNull List<Field> fields,
                @NotNull String name,
                @Nullable String superName
        ) throws IOException {
            buffer.append("/* Generated by Hierarchies. Edits will be lost. */\n");
            buffer.append("public class ").append(name);
            if (superName != null)
                buffer.append(" extends ").append(superName);
            buffer.append(" {\n    ");
            appendFieldsFormatted(this::appendFieldDeclaration, fields, "");
            buffer.append("public ").append(name).append("(");
            appendFieldsFormatted(this::appendConstructorParameter, fields, ", ");
            buffer.append(") {\n        ");
            appendFieldsFormatted(this::appendConstructorAssignment, fields, "\n        ");
            buffer.append("\n    }");
            appendFieldsFormatted(this::appendGetter, fields, "\n");
            appendFieldsFormatted(this::appendSetter, fields, "\n");
            if (superName != null)
                appendAcceptMethod(superName);
            buffer.append("\n}");
        }

        private void appendFieldDeclaration(@NotNull Field field) throws IOException {
            appendIsOptional(field);
            buffer.append("\n    private ");
            appendType(field);
            buffer.append(" ").append(field.name);
            buffer.append(";\n    ");
        }

        private void appendConstructorParameter(@NotNull Field field) throws IOException {
            appendIsOptional(field);
            buffer.append(" ");
            appendType(field);
            buffer.append(" ").append(field.name);
        }

        private void appendConstructorAssignment(@NotNull Field field) throws IOException {
            String name = field.name;
            buffer.append("this.");
            buffer.append(name);
            buffer.append(" = ");
            buffer.append(name);
            buffer.append(";");
        }

        private void appendGetter(@NotNull Field field) throws IOException {
            appendIsOptional(field);
            buffer.append("\n    public ");
            appendType(field);
            buffer.append(" get");
            appendFieldNameCapitalised(field);
            buffer.append("() {\n        return this.");
            buffer.append(field.name);
            buffer.append(";\n    }");
        }

        private void appendSetter(@NotNull Field field) throws IOException {
            buffer.append("\n    public void set");
            appendFieldNameCapitalised(field);
            buffer.append("(");
            appendIsOptional(field);
            buffer.append(" ");
            appendType(field);
            buffer.append(" ");
            buffer.append(field.name);
            buffer.append(") {\n        this.");
            buffer.append(field.name);
            buffer.append(" = ");
            buffer.append(field.name);
            buffer.append(";\n    }");
        }


        private void appendAcceptMethod(@NotNull String superName) throws IOException {
            buffer.append("    @Override\n")
                    .append("    public <I, O, X extends Throwable> O accept(@NotNull ")
                    .append(superName)
                    .append("Visitor<I, O, X> visitor, I input) throws X {\n")
                    .append("        return visitor.visit(this, input);\n")
                    .append("    }");
        }

        private void appendFieldsFormatted(
                @NotNull ThrowingConsumer<Field, IOException> action,
                @NotNull List<Field> fields,
                @NotNull String intersperseText)
                throws IOException {
            ThrowingConsumer.consumeListWithIntermediateAction(action, fields, __ -> {
                buffer.append(intersperseText);
            });
        }

        private void appendIsOptional(@NotNull Field field) throws IOException {
            buffer.append(field.isOptional ? "@Nullable" : "@NotNull");
        }

        private void appendType(@NotNull Field field) throws IOException {
            boolean isList = field.isList;
            buffer.append(isList ? "List<" : "");
            buffer.append(field.type);
            buffer.append(isList ? ">" : "");
        }

        private void appendFieldNameCapitalised(@NotNull Field field) throws IOException {
            buffer.append(Character.toUpperCase(field.name.charAt(0)));
            buffer.append(field.name.substring(1));
        }
    }
}
