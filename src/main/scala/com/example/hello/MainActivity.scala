package com.example.hello

import android.app.Activity
import android.view.View.OnClickListener
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.graphics.drawable.Animatable
import android.util.Log
import android.view.View
import com.example.hello.board.Board

class MainActivity extends AppCompatActivity {
  // allows accessing `.value` on TR.resource.constants
  implicit val context = this

  private val board = new Board(4)

  private def renderBoard(): Unit = {

  }

  private def listener(func: View => Unit): OnClickListener = new OnClickListener {
    override def onClick(view: View): Unit = func(view)
  }

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    // type ascription is required due to SCL-10491
    val vh: TypedViewHolder.activity_main = TypedViewHolder.setContentView(this, TR.layout.activity_main)
    vh.btnUp.setOnClickListener(listener(view => Log.d("hi", "hi")))
  }
}