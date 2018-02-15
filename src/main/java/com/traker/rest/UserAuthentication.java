package main.java.com.traker.rest;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import main.java.com.traker.Authenticate.*;
import main.java.com.traker.Authenticate.Authenticator;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.UserDAO;
import main.java.com.traker.DAO.UserDAOImpl;
import main.java.com.traker.Markers.Secured;
import main.java.com.traker.Users.GenericUser;
import main.java.com.traker.Users.LocalUser;
import main.java.com.traker.Users.ExternalUser;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static main.java.com.traker.Authenticate.OauthVerifier.verifyFBToken;
import static main.java.com.traker.Authenticate.OauthVerifier.verifyGoogleIdToken;

@Path("auth")
public class UserAuthentication
{
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(LocalUser user) {

        DAOFactory daoFactory = new DAOFactory();
        UserDAOImpl userDAO = daoFactory.getUserDAO();

        boolean result = OauthVerifier.CaptchaVerifier(user.getResponseToken());

        if(result){
            GenericUser genericUser = userDAO.insertUser(user);
            if(genericUser.isNew())
            {
                return Response.ok(JsonResponseFactory.authSuccessfulJson(genericUser).toString()).build();
            }
            else
            {
                return Response.status(Response.Status.CONFLICT).entity(JsonResponseFactory.accountAlreadyExistsJson().toString()).build();
            }
        } else {
            return Response.status(Response.Status.CONFLICT).entity(JsonResponseFactory.robotFoundJson().toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(LocalUser user)
    {
        DAOFactory daoFactory = new DAOFactory();
        UserDAOImpl userDAO = daoFactory.getUserDAO();

        LocalUser localUser = userDAO.findUser(user.getEmail());

        if(localUser.getPassword() == null || localUser.getPassword().equals(""))
        {
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(JsonResponseFactory.accountNotExistJson().toString()).build();
        }
        else if(Password.validatePassword(user.getPassword(), localUser.getPassword()))
        {
            return  Response.ok(JsonResponseFactory.localAuthSuccessfulJson(localUser).toString()).build();
        }
        else
        {
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(JsonResponseFactory.authFailPasswordIncorrectJson().toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("googlelogin")
    public Response googlelogin(ExternalUser user) throws IOException, GeneralSecurityException
    {
        GoogleIdToken idToken = verifyGoogleIdToken(user.getAccessToken());

        if (idToken != null)
        {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
//          String name = (String) payload.get("name");

            DAOFactory daoFactory = new DAOFactory();
            UserDAOImpl rcvdUser = daoFactory.getUserDAO();
            GenericUser genericUser = rcvdUser.insertUser(user);
            genericUser.setName(user.getName());
            genericUser.setAndroidId(user.getAndroidId());
            genericUser.setDevice(user.getDevice());

            return  Response.ok(JsonResponseFactory.authSuccessfulJson(genericUser).toString()).build();
        }
        else
        {
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(JsonResponseFactory.authFailPasswordIncorrectJson().toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("fblogin")
    public Response fblogin(ExternalUser user) throws IOException
    {
        if(verifyFBToken(user.getAccessToken()))
        {
            DAOFactory daoFactory = new DAOFactory();
            UserDAOImpl rcvdUser = daoFactory.getUserDAO();
            GenericUser genericUser = rcvdUser.insertUser(user);

            return  Response.ok(JsonResponseFactory.authSuccessfulJson(genericUser).toString()).build();
        }
        else
        {
            return Response.status(Response.Status.UNAUTHORIZED).type("application/json").entity(JsonResponseFactory.authFailPasswordIncorrectJson().toString()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("forgotten-password")
    public Response forgottenPassword(String email) throws IOException
    {
        DAOFactory daoFactory = new DAOFactory();
        UserDAO userDAO = daoFactory.getUserDAO();

        LocalUser user = userDAO.findUser(email);
        if(user != null){
            String token = Password.getRandomPasswordToken(8);
            userDAO.saveUserToken(email, token);

            final String username = "testotestov666@gmail.com";
            final String password = "testotestov";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testotestov666@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Password code");
                message.setText(token);

                Transport.send(message);

                System.out.println("Done");

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("access-token")
    public Response getAccessToken(String jsonObject){

        JSONObject object = new JSONObject(jsonObject);
         Jws<Claims> claimsJws = Authenticator.validateRefreshJwt(object.getString("refresh_token"));
        String userID = (String) claimsJws.getBody().get("userID");

        String accessToken = main.java.com.traker.Authenticate.Authenticator.getAccessToken(userID,
                                                                                            object.getString("android_id"),
                                                                                            object.getString("device"),
                                                                                            object.getString("refresh_token"));

        return Response.ok().entity(accessToken).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("password")
    public Response changePassword(String data){
        JSONObject jsonObject = new JSONObject(data);

        DAOFactory daoFactory = new DAOFactory();
        UserDAO userDAO = daoFactory.getUserDAO();
        int result = userDAO.changePassword(jsonObject);
        if(result == 0){
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("captcha")
    @Produces(MediaType.TEXT_HTML)
    public Response getCaptcha(@Context HttpServletResponse res){

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
