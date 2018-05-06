package controllers;

import play.mvc.*;

import views.html.*;


public class IndentJsonController extends Controller {
	
	public Result index() {
        return ok(indent_json.render("Your new application is ready."));
    }

}
