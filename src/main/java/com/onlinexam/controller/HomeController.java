package com.onlinexam.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinexam.domain.User;
import com.onlinexam.domain.security.Role;
import com.onlinexam.domain.security.UserRole;
import com.onlinexam.repository.UserRepository;
import com.onlinexam.service.UserService;
import com.onlinexam.service.impl.UserSecurityService;
import com.onlinexam.utility.MailConstructor;
import com.onlinexam.utility.SecurityUtility;

@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String index() {
		return "profile";
	}
	@RequestMapping("/TypingTest")
	public String typing() {
		return "typing";
	}
	@RequestMapping(value = "/text", method = RequestMethod.GET)
	@ResponseBody
	public String text(@RequestParam String name) throws IOException {
//		System.out.println(name);
		name = "src/main/resources/"+name;
		System.out.println(name);
		String data = ""; 
	    data = new String(Files.readAllBytes(Paths.get(name)));
        return data;
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "index";
	}
	
	@RequestMapping("/home")
	public String profile(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "profile";
	}

	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("name") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("name", username);
//		System.out.println(username);

//
		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "index";
		}
//
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		System.out.println(username);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		System.out.println(password);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", "true");

		return "index";
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	public String forgetPost(HttpServletRequest request, @ModelAttribute("email") String userEmail, Model model)
			throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
//		System.out.println(username);

//
		if (userService.findByEmail(userEmail) == null) {
			model.addAttribute("usernotExists", true);
			return "index";
		}
//
		User user = userService.findByEmail(userEmail);

		String password = SecurityUtility.randomPassword();

		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userRepository.save(user);
		String token = UUID.randomUUID().toString();
		System.out.println(password);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);

		mailSender.send(email);

		model.addAttribute("emailSent", "true");

		return "index";
	}

}
