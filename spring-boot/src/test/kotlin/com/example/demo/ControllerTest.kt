package com.example.demo

import com.example.demo.controller.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
class ControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 2xx and correct output`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/get"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.content().string("Something!"))
    }
}