package waitress;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;



public class Waitress extends Agent {
	
	
	protected void setup() {
		MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		addBehaviour(new RespondMenueToCustomer(this, template));
	}
	
	
	
}
