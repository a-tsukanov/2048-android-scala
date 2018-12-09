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
import com.example.hello.MainActivity

class RestartActivity extends AppCompatActivity {
  // allows accessing `.value` on TR.resource.constants
  implicit val context = this

  private lazy val vh: TypedViewHolder.restart = TypedViewHolder.setContentView(this, TR.layout.restart)

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    vh.restart.setOnClickListener(
      new OnClickListener {
        override def onClick(view: View): Unit = {
          val intent = new Intent(RestartActivity.this, classOf[MainActivity])
          RestartActivity.this.startActivity(intent)
        }
      }
    )
  }

}

