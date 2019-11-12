package smallstepsemantics

trait Expression
case class Num(num: Int) extends Expression
case class Add(left: Expression, right: Expression) extends Expression
case class Sub(left: Expression, right: Expression) extends Expression
case class Id(name: String) extends Expression
case class Fun(param: String, body: Expression) extends Expression
case class App(fun: Expression, arg: Expression) extends Expression
case class Withcc(name: String, body: Expression) extends Expression