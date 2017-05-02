package models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.Transactional;

@Entity
@Table(name = "User")
public class User implements Serializable {
	

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
	
	public String name;
	public String password;
	
	private static final JpaHelper<User, Integer> helper = new JpaHelper<>(User.class);
	
	@Transactional
	public static List<User> findAll() {
		return helper.findAll();
	}
}
