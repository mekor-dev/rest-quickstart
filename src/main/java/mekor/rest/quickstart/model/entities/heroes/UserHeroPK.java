package mekor.rest.quickstart.model.entities.heroes;

import java.io.Serializable;

/**
 * Composite key of the class {@link UserHero}
 * 
 * @author mekor
 *
 */
public class UserHeroPK implements Serializable {

	private static final long serialVersionUID = -1544427709376375677L;

	private Long user;

	private Long hero;

	public UserHeroPK() {
		super();
	}

	public UserHeroPK(Long user, Long hero) {
		super();
		this.user = user;
		this.hero = hero;
	}

	/**
	 * @see {@link #user}
	 */
	public Long getUser() {
		return user;
	}

	/**
	 * @see {@link #user}
	 */
	public void setUser(Long user) {
		this.user = user;
	}

	/**
	 * @see {@link #hero}
	 */
	public Long getHero() {
		return hero;
	}

	/**
	 * @see {@link #hero}
	 */
	public void setHero(Long hero) {
		this.hero = hero;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hero == null) ? 0 : hero.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserHeroPK other = (UserHeroPK) obj;
		if (hero == null) {
			if (other.hero != null)
				return false;
		}
		else if (!hero.equals(other.hero))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		}
		else if (!user.equals(other.user))
			return false;
		return true;
	}

}
