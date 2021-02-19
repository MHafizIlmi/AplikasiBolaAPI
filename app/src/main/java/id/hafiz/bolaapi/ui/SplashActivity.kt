/*
 * hafiz on 10/30/19
 * https://hafiz.id
 */
package id.hafiz.bolaapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity<MainActivity>()
    finish()
  }
}