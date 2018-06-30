package tracker.rest;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.json.JSONObject;
import tracker.Authenticate.*;

import tracker.DAO.DAOServices.UserService;

import tracker.Entities.User;
import tracker.Entities.UserTokens;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.Properties;

@Path("auth")
public class UserAuthentication {

    private UserService service;

    public UserAuthentication(){}

    @Inject
    public UserAuthentication(UserService service){
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(User user) {

        JSONObject response = new JSONObject();
        UserTokens userTokens = null;
        userTokens = service.insertUser(user);

        if (userTokens.isUserNew()) {
            response.put("login", "success")
            .put("refresh_token", userTokens.getRefreshToken())
            .put("access_token", userTokens.getAccessToken())
            .put("id", user.getId())
            .put("new_user", true);
            return Response.ok(response.toString()).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(User user) {

        UserTokens userTokens = this.service.loginUser(user);
        JSONObject response = new JSONObject();
        if(userTokens != null){
            response.put("login", "success")
                    .put("refresh_token", userTokens.getRefreshToken())
                    .put("access_token", userTokens.getAccessToken())
                    .put("id", user.getId())
                    .put("new_user", false);
            return Response.ok(response.toString()).build();
        } else{
            response.put("login", "fail")
                    .put("message", "Account does not exist");
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(response.toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("googlelogin")
    public Response googlelogin(User user) throws IOException, GeneralSecurityException {
        OauthVerifier verifier = new OauthVerifier();
        GoogleIdToken idToken = verifier.verifyGoogleIdToken(user.getAccessToken());
        JSONObject response = new JSONObject();

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
//            String email = payload.getEmail();
//          String name = (String) payload.get("name");

            UserTokens userTokens = this.service.insertOAuthUser(user);
            response.put("login", "success")
                    .put("refresh_token", userTokens.getRefreshToken())
                    .put("access_token", userTokens.getAccessToken())
                    .put("id", user.getId())
                    .put("new_user", false);
            return Response.ok(response.toString()).build();
        } else {
            response.put("login", "fail")
                    .put("message", "Invalid email or password");
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(response.toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("fblogin")
    public Response fblogin(User user) throws IOException {
        OauthVerifier verifier = new OauthVerifier();
        JSONObject response = new JSONObject();
        if (verifier.verifyFBToken(user.getAccessToken())) {
            UserTokens userTokens = this.service.insertOAuthUser(user);
            response.put("login", "success")
                    .put("refresh_token", userTokens.getRefreshToken())
                    .put("access_token", userTokens.getAccessToken())
                    .put("id", user.getId())
                    .put("new_user", false);
            return Response.ok(response.toString()).build();
        } else {
            response.put("login", "fail")
                    .put("message", "Invalid email or password");
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(response.toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("forgotten-password")
    public Response forgottenPassword(String email) {
        boolean userExists = this.service.sendPasswordCodeToUser(email);
        if(userExists){
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("access-token")
    public Response getAccessToken(String jsonObject) {

        JSONObject object = new JSONObject(jsonObject);
        String refreshToken = object.getString("refresh_token");
        String accessToken = this.service.getAccessTokenFromRefreshToken(refreshToken);

        return Response.ok().entity(accessToken).build();
    }

    /*
        REST endpoint for changing users password.
        @param String JSONObject containing email as "email", password as "password"
        @return Response ok() code 200
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("password")
    public Response changePassword(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String email = jsonObject.getString("email");
        String code = jsonObject.getString("code");
        String newPassword = jsonObject.getString("password");

        boolean flag = this.service.changePassword(email, code, newPassword);
        if(flag){
            return Response.ok().build();
        }
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("captcha")
    @Produces(MediaType.TEXT_HTML)
    public Response getCaptcha(@Context HttpServletResponse res) {

        return Response.ok().entity("<html>" +
                "  <head>" +
                "    <title>reCAPTCHA demo: Simple page</title>" +
                "     <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>" +
                "  </head>" +
                "  <body>" +
                "    <div class=\"g-recaptcha\"" +
                "          data-sitekey=\"6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI\"" +
                "          data-callback=\"captchaResponse\"" +
                "          data-size=\"invisible\">" +
                "    </div>" +
                " <script type=\"text/javascript\">" +
                "function executeCaptcha(){grecaptcha.execute();}" +
                "      function captchaResponse(token){" +
                "BridgeWebViewClass.reCaptchaCallbackInAndroid(token)}" +
                "</script>" +
                "  </body>" +
                "</html>").build();
    }

}
