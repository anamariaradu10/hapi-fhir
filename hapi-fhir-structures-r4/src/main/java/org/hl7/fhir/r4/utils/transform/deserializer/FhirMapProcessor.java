//
// Translated by Java2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;

import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.DebugParseListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.ThrowErrorHandler;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.ThrowExceptionErrorListener;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaLexer;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;

import java.nio.CharBuffer;

/**
* Process Adl Language data.
*/
public class FhirMapProcessor {
  /**
  * If true, output debug info during parsing.
  */
  private boolean __DebugFlag;
  private boolean getDebugFlag() {
    return __DebugFlag;
  }

  private void setDebugFlag(boolean value) {
    __DebugFlag = value;
  }

  /**
  * Constructor.
  *
  */
  public FhirMapProcessor() throws Exception {
    setDebugFlag(false);
  }

  /**
  * Method to load string grammar.
  *
  *  @param text Adl text
  *  @return ANTLR parser
  */
  public FhirMapJavaParser loadGrammar(String text) throws Exception {
    CharBuffer buffer = CharBuffer.allocate(text.length());
    buffer.append(text);
    buffer.position(0);
    FhirMapJavaLexer lexer = new FhirMapJavaLexer(CodePointCharStream.fromBuffer(CodePointBuffer.withChars(buffer)));
    lexer.addErrorListener(new ThrowExceptionErrorListener(text));
    CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
    FhirMapJavaParser grammar = new FhirMapJavaParser(commonTokenStream);
    if (this.getDebugFlag()) {
      DebugParseListener parseListener = new DebugParseListener(grammar, System.err::println);
      grammar.addParseListener(parseListener);
    }
    grammar.removeErrorListeners();
    grammar.setErrorHandler(new ThrowErrorHandler());
    grammar.addErrorListener(new ThrowExceptionErrorListener(text));
    return grammar;
  }

  /**
  * Parse Adl Definition text.
  *
  *  @param FhirMapText Adl Archetype Text to process
  */
  public void parseFhirMap(String FhirMapText, IFhirMapExecutor executor) throws Exception {
    FhirMapJavaParser grammar = this.loadGrammar(FhirMapText);
    ParseTree parseTree = grammar.mappingUnit();
    FhirMapVisitor visitor = new FhirMapVisitor(executor);
    visitor.visit(parseTree);
  }

}