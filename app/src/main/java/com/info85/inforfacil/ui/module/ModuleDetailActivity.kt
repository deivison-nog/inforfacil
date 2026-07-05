package com.info85.inforfacil.ui.module

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.info85.inforfacil.content.ActivityType
import com.info85.inforfacil.content.LearningContentProvider
import com.info85.inforfacil.content.ModuleContent
import com.info85.inforfacil.content.PracticeActivityItem
import com.info85.inforfacil.data.local.ModuloProgress
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivityModuleDetailBinding
import com.info85.inforfacil.ui.base.BaseActivity
import com.info85.inforfacil.utils.FeedbackManager
import com.info85.inforfacil.utils.applyFeedbackBounce
import com.info85.inforfacil.utils.applyFeedbackShake
import com.info85.inforfacil.utils.showToast
import kotlinx.coroutines.launch

class ModuleDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityModuleDetailBinding
    private lateinit var moduleContent: ModuleContent
    private lateinit var repository: ProgressRepository
    private lateinit var feedbackManager: FeedbackManager

    private var selectedOption: String? = null
    private var currentIndex = 0
    private var correctAnswers = 0
    private var currentChecked = false
    private val dragUserMapping = mutableMapOf<String, String>()

    private val viewModel: ModuleDetailViewModel by viewModels {
        ModuleDetailViewModel.Factory(
            application,
            intent.getStringExtra(EXTRA_MODULE_ID).orEmpty()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = ProgressRepository(ProgressDataStore(applicationContext))
        feedbackManager = FeedbackManager(this)
        val moduleId = intent.getStringExtra(EXTRA_MODULE_ID).orEmpty()
        moduleContent = LearningContentProvider.getModuleContent(moduleId)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = moduleContent.title

        setupTabs()
        setupLearnTab()
        setupPracticeTab()
        setupProgressTab()
        observeProgress()
    }

    private fun setupTabs() {
        binding.tabsModule.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showSection(Section.LEARN)
                    1 -> showSection(Section.PRACTICE)
                    2 -> showSection(Section.PROGRESS)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
        showSection(Section.LEARN)
    }

    private fun setupLearnTab() {
        val adapter = TopicPagerAdapter(moduleContent.topics)
        binding.viewPagerLearn.adapter = adapter
        binding.tvLearnCounter.text = "1/${moduleContent.topics.size}"
        binding.viewPagerLearn.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tvLearnCounter.text = "${position + 1}/${moduleContent.topics.size}"
            }
        })
    }

    private fun setupPracticeTab() {
        binding.btnCheckPractice.setOnClickListener {
            if (currentChecked) {
                goToNextActivity()
            } else {
                checkCurrentAnswer()
            }
        }
        renderPracticeActivity()
    }

    private fun setupProgressTab() {
        binding.btnRetryModule.setOnClickListener {
            currentIndex = 0
            correctAnswers = 0
            currentChecked = false
            selectedOption = null
            dragUserMapping.clear()
            renderPracticeActivity()
            binding.tabsModule.getTabAt(1)?.select()
        }
    }

    private fun observeProgress() {
        viewModel.moduloProgress.observe(this) { progress ->
            val stars = progress.estrelas
            binding.tvProgressSummary.text = "${progress.percentualConcluido.times(100).toInt()}% concluído"
            binding.tvProgressStars.text = when (stars) {
                3 -> "⭐⭐⭐"
                2 -> "⭐⭐☆"
                1 -> "⭐☆☆"
                else -> "☆☆☆"
            }
        }
    }

    private fun renderPracticeActivity() {
        if (currentIndex >= moduleContent.activities.size) {
            finishModulePractice()
            return
        }

        val activity = moduleContent.activities[currentIndex]
        selectedOption = null
        currentChecked = false
        dragUserMapping.clear()

        binding.tvPracticeCounter.text = "Atividade ${currentIndex + 1}/${moduleContent.activities.size}"
        binding.tvPracticeQuestion.text = activity.question
        binding.tvPracticeFeedback.text = ""
        binding.tvPracticeFeedback.visibility = View.GONE
        binding.btnCheckPractice.text = getString(com.info85.inforfacil.R.string.check_answer)

        when (activity.type) {
            ActivityType.MULTIPLE_CHOICE,
            ActivityType.VISUAL_IDENTIFICATION -> setupOptionButtons(activity)
            ActivityType.DRAG_AND_DROP -> setupDragDrop(activity)
        }
    }

    private fun setupOptionButtons(activity: PracticeActivityItem) {
        binding.groupOptions.visibility = View.VISIBLE
        binding.groupDragDrop.visibility = View.GONE

        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEachIndexed { index, button ->
            val option = activity.options.getOrNull(index).orEmpty()
            button.text = option
            button.visibility = if (option.isBlank()) View.GONE else View.VISIBLE
            button.icon = null
            activity.visualOptionIcons[option]?.let { iconRes ->
                button.setIconResource(iconRes)
            }
            button.setOnClickListener {
                selectedOption = option
                buttons.forEach { b -> b.isChecked = false }
                button.isChecked = true
            }
        }
    }

    private fun setupDragDrop(activity: PracticeActivityItem) {
        binding.groupOptions.visibility = View.GONE
        binding.groupDragDrop.visibility = View.VISIBLE
        binding.layoutDragItems.removeAllViews()
        binding.layoutDropTargets.removeAllViews()

        activity.options.forEach { option ->
            val item = createDragItem(option)
            binding.layoutDragItems.addView(item)
        }

        activity.dragTargets.forEach { target ->
            val targetView = createDropTarget(target)
            binding.layoutDropTargets.addView(targetView)
        }
    }

    private fun createDragItem(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setPadding(24, 20, 24, 20)
            contentDescription = "Arrastar item $text"
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 20f
                setColor(Color.WHITE)
                setStroke(2, Color.parseColor("#2196F3"))
            }
            setOnLongClickListener { view ->
                val clipData = ClipData(
                    ClipDescription("drag_item", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)),
                    ClipData.Item(text)
                )
                val shadow = View.DragShadowBuilder(view)
                view.startDragAndDrop(clipData, shadow, text, 0)
                true
            }
        }
    }

    private fun createDropTarget(target: String): TextView {
        return TextView(this).apply {
            this.text = "$target\n(sem item)"
            textSize = 14f
            setPadding(20, 20, 20, 20)
            contentDescription = "Destino $target"
            val normalBackground = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 20f
                setColor(Color.parseColor("#F5F5F5"))
                setStroke(2, Color.parseColor("#4CAF50"))
            }
            background = normalBackground
            setOnDragListener { _, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        val item = event.localState as? String ?: return@setOnDragListener false
                        dragUserMapping[item] = target
                        this.text = "$target\n$item"
                        true
                    }
                    else -> true
                }
            }
        }
    }

    private fun checkCurrentAnswer() {
        val activity = moduleContent.activities[currentIndex]
        val isCorrect = when (activity.type) {
            ActivityType.MULTIPLE_CHOICE,
            ActivityType.VISUAL_IDENTIFICATION -> selectedOption == activity.correctOption
            ActivityType.DRAG_AND_DROP -> isDragDropCorrect(activity)
        }

        if (isCorrect) {
            correctAnswers += 1
            feedbackManager.playSuccess()
            showFeedback(true, "Correto! Muito bem!")
            binding.btnCheckPractice.text = getString(com.info85.inforfacil.R.string.next_activity)
            currentChecked = true
        } else {
            feedbackManager.playError()
            showFeedback(false, "Ops! Tente novamente. Dica: ${activity.hint}")
            currentChecked = false
        }
    }

    private fun isDragDropCorrect(activity: PracticeActivityItem): Boolean {
        if (dragUserMapping.size != activity.dragCorrectMapping.size) return false
        return activity.dragCorrectMapping.all { (item, target) ->
            dragUserMapping[item] == target
        }
    }

    private fun showFeedback(success: Boolean, message: String) {
        binding.tvPracticeFeedback.visibility = View.VISIBLE
        binding.tvPracticeFeedback.text = message
        if (success) {
            binding.tvPracticeFeedback.setTextColor(Color.parseColor("#2E7D32"))
            binding.tvPracticeFeedback.applyFeedbackBounce()
        } else {
            binding.tvPracticeFeedback.setTextColor(Color.parseColor("#B00020"))
            binding.tvPracticeFeedback.applyFeedbackShake()
        }
    }

    private fun goToNextActivity() {
        currentIndex += 1
        renderPracticeActivity()
    }

    private fun finishModulePractice() {
        val total = moduleContent.activities.size
        val percentual = if (total == 0) 0f else correctAnswers.toFloat() / total.toFloat()
        val estrelas = when {
            percentual >= 0.8f -> 3
            percentual >= 0.6f -> 2
            percentual >= 0.4f -> 1
            else -> 0
        }

        lifecycleScope.launch {
            val moduloAtual = viewModel.moduloProgress.value
            val progress = ModuloProgress(
                moduloId = moduleContent.moduleId,
                nivel = moduloAtual?.nivel ?: 1,
                estrelas = estrelas,
                concluido = true,
                percentualConcluido = percentual
            )
            repository.atualizarModulo(moduleContent.moduleId, progress)
            feedbackManager.playModuleComplete()
            showToast("Parabéns! Módulo concluído!")
        }

        binding.tabsModule.getTabAt(2)?.select()
    }

    private fun showSection(section: Section) {
        binding.sectionLearn.visibility = if (section == Section.LEARN) View.VISIBLE else View.GONE
        binding.sectionPractice.visibility = if (section == Section.PRACTICE) View.VISIBLE else View.GONE
        binding.sectionProgress.visibility = if (section == Section.PROGRESS) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private enum class Section {
        LEARN,
        PRACTICE,
        PROGRESS
    }

    companion object {
        const val EXTRA_MODULE_ID = "extra_module_id"
    }
}
