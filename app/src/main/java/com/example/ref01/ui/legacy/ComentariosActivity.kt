package com.example.ref01.ui.legacy

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.ref01.R

/**
 * Host para el Fragment (demuestra integración Compose -> Activity -> Fragment)
 */
class ComentariosActivity : FragmentActivity(R.layout.activity_comentarios) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bookId = intent?.getIntExtra(EXTRA_BOOK_ID, 0) ?: 0

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ComentariosFragment.newInstance(bookId))
                .commit()
        }
    }
    companion object { const val EXTRA_BOOK_ID = "extra_book_id" }
}
