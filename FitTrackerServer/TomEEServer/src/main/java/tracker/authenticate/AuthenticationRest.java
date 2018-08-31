package tracker.authenticate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.json.JSONObject;
import tracker.utils.API;
import tracker.authenticate.utils.OauthVerifier;
import tracker.authenticate.UserService;
import tracker.authenticate.User;
import tracker.authenticate.UserTokens;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Path(API.authentication)
public class AuthenticationRest {

    private UserService service;

    public AuthenticationRest(){}

    @Inject
    public AuthenticationRest(UserService service){
        this.service = service;
    }

    /*
        REST endpoint for users to register a local account.
        @param user User details - email, password.
        @return     Response ok(200) if the user doesn't exist in the database with new refresh and access token. Otherwise
        Response UNAUTHORIZED(409).
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(API.register)
    public Response register(User user) {

        JSONObject response = new JSONObject();
        UserTokens userTokens = service.insertUser(user);
        
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

    /*
        REST endpoint for users to login with local account. Verifies user password and creates
        refresh and access tokens for the user.
        @param user User details - email, password.
        @return     Response ok(200) if the password is correct token with new refresh and access token. Otherwise
        Response UNAUTHORIZED(409).
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(API.login)
    public Response login(User user) {
        JSONObject responseMsg = new JSONObject();
        Optional<UserTokens> userTokens = this.service.loginUser(user);
        Response response = userTokens.map(u ->
                        Response.ok( responseMsg.put("login", "success")
                        .put("refresh_token", u.getRefreshToken())
                        .put("access_token", u.getAccessToken())
                        .put("id", user.getId())
                        .put("new_user", false).toString())
                        .build())
                .orElseGet(() ->
                                Response.status(Response.Status.UNAUTHORIZED)
                                .type("application/json")
                                .entity(responseMsg.put("login", "fail")
                                        .put("message", "Account does not exist").toString())
                                .build());
        return response;
    }

    /*
        REST endpoint for users to login with google account. Verifies google access token and creates
        refresh and access tokens for the user.
        @param user User details - email.
        @return     Response ok(200) if the access token is verified with new refresh and access token. Otherwise
        Response UNAUTHORIZED(409).
    */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(API.googleLogin)
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

    /*
        REST endpoint for users to login with facebook account. Verifies facebook access token and creates
        refresh and access tokens for the user.
        @param user User details - email.
        @return     Response ok(200) if the access token is verified with new refresh and access token. Otherwise
        Response UNAUTHORIZED(409).
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(API.facebookLogin)
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

    /*
        REST endpoint for user forgotten password. Sends email with code to the user for password retrieval.
        @param email User email.
        @return     Response ok (200) if user is found in the database otherwise Response NOT_FOUND (404).
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(API.forgottenPassword)
    public Response forgottenPassword(String email) {
        boolean userExists = this.service.sendPasswordCodeToUser(email);
        if(userExists){
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /*
        REST endpoint for getting access token from an active refresh token.
        @param jsonObject JSON object with refresh_token inside.
        @return String New generated access token.
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path(API.accessToken)
    public Response getAccessToken(String jsonObject) {

        JSONObject object = new JSONObject(jsonObject);
        String refreshToken = object.getString("refresh_token");
        String accessToken = this.service.getAccessTokenFromRefreshToken(refreshToken);

        return Response.ok().entity(accessToken).build();
    }

    /*
        REST endpoint for changing users password.
        @param data JSONObject containing email as "email", password as "password"
        @return Response ok() code 200
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path(API.changePassword)
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

}
