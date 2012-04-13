package yana2

import static org.junit.Assert.*
import org.junit.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class LoginTests {

    @Test
    void testLogin() {
		//SecurityContextHolder.context.authentication = new UsernamePasswordAuthenticationToken('admin', 'admin')
		assert new UsernamePasswordAuthenticationToken('admin', 'admin')
    }
}
