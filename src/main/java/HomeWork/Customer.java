package HomeWork;

import java.util.ArrayList;
import java.util.Date;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.util.Random;

public class Customer extends Agent {
	ArrayList<String> receivedMenu = new ArrayList<String>();
	String sreceivedMenu;
	String choice;

	@Override
	protected void setup() {
		addBehaviour(new MenuRequest(this, 2000));

	}

	class MenuRequest extends WakerBehaviour {
		public MenuRequest(Agent a, long time) {
			super(a, time);
		}

		@Override
		protected void onWake() {
			String requestedAction = "RequestMenu";
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("waitress"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			System.out.println("Customer      : Asking for Menu");
			myAgent.addBehaviour(new RequestToDoSomethingNow(myAgent, msg));
		}

	}

	class ItemRequest extends OneShotBehaviour {
		public ItemRequest(String choice) {
			super();
		}

		public void action() {
			String requestedItem = choice;
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("waitress"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedItem);
			myAgent.addBehaviour(new RequestToDoSomethingNow(myAgent, msg));
			if (requestedItem.toString() != "none") {
				System.out.println("Customer      : My choice is: " + requestedItem);
			}
		}
	}

	class RequestToDoSomethingNow extends AchieveREInitiator {
		public RequestToDoSomethingNow(Agent a, ACLMessage msg) {
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
			// sreceivedMenu = inform.getContent();

			try {
				receivedMenu = (ArrayList) inform.getContentObject();
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * if (sreceivedMenu.matches("none")) { }else {
			 */
			String temp;

			System.out.println("Customer      : Thank you for the menu. This is what is offered: ");
			for (int i = 0; i < receivedMenu.size(); i++) {
				temp = receivedMenu.get(i);
				System.out.println("                 "+i+": "+ temp);
			}
		DecideForItem(receivedMenu);
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("Error: received failure");
		}

	}

	public String DecideForItem(ArrayList<String> receivedMenu2) {
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(receivedMenu2.size());	
		choice = receivedMenu2.get(randomInt);

		addBehaviour(new ItemRequest(choice));
		return choice;

	}
}
