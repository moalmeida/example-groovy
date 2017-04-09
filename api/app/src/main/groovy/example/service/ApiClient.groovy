package example.service

import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

import example.model.SMS
import example.repository.SMSRepository

@Component
class ApiClient {

	private static final Logger log = LoggerFactory.getLogger(ApiClient.class)

	@Autowired
	String apiUrl

	@Autowired
	SMSRepository smsRepository

	@Autowired
	PubSubQueue publisherQueue

	@Autowired
	RestTemplate restTemplate


	/**
	 *
	 * @param sms
	 * @return
	 */
	def process(SMS sms) {
		if(sms.notExpired()) {
			try {
				sms = put(sms)
				try {
					sms = smsRepository.save(sms)
					log.debug("[x] savd ==> " + sms.asMap())
				} catch (Exception ex) {
					log.error(ex.message)
				}
			} catch (Exception ex) {
				log.debug(ex.message)
				publisherQueue.send(sms)
			}
		}
		return sms
	}

	/**
	 *
	 * @param sms
	 * @return
	 */
	def put(SMS sms) throws Exception {
		JSONObject JSON = new JSONObject(sms.asMap())
		HttpHeaders headers = new HttpHeaders()
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
		headers.setContentType(MediaType.APPLICATION_JSON)
		headers.add("Content-type", "application/json; charset=utf-8")
		HttpEntity<String> entity = new HttpEntity<>(JSON.toString(), headers)
		ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/sms", HttpMethod.PUT, entity, String.class)
		if (response.getStatusCode() == HttpStatus.CREATED) {
			sms.ok = true
			sms.status = HttpStatus.CREATED.value
		}
		if (response.getStatusCode() == HttpStatus.METHOD_NOT_ALLOWED) {
			sms.status = HttpStatus.METHOD_NOT_ALLOWED.value
		}
		if (response.getStatusCode() in [	HttpStatus.INTERNAL_SERVER_ERROR,
		   HttpStatus.NOT_FOUND, HttpStatus.UNAUTHORIZED ]) {
			throw [response.getStatusCode()] as Exception
		}
		return sms
	}
}
