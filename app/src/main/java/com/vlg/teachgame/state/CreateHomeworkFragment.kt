package com.vlg.teachgame.state

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
    private lateinit var answerEdit: TextInputEditText
    private lateinit var previewImage: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveButton: Button

    private var selectedImageUri: Uri? = null

    // Регистрируем результат выбора изображения
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
        answerEdit = view.findViewById(R.id.answerEdit)
        previewImage = view.findViewById(R.id.previewImage)
        selectImageButton = view.findViewById(R.id.selectImageButton)
        saveButton = view.findViewById(R.id.saveButton)

        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            saveHomework()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickImageLauncher.launch(Intent.createChooser(intent, "Выберите изображение"))
    }

    private fun saveHomework() {
        val question = questionEdit.text.toString().trim()
        val answer = answerEdit.text.toString().trim()

        if (question.isEmpty()) {
            Toast.makeText(requireContext(), "Введите вопрос", Toast.LENGTH_SHORT).show()
            return
        }
        if (answer.isEmpty()) {
            Toast.makeText(requireContext(), "Введите правильный ответ", Toast.LENGTH_SHORT).show()
            return
        }

        val imageUriString = selectedImageUri?.toString()

        val homework = CreatedHomework(
            question = question,
            correctAnswer = answer,
            imageUri = imageUriString
        )

        gameManager.addCreatedHomework(homework)
        Toast.makeText(requireContext(), "Задание сохранено!", Toast.LENGTH_SHORT).show()

        questionEdit.text?.clear()
        answerEdit.text?.clear()
        previewImage.setImageResource(R.drawable.ic_image_placeholder)
        selectedImageUri = null

        navigator.startFragment(MenuFragment())
    }
}