package com.example.tiendadeportiva.data

import com.example.tiendadeportiva.model.CalzadoDeportivo
import com.example.tiendadeportiva.model.CategoriaDeportiva
import com.example.tiendadeportiva.model.EquipamientoDeportivo
import com.example.tiendadeportiva.model.ProductoDeportivo
import com.example.tiendadeportiva.model.ResumenPedido

object SportsStoreRepository {

    val categorias: List<CategoriaDeportiva> = listOf(
        CategoriaDeportiva(
            id = 1,
            nombre = "Fútbol",
            iconoSimbolo = "⚽",
            descripcion = "Calzado, balones y equipamiento oficial de fútbol.",
            popularidad = 5
        ),
        CategoriaDeportiva(
            id = 2,
            nombre = "Baloncesto",
            iconoSimbolo = "🏀",
            descripcion = "Zapatillas de alto impacto, balones y canastas.",
            popularidad = 5
        ),
        CategoriaDeportiva(
            id = 3,
            nombre = "Running / Atletismo",
            iconoSimbolo = "🏃",
            descripcion = "Zapatillas ultraligeras y accesorios para corredores.",
            popularidad = 4
        ),
        CategoriaDeportiva(
            id = 4,
            nombre = "Tenis & Padel",
            iconoSimbolo = "🎾",
            descripcion = "Raquetas de precisión, raquetbol y pelotas de tenis.",
            popularidad = 4
        ),
        CategoriaDeportiva(
            id = 5,
            nombre = "Gimnasio & Fitness",
            iconoSimbolo = "🏋️",
            descripcion = "Mancuernas, bandas de resistencia y vestimenta de entrenamiento.",
            popularidad = 5
        )
    )

    val productos: List<ProductoDeportivo> = listOf(
        CalzadoDeportivo(
            id = 101,
            nombre = "Zapatillas Predator Elite FG",
            marca = "Adidas",
            precioBase = 189.99,
            stock = 12,
            descripcion = "Botines de alta precisión para césped natural firme.",
            categoriaId = 1,
            imagenSimbolo = "👟",
            tallaEu = 42.5,
            tipoSuela = "FG (Firm Ground)",
            porcentajeDescuentoPromocional = 0.15
        ),
        CalzadoDeportivo(
            id = 102,
            nombre = "Nike Air Zoom GT Cut 3",
            marca = "Nike",
            precioBase = 169.50,
            stock = 8,
            descripcion = "Zapatillas de básquetbol con amortiguación Zoom Air contigua.",
            categoriaId = 2,
            imagenSimbolo = "👟",
            tallaEu = 44.0,
            tipoSuela = "Goma antideslizante parquet",
            porcentajeDescuentoPromocional = 0.10
        ),
        CalzadoDeportivo(
            id = 103,
            nombre = "Asics Gel-Nimbus 26",
            marca = "Asics",
            precioBase = 150.00,
            stock = 15,
            descripcion = "Máxima amortiguación para largas distancias en asfalto.",
            categoriaId = 3,
            imagenSimbolo = "👟",
            tallaEu = 41.0,
            tipoSuela = "AHARPLUS de alta resistencia",
            porcentajeDescuentoPromocional = 0.05
        ),
        EquipamientoDeportivo(
            id = 201,
            nombre = "Balón Oficial Champions League 2024",
            marca = "Adidas",
            precioBase = 140.00,
            stock = 25,
            descripcion = "Balón de fútbol termosellado con certificación FIFA Quality Pro.",
            categoriaId = 1,
            imagenSimbolo = "⚽",
            deporte = "Fútbol",
            material = "Cuero Sintético TPU",
            esProfesional = true
        ),
        EquipamientoDeportivo(
            id = 202,
            nombre = "Raqueta Wilson Pro Staff v14",
            marca = "Wilson",
            precioBase = 260.00,
            stock = 5,
            descripcion = "Raqueta de control superior utilizada por jugadores de élite.",
            categoriaId = 4,
            imagenSimbolo = "🎾",
            deporte = "Tenis",
            material = "Grafito de carbono braided",
            esProfesional = true
        ),
        EquipamientoDeportivo(
            id = 203,
            nombre = "Set de Mancuernas Hexagonales 20kg",
            marca = "PowerGym",
            precioBase = 85.00,
            stock = 10,
            descripcion = "Par de mancuernas recubiertas de goma para mayor durabilidad.",
            categoriaId = 5,
            imagenSimbolo = "🏋️",
            deporte = "Fitness",
            material = "Hierro fundido recubierto",
            esProfesional = false
        ),
        EquipamientoDeportivo(
            id = 204,
            nombre = "Balón Wilson NBA Official Game",
            marca = "Wilson",
            precioBase = 175.00,
            stock = 7,
            descripcion = "Balón de cuero genuino oficial de la NBA.",
            categoriaId = 2,
            imagenSimbolo = "🏀",
            deporte = "Baloncesto",
            material = "Cuero genuino",
            esProfesional = true
        )
    )

    val pedidosRecientes: List<ResumenPedido> = listOf(
        ResumenPedido(
            id = 5001,
            cliente = "Carlos Mendoza",
            fecha = "24/10/2024",
            itemsCantidad = 2,
            totalPagar = 329.99,
            estado = "Enviado"
        ),
        ResumenPedido(
            id = 5002,
            cliente = "Ana María Torres",
            fecha = "25/10/2024",
            itemsCantidad = 1,
            totalPagar = 152.55,
            estado = "Entregado"
        ),
        ResumenPedido(
            id = 5003,
            cliente = "Roberto Gómez",
            fecha = "26/10/2024",
            itemsCantidad = 3,
            totalPagar = 410.00,
            estado = "Procesando"
        )
    )

    /**
     * Búsqueda y filtrado a prueba de fallos (Evita NullPointer, IndexOutOfBounds o Crashes)
     */
    fun buscarProductos(query: String, categoriaIdFiltro: Int?): List<ProductoDeportivo> {
        val busqueda = query.trim().lowercase()
        return productos.filter { prod ->
            val coincideCategoria = (categoriaIdFiltro == null || categoriaIdFiltro == 0 || prod.categoriaId == categoriaIdFiltro)
            val coincideTexto = busqueda.isEmpty() ||
                    prod.nombre.lowercase().contains(busqueda) ||
                    prod.marca.lowercase().contains(busqueda) ||
                    prod.descripcion.lowercase().contains(busqueda)
            coincideCategoria && coincideTexto
        }
    }

    fun obtenerNombreCategoria(categoriaId: Int): String {
        return categorias.find { it.id == categoriaId }?.nombre ?: "General"
    }
}
