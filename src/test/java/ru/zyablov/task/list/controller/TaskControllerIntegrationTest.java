package ru.zyablov.task.list.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/tasks.sql")
    void getListTasks_TasksExist_ReturnTasks() throws Exception {
        // given
        var reqBuilder = MockMvcRequestBuilders.get("/tasks");

        // when
        mockMvc.perform(reqBuilder)

                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {
                                "id": 1,
                                "title": "Задача №1",
                                "description": "Описание №1",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": true
                                },
                                {
                                "id": 2,
                                "title": "Задача №2",
                                "description": "Описание №2",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": true
                                },
                                {
                                "id": 3,
                                "title": "Задача №3",
                                "description": "Описание №3",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": false
                                },
                                {
                                "id": 4,
                                "title": "Задача №4",
                                "description": "Описание №4",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": true
                                },
                                {
                                "id": 5,
                                "title": "Задача №5",
                                "description": "Описание №5",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": false
                                }
                                ]""")
                );
    }

    @Test
    void getListTasks_TasksNotExist_ReturnEmptyList() throws Exception {
        // given
        var req = MockMvcRequestBuilders.get("/tasks");

        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("[]")
                );
    }


    @Test
    @Sql("/sql/tasks.sql")
    void getTask_TaskExist_ReturnTask() throws Exception {
        // given
        var req = MockMvcRequestBuilders.get("/tasks/1");

        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "id": 1,
                                "title": "Задача №1",
                                "description": "Описание №1",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": true
                                }
                                """)
                );
    }

    @Test
    void getTask_TaskNotExist_ReturnBadReq() throws Exception {
        // given
        var req = MockMvcRequestBuilders.get("/tasks/1");
        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void createTask_ReqValid_ReturnTask() throws Exception {
        // given
        var reqBuilder = MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id": 5,
                        "title": "Задача №5",
                        "description": "Описание №5",
                        "dueDate": "2024-04-26T10:00:00",
                        "completed": false
                        }
                        """
                );

        // when
        mockMvc.perform(reqBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "id": 1,
                                "title": "Задача №5",
                                "description": "Описание №5",
                                "dueDate": "2024-04-26T10:00:00",
                                "completed": false
                                }
                                """)
                );
    }

    @Test
    void createTask_ReqInvalid_ReturnException() throws Exception {
        // given
        var reqBuilder = MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id": 5,
                        "title": "   ",
                        "description": null,
                        "dueDate": "  ",
                        "completed": false
                        }
                        """
                );

        // when
        mockMvc.perform(reqBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                );
    }

    @Test
    @Sql("/sql/tasks.sql")
    void createTask_ReqIsInvalid_Return400() throws Exception {
        // given
        var req = MockMvcRequestBuilders.post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                          "id": 1,
                          "title": "Н",
                          "description": "Новое описание",
                          "dueDate": null,
                          "completed": false
                          }
                        """
                );
        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                                {
                                    "errors":[
                                    "Поле dueDate не должно быть пустым",
                                    "Размер title должен находиться в диапазоне от 3 до 200 символов"
                                  ]
                                  }
                                """)
                );
    }

    @Test
    @Sql("/sql/tasks.sql")
    void updateTask_TaskExist_ReturnTask() throws Exception {
        // given
        var reqBuilder = MockMvcRequestBuilders.put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id": 1,
                        "title": "Новая задача",
                        "description": "Новое описание",
                        "dueDate": "2024-09-26T10:00:00",
                        "completed": false
                        }
                        """
                );

        // when
        mockMvc.perform(reqBuilder)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                "id": 1,
                                "title": "Новая задача",
                                "description": "Новое описание",
                                "dueDate": "2024-09-26T10:00:00",
                                "completed": false
                                }
                                """)
                );
    }


    @Test
    void updateTask_ReqIsInvalidId_ReturnErrorNoSuchTaskException() throws Exception {
        // given
        var req = MockMvcRequestBuilders.put("/tasks/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                          "id": 1,
                          "title": "Новая задача",
                          "description": "Новое описание",
                          "dueDate": "2024-09-26T10:00:00",
                          "completed": false
                          }
                        """
                );
        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    @Sql("/sql/tasks.sql")
    void deleteTask_TaskExistOrNotExist_ReturnsNoContent() throws Exception {
        // given
        var req = MockMvcRequestBuilders.delete("/tasks/1");

        // when
        mockMvc.perform(req)
                // then
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );
    }
}