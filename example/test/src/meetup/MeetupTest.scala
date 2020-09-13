package meetup

import arithmetic.LazyTree
import org.antlr.v4.runtime.tree.{ParseTree, TerminalNode}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class MeetupTest extends AnyFunSuite with Matchers {
  def beautifyName(node: ParseTree): String = {
    node.getClass.getSimpleName
      .replace("Context", "")
      .replace("Impl", "")
  }

  def show(text: String)(f: String => String): Unit = {
    println("<-- Original Text")
    println(text)
    println("--> Original Text\n")

    println("<-- Evaluated")
    println(f(text))
    println("--> Evaluated\n\n")
  }


  ignore("Demo 1: bfs traverse of `1 + a + 2`") {
    val lazyTree = LazyTree("1 + a + 2")
    lazyTree.expression().foreach { node =>
      println(s"${beautifyName(node)} [${lazyTree.extractViaTree(node)}]")
    }
  }

  ignore("Demo 2: validate") {
    Meetup.validate("1 + 1") should be (true)
    Meetup.validate("1 - a") should be (false)
  }

  ignore("Demo 3: parenthesize") {
    show("1 + a + 2")(Meetup.parenthesize)
    show("(1 + a + 2)")(Meetup.parenthesize)
    show("((1 + a) + 2)")(Meetup.parenthesize)
  }

  ignore("Demo 4: evaluate") {
    show("(a + 1) + 2")(Meetup.evaluate)
    show("a + (1 + 2)")(Meetup.evaluate)
    show("(1+2)+(3+4)+(a + b)")(Meetup.evaluate)
  }

  ignore("Demo 5: commutate") {
    show("((1 + a) + 2)")(Meetup.commutate)
    show("(1 + (a + 2))")(Meetup.commutate)
    show("((a + 1) + 2)")(Meetup.commutate)
    show("(1 + (2 + a))")(Meetup.commutate)
  }

  ignore("Demo 6: associate") {
    show("(((3+a)+b)+c)")(Meetup.associate)
    show("((a + 1) + 2)")(Meetup.associate)
    show("(2 + (1 + a))")(Meetup.associate)
  }

  test("Demo 7: calculator") {
    Meetup.calculator("(1+2) + (3+4) + 5")
    Meetup.calculator("1 + a + 2")
    Meetup.calculator("1 + a + 2 + b + 3 + c + 4")
  }
}