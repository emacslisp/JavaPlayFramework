package controllers;

import java.util.List;

import common.models.User;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class UsersController extends Controller{
	
	@Transactional
	public Result index() {
		
		String result = "";
		List<User> users = User.findAll();
		
		for(User u : users) {
			result += u.name;
		}
		
        return ok(result);
    }
}
