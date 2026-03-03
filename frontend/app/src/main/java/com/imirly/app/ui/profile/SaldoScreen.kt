package com.imirly.app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaldoScreen(navController: NavController, viewModel: SaldoViewModel = viewModel()) {
    val iMirlyPurple = Color(0xFF6C5CE7)
    val bgLight = Color(0xFFFAFAFA)
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(bgLight)
    ) {
        // --- SECCIÓN SUPERIOR MORADA ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(iMirlyPurple)
                    .padding(bottom = 32.dp)
            ) {
                Column {
                    // Toolbar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 8.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Saldo", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 36.dp))
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tarjeta Principal
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Saldo disponible", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            if (viewModel.isLoading.value) {
                                Text("Cargando...", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                            } else {
                                Text(
                                    String.format("%.2f€", viewModel.saldo.value),
                                    color = Color.White,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Botones de Retirar e Ingresar
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(
                                    onClick = { 
                                        if (viewModel.saldo.value >= 10.0) {
                                            viewModel.retirarSaldo()
                                            Toast.makeText(context, "TODO: Retiro de 10€ a tu banco en proceso", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Saldo insuficiente para retirar", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = iMirlyPurple),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Retirar", fontWeight = FontWeight.SemiBold)
                                }
                                
                                OutlinedButton(
                                    onClick = { 
                                        viewModel.ingresarSaldo()
                                        Toast.makeText(context, "TODO: Recarga de 10€ desde tu banco en proceso", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Ingresar", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- SECCIÓN BLANCA INFERIOR ---
        item {
            Column(modifier = Modifier.padding(horizontal = 24.dp).offset(y = (-20).dp)) {
                // Tarjetas pequeñas "Pendiente" y "Este mes"
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFFF7E6)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = Color(0xFFFFB800), modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Pendiente", color = Color.Gray, fontSize = 12.sp)
                            Text(String.format("%.2f€", viewModel.pendiente.value), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE6F9F0)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Este mes", color = Color.Gray, fontSize = 12.sp)
                            Text(String.format("%.2f€", viewModel.esteMes.value), color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Banner "Invita y gana"
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF3F1FF),
                    shadowElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = iMirlyPurple)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("¡Invita y gana!", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
                            Text("Gana 10€ por cada amigo que se registre", color = Color.DarkGray, fontSize = 12.sp)
                        }
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = iMirlyPurple, modifier = Modifier.size(16.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Transacciones recientes
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Transacciones recientes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Divider(color = Color(0xFFF0F0F0))
                        
                        if (viewModel.transacciones.value.isEmpty()) {
                            Text("No hay transacciones recientes.", color = Color.Gray, fontSize = 14.sp)
                        } else {
                            viewModel.transacciones.value.forEach { tr ->
                                TransactionListItem(
                                    title = tr.titulo,
                                    subtitle = tr.subTitulo,
                                    amount = tr.cantidadFormateada,
                                    status = tr.estado,
                                    time = tr.tiempo
                                )
                                Divider(color = Color(0xFFF0F0F0))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(title: String, subtitle: String, amount: String, status: String, time: String) {
    val isPendiente = status.contains("Pendiente", ignoreCase = true)
    val isRetiro = amount.startsWith("-") || amount.contains("-") || title.contains("Retiro", ignoreCase = true) || title.contains("Pago", ignoreCase = true)
    
    val statusColor = if (isPendiente) Color(0xFFFFB800) else Color(0xFF2ECC71)
    val statusBgColor = if (isPendiente) Color(0xFFFFF7E6) else Color(0xFFE6F9F0)
    val amountColor = if (isRetiro) Color.Red else statusColor
    
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            Text(time, color = Color.LightGray, fontSize = 11.sp)
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(amount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = amountColor)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = statusBgColor
            ) {
                Text(status, color = statusColor, fontSize = 9.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
        }
    }
}
