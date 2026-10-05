package com.example.tiendadeportiva

import com.example.tiendadeportiva.data.SportsStoreRepository
import com.example.tiendadeportiva.model.CalzadoDeportivo
import com.example.tiendadeportiva.model.EquipamientoDeportivo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductoDeportivoTest {

    @Test
    fun testCalzadoDeportivoDescuento() {
        val calzado = CalzadoDeportivo(
            id = 1,
            nombre = "Zapatillas Test",
            marca = "Nike",
            precioBase = 100.0,
            stock = 5,
            descripcion = "Zapatilla de prueba",
            categoriaId = 1,
            imagenSimbolo = "👟",
            tallaEu = 42.0,
            tipoSuela = "Goma",
            porcentajeDescuentoPromocional = 0.20 // 20%
        )

        assertEquals(20.0, calzado.calcularDescuento(), 0.001)
        assertEquals(80.0, calzado.obtenerPrecioFinal(), 0.001)
        assertTrue(calzado.estaDisponible())
    }

    @Test
    fun testEquipamientoDeportivoDescuentoProfesional() {
        val equipamientoProf = EquipamientoDeportivo(
            id = 2,
            nombre = "Balón Oficial Test",
            marca = "Adidas",
            precioBase = 200.0,
            stock = 0,
            descripcion = "Balón de prueba",
            categoriaId = 1,
            imagenSimbolo = "⚽",
            deporte = "Fútbol",
            material = "Cuero",
            esProfesional = true // 15%
        )

        assertEquals(30.0, equipamientoProf.calcularDescuento(), 0.001)
        assertEquals(170.0, equipamientoProf.obtenerPrecioFinal(), 0.001)
        assertFalse(equipamientoProf.estaDisponible())
    }

    @Test
    fun testBuscarProductosRepositorySinCrash() {
        val resultado = SportsStoreRepository.buscarProductos("adidas", null)
        assertTrue(resultado.isNotEmpty())

        val busquedaInexistente = SportsStoreRepository.buscarProductos("xxxxxyyyyyzzzzz", 999)
        assertTrue(busquedaInexistente.isEmpty())
    }
}
