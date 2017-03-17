package controllers;

import java.util.List;

import javax.inject.Inject;

import models.User;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.mvc.*;

import views.html.*;

import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
	private final FormFactory formFactory;
	private final JPAApi jpaApi;
	
    @Inject
    public HomeController(FormFactory formFactory, JPAApi jpaApi) {
        this.formFactory = formFactory;
        this.jpaApi = jpaApi;
    }
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Transactional
    public Result index() {
    	 List<User> users = (List<User>) jpaApi.em().createQuery("select q from User q").getResultList();
         return ok(toJson(users));
    }

}
