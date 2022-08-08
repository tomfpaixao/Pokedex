import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.ActivityMainBinding.inflate
import com.tomasfp.pokedex.databinding.StatsBarLayoutBinding

class StatBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = StatsBarLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    fun setBarValues(statName: String, statValue: Int) {
        binding.apply {
            label.text = statName.take(3).uppercase()
            bar.progress = statValue
            value.text = statValue.toString() }
    }

}