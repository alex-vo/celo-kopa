package lv.celokopa.test

import lv.celokopa.app.util.EmailSender
import spock.lang.Specification


/**
 * Created by alex on 17.12.8.
 */

class EmailSenderTest extends Specification {

    def "Test EmailSender"(){
        given:
            def sendEmailClosure = {
                String subject,
                String text,
                String recipient,
                String username,
                String password
                    ->
                    println "RRRRRRRRRRRRRRRRR"
            }
            EmailSender.getInstance().metaClass.sendEmail = sendEmailClosure
            EmailSender.getInstance().metaClass.hashCode = {1}
            EmailSender.getInstance().sendEmail("a", "b", "c", "d", "e")
            println EmailSender.getInstance().hashCode()
        expect:
            1==1
    }

}