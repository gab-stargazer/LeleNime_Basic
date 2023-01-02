package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.BottomSheetCharacterBinding
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException

@AndroidEntryPoint
class FragmentCharacterBottomSheet :
    BottomSheetDialogFragment(R.layout.bottom_sheet_character) {

    private val args: FragmentCharacterBottomSheetArgs by navArgs()
    private val binding: BottomSheetCharacterBinding by viewBinding()
    private val viewModel: CharacterBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
            .getCharacterDetailByCharacterId(args.characterId)
            .catch { t ->
                binding.progressCircular.visibility = View.GONE
                val errorMessage =
                    if (t is HttpException) "${t.code()} - ${t.message()}"
                    else t.localizedMessage
                binding.tvError.text = errorMessage
                binding.tvError.visibility = View.VISIBLE
            }
            .asLiveData()
            .observe(viewLifecycleOwner) { characterDetail ->
                binding.apply {
                    progressCircular.visibility = View.GONE
                    ivCharacterImage.load(characterDetail.images) {
                        transformations(RoundedCornersTransformation(15f))
                        build()
                    }
                    tvCharacterName.text = characterDetail.name
                    tvCharacterFavorite.text = characterDetail.favoriteBy.toString()
                    ivFavorite.visibility = View.VISIBLE
                    setRomajiName(characterDetail.characterKanjiName)
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
    }

    private fun BottomSheetCharacterBinding.setNickName(
        nickname: List<String>,
        characterInformation: String
    ) {
        if (nickname.isEmpty()) {
            tvCharacterNickname.visibility = View.GONE
            return
        }

        if (characterInformation.contains(nickname.toString())) {
            tvCharacterNickname.visibility = View.GONE
            return
        }

        tvCharacterNickname.text = ListToString().invoke(nickname)
    }

    private fun BottomSheetCharacterBinding.setRomajiName(
        kanjiName: String
    ) {
        if (kanjiName.isEmpty()) tvCharacterRomaji.visibility = View.GONE
        tvCharacterRomaji.text = kanjiName
    }
}
