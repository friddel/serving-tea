package HomeWork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

public class Waitress extends Agent {
	String Order;
	String CustomerOrder = "none";
	String Conversation = "none";
	String dummy = "tea";
	ArrayList<String> menuForCustomer = new ArrayList<String>();

	protected void setup() {
		menuForCustomer.add("tea");
		menuForCustomer.add("coffee");
		menuForCustomer.add("water");
		MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		addBehaviour(new RespondToCustomer(this, template, menuForCustomer));
	}

	public void GetItem() {
		System.out.println("##############################################################");
		addBehaviour(new TalkToCoffeeMaschine(this, 200));
	}

	public void GiveItemToCustomer() {
		System.out.println("##############################################################");
		System.out.println("Waitress      : Your " + CustomerOrder + ". Enjoy it!");

	}

	public class RespondToCustomer extends AchieveREResponder {

		public RespondToCustomer(Waitress waitress, MessageTemplate mt) {
			super(waitress, mt);
			// TODO Auto-generated constructor stub
		}

		public RespondToCustomer(Agent a, MessageTemplate mt, ArrayList<String> menuForCustomer) {
			super(a, mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
			// send AGREE
			Conversation = request.getContent();
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
			// send REFUSE
			// throw new RefuseException("check-failed");
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
				throws FailureException {
			// if agent AGREEd to request
			// send INFORM
			ACLMessage inform = request.createReply();
			switch (Conversation.toString()) {
			case "RequestMenu":
				// inform.setContent(dummy);
				try {
					inform.setContentObject(menuForCustomer);
				} catch (IOException e) {
					e.printStackTrace();
				}
				inform.setPerformative(ACLMessage.INFORM);
				System.out.println("Waitress      : This is the menu:  " + Conversation);
				break;
			case "tea":
				inform.setContent("none");
				inform.setPerformative(ACLMessage.INFORM);
				CustomerOrder = Conversation.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Conversation);
				Waitress.this.GetItem();
				break;
			case "coffee":
				inform.setContent("none");
				inform.setPerformative(ACLMessage.INFORM);
				CustomerOrder = Conversation.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Conversation);
				Waitress.this.GetItem();
				break;
			case "water":
				inform.setContent("none");
				inform.setPerformative(ACLMessage.INFORM);
				CustomerOrder = Conversation.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Conversation);
				Waitress.this.GetItem();
				break;
			default:
				System.out.println("------>Waitress was fired!");
			}

			return inform;
			// send FAILURE
			// throw new FailureException("unexpected-error");
		}

		private static final long serialVersionUID = -8009542545033008746L;
	}

	class TalkToCoffeeMaschine extends WakerBehaviour {
		public TalkToCoffeeMaschine(Agent a, long time) {
			super(a, time);
		}

		protected void onWake() {
			String requestedAction = CustomerOrder.toString();
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("Coffee"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			System.out.println("Waitress      : Coffee Maschine produce " + msg.getContent().toString() + "!");
			myAgent.addBehaviour(new Talk(myAgent, msg));
		}

	}

	class Talk extends AchieveREInitiator {
		public Talk(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			System.out.println("Error: received refuse");
		}

		@Override
		protected void handleAgree(ACLMessage agree) {
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			if (inform.getContent().matches(CustomerOrder)) {
				System.out.println("Waitress      : Received item from Coffee Maschine");
				GiveItemToCustomer();
			} else {
				System.out.println("Waitress      : Coffee Maschine is not working -> buy a new one!");
			}
		}
		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("Error: received failure");
		}

	}
}
