package smallstepsemantics

import scala.sys.error

object Interpreter {
  private def applyOperator(operator: (Int, Int) => Int, leftVal: Value, rightVal: Value): Value =
    (leftVal, rightVal) match {
      case (NumVal(leftNum), NumVal(rightNum)) => NumVal(operator(leftNum, rightNum))
      case _ => error(s"not both numbers: $leftVal, $rightVal")
    }

  private def interpret(expr: Expression, env: Environment, k: Value => Value): Value = expr match {
    case Num(num) => k(NumVal(num))

    case Add(left, right) =>
      interpret(left, env, leftVal =>
        interpret(right, env, rightVal =>
          k(applyOperator(_ + _, leftVal, rightVal))))

    case Sub(left, right) =>
      interpret(left, env, leftVal =>
        interpret(right, env, rightVal =>
          k(applyOperator(_ - _, leftVal, rightVal))))

    case Id(name) => k(env.e.getOrElse(name, error(s"free identifier: $name")))

    case Fun(param, body) => k(CloVal(param, body, env))

    case App(fun, arg) =>
      interpret(fun, env, {
        case CloVal(param, body, cloEnv) =>
          interpret(arg, env, argVal =>
            interpret(body, Environment(cloEnv.e + (param -> argVal)), k))
        case ContVal(newK) =>
          interpret(arg, env, newK)
        case v => error(s"not a closure or continuation: $v")
      })

    case Withcc(name, body) =>
      interpret(body, Environment(env.e + (name -> ContVal(k))), k)
  }

  def run(program: String): String =
    interpret(ExpressionParser.parse(program), Environment(Map()), v => v).toString
}