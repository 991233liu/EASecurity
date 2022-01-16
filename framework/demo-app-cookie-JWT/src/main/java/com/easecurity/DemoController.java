package com.easecurity;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easecurity.core.access.annotation.EaSecured;
import com.easecurity.core.authentication.JWT;
import com.easecurity.core.authentication.JWTExpirationException;
import com.easecurity.core.authentication.LoginService;
import com.easecurity.core.authentication.UserDetails;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    LoginService loginService;
    @Value("${easecurity.jwt.public.key}")
    String publicKey;

    @RequestMapping("/login")
    public void login(HttpServletRequest request) throws IOException {
	UserDetails userDetails = null;
	try {
	    RSAPublicKey rsaPublicKey = loginService.getRSAPublicKey(new File(this.getClass().getResource(publicKey).getFile()));
	    JWT jwt = loginService.getCurrentUserJWT(request, rsaPublicKey);
	    if(jwt.verify()) {
		userDetails = jwt.userDetails;
		if (userDetails == null) {
		    System.out.println("anonymousUser");
		} else {
		    request.getSession(true).setAttribute("userDetails", userDetails);
		    System.out.println("-----## 当前登录人为：" + userDetails.account);
		}
	    } else {
		throw new JWTExpirationException(jwt.sub + " tocken has expiration, exp is:" + jwt.exp);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (JWTExpirationException e) {
	    e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	} catch (InvalidKeySpecException e) {
	    e.printStackTrace();
	}
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session) throws IOException {
	session.invalidate();
    }

    @RequestMapping("/queryData3")
    @EaSecured(org = "{id:['1','4']}")
    public UserDetails queryData3(HttpServletRequest request) {
	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
	System.out.println("-----## 当前登录人userDetails：" + userDetails);
	return userDetails;
    }
}
