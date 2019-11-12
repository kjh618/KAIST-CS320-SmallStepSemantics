package smallstepsemantics

object Main {
  def main(args: Array[String]): Unit = {
    val program =
      """{withcc done
        |  { {withcc esc
        |      {done {+ 1 {withcc k {esc k}}}}
        |    }
        |    3
        |  }
        |}
        |""".stripMargin
    println(Interpreter.run(program))
  }
}