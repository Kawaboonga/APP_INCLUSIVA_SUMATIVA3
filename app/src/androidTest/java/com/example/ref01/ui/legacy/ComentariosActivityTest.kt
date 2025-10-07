package com.example.ref01.ui.legacy

import org.junit.Assert.*
import org.junit.Test

class ComentariosViewModelTest {

    @Test
    fun `setBook cambia id`() {
        val vm = ComentariosViewModel(clock = { 100L })
        vm.setBook(7)
        assertEquals(7, vm.bookId)
    }

    @Test
    fun `addComment agrega y marca estado`() {
        val vm = ComentariosViewModel(clock = { 200L })
        val ok = vm.addComment(" Hola ")
        assertTrue(ok)
        assertEquals(1, vm.comments.value.size)
        assertEquals("added", vm.status.value)
        assertEquals(200L, vm.comments.value.first().timestamp)
    }

    @Test
    fun `addComment ignora vacío`() {
        val vm = ComentariosViewModel()
        assertFalse(vm.addComment("   "))
        assertEquals("empty", vm.status.value)
        assertTrue(vm.comments.value.isEmpty())
    }

    @Test
    fun `removeAt y clear`() {
        val vm = ComentariosViewModel()
        vm.addComment("a"); vm.addComment("b")
        assertTrue(vm.removeAt(0))
        assertEquals("removed", vm.status.value)
        vm.clear()
        assertTrue(vm.comments.value.isEmpty())
        assertEquals("cleared", vm.status.value)
    }
}
