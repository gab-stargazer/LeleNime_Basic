package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.core.common.Constant.UNKNOWN
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.BottomSheetCharacterBinding
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCharacterBottomSheet :
    BottomSheetDialogFragment(R.layout.bottom_sheet_character) {

    private val args: FragmentCharacterBottomSheetArgs by navArgs()
    private val binding: BottomSheetCharacterBinding by viewBinding()
    private val viewModel: CharacterBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacterDetailByID(args.characterId)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.character.collect { resourceOfCharacter ->
                when (resourceOfCharacter) {
                    is Resource.Error -> {
                        //  Check whether the error still contains data or not
                        val data: CharacterDetail? = resourceOfCharacter.data
                        data?.let { characterDetail ->
                            binding.setContentView(characterDetail)
                            Snackbar
                                .make(
                                    binding.root,
                                    resourceOfCharacter.message ?: UNKNOWN,
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                        }

                        //  Handle the Error
                        if (data == null) {
                            binding.apply {
                                layoutContent.root.visibility = View.GONE
                                layoutError.root.visibility = View.VISIBLE
                                setErrorView(resourceOfCharacter.message ?: UNKNOWN)
                            }
                        }
                    }
                    is Resource.Success -> resourceOfCharacter.data?.let { characterDetail ->
                        binding.setContentView(characterDetail)
                    }
                    else -> {
                        val errorLayout = binding.layoutError
                        val contentLayout = binding.layoutContent
                        val isErrorScreenVisible: Boolean = errorLayout
                            .root
                            .visibility == View.VISIBLE

                        if (isErrorScreenVisible) {
                            errorLayout.root.visibility = View.GONE
                            contentLayout.apply {
                                root.visibility = View.VISIBLE
                                progressCircular.visibility = View.VISIBLE
                            }

                            return@collect
                        }
                    }
                }
            }
        }
    }

    private fun BottomSheetCharacterBinding.setContentView(characterDetail: CharacterDetail) {
        layoutContent.apply {
            progressCircular.visibility = View.GONE

            ivCharacterImage.load(characterDetail.images) {
                transformations(RoundedCornersTransformation(15f))
                build()
            }

            tvCharacterName.text = characterDetail.name
            tvCharacterFavorite.text = characterDetail.favoriteBy.toString()
            ivFavorite.visibility = View.VISIBLE

            tvCharacterRomaji.text = characterDetail.characterKanjiName
                .ifEmpty {
                    tvCharacterRomaji.visibility = View.GONE
                    UNKNOWN
                }

            setNickName(
                characterDetail.characterNickNames,
                characterDetail.characterInformation
            )
            tvCharacterInformation.text =
                characterDetail
                    .characterInformation
                    .ifEmpty { getString(R.string.no_information_by_mal) }
        }
    }

    private fun BottomSheetCharacterBinding.setErrorView(message: String) {
        layoutError.apply {
            val characterID = args.characterId
            tvErrorMessage.text = message
            btnRetryConnection.setOnClickListener {
                viewModel.getCharacterDetailByID(characterID)
            }
        }
    }

    private fun BottomSheetCharacterBinding.setNickName(
        nickname: List<String>,
        characterInformation: String
    ) {
        layoutContent.apply {
            val nicknameIsNotDifferent = characterInformation
                .contains(nickname.joinToString().toRegex())
            if (nickname.isEmpty() || nicknameIsNotDifferent) {
                tvCharacterNickname.visibility = View.GONE
                return
            }

            tvCharacterNickname.text = ListToString().invoke(nickname)
        }
    }
}
