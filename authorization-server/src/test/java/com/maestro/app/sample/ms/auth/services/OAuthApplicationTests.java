package com.maestro.app.sample.ms.auth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maestro.app.sample.ms.auth.services.utils.CustomJwt;
import com.maestro.app.sample.ms.auth.services.utils.TestUtils;
import com.maestro.app.utils.CommonUtils;
import com.maestro.app.utils.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

/**
 * Created by Maestro on Apr-2020.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OAuthApplicationTests {
	ObjectMapper mapper = new ObjectMapper()
								.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Value("${auth-server.user}")
	private String CLIENT_ID;
	@Value("${auth-server.secret}")
	private String CLIENT_PSW;

	private String IdAdminUser;
	private String IdOrgAdminUser;
	private String IdUser;
	private final String IdOrg = TestUtils.PRIME_IDORG;

	@BeforeAll
	public void setup() {
		IdAdminUser = CommonUtils.generateGuid();
		IdOrgAdminUser = CommonUtils.generateGuid();
		IdUser = CommonUtils.generateGuid();

		final String psw = "{bcrypt}$2y$12$CyHvit6/F5S3Z2IFf552S.vRPNbe7Y.7ekePMB2BfQsZCqo7D.ixu";

		jdbcTemplate.update(TestUtils.SQL_USER_CREATE,
				new Object[]{ IdAdminUser, IdAdminUser, "TEST-USER-" + IdAdminUser, "test-user." + IdAdminUser + "@domain.dev", 1, IdOrg, IdOrg, psw });
		jdbcTemplate.update(TestUtils.SQL_ROLE_CREATE, new Object[] { IdAdminUser, 1, new Date() } );

		jdbcTemplate.update(TestUtils.SQL_USER_CREATE,
				new Object[]{ IdOrgAdminUser, IdOrgAdminUser, "TEST-USER-" + IdAdminUser, "test-user." + IdAdminUser + "@domain.dev", 1, IdOrg, IdOrg, psw });
		jdbcTemplate.update(TestUtils.SQL_ROLE_CREATE, new Object[] { IdOrgAdminUser, 2, new Date() } );

		jdbcTemplate.update(TestUtils.SQL_USER_CREATE,
				new Object[]{ IdUser, IdUser, "TEST-USER-" + IdAdminUser, "test-user." + IdAdminUser + "@domain.dev", 1, IdOrg, IdOrg, psw });
	}

	@AfterAll
	public void end() {
		jdbcTemplate.update(TestUtils.SQL_USER_DELETE, new Object[]{ IdAdminUser });
		jdbcTemplate.update(TestUtils.SQL_USER_DELETE, new Object[]{ IdOrgAdminUser });
		jdbcTemplate.update(TestUtils.SQL_USER_DELETE, new Object[]{ IdUser });
	}

	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

	@Test
	@DisplayName("Connecting by USER 1")
	public void connectUser_1() {
		AuthUser authUSer = TestUtils.queryAuthUser(jdbcTemplate, IdAdminUser);
		connectAuthUser(authUSer.getUsername(), "!Qwerty2");
	}

	@Test
	@DisplayName("Connecting by USER 2")
	public void connectUser_2() {
		AuthUser authUSer = TestUtils.queryAuthUser(jdbcTemplate, IdOrgAdminUser);
		connectAuthUser(authUSer.getUsername(), "!Qwerty2");
	}

	@Test
	@DisplayName("Connecting by USER 3")
	public void connectUser_3()  {
		AuthUser authUSer = TestUtils.queryAuthUser(jdbcTemplate, IdUser);
		connectAuthUser(authUSer.getUsername(), "!Qwerty2");
	}

	@Test
	@DisplayName("Connection by no existing user")
	public void connectUsingNoExistingUser() throws Exception {
		ResultActions result = validateOAuthUser("test_user", "password123456");
		result
				.andExpect(status().is(401));
	}

	private ResultActions validateOAuthUser (String username, String password) throws Exception {
		System.out.println(" ** connecting: " + username);
		try {
			final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "password");
			params.add("client_id", CLIENT_ID);
			params.add("username", username);
			params.add("password", password);

			ResultActions result = mockMvc.perform(post("/oauth/token")
					.params(params)
					.with(httpBasic(CLIENT_ID, CLIENT_PSW))
					.accept(CONTENT_TYPE));
			System.out.println("\t" + result.andReturn().getResponse().getContentAsString());
			return  result;
		} catch (UnsupportedEncodingException | JsonProcessingException ex) {
			throw new Exception(ex);
		}
	}

	private void connectAuthUser(final String user, final String password)  {
		try {
			ResultActions result = validateOAuthUser(user, password);
			result
					.andExpect(status().isOk())
					.andExpect(content().contentType(CONTENT_TYPE));
			String response = result.andReturn().getResponse().getContentAsString();

//		JacksonJsonParser jsonParser = new JacksonJsonParser();
//		Map<String, Object> json = jsonParser.parseMap(response);
//		json.forEach((k, v) -> {
//			System.out.println(String.format("\t '%s' = '%s'", k, v));
//		});
			CustomJwt jwt = mapper.readValue(response, CustomJwt.class);
			System.out.println(jwt);

			assertNotNull(jwt);
			assertNotNull(jwt.getUser());
			assertNotNull(jwt.getUser().getOrganization());
			assertTrue(jwt.getUser().getUsername().equalsIgnoreCase(user));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
