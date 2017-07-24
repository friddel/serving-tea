package waitress;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

class RespondMenueToCustomer extends AchieveREResponder {

	String Conversation = "none";

	public RespondMenueToCustomer(Waitress waitress, MessageTemplate mt) {
		super(waitress, mt);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		// send AGREE
		Conversation = request.getContent();
		System.out.println(Conversation);
		ACLMessage agree = request.createReply();
		agree.setPerformative(ACLMessage.AGREE);
		return agree;
		// send REFUSE
		//throw new RefuseException("check-failed");
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
		// if agent AGREEd to request
		// send INFORM
		ACLMessage inform = request.createReply();
		System.out.println("asjgdzjdsad");
		if (Conversation == "RequestMenu") {
			inform.setContent("Send Menu");
			
			inform.setPerformative(ACLMessage.INFORM);
		}
		return inform;
		// send FAILURE
		// throw new FailureException("unexpected-error");
	}
	private static final long serialVersionUID = -8009542545033008746L;
}
