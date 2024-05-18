package com.example.myfitfriend.presentation.dietarylogs.Foods
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.myfitfriend.R

import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.myfitfriend.util.Screen
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
@OptIn(ExperimentalGetImage::class)
@Composable
fun BarcodeScannerScreen(
    navController: NavController,
    viewModel: BarcodeScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(true) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {
        var isCameraInitialized by remember { mutableStateOf(false) }
        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        val scannedItemName by viewModel.scannedItemName.collectAsState()
        val foodId by viewModel.scannedItem.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (!isCameraInitialized) {
                    // Display placeholder image while camera is initializing
                    Image(
                        painter = painterResource(id = R.drawable.barcodeimage),
                        contentDescription = "Placeholder Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AndroidView(
                    factory = { context ->
                        val previewView = androidx.camera.view.PreviewView(context)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder()
                                .build()
                                .also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                            val imageAnalyzer = ImageAnalysis.Builder()
                                .setTargetResolution(Size(1280, 720))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                                        viewModel.processImageProxy(imageProxy) { barcode ->
                                            viewModel.getItemOfBarCode(barcode)
                                        }
                                    }
                                }

                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    context as LifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalyzer
                                )
                                isCameraInitialized = true // Set the flag to true once camera is initialized
                            } catch (e: Exception) {
                                // Handle exception
                            }
                        }, ContextCompat.getMainExecutor(context))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (scannedItemName.isNotEmpty()) {
                Text(text = scannedItemName)
                Button(onClick = {
                    navController.navigate(Screen.AddDietaryLogScreen.route+ "?foodId=${foodId}")
                }) {
                    Text(text = "Is it right?")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigateUp() }) {
                Text(text = "Return")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.barcodeimage),
                contentDescription = "Placeholder Image",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Camera permission is required to scan barcodes.")
        }
    }
}
