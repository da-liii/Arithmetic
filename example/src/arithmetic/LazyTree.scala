package arithmetic

import arithmetic.antlr.{ArithmeticLexer, ArithmeticParser}
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime._
import arithmetic.ParseTreeOps._

case class LazyTree(text: String) {
  val lexer       = new ArithmeticLexer(CharStreams.fromString(text))
  val tokenStream = new CommonTokenStream(lexer)
  val parser = new ArithmeticParser(tokenStream)

  def expression(): AbstractParseTreeOps = {
    parser.expression()
  }

  private def extractText(parser: ArithmeticParser, tree: ParseTree): String = {
    parser.getTokenStream.getText(tree.getSourceInterval)
  }


  def extractViaTree(tree: ParseTree): String = {
    extractText(parser, tree)
  }

  def extractViaTree(locate: LazyTree => Option[ParseTree]): Option[String] = {
    val treeOption = locate(this)
    treeOption.map(tree => extractText(parser, tree))
  }

  def rewriteTree(source: ParseTree, target: String): String = {
    val rewriter = new TokenStreamRewriter(tokenStream)
    val startPos = source.getSourceInterval.a
    val endPos = source.getSourceInterval.b
    rewriter.delete(startPos+1, endPos)
    rewriter.replace(startPos, target)
    rewriter.getText
  }
}
