package com.example.tiendadeportiva.model

/**
 * Checkpoint 1 & 2: Clase Abstracta Base (POO - Encapsulamiento y Abstracción)
 * Representa la entidad general de un producto deportivo.
 */
abstract class ProductoDeportivo(
    val id: Int,
    val nombre: String,
    val marca: String,
    val precioBase: Double,
    var stock: Int,
    val descripcion: String,
    val categoriaId: Int,
    val imagenSimbolo: String
) {
    /**
     * Método polimórfico abstracto para calcular descuento según el tipo de producto.
     */
    abstract fun calcularDescuento(): Double

    /**
     * Calcula el precio final aplicando el descuento calculado.
     */
    fun obtenerPrecioFinal(): Double {
        val descuento = calcularDescuento()
        val precio = precioBase - descuento
        return if (precio < 0.0) 0.0 else precio
    }

    /**
     * Verifica la disponibilidad del producto.
     */
    fun estaDisponible(): Boolean = stock > 0
}

/**
 * Subclase CalzadoDeportivo (POO - Herencia y Polimorfismo)
 */
class CalzadoDeportivo(
    id: Int,
    nombre: String,
    marca: String,
    precioBase: Double,
    stock: Int,
    descripcion: String,
    categoriaId: Int,
    imagenSimbolo: String,
    val tallaEu: Double,
    val tipoSuela: String,
    val porcentajeDescuentoPromocional: Double = 0.10 // 10% de descuento por defecto
) : ProductoDeportivo(
    id, nombre, marca, precioBase, stock, descripcion, categoriaId, imagenSimbolo
) {
    override fun calcularDescuento(): Double {
        return precioBase * porcentajeDescuentoPromocional
    }
}

/**
 * Subclase EquipamientoDeportivo (POO - Herencia y Polimorfismo)
 */
class EquipamientoDeportivo(
    id: Int,
    nombre: String,
    marca: String,
    precioBase: Double,
    stock: Int,
    descripcion: String,
    categoriaId: Int,
    imagenSimbolo: String,
    val deporte: String,
    val material: String,
    val esProfesional: Boolean = false
) : ProductoDeportivo(
    id, nombre, marca, precioBase, stock, descripcion, categoriaId, imagenSimbolo
) {
    override fun calcularDescuento(): Double {
        // Equipamiento profesional o general tiene diferente descuento
        val porcentaje = if (esProfesional) 0.15 else 0.05
        return precioBase * porcentaje
    }
}

/**
 * Objeto / Data Class de Categoría Deportiva (Segunda Entidad principal)
 */
data class CategoriaDeportiva(
    val id: Int,
    val nombre: String,
    val iconoSimbolo: String,
    val descripcion: String,
    val popularidad: Int // 1 a 5 estrellas
)

/**
 * Objeto / Data Class de Resumen de Pedido (Tercera Entidad para gestión de compras)
 */
data class ResumenPedido(
    val id: Int,
    val cliente: String,
    val fecha: String,
    val itemsCantidad: Int,
    val totalPagar: Double,
    val estado: String
)
