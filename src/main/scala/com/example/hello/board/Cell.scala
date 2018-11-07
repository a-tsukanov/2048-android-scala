package com.example.hello.board

case class Cell(value: Option[Int]) {
  override def toString: String =
    value match {
      case Some(x) => x.toString
      case None => "*"
    }

  def * (n: Int): Cell =
    value match {
      case Some(x) => Cell(Some(n * x))
      case None => throw new IllegalArgumentException
    }
}