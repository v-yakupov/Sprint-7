package ru.sber.sbermvc

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.sber.sbermvc.service.Record
import javax.servlet.http.Cookie

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ContextConfiguration
@Import(WebSecurityConfig::class)
class SbermvcApplicationTests {
    @LocalServerPort
    var port: Int = 65535

    @Autowired
    lateinit var mockMvc: MockMvc

    @BeforeEach
    @WithMockUser(
        username = "admin",
        password = "admin",
        roles = ["ADMIN"]
    )
    fun setup() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/app/add")
                .param("name", "Spurdo Spärde")
                .param("address", "Benin")
        )
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/app/add")
                .param("name", "Jean Pierre Karasique")
                .param("address", "Paris")
        )
    }

    @Test
    @WithAnonymousUser
    fun `should redirect unauthorized`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("http://localhost/login"))
    }

    @Test
    @WithAnonymousUser
    fun `should login successfully and show default page`() {
        mockMvc.perform(
            SecurityMockMvcRequestBuilders
                .formLogin("/login")
                .user("login","admin")
                .password("password","admin"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should restrict api access`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/list"))
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(
        username = "api",
        password = "api",
        roles = ["API"]
    )
    fun `should restrict removal`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/0/delete"))
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should list all records`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(containsString("Spurdo Spärde")))
            .andExpect(content().string(containsString("Jean Pierre Karasique")))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should list one record`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/0/view")
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(containsString("Spurdo Spärde")))
            .andExpect(content().string(containsString("Benin")))
            .andExpect(view().name("record"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should find one record`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0"))
                .queryParam("id", "0"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(containsString("Spurdo Spärde")))
            .andExpect(content().string(containsString("Benin")))
            .andExpect(content().string(not(containsString("Jean Pierre Karasique"))))
            .andExpect(content().string(not(containsString("Paris"))))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should find two records`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0"))
                .queryParam("id", "0")
                .queryParam("address", "Paris"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(containsString("Spurdo Spärde")))
            .andExpect(content().string(containsString("Benin")))
            .andExpect(content().string(containsString("Jean Pierre Karasique")))
            .andExpect(content().string(containsString("Paris")))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should add record`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/app/add")
                .cookie(Cookie("auth", "0"))
                .param("name", "Pepe")
                .param("address", "Washington"))
            .andExpect(status().is3xxRedirection)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(containsString("Pepe")))
            .andExpect(content().string(containsString("Washington")))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "admin",
        password = "admin",
        roles = ["ADMIN"]
    )
    fun `should remove record`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/1/delete")
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is3xxRedirection)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string(not(containsString("Jean Pierre Karasique"))))
            .andExpect(content().string(not(containsString("Paris"))))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "user",
        password = "user",
        roles = ["USER"]
    )
    fun `should edit record`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/1/edit")
                .cookie(Cookie("auth", "0")))
            .andExpect(view().name("edit"))
            .andExpect(status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/app/1/edit")
                .cookie(Cookie("auth", "0"))
                .param("name", "Pepe")
                .param("address", "Washington"))
            .andExpect(status().is3xxRedirection)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/app/list")
                .cookie(Cookie("auth", "0")))
            .andExpect(content().string(not(containsString("Jean Pierre Karasique"))))
            .andExpect(content().string(not(containsString("Paris"))))
            .andExpect(content().string(containsString("Pepe")))
            .andExpect(content().string(containsString("Washington")))
            .andExpect(view().name("all"))
    }

    @Test
    @WithMockUser(
        username = "api",
        password = "api",
        roles = ["API"]
    )
    fun `should list one record in JSON`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/0/view")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Spurdo Spärde"))
            .andExpect(jsonPath("$.address").value("Benin"))
    }

    @Test
    @WithMockUser(
        username = "api",
        password = "api",
        roles = ["API"]
    )
    fun `should list all records in JSON`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/list")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].name", containsInAnyOrder("Spurdo Spärde", "Jean Pierre Karasique")))
            .andExpect(jsonPath("$[*].address", containsInAnyOrder("Benin", "Paris")))
    }

    @Test
    @WithMockUser(
        username = "apiadmin",
        password = "apiadmin",
        roles = ["API", "ADMIN"]
    )
    fun `should delete record in JSON`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/0/delete")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/list")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].name", not(containsInAnyOrder("Spurdo Spärde"))))
            .andExpect(jsonPath("$[*].address", not(containsInAnyOrder("Benin"))))
    }

    @Test
    @WithMockUser(
        username = "api",
        password = "api",
        roles = ["API"]
    )
    fun `should update record in JSON`() {
        val user = ObjectMapper().writeValueAsString(Record("Pepe", "Washington"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/0/edit")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(user))
            .andExpect(status().is2xxSuccessful)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/0/view")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(Cookie("auth", "0")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", not(`is`("Spurdo Spärde"))))
            .andExpect(jsonPath("$.address", not(`is`("Benin"))))
            .andExpect(jsonPath("$.name", `is`("Pepe")))
            .andExpect(jsonPath("$.address", `is`("Washington")))
    }
}
