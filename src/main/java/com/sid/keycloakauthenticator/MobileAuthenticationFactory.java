/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sid.keycloakauthenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordFormFactory;
import org.keycloak.models.KeycloakSession;

/**
 *
 * @author sidde
 */
public class MobileAuthenticationFactory extends UsernamePasswordFormFactory {

   public static final String PROVIDER_ID = "mobile-authenticator";
   public static final MobileAuthenticator SINGLETON = new MobileAuthenticator();

   @Override
   public Authenticator create(KeycloakSession session) {
	return SINGLETON;
   }

   @Override
   public void init(Config.Scope scope) {
   }

   @Override
   public String getId() {
	return PROVIDER_ID;
   }

   @Override
   public String getDisplayType() {
	return "Mobile Based User Form";
   }

   @Override
   public String getHelpText() {
	return "Validates a mobile and password from login form.";
   }
}
