package net.alexjeffery.hierarchies.output;

import net.alexjeffery.hierarchies.syntax.Declaration;
import net.alexjeffery.hierarchies.util.Pair;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringCodeGenerator {

    public static List<Pair<String, String>> stringifyDeclarations(
            @NotNull List<Declaration> declarations
    ) throws IOException {
        List<Pair<String, String>> outputs = new ArrayList<>();
        for (Declaration declaration : declarations) {
            List<Pair<String, Appendable>> fromDeclaration =
                CodeGenerator.genDeclaration(StringBuilder::new, declaration);
            for (Pair<String, Appendable> fileNameAndFileContents : fromDeclaration) {
                String fileName = fileNameAndFileContents._1;
                Appendable fileContents = fileNameAndFileContents._2;
                outputs.add(Pair.of(fileName, ((StringBuilder) fileContents).toString()));
            }
        }
        return outputs;
    }
}
