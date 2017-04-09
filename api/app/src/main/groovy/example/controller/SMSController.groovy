package example.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import example.controller.SMSController
import example.model.SMS
import example.repository.SMSRepository
import example.service.ApiClient

@Controller
class SMSController {

	private static Logger log = LoggerFactory.getLogger(SMSController.class)

	@Autowired
	ApiClient apiClient

	@Autowired
	SMSRepository smsRepository

	@Autowired
	int queueExpire

	/**
	 *
	 * @param sms
	 * @return
	 */
	@RequestMapping("/sms")
	@ResponseBody ResponseEntity put(@RequestBody SMS sms) {
		if(validate(sms)) {
			sms.expireLimit(queueExpire)
			try {
				sms = smsRepository.save(sms)
			} catch (Exception ex) {
				log.error(ex.message)
			}
			sms = apiClient.process(sms)
			return new ResponseEntity(sms, HttpStatus.valueOf(sms.status))
		}
		sms.status = HttpStatus.BAD_REQUEST.value
		new ResponseEntity(sms, HttpStatus.BAD_REQUEST)
	}

	boolean validate(SMS sms) {
		final int SMSLimit = 160
		// check expire date after now
		if(sms?.expire && sms?.expire < new Date()) {
			return false
		}
		// check sms limit
		if(sms?.body.length() > SMSLimit) {
			return false
		}
		true
	}

}
