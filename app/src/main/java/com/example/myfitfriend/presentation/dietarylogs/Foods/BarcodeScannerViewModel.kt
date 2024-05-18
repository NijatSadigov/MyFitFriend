package com.example.myfitfriend.presentation.dietarylogs.Foods

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitfriend.domain.use_case.users.GetFoodByBarCodeUseCase
import com.example.myfitfriend.util.Resources
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    private val getFoodByBarCodeUseCase: GetFoodByBarCodeUseCase
) : ViewModel() {
    private val scanner = BarcodeScanning.getClient()

    private val _scannedItem = MutableStateFlow("")
    val scannedItem: StateFlow<String> = _scannedItem
    private val _scannedItemName = MutableStateFlow("")
    val scannedItemName: StateFlow<String> = _scannedItemName



    fun getItemOfBarCode(barcodeString: String) {
        viewModelScope.launch {
            getFoodByBarCodeUseCase.invoke(barcodeString).onEach { r ->
                when (r) {
                    is Resources.Error -> {
                        println("Error at GetIdOfBarcode, error:${r.message} ::: ${r.data}")

                    }
                    is Resources.Loading -> {}
                    is Resources.Success -> {
                        println("getItem revoked")
                        _scannedItem.value = r.data?.foodId ?: ""
                        _scannedItemName.value = r.data?.foodName ?: ""
                    }
                }
            }.launchIn(this)
        }
    }

    @ExperimentalGetImage
    fun processImageProxy(imageProxy: ImageProxy, onBarcodeDetected: (String) -> Unit) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        when (barcode.valueType) {
                            Barcode.TYPE_TEXT, Barcode.TYPE_PRODUCT -> {
                                onBarcodeDetected(barcode.displayValue ?: "Unknown")
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
}
