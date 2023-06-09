package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
        private val userRepository: UserRepository,
        private val userService: UserService
) {

    @AfterEach
    fun clear() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("유저 저장 동작")
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("River", null)

        //when
        userService.saveUser(request)

        //then
        userRepository.findAll().apply {
            assertThat(this).hasSize(1)
            assertThat(this[0].name).isEqualTo("River")
            assertThat(this[0].age).isNull()
        }
    }

    @Test
    @DisplayName("유저 조회 동작")
    fun getUsersTest() {
        //given
        userRepository.saveAll(listOf(User("A", 10), User("B", null)))

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(10, null)
    }

    @Test
    @DisplayName("유저 이름 수정 동작")
    fun updateUserNameTest() {
        val savedUser = userRepository.save(User("A", 10))
        val request = UserUpdateRequest(savedUser.id, "B")

        userService.updateUserName(request)

        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제 동작")
    fun deleteUserTest() {
        userRepository.save(User("A", 10))
        userService.deleteUser("A")
        assertThat(userRepository.findAll()).isEmpty()
    }

}