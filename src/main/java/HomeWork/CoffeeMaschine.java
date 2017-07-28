package HomeWork;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;


public class CoffeeMaschine extends Agent {
	String Conversation1;
	protected void setup() {
		MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		addBehaviour(new RespondToWaitress(this, template));
	}

	public class RespondToWaitress extends AchieveREResponder {

		public RespondToWaitress(CoffeeMaschine coffeeMaschine, MessageTemplate mt) {
			super(coffeeMaschine, mt);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
			// send AGREE
			Conversation1 = request.getContent();
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			System.out.println("CoffeeMaschine: My task is to do the "+ Conversation1.toString());
			return agree;
			// send REFUSE
			// throw new RefuseException("check-failed");
		}
		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
			ACLMessage inform = request.createReply();
			System.out.println("CoffeeMaschine: "+ Conversation1+" is done! Give Waitress...");
				inform.setContent(Conversation1);
				inform.setPerformative(ACLMessage.INFORM);

			return inform;
		}
		private static final long serialVersionUID = -8009542545033008746L;
	}
}
