
package example.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "sms")
class SMS implements Serializable {

	/**
	 * SMS Identifier
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id

	/**
	 * SMS sender
	 */
	@NotNull
	@Column(name="\"from\"")
	String from

	/**
	 * SMS destination
	 */
	@NotNull
	@Column(name="\"to\"")
	String to

	/**
	 * SMS body
	 */
	@NotNull
	@Column(name="body", length = 160)
	String body

	/**
	 * SMS created time
	 */
	Date created = new Date().toTimestamp()

	/**
	 * SMS expire time
	 */
	Date expire

	/**
	 * SMS sent confirmation
	 */
	boolean ok = false

	/**
	 * SMS status
	 */
	int status

	/**
	 *
	 * @param timeExpire
	 * @return
	 */
	def expireLimit(int timeExpire) {
		if(!this.expire) {
			use (groovy.time.TimeCategory) {
				this.expire = (timeExpire).minute.from.now
			}
		}
	}

	def notExpired() {
		if(this.expire && this.expire < new Date()) {
			return false
		}
		return true
	}


	Map asMap() {
		this.class.declaredFields.findAll { !it.synthetic }.collectEntries {
			[ (it.name):this."$it.name" ]
		}
	}
}
