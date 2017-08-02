package controllers;

import java.util.List;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;

public class UsersControlelr extends Controller{
	public Result index() {
		
		String result = "";
		List<User> users = User.findAll();
		
		for(User u : users) {
			result += u.name;
		}
		
        return ok(result);
    }
}
