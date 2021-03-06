package com.example.hello

import android.R
import android.app.Activity
import android.content.Intent
import android.view.View.OnClickListener
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.graphics.drawable.Animatable
import android.util.Log
import android.view.View
import android.view.KeyEvent
import android.widget.Toast
import com.example.hello.board.{Board, Cell}

class MainActivity extends AppCompatActivity {
  // allows accessing `.value` on TR.resource.constants
  implicit val context = this

  private var board = new Board(4)
    .updatedRandomCellWithOneOrTwo

  private lazy val vh: TypedViewHolder.activity_main = TypedViewHolder.setContentView(this, TR.layout.activity_main)

  private def updateCell(id: String, newValue: Cell): Unit = {
    val cellToUpdate = id match {
      case "00" => vh.cell00
      case "01" => vh.cell01
      case "02" => vh.cell02
      case "03" => vh.cell03

      case "10" => vh.cell10
      case "11" => vh.cell11
      case "12" => vh.cell12
      case "13" => vh.cell13

      case "20" => vh.cell20
      case "21" => vh.cell21
      case "22" => vh.cell22
      case "23" => vh.cell23

      case "30" => vh.cell30
      case "31" => vh.cell31
      case "32" => vh.cell32
      case "33" => vh.cell33
    }
    cellToUpdate.setText(newValue match {
      case Cell(Some(v)) => v.toString
      case Cell(None) => ""
    })
  }

  private def filled: Boolean =
    board.rows.forall(
      row => row.forall(
        cell => cell match {
        case Cell(Some(_)) => true
        case Cell(None) => false
      })
    )

  private def renderBoard(): Unit = {
    for {
      (row, i) <- board.rows.zipWithIndex
      (cell, j) <- row.zipWithIndex
    } updateCell(i.toString + j.toString, cell)
  }

  private def generateCellAndRenderBoardAndCheckFilled() = {
    board = board.updatedRandomCellWithOneOrTwo

    renderBoard()

    if (filled) {
      List(vh.btnUp, vh.btnDown, vh.btnLeft, vh.btnRight)
        .map(_.setEnabled(false))


      val intent = new Intent(MainActivity.this, classOf[RestartActivity])
      MainActivity.this.startActivity(intent)

    }
  }

  private def listener(func: Unit => Unit): OnClickListener = new OnClickListener {
    override def onClick(view: View): Unit = {
      func()
      board = board.updatedRandomCellWithOneOrTwo
      renderBoard()

      if (filled) {
        List(vh.btnUp, vh.btnDown, vh.btnLeft, vh.btnRight)
          .map(_.setEnabled(false))


        val intent = new Intent(MainActivity.this, classOf[RestartActivity])
        MainActivity.this.startActivity(intent)

      }
    }
  }

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    renderBoard()

    vh.btnUp.setOnClickListener(
      listener(_ => board = board.movedUp)
    )
    vh.btnDown.setOnClickListener(
      listener(_ => board = board.movedDown)
    )
    vh.btnRight.setOnClickListener(
      listener(_ => board = board.movedRight)
    )
    vh.btnLeft.setOnClickListener(
      listener(_ => board = board.movedLeft)
    )

    vh.game_board.setOnTouchListener(new SwipeDetector(context) {
      override def onSwipeUp() = {
        super.onSwipeUp()
        board = board.movedUp
        generateCellAndRenderBoardAndCheckFilled()
      }
      override def onSwipeDown() = {
        super.onSwipeDown()
        board = board.movedDown
        generateCellAndRenderBoardAndCheckFilled()
      }
      override def onSwipeLeft() = {
        super.onSwipeUp()
        board = board.movedLeft
        generateCellAndRenderBoardAndCheckFilled()
      }
      override def onSwipeRight() = {
        super.onSwipeUp()
        board = board.movedRight
        generateCellAndRenderBoardAndCheckFilled()
      }
    })

  }



}