package com.example.tiendadeportiva.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendadeportiva.data.SportsStoreRepository
import com.example.tiendadeportiva.model.CalzadoDeportivo
import com.example.tiendadeportiva.model.EquipamientoDeportivo
import com.example.tiendadeportiva.model.ProductoDeportivo
import java.util.Locale

/**
 * Pantalla 1: Catálogo de Productos (Checkpoint 4 & 5 - Jetpack Compose UI)
 * Muestra el listado de objetos implementados (CalzadoDeportivo y EquipamientoDeportivo)
 * con búsqueda, filtros y diálogos sin riesgo de cierres inesperados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onAgregarAlCarrito: (ProductoDeportivo) -> Unit,
    modifier: Modifier = Modifier
) {
    var queryBusqueda by rememberSaveable { mutableStateOf("") }
    var categoriaSeleccionadaId by rememberSaveable { mutableIntStateOf(0) } // 0 = Todas
    var productoDetalleModal by remember { mutableStateOf<ProductoDeportivo?>(null) }
    var mensajeConfirmacion by remember { mutableStateOf<String?>(null) }

    // Búsqueda filtrada de forma segura contra excepciones
    val productosFiltrados = remember(queryBusqueda, categoriaSeleccionadaId) {
        try {
            SportsStoreRepository.buscarProductos(
                query = queryBusqueda,
                categoriaIdFiltro = if (categoriaSeleccionadaId == 0) null else categoriaSeleccionadaId
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        // Barra de Búsqueda
        OutlinedTextField(
            value = queryBusqueda,
            onValueChange = { queryBusqueda = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            placeholder = { Text("Buscar zapatillas, balones, raquetas...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            trailingIcon = {
                if (queryBusqueda.isNotEmpty()) {
                    IconButton(onClick = { queryBusqueda = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Filtro por Categorías (LazyRow)
        Text(
            text = "Categorías Deportivas",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                FilterChip(
                    selected = categoriaSeleccionadaId == 0,
                    onClick = { categoriaSeleccionadaId = 0 },
                    label = { Text("Todas") },
                    leadingIcon = if (categoriaSeleccionadaId == 0) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
            items(
                items = SportsStoreRepository.categorias,
                key = { it.id }
            ) { cat ->
                FilterChip(
                    selected = categoriaSeleccionadaId == cat.id,
                    onClick = { categoriaSeleccionadaId = cat.id },
                    label = { Text("${cat.iconoSimbolo} ${cat.nombre}") },
                    leadingIcon = if (categoriaSeleccionadaId == cat.id) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mensaje de Notificación al agregar producto
        AnimatedVisibility(
            visible = mensajeConfirmacion != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            mensajeConfirmacion?.let { msg ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { mensajeConfirmacion = null }) {
                            Text("OK")
                        }
                    }
                }
            }
        }

        // Contador de Resultados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Productos Disponibles (${productosFiltrados.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de Productos
        if (productosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏀", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No se encontraron artículos deportivos",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Intenta con otra búsqueda o categoría",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(
                    items = productosFiltrados,
                    key = { it.id }
                ) { producto ->
                    ProductoCardItem(
                        producto = producto,
                        onVerDetalle = { productoDetalleModal = producto },
                        onAgregarClick = {
                            onAgregarAlCarrito(producto)
                            mensajeConfirmacion = "¡Añadido: ${producto.nombre}!"
                        }
                    )
                }
            }
        }
    }

    // Modal de Detalle Completo del Producto
    productoDetalleModal?.let { prod ->
        AlertDialog(
            onDismissRequest = { productoDetalleModal = null },
            icon = { Text(prod.imagenSimbolo, fontSize = 40.sp) },
            title = {
                Text(
                    text = prod.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Marca: ${prod.marca}", fontWeight = FontWeight.SemiBold)
                    Text("Categoría: ${SportsStoreRepository.obtenerNombreCategoria(prod.categoriaId)}")
                    Text(prod.descripcion, style = MaterialTheme.typography.bodySmall)

                    // Atributos Específicos según POO (Polimorfismo / Herencia)
                    when (prod) {
                        is CalzadoDeportivo -> {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("👟 Calzado Deportivo", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                    Text("Talla EU: ${prod.tallaEu}")
                                    Text("Tipo Suela: ${prod.tipoSuela}")
                                    Text("Descuento Promocional: ${(prod.porcentajeDescuentoPromocional * 100).toInt()}%")
                                }
                            }
                        }
                        is EquipamientoDeportivo -> {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text("⚽ Equipamiento Deportivo", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                    Text("Deporte: ${prod.deporte}")
                                    Text("Material: ${prod.material}")
                                    Text("Gama: ${if (prod.esProfesional) "Profesional Elite 🏆" else "Entrenamiento Standard"}")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Cálculo de precios mediante métodos de la clase base
                    val precioOriginal = prod.precioBase
                    val descuento = prod.calcularDescuento()
                    val precioFinal = prod.obtenerPrecioFinal()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            if (descuento > 0) {
                                Text(
                                    text = String.format(Locale.US, "$%.2f", precioOriginal),
                                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = String.format(Locale.US, "$%.2f", precioFinal),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (prod.estaDisponible()) "Stock: ${prod.stock} ud." else "Agotado",
                            color = if (prod.estaDisponible()) Color(0xFF2E7D32) else Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAgregarAlCarrito(prod)
                        mensajeConfirmacion = "¡Añadido al carrito: ${prod.nombre}!"
                        productoDetalleModal = null
                    },
                    enabled = prod.estaDisponible()
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Agregar al Carrito")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoDetalleModal = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun ProductoCardItem(
    producto: ProductoDeportivo,
    onVerDetalle: () -> Unit,
    onAgregarClick: () -> Unit
) {
    ElevatedCard(
        onClick = onVerDetalle,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono / Emoji del Producto
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = producto.imagenSimbolo, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del Producto
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = producto.marca.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    // Badge de Tipo de Objeto Implementado (POO)
                    val etiquetaTipo = when (producto) {
                        is CalzadoDeportivo -> "Calzado (Talla ${producto.tallaEu})"
                        is EquipamientoDeportivo -> producto.deporte
                        else -> "Deporte"
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = etiquetaTipo,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Precio y Botón
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val precioFinal = producto.obtenerPrecioFinal()
                    val precioBase = producto.precioBase

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format(Locale.US, "$%.2f", precioFinal),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (producto.calcularDescuento() > 0) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format(Locale.US, "$%.2f", precioBase),
                                style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.LineThrough),
                                color = Color.Gray
                            )
                        }
                    }

                    Button(
                        onClick = onAgregarClick,
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Agregar",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
