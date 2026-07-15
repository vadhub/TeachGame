package com.vlg.teachgame.state

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.vlg.teachgame.GameManager
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.data.CreatedHomework

class CreateHomeworkFragment : Fragment() {

    private lateinit var navigator: Navigator
    private lateinit var gameManager: GameManager

    private lateinit var questionEdit: TextInputEditText
    private lateinit var optionsContainer: LinearLayout
    private lateinit var previewImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveButton: Button
    private lateinit var addOptionButton: Button

    private val optionViews = mutableListOf<View>()
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                selectedImageUri = uri
                previewImage.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "Не удалось получить изображение", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
        gameManager = context as GameManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_homework, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionEdit = view.findViewById(R.id.questionEdit)
        optionsContainer = view.findViewById(R.id.optionsContainer)
        previewImage = view.findViewById(R.id.previewImage)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        saveButton = view.findViewById(R.id.saveButton)
        addOptionButton = view.findViewById(R.id.addOptionButton)
        addOption()
        addOption()

        addOptionButton.setOnClickListener {
            if (optionViews.size < 6) {
                addOption()
            } else {
                Toast.makeText(requireContext(), "Максимум 6 вариантов", Toast.LENGTH_SHORT).show()
            }
        }

        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            saveHomework()
        }
    }

    private fun addOption() {
        val inflater = LayoutInflater.from(context)
        val optionView = inflater.inflate(R.layout.option_item, optionsContainer, false)
        val removeButton = optionView.findViewById<android.widget.ImageButton>(R.id.removeOptionButton)
        removeButton.setOnClickListener {
            optionsContainer.removeView(optionView)
            optionViews.remove(optionView)
        }
        optionsContainer.addView(optionView)
        optionViews.add(optionView)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickImageLauncher.launch(Intent.createChooser(intent, "Выберите изображение"))
    }

    private fun getOptions(): List<String> {
        val options = mutableListOf<String>()
        for (view in optionViews) {
            val editText = view.findViewById<TextInputEditText>(R.id.optionEdit)
            val text = editText.text.toString().trim()
            if (text.isNotEmpty()) {
                options.add(text)
            }
        }
        return options
    }

    private fun getCorrectIndices(): List<Int> {
        val indices = mutableListOf<Int>()
        for ((index, view) in optionViews.withIndex()) {
            val checkBox = view.findViewById<CheckBox>(R.id.optionCheck)
            if (checkBox.isChecked) {
                indices.add(index)
            }
        }
        return indices
    }

    private fun saveHomework() {
        val question = questionEdit.text.toString().trim()
        val options = getOptions()
        val correctIndices = getCorrectIndices()

        if (question.isEmpty()) {
            Toast.makeText(requireContext(), "Введите вопрос", Toast.LENGTH_SHORT).show()
            return
        }
        if (options.size < 2) {
            Toast.makeText(requireContext(), "Добавьте минимум 2 варианта", Toast.LENGTH_SHORT).show()
            return
        }
        if (correctIndices.isEmpty()) {
            Toast.makeText(requireContext(), "Отметьте хотя бы один правильный вариант", Toast.LENGTH_SHORT).show()
            return
        }

        val imageUriString = selectedImageUri?.toString()

        val homework = CreatedHomework(
            question = question,
            options = options,
            correctIndices = correctIndices,
            imageUri = imageUriString
        )

        gameManager.addCreatedHomework(homework)
        Toast.makeText(requireContext(), "Задание сохранено!", Toast.LENGTH_SHORT).show()

        questionEdit.text?.clear()
        optionsContainer.removeAllViews()
        optionViews.clear()
        addOption()
        addOption()
        previewImage.setImageResource(R.drawable.ic_image_placeholder)
        selectedImageUri = null

        navigator.startFragment(MenuFragment())
    }
}