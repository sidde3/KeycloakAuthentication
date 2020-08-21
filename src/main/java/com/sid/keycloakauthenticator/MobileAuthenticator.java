/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sid.keycloakauthenticator;

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.events.Errors;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.events.Details;
import org.keycloak.models.ModelDuplicateException;
import org.keycloak.models.UserModel;
import org.keycloak.services.ServicesLogger;
import org.keycloak.services.messages.Messages;

/**
 *
 * @author sidde
 */
public class MobileAuthenticator extends UsernamePasswordForm implements Authenticator {
//
//   @Override
//   protected boolean validateForm(AuthenticationFlowContext context, MultivaluedMap<String, String> formData) {
//	return validateUserAndPassword(context, formData);
//   }

   @Override
   public boolean validateUserAndPassword(AuthenticationFlowContext context, MultivaluedMap<String, String> inputData) {
	String username = inputData.getFirst(AuthenticationManager.FORM_USERNAME);
	if (username == null) {
	   context.getEvent().error(Errors.USER_NOT_FOUND);
	   Response challengeResponse = challenge(context, Messages.INVALID_USER);
	   context.failureChallenge(AuthenticationFlowError.INVALID_USER, challengeResponse);
	   return false;
	}

	// remove leading and trailing whitespace
	username = username.trim();

	context.getEvent().detail(Details.USERNAME, username);
	context.getAuthenticationSession().setAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME, username);

	UserModel user = null;
	try {
	   //user = KeycloakModelUtils.findUserByNameOrEmail(context.getSession(), context.getRealm(), username);
	   List<UserModel> users = context.getSession().users().searchForUserByUserAttribute("mobile", username, context.getRealm());
	   System.out.println(users.get(0).getUsername());
	   if (users != null && users.size() == 1) {
		user = users.get(0);
	   }
	} catch (ModelDuplicateException mde) {

	   if (mde.getDuplicateFieldName() != null && mde.getDuplicateFieldName().equals(UserModel.EMAIL)) {
		setDuplicateUserChallenge(context, Errors.EMAIL_IN_USE, Messages.EMAIL_EXISTS, AuthenticationFlowError.INVALID_USER);
	   } else {
		setDuplicateUserChallenge(context, Errors.USERNAME_IN_USE, Messages.USERNAME_EXISTS, AuthenticationFlowError.INVALID_USER);
	   }

	   return false;
	}

	if (invalidUser(context, user)) {
	   return false;
	}

	if (!validatePassword(context, user, inputData)) {
	   return false;
	}

	if (!enabledUser(context, user)) {
	   return false;
	}

	String rememberMe = inputData.getFirst("rememberMe");
	boolean remember = rememberMe != null && rememberMe.equalsIgnoreCase("on");
	if (remember) {
	   context.getAuthenticationSession().setAuthNote(Details.REMEMBER_ME, "true");
	   context.getEvent().detail(Details.REMEMBER_ME, "true");
	} else {
	   context.getAuthenticationSession().removeAuthNote(Details.REMEMBER_ME);
	}
	context.setUser(user);
	return true;
   }

}
