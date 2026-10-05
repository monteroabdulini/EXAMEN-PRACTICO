package com.example.tiendadeportiva.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendadeportiva.model.ProductoDeportivo
import com.example.tiendadeportiva.ui.screens.CategoriasScreen
import com.example.tiendadeportiva.ui.screens.ProductosScreen
import java.util.Locale

/**
 * Item del Carrito con cantidad
 */
data class ItemCarrito(
    val producto: ProductoDeportivo,
    var cantidad: Int
)

/**
 * Contenedor Principal de la App (Checkpoints 4 & 5)
 * Ofrece navegación fluida y segura entre dos pantallas principales y carrito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaDeportivaApp() {
    var pantallaActual by rememberSaveable { mutableIntStateOf(0) } // 0 = Catálogo, 1 = Categorías
    val carritoItems = remember { mutableStateListOf<ItemCarrito>() }
    var mostrarModalCarrito by remember { mutableStateOf(false) }
    var mostrarConfirmacionCompra by remember { mutableStateOf(false) }

    // Cantidad total de items en el carrito de forma reactiva
    val totalItemsCarrito = carritoItems.sumOf { it.cantidad }
    val totalPagarCarrito = carritoItems.sumOf { it.producto.obtenerPrecioFinal() * it.cantidad }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚽ ", fontSize = 22.sp)
                        Text(
                            text = "Tienda Deportiva",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { mostrarModalCarrito = true }) {
                        BadgedBox(
                            badge = {
                                if (totalItemsCarrito > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = Color.White
                                    ) {
                                        Text(text = "$totalItemsCarrito", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Ver Carrito",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = pantallaActual == 0,
                    onClick = { pantallaActual = 0 },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Catálogo") },
                    label = { Text("Catálogo") }
                )
                NavigationBarItem(
                    selected = pantallaActual == 1,
                    onClick = { pantallaActual = 1 },
                    icon = { Icon(Icons.Default.Category, contentDescription = "Categorías & Pedidos") },
                    label = { Text("Categorías & Pedidos") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (pantallaActual) {
                0 -> ProductosScreen(
                    onAgregarAlCarrito = { prod ->
                        val itemExistente = carritoItems.find { it.producto.id == prod.id }
                        if (itemExistente != null) {
                            itemExistente.cantidad += 1
                        } else {
                            carritoItems.add(ItemCarrito(producto = prod, cantidad = 1))
                        }
                    }
                )
                1 -> CategoriasScreen()
            }
        }
    }

    // Modal Diálogo del Carrito de Compras (Gestión segura)
    if (mostrarModalCarrito) {
        AlertDialog(
            onDismissRequest = { mostrarModalCarrito = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mi Carrito de Compras", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                if (carritoItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🛒", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "El carrito está vacío",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Agrega artículos desde el catálogo",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Column {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = carritoItems,
                                key = { it.producto.id }
                            ) { item ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(item.producto.imagenSimbolo, fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.producto.nombre,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = String.format(Locale.US, "$%.2f c/u", item.producto.obtenerPrecioFinal()),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        // Controles + / - / Eliminar de forma segura
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = {
                                                    if (item.cantidad > 1) {
                                                        item.cantidad -= 1
                                                    } else {
                                                        carritoItems.remove(item)
                                                    }
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    if (item.cantidad == 1) Icons.Default.Delete else Icons.Default.Remove,
                                                    contentDescription = "Menos",
                                                    modifier = Modifier.size(16.dp),
                                                    tint = if (item.cantidad == 1) Color.Red else MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            Text(
                                                text = "${item.cantidad}",
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )

                                            IconButton(
                                                onClick = { item.cantidad += 1 },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Total acumulado
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total a Pagar:", fontWeight = FontWeight.Bold)
                                Text(
                                    text = String.format(Locale.US, "$%.2f", totalPagarCarrito),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (carritoItems.isNotEmpty()) {
                    Button(
                        onClick = {
                            mostrarModalCarrito = false
                            mostrarConfirmacionCompra = true
                        }
                    ) {
                        Text("Finalizar Compra")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarModalCarrito = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo de Confirmación de Compra Exitosa
    if (mostrarConfirmacionCompra) {
        AlertDialog(
            onDismissRequest = {
                carritoItems.clear()
                mostrarConfirmacionCompra = false
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¡Compra Realizada con Éxito!", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Gracias por tu compra en Tienda Deportiva. Tu pedido ha sido procesado y registrado sin errores.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        carritoItems.clear()
                        mostrarConfirmacionCompra = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
