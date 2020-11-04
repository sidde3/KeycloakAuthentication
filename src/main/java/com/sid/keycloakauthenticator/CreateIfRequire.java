/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sid.keycloakauthenticator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.broker.IdpCreateUserIfUniqueAuthenticator;
import org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.UserModel;



/**
 *
 * @author sidde
 */
public class CreateIfRequire extends IdpCreateUserIfUniqueAuthenticator {
    //private final List<String> users = Arrays.asList("siddhartha.de@mail.com","sidde3");
    private static List<String> users = new ArrayList<String>();
    
    static{
        File file = new File(System.getProperty("userlist"));
        if (file.exists()) {
            try {
                Scanner sc = new Scanner(file);
                sc.useDelimiter(",");
                while (sc.hasNext()) {
                    users.add(sc.next());
                }
            }catch (FileNotFoundException ex) {
                Logger.getLogger(CreateIfRequire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    protected void userRegisteredSuccess(AuthenticationFlowContext context, UserModel registeredUser, SerializedBrokeredIdentityContext serializedCtx, BrokeredIdentityContext brokerContext) {
        System.out.println(registeredUser.getUsername()+" User is successfully registered...");
        if(!users.contains(registeredUser.getUsername())){
            registeredUser.setEnabled(false);
        }
        
    }
}
