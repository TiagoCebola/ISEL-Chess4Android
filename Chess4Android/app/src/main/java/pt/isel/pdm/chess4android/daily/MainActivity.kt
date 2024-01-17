package pt.isel.pdm.chess4android.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import pt.isel.pdm.chess4android.challenges.create.CreateChallengeActivity
import pt.isel.pdm.chess4android.challenges.list.ChallengesListActivity
import pt.isel.pdm.chess4android.common.LoggingActivity
import pt.isel.pdm.chess4android.credits.CreditActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.history.HistoryActivity
import pt.isel.pdm.chess4android.localGame.LocalGameActivity
import pt.isel.pdm.chess4android.resolution.ResolutionActivity

class MainActivity : LoggingActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.playButton.setOnClickListener{
            Log.v("APP_TAG", "PLAY GAME")
            startActivity(Intent(this, ResolutionActivity::class.java))
        }

        binding.pvpButton?.setOnClickListener{
            Log.v("APP_TAG", "LOCAL")
            startActivity(Intent(this, LocalGameActivity::class.java))
        }

        binding.historyButton?.setOnClickListener{
            Log.v("APP_TAG", "HISTORY")
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.creditsButton.setOnClickListener{
            Log.v("APP_TAG", "CREDITS")
            startActivity(Intent(this, CreditActivity::class.java))
        }

    }
}