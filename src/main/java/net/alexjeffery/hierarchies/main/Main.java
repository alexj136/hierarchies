package net.alexjeffery.hierarchies.main;

import net.alexjeffery.hierarchies.output.SystemOutCodeGenerator;
import net.alexjeffery.hierarchies.parser.HierarchiesLexer;
import net.alexjeffery.hierarchies.parser.HierarchiesParser;
import net.alexjeffery.hierarchies.syntax.Declaration;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String inputFileName = "examples/lambda.hcs";
        HierarchiesLexer lexer = new HierarchiesLexer(new ANTLRFileStream(inputFileName));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HierarchiesParser parser = new HierarchiesParser(tokens);
        List<Declaration> declarations = parser.declarations().out;
        SystemOutCodeGenerator.printDeclarations(declarations);
    }
}
