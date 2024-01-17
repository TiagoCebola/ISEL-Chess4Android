package pt.isel.pdm.chess4android.credits

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import pt.isel.pdm.chess4android.common.LoggingActivity
import pt.isel.pdm.chess4android.R

private const val GitHubURL = "https://github.com/gustavodev1998/PDM-2122i-LI5X-G30"
private const val LichessURL = "https://lichess.org/"

private const val GitHubGustavoURL = "https://github.com/gustavodev1998"
private const val LinkedInGustavoURL = "https://www.linkedin.com/in/gustavo-campos-8918711b4/"

private const val GitHubTiagoURL = "https://github.com/TiagoCebola"
private const val LinkedInTiagoURL = "https://www.linkedin.com/in/tiago-cebola-85610922a/"

private const val GitHubRubenURL = "https://github.com/RubenSou"
private const val LinkedInRubenURL = "https://www.linkedin.com/in/ruben-sousa-103729223"

class CreditActivity : LoggingActivity() {

    init {
        Log.v("APP_TAG", "CREDITS")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit)

        findViewById<ImageView>(R.id.lichess_logo).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LichessURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        /* GITHUB & LINKED ICONS REDIRECT TO OUR OWN SOCIAL MEDIA */
        /* Social Gustavo */
        findViewById<ImageView>(R.id.github_icon_Gustavo).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GitHubGustavoURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.linkedin_icon_Gustavo).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LinkedInGustavoURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        /* Social Tiago */
        findViewById<ImageView>(R.id.github_icon_Tiago).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GitHubTiagoURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.linkedin_icon_Tiago).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LinkedInTiagoURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        /* Social Ruben */
        findViewById<ImageView>(R.id.github_icon_Ruben).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GitHubRubenURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.linkedin_icon_Ruben).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LinkedInRubenURL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}