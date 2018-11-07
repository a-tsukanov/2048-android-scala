package com.example.hello.board


import scala.annotation.tailrec
import scala.collection.immutable.Vector
import scala.util.Random


class Board private (values: Vector[Vector[Cell]]) {
  private val cells = values
  val size: Int = cells.size

  def this(size: Int) =
    this(Board.empty(size))

  def updated(i: Int, j: Int)(newValue: Cell): Board =
    new Board(
      cells.updated(
        i, cells(i).updated(j, newValue)
      )
    )

  def updatedRandomCellWithOneOrTwo: Board = {
    @tailrec def generateNotTakenCoordinate: (Int, Int) = {
      val x = Board.rnd.nextInt(4)
      val y = Board.rnd.nextInt(4)
      cells(x)(y) match {
        case Cell(None) => (x, y)
        case Cell(Some(_)) => generateNotTakenCoordinate
      }
    }
    val (x, y) = generateNotTakenCoordinate
    val value = Board.rnd.nextInt(2) + 1
    updated(x, y)(Cell(Some(value)))
  }

  def rows: Vector[Vector[Cell]] =
    cells

  def cols: Vector[Vector[Cell]] =
    cells.transpose

  implicit def toLine(from: Vector[Cell]): Line = Line(from)
  implicit def toBoard(from: Vector[Vector[Cell]]): Board = new Board(from)

  type BoardTransformer = Vector[Vector[Cell]] => Vector[Vector[Cell]]
  private val emptyTransformer: BoardTransformer = b => b

  def anchorAllLeft: Vector[Vector[Cell]] =
    rows map (_.anchor)

  private def movedSomewhere(lines: Vector[Vector[Cell]],
                             actionAfter: Option[BoardTransformer]): Board = {
    val movedLeft = lines
      .anchorAllLeft
    val transformedBack = actionAfter
      .getOrElse(emptyTransformer)
      .apply(movedLeft)
    new Board(transformedBack)
  }

  def movedUp: Board = movedSomewhere(
    lines = cols,
    actionAfter = Some(_.transpose)
  )

  def movedDown: Board = movedSomewhere(
    lines = cols.map(_.reverse),
    actionAfter = Some(board => board.map(_.reverse).transpose)
  )

  def movedLeft: Board = movedSomewhere(
    lines = rows,
    actionAfter = None
  )

  def movedRight: Board = movedSomewhere(
    lines = rows.map(_.reverse),
    actionAfter = Some(board => board.map(_.reverse))
  )

  override def toString: String =
    rows
      .map(_.mkString("\t\t"))
      .mkString("\n\n")

  case class Line(line: Vector[Cell]) {

    def moved: Vector[Cell] =
      line match {
        case Vector() => Vector()
        case cell +: otherCells =>
          cell match {
            case Cell(Some(v)) => Cell(Some(v)) +: Line(otherCells).moved
            case Cell(None) => Line(otherCells).moved
          }
      }

    def merged: Vector[Cell] =
      line match {
        case Vector() => Vector()
        case (cell1 @ Cell(Some(_)))
          +: (cell2 @ Cell(Some(_)))
          +: other
          if cell1 == cell2 =>
            (cell1 * 2) +: Line(other).merged
        case cell +: other => cell +: Line(other).merged
      }

    def withEmpties: Vector[Cell] = {
      val numCellsToAdd = size - line.length
      line ++ Vector.fill(numCellsToAdd)(Cell(None))
    }

    def anchor: Vector[Cell] =
      line
        .moved
        .merged
        .withEmpties
  }

}

object Board {
  private val rnd = new Random()

  private def empty(size: Int): Vector[Vector[Cell]] =
    Vector.fill(size)(
      Vector.fill(size)(
        Cell(None)
      )
    )
}