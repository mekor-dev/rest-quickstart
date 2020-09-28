package mekor.rest.quickstart.model.entities.heroes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;

/**
 * Association between a {@link Hero} and a {@link User}. Store informations
 * about the owned hero.
 * 
 * @author mekor
 *
 *
 */
@Entity
@Table
@IdClass(UserHeroPK.class)
public class UserHero extends GenericEntity {

	private static final long serialVersionUID = -7259848691645683049L;

	/**
	 * The user owning the hero.
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "userID")
	private User user;

	/**
	 * The hero owned by the user.
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "heroID")
	private Hero hero;

	/**
	 * Power value of the hero
	 */
	@Column
	private Integer power;

	/**
	 * @see {@link #user}
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @see {@link #user}
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @see {@link #hero}
	 */
	public Hero getHero() {
		return hero;
	}

	/**
	 * @see {@link #hero}
	 */
	public void setHero(Hero hero) {
		this.hero = hero;
	}

	/**
	 * @see {@link #power}
	 */
	public Integer getPower() {
		return power;
	}

	/**
	 * @see {@link #power}
	 */
	public void setPower(Integer power) {
		this.power = power;
	}

}
