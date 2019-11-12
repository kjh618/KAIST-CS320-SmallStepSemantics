package smallstepsemantics

import scala.sys.error
import scala.util.parsing.combinator._

object ExpressionParser extends RegexParsers {
  private def wrap[T](rule: Parser[T]): Parser[T] = "{" ~> rule <~ "}"

  private lazy val int: Parser[Int] = """-?\d+""".r ^^ (_.toInt)

  private lazy val str: Parser[String] = """[a-zA-Z][a-zA-Z0-9_-]*""".r

  private lazy val expr: Parser[Expression] =
    int                                       ^^ { case n => Num(n) }                        |
    wrap("+" ~> expr ~ expr)                  ^^ { case l ~ r => Add(l, r) }                 |
    wrap("-" ~> expr ~ expr)                  ^^ { case l ~ r => Sub(l, r) }                 |
    str                                       ^^ { case x => Id(x) }                         |
    wrap("fun" ~> wrap(str) ~ expr)           ^^ { case p ~ b => Fun(p, b) }                 |
    wrap("withcc" ~> str ~ expr)              ^^ { case x ~ b => Withcc(x, b) }              |
    wrap(expr ~ expr)                         ^^ { case f ~ a => App(f, a) }

  def parse(str: String): Expression = parseAll(expr, str).getOrElse(error(s"bad syntax: $str"))
}