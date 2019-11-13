package smallstepsemantics

trait Expression

case class Num(num: Int) extends Expression {
  override def toString: String = num.toString
}

case class Add(left: Expression, right: Expression) extends Expression {
  override def toString: String = s"{+ $left $right}"
}

case class Sub(left: Expression, right: Expression) extends Expression {
  override def toString: String = s"{- $left $right}"
}

case class Id(name: String) extends Expression {
  override def toString: String = name
}

case class Fun(param: String, body: Expression) extends Expression {
  override def toString: String = s"{fun {$param} $body}"
}

case class App(fun: Expression, arg: Expression) extends Expression {
  override def toString: String = s"{$fun $arg}"
}

case class Withcc(name: String, body: Expression) extends Expression {
  override def toString: String = s"{withcc $name $body}"
}